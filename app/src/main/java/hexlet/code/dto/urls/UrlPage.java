package hexlet.code.dto.urls;

import hexlet.code.models.Url;
import hexlet.code.models.UrlCheck;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UrlPage {
    private Url url;
    private List<UrlCheck> urlCheck;
}
