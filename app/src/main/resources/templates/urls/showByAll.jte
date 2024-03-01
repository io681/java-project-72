@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.utils.TimestampFormatter
@import hexlet.code.utils.NamedRoutes
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <h2>Сайты</h2>
    @for(var url : page.getUrls())
        <div>
            <h4>${url.getId()}</h4>
            <h4><a href="${NamedRoutes.showUrlById(url.getId())}">${url.getName()}</a></h4>
            <h4>${TimestampFormatter.getTimeStampToString(url.getCreatedAt())}</h4>

        @if(!page.getUrlsCheckMap().get(Long.toString(url.getId())).isEmpty())
            <h4>${page.getUrlsCheckMap().get(Long.toString(url.getId())).get(0).getStatusCode()}</h4>
            <h4>${TimestampFormatter.getTimeStampToString(page.getUrlsCheckMap().get(Long.toString(url.getId())).get(0).getCreatedAt())}</h4>
        @endif
        </div>
    @endfor
`,
)