package hexlet.code;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.utils.TimestampFormatter;
import hexlet.code.repositories.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import static hexlet.code.utils.TimestampFormatter.getCurrentTimeStamp;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    Javalin app;
    MockWebServer mockWebServer;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertEquals(200, response.code(), "Not valid Code");
            assertNotNull(response.body());
            assertTrue(response.body().string().contains("Проверить"), "Page not found");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls");
            assertEquals(200, response.code(), "Not valid Code");
            assertNotNull(response.body());
            assertTrue(response.body().string().contains("Сайты"), "Page not found");
        });
    }
    @Test
    public void testUrlPage() throws SQLException {
        var url = new Url("https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/net/URI.html#toURL()",
                getCurrentTimeStamp());
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/urls/" + url.getId());
            assertTrue(response.body().string().contains("https://docs.oracle.com"), "Page not found");
        });
    }
    @Test
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var url = "https://getbootstrap.com/docs/5.3/examples/";
            var response = client.post("/urls", "url=" + url);
            assertEquals(200, response.code(), "Not valid Code");
            var expectedNameUrl = "https://getbootstrap.com";
            assertTrue(response.body().string().contains(expectedNameUrl), "Not Created");
            assertTrue(UrlRepository.findByName(expectedNameUrl).isPresent(), "Url not found in DB after created");
        });
    }

    @Test
    public void testNotValidUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = "yaya";
            var response = client.post("/urls", "url=" + url);
            assertFalse(response.body().string().contains(url), "Bad Url created");
        });
    }

    @Test
    public void testCreateUrlIfExist() throws SQLException {
        var urlName = "https://getbootstrap.com/docs/5.3/examples/";
        Url urlModel = new Url(urlName, TimestampFormatter.getCurrentTimeStamp());
        UrlRepository.save(urlModel);
        var urlModelAfterSave = UrlRepository.findByName(urlName).get();

        assertEquals(urlName, urlModelAfterSave.getName(), "Different Urls");

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://javalintest.io";
            var response = client.post("/urls", requestBody);
            assertEquals(200, response.code(), "Status code not corrected");
            assertTrue(response.body().string().contains("Анализатор страниц"), "Page not Found");
        });
    }


    @Test
    public void testSEOAnalyze() throws IOException, SQLException {
        MockResponse mockResponse = new MockResponse()
                .setResponseCode(200)
                .setBody(Files.readString(Paths.get("./src/test/resources/testPageCodeclimate.html")));

        mockWebServer.enqueue(mockResponse);
        HttpUrl mockHttpUrl = mockWebServer.url("/test");

        var urlModel = new Url(mockHttpUrl.toString(), TimestampFormatter.getCurrentTimeStamp());
        UrlRepository.save(urlModel);

        JavalinTest.test(app, (server, client) -> {
            client.post("/urls/" + urlModel.getId() + "/checks", "");
            var response = client.get("/urls/" + urlModel.getId());
            assertEquals(200, response.code(), "Status code not corrected");
            assertTrue(response.body().string().contains("Анализатор страниц"), "Page not Found");
        });

        List<UrlCheck> urlCheckList = UrlCheckRepository.findChecksByUrlId(urlModel.getId());
        UrlCheck urlCheck = urlCheckList.get(0);

        assertEquals(1L, urlCheck.getId(), "Not Valid UrlCheckId");
        assertEquals(200, urlCheck.getStatusCode(), "Not Valid Status Code");

        var expectedTitle = "Тестовый Title Code Climat";
        assertEquals(expectedTitle, urlCheck.getTitle(), "Not Valid Title");

        var expectedH1 = "Тестовый H1 Code Climat";
        assertEquals(expectedH1, urlCheck.getH1(), "Not Valid H1");

        var expectedDescription = "Code Climate's industry-leading Software Engineering Intelligence platform"
            + " helps unlock the full potential of your organization to ship better code,…";
        assertEquals(expectedDescription, urlCheck.getDescription(), "Not Valid Description");
    }
    @AfterEach
    public final void closeConnects() throws IOException {
        mockWebServer.shutdown();
    }
}
