package hexlet.code.repositories;

import hexlet.code.models.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UrlCheckRepository extends BaseRepository {
    public static void runCheck(UrlCheck urlCheck) throws SQLException {
        String sql = "INSERT INTO url_checks (status_code, title, h1, description, url_Id, created_at)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, urlCheck.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            // Устанавливаем ID в сохраненную сущность
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> findChecksByUrlId(Long urlId) throws SQLException {
        final var sql = "SELECT * FROM url_checks WHERE url_Id = ? ORDER BY created_at";
        List<UrlCheck> result = new ArrayList<>();
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);
            var resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck();

                urlCheck.setId(id);
                urlCheck.setStatusCode(statusCode);
                urlCheck.setTitle(title);
                urlCheck.setH1(h1);
                urlCheck.setDescription(description);
                urlCheck.setUrlId(urlId);
                urlCheck.setCreatedAt(createdAt);

                result.add(urlCheck);
            }
            return result;
        }
    }
}
