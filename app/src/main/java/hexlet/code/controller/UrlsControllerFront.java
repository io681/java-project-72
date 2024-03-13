package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.util.Collections;

public class UrlsControllerFront {

    public static void showMainPage(Context ctx) {
        BasePage page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
    public static  void showAllUrls(Context ctx) throws SQLException {
        var page = new UrlsPage();
        var dataForPage = UrlCheckRepository.findLastCheckForEachUrl();

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        page.setDataLastCheckForEachUrl(dataForPage);

        ctx.render("urls/showByAll.jte", Collections.singletonMap("page", page));
    }

    public static void showUrlById(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId)
                    .orElseThrow(() -> new NotFoundResponse("urlId not found"));
        var page = new UrlPage();
        var urlsCheck = UrlCheckRepository.findChecksByUrlId(urlId);

        page.setUrl(url);
        page.setUrlChecks(urlsCheck);

        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("urls/showById.jte", Collections.singletonMap("page", page));
    }
}
