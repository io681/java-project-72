@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.utils.TimestampFormatter
@import hexlet.code.utils.NamedRoutes
@param UrlsPage page

@template.layout.page(
page = page,
content = @`
    <section>
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
                @if(!page.getDataLastCheckForEachUrl().isEmpty())
                    @for(var entry : page.getDataLastCheckForEachUrl().entrySet())
                        <tr>
                            <td>${entry.getKey().getId()}</td>
                            <td>
                                <a href="${NamedRoutes.showUrlById(entry.getKey().getId())}">${entry.getKey().getName()}</a>
                            </td>

                            @if (entry.getValue().getCreatedAt() != null)
                            <td>
                                    ${TimestampFormatter.getTimeStampToString(entry.getValue().getCreatedAt())}
                            </td>
                            @else
                                <td></td>
                            @endif

                            @if(entry.getValue().getStatusCode() != 0)
                                <td>${entry.getValue().getStatusCode()}</td>
                            @else
                                <td></td>
                            @endif
                        </tr>
                    @endfor
                @endif
                </tbody>

<%--                <tbody>--%>
<%--                @if(page.getUrls() != null)--%>
<%--                    @for(var url : page.getUrls())--%>
<%--                        <tr>--%>
<%--                            <td>${url.getId()}</td>--%>
<%--                            <td>--%>
<%--                                <a href="${NamedRoutes.showUrlById(url.getId())}">${url.getName()}</a>--%>
<%--                            </td>--%>

<%--                            @if(page.getUrlsCheckMap() != null && page.getUrlsCheckMap().get(Long.toString(url.getId())) != null)--%>
<%--                                <td>--%>
<%--                                    ${TimestampFormatter.getTimeStampToString(page.getUrlsCheckMap().get(Long.toString(url.getId())).getCreatedAt())}--%>
<%--                                </td>--%>
<%--                                <td>${page.getUrlsCheckMap().get(Long.toString(url.getId())).getStatusCode()}</td>--%>
<%--                            @else--%>
<%--                                <td></td>--%>
<%--                                <td></td>--%>
<%--                            @endif--%>
<%--                        </tr>--%>
<%--                    @endfor--%>
<%--                @endif--%>
<%--                </tbody>--%>
            </table>
        </div>
    </section>
`,
)