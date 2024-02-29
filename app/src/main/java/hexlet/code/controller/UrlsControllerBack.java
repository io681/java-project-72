package hexlet.code.controller;

import hexlet.code.models.Url;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;
import io.javalin.validation.ValidationException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

import static hexlet.code.utils.TimestampFormatter.getCurrentTimeStamp;

public class UrlsControllerBack {
    public static void create(Context ctx) throws SQLException {

        try {
            var urlText = ctx.formParam("url");
            var urlTextNormalized = urlText.trim().toLowerCase();
            URI uri = new URI(urlTextNormalized);
            URL url = uri.toURL();

            var urlNormalized = (url.getPort() == -1) ? uri.getScheme() + "://" + url.getHost()
                    : uri.getScheme() + "://" + url.getHost() + ":" + url.getPort();

            if (UrlRepository.findByName(urlNormalized).isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует");
                ctx.sessionAttribute("flash-type", "danger");
                ctx.redirect("/urls");
            } else {
                var urlModel = new Url(urlNormalized, getCurrentTimeStamp());
                UrlRepository.save(urlModel);
                ctx.sessionAttribute("flash", "Страница успешно добавлена");
                ctx.sessionAttribute("flash-type", "success");
                ctx.redirect("/urls");
            }
        } catch (NullPointerException | IllegalArgumentException
                 | ValidationException | MalformedURLException | URISyntaxException e) {
            ctx.sessionAttribute("flash", "Некорректный URL");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/urls");
        }
    }
}
