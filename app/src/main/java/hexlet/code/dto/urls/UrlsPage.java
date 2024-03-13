package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class UrlsPage extends BasePage {
    private Map<Url, UrlCheck> dataLastCheckForEachUrl = new HashMap<>();
}
