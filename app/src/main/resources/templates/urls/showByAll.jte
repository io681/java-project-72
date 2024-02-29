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
        </div>
    @endfor
`,
)