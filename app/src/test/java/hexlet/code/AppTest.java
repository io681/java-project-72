package hexlet.code;

import hexlet.code.models.Url;
import hexlet.code.repositories.UrlRepository;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static hexlet.code.utils.TimestampFormatter.getCurrentTimeStamp;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppTest {
    Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
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
    public void testCreateUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "https://getbootstrap.com/docs/5.3/examples/";
            var response = client.post("/urls", requestBody);
            assertEquals(200, response.code(), "Not valid Code");
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
    public void testNotValidUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "yaya";
            var responseByPage = client.get("/urls");
            assertFalse(responseByPage.body().string().contains("yaya"), "Bad Url created");
        });
    }
}
