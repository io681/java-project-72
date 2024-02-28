package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.BuildUrlPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.models.Url;
import hexlet.code.repositories.BaseRepository;
import hexlet.code.repositories.UrlRepository;
import io.javalin.Javalin;
//import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
//import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.stream.Collectors;

public class App {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static Javalin getApp() throws IOException, SQLException {
        var hikariConfig = new HikariConfig();
        String jdbcUrl = System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");
        hikariConfig.setJdbcUrl(jdbcUrl);
        if (!jdbcUrl.equals("jdbc:h2:mem:project")) {
            String username = System.getenv("JDBC_DATABASE_USERNAME");
            String password = System.getenv("JDBC_DATABASE_PASSWORD");
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
        }

        var dataSource = new HikariDataSource(hikariConfig);
        var sql = readResourceFile("schema.sql");
//        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        JavalinJte.init(createTemplateEngine());

        //Render pages
        app.get("/", ctx -> {
            var page = new BasePage();
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("index.jte", Collections.singletonMap("page", page));
        });
        app.get("/urls", ctx -> {
            var urlsList = UrlRepository.getEntities();
            var page = new UrlsPage(urlsList);
            page.setFlash(ctx.consumeSessionAttribute("flash"));
            ctx.render("urls/showByAll.jte", Collections.singletonMap("page", page));
        });

        app.get("/urls/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).get();
            var url = UrlRepository.find(id).get();
//                    .orElseThrow(() -> new NotFoundResponse("Post not found"));
            var page = new UrlPage(url);
            ctx.render("urls/showById.jte", Collections.singletonMap("page", page));
        });

        //Back
        app.post("/urls", ctx -> {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = timestamp.toLocalDateTime().format(FORMATTER);

//            var str = "dagkhgk";
//            URI exampleuri = new URI(str);

            try {
                var urlText = ctx.formParamAsClass("url", String.class)
                        .check(value -> value.length()  > 2, "Некорректный URL")
                        .get();
                if (UrlRepository.findByName(urlText).isPresent()) {
                    ctx.sessionAttribute("flash", "Страница уже существует");
                    ctx.redirect("/");
                } else {
                    var url = new Url(urlText, timestamp);
                    UrlRepository.save(url);
                    ctx.sessionAttribute("flash", "Страница успешно добавлена");
                    ctx.redirect("/urls");
                }
            } catch (ValidationException e) {
                var urlText = ctx.formParam("url");
                var page = new BuildUrlPage(urlText, ts, e.getErrors());
                ctx.render("/", Collections.singletonMap("page", page));
            }
        });


        return app;
    }
    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.parseInt(port);
    }

    private static String readResourceFile(String fileName) throws IOException {
        var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
}
