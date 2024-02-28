@template.layout.page(
content = @`
    <form action="/urls" method="post">
        <div class="mb-3">
            <label class="form-label">
                Ссылка
                <input type="text" class="form-control" name="url" />
            </label>
        </div>
        <input type="submit" class="btn btn-primary" value="Проверить" />
    </form>
`
)