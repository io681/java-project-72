@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.utils.NamedRoutes
@import hexlet.code.utils.TimestampFormatter

@param UrlPage page

@template.layout.page(

content = @`
    <div class="mb-3">
        <h1>${page.getUrl().getId()}</h1>
    </div>

    <div class="mb-3">
        ${page.getUrl().getName()}
    </div>

    <h2>Проверки</h2>
    <form action="${NamedRoutes.checkPathById(page.getUrl().getId())}" method="post">
        <input type="submit" class="btn btn-primary" value="Запустить проверку" name="urlCheck"/>
<%--        <div class="mb-3">--%>
<%--            <label class="form-label">--%>
<%--                Ссылка--%>
<%--                <input type="text" class="form-control" name="urlCheck" />--%>
<%--            </label>--%>
<%--        </div>--%>
    </form>
    @if(page.getUrlCheck() != null)
        @for (var urlCheck : page.getUrlCheck())
            <div>
                <h4>${urlCheck.getId()}</h4>
                <h4>${urlCheck.getStatusCode()}</h4>
                <h4>${urlCheck.getTitle()}</h4>
                <h4>${urlCheck.getH1()}</h4>
                <h4>${urlCheck.getDescription()}</h4>
                <h4>${urlCheck.getUrlId()}</h4>
                <h4>${TimestampFormatter.getTimeStampToString(urlCheck.getCreatedAt())}</h4>
            </div>
        @endfor
    @endif
`,
)