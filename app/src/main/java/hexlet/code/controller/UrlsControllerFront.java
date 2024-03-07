package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.models.UrlCheck;
import hexlet.code.repositories.UrlCheckRepository;
import hexlet.code.repositories.UrlRepository;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;


public class UrlsControllerFront {

    public static void showMainPage(Context ctx) {
        BasePage page = new BasePage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        ctx.render("index.jte", Collections.singletonMap("page", page));
    }
    public static  void showAllUrls(Context ctx) throws SQLException {
        var urlsList = UrlRepository.getEntities();
        var page = new UrlsPage();

        Map<String, UrlCheck> urlsCheckMap = new HashMap<>();

        for (var url : urlsList) {
            var checks = UrlCheckRepository.findChecksByUrlId(url.getId());
            if (!checks.isEmpty()) {
                urlsCheckMap.put(Long.toString(url.getId()), checks.get(checks.size() - 1));
            }
        }

        page.setUrls(urlsList);
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));

        if (!urlsCheckMap.isEmpty()) {
            page.setUrlsCheckMap(urlsCheckMap);
        }


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
