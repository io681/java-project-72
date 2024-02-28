@import gg.jte.Content
@import hexlet.code.dto.BasePage
@param Content content
@param BasePage page = null

<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
              crossorigin="anonymous">
        <title>Hexlet project</title>
    </head>
    <body>
    <div class="mx-auto p-4 py-md-5">
        <main>
            <a>Анализатор страниц</a>
        </main>
    </div>
    @if(page != null && page.getFlash() != null)
        @if(page.getFlash().contains("успешно"))
            <p class="alert alert-success" role="alert">${page.getFlash()}</p>
        @else
            <p class="alert alert-danger" role="alert">${page.getFlash()}</p>
        @endif
    @endif
    ${content}
    </body>
</html>