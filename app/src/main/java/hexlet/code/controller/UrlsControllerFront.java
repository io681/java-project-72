package hexlet.code.controller;

import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.models.UrlCheck;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class UrlsControllerFront {

    public static void showMainPage(Context ctx) {
        ctx.render("index.jte");
    }
    public static  void showAllUrls(Context ctx) throws SQLException {
        var urlsList = UrlRepository.getEntities();
        var page = new UrlsPage();
        page.setUrls(urlsList);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        Map<String, List<UrlCheck>> urlsCheckMap = new HashMap<>();
        for (var url : urlsList) {
            urlsCheckMap.put(Long.toString(url.getId()), UrlCheckRepository.findChecksByUrlId(url.getId()));

        }

        if (!urlsCheckMap.isEmpty()) {
            page.setUrlsCheckMap(urlsCheckMap);
        }


        ctx.render("urls/showByAll.jte", Collections.singletonMap("page", page));
    }

    public static void showUrlById(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId).get();
//                    .orElseThrow(() -> new NotFoundResponse("Post not found"));
        var page = new UrlPage();
        var urlsCheck = UrlCheckRepository.findChecksByUrlId(urlId);

        page.setUrl(url);
        page.setUrlChecks(urlsCheck);

        ctx.render("urls/showById.jte", Collections.singletonMap("page", page));
    }
}
