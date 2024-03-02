@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.utils.TimestampFormatter
@import hexlet.code.utils.NamedRoutes
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <div class="container-lg mt-5">
        <h1>Сайты</h1>

        <table class="table table-bordered table-hover mt-3">
            <thead>
            <tr>
                <th class="col-1">ID</th>
                <th>Имя</th>
                <th class="col-2">Последняя проверка</th>
                <th class="col-1">Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @if(page.getUrls() != null)
                @for(var url : page.getUrls())
                    <tr>
                        <td>${url.getId()}</td>
                        <td>
                            <a href="${NamedRoutes.showUrlById(url.getId())}">${url.getName()}</a>
                        </td>

                        @if(!page.getUrlsCheckMap().get(Long.toString(url.getId())).isEmpty())
                            <td>${page.getUrlsCheckMap().get(Long.toString(url.getId())).get(0).getStatusCode()}</td>
                            <td>
                                ${TimestampFormatter.getTimeStampToString(page.getUrlsCheckMap().get(Long.toString(url.getId())).get(0).getCreatedAt())}
                            </td>
                        @else
                            <td></td>
                            <td></td>
                        @endif
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    </div>
`,
)