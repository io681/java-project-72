@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.utils.NamedRoutes
@import hexlet.code.utils.TimestampFormatter

@param UrlPage page

@template.layout.page(

content = @`

    <div class="container-lg mt-5">

    <h1>Сайт: ${page.getUrl().getName()}</h1>

    <table class="table table-bordered table-hover mt-3">
        <tbody>
        <tr>
            <td>ID</td>
            <td>${page.getUrl().getId()}</td>
        </tr>
        <tr>
            <td>Имя</td>
            <td>${page.getUrl().getName()}</td>
        </tr>
        <tr>
            <td>Дата создания</td>
            <td>${TimestampFormatter.getTimeStampToString(page.getUrl().getCreatedAt())}</td>
        </tr>
        </tbody>
    </table>

    <h2>Проверки</h2>
    <form action="${NamedRoutes.checkPathById(page.getUrl().getId())}" method="post">
        <input type="submit" class="btn btn-primary" value="Запустить проверку" name="urlCheck"/>
    </form>

    <table class="table table-bordered table-hover mt-3">
        <thead>
        <th class="col-1">ID</th>
        <th class="col-1">Код ответа</th>
        <th>title</th>
        <th>h1</th>
        <th>description</th>
        <th class="col-2">Дата проверки</th>
        </thead>

    @if(page.getUrlChecks() != null)
        @for (var urlCheck : page.getUrlChecks())
        <tbody>
            <tr>
                <td>${urlCheck.getId()}</td>
                <td>${urlCheck.getStatusCode()}</td>
                <td>${urlCheck.getTitle()}</td>
                <td>${urlCheck.getH1()}</td>
                <td>${urlCheck.getDescription()}</td>
                <td>${TimestampFormatter.getTimeStampToString(urlCheck.getCreatedAt())}</td>
            </tr>
        </tbody>
        @endfor
    @endif
    </table>
    </div>
`,
)