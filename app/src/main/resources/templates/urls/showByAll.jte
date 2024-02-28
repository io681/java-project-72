@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <a>Сайты</a>
    @for(var url : page.getUrls())
        <div>
            <p>${url.getId()}</p>
            <h2><a href="/">${url.getName()}</a></h2>
        </div>
    @endfor
`,
)