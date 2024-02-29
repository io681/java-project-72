package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.Collections;

public class UrlsControllerFront {

    public static void showMainPage(Context ctx) {
        ctx.render("index.jte");
    }
    public static  void showAllUrls(Context ctx) throws SQLException {
        var urlsList = UrlRepository.getEntities();
        var page = new UrlsPage(urlsList);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("urls/showByAll.jte", Collections.singletonMap("page", page));
    }

    public static void showUrlById(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id).get();
//                    .orElseThrow(() -> new NotFoundResponse("Post not found"));
        var page = new UrlPage(url);
        ctx.render("urls/showById.jte", Collections.singletonMap("page", page));
    }
}
