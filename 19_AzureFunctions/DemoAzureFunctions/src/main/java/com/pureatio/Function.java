package com.pureatio;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.microsoft.azure.functions.annotation.*;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    // リソース
    private static ResourceBundle appBundle = ResourceBundle.getBundle("application");
    // Jackson
    private static ObjectMapper jsonMapper = new ObjectMapper();

    /**
     * メンバー検索
     * 
     * @param request リクエスト
     * @param context コンテキスト
     * @return 検索結果
     */
    @FunctionName("searchMember")
    public HttpResponseMessage searchMember(@HttpTrigger(name = "req", methods = { HttpMethod.GET,
            HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        String query = request.getQueryParameters().get("id");
        String id = request.getBody().orElse(query);

        if (id == null) {
            // 全メンバーを取得する。
            try (Connection con = DriverManager.getConnection(this.getConnectionString())) {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM [dbo].[SAMPLE];");
                ResultSet result = statement.executeQuery();

                List<Member> members = this.getMembers(result);

                return request.createResponseBuilder(HttpStatus.OK).body(jsonMapper.writeValueAsString(members))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure search Members.")
                        .build();
            }
        } else {
            // 特定のメンバーを取得する。
            try (Connection con = DriverManager.getConnection(this.getConnectionString())) {
                PreparedStatement statement = con.prepareStatement("SELECT * FROM [dbo].[SAMPLE] WHERE id = ?;");
                statement.setString(1, id);
                ResultSet result = statement.executeQuery();

                List<Member> members = this.getMembers(result);

                return request.createResponseBuilder(HttpStatus.OK).body(jsonMapper.writeValueAsString(members))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(String.format("Failure search Members. %s", e.getMessage())).build();
            }
        }
    }

    /**
     * メンバー追加
     * 
     * @param request リクエスト
     * @param context コンテキスト
     * @return 追加結果
     */
    @FunctionName("addMember")
    public HttpResponseMessage addMember(@HttpTrigger(name = "req", methods = {
            HttpMethod.POST }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        String body = request.getBody().get();

        try (Connection con = DriverManager.getConnection(this.getConnectionString())) {
            Properties props = this.getBodyProperties(body);

            PreparedStatement statement = con
                    .prepareStatement("INSERT INTO [dbo].[SAMPLE] (id, name, age) VALUES (?, ?, ?);");
            String id = RandomStringUtils.randomNumeric(10);
            statement.setString(1, id);
            statement.setString(2, props.getProperty("name"));
            statement.setInt(3, Integer.valueOf(props.getProperty("age")));

            int insertCount = statement.executeUpdate();
            if (insertCount == 0) {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure add Members.")
                        .build();
            }

            return request.createResponseBuilder(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Failure add Members. %s", e.getMessage())).build();
        }
    }

    /**
     * メンバー更新
     * 
     * @param request リクエスト
     * @param context コンテキスト
     * @return 更新結果
     */
    @FunctionName("updateMember")
    public HttpResponseMessage updateMember(@HttpTrigger(name = "req", methods = {
            HttpMethod.PUT }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        String body = request.getBody().get();

        try (Connection con = DriverManager.getConnection(this.getConnectionString())) {
            Properties props = this.getBodyProperties(body);

            PreparedStatement statement = con
                    .prepareStatement("UPDATE [dbo].[SAMPLE] SET name = ?, age = ? WHERE id = ?;");
            statement.setString(1, props.getProperty("name"));
            statement.setInt(2, Integer.valueOf(props.getProperty("age")));
            statement.setString(3, props.getProperty("id"));

            int updateCount = statement.executeUpdate();
            if (updateCount == 0) {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure update Members.")
                        .build();
            }

            return request.createResponseBuilder(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Failure update Members. %s", e.getMessage())).build();
        }
    }

    /**
     * メンバー削除
     * 
     * @param request リクエスト
     * @param context コンテキスト
     * @return 削除結果
     */
    @FunctionName("deleteMember")
    public HttpResponseMessage deleteMember(@HttpTrigger(name = "req", methods = {
            HttpMethod.DELETE }, authLevel = AuthorizationLevel.FUNCTION) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        String query = request.getQueryParameters().get("id");
        String id = request.getBody().orElse(query);

        try (Connection con = DriverManager.getConnection(this.getConnectionString())) {
            PreparedStatement statement = con.prepareStatement("DELETE FROM [dbo].[SAMPLE] WHERE id = ?;");
            statement.setString(1, id);

            int deleteCount = statement.executeUpdate();
            if (deleteCount == 0) {
                return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure delete Members.")
                        .build();
            }

            return request.createResponseBuilder(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(String.format("Failure delete Members. %s", e.getMessage())).build();
        }
    }

    /**
     * データベースの接続文字列を取得する。
     * 
     * @return 接続文字列
     */
    private String getConnectionString() {
        return appBundle.getString("database.url");
    }

    /**
     * リクエストパラメータ取得
     * 
     * @param body ボディ
     * @return ボディから取得したリクエストパラメータ
     * @throws UnsupportedEncodingException
     */
    private Properties getBodyProperties(String body) throws UnsupportedEncodingException {

        String[] props = body.split("&");
        Properties properties = new Properties();

        for (String prop : props) {
            String[] values = prop.split("=");
            properties.put(values[0], URLDecoder.decode(values[1], "UTF-8"));
        }

        return properties;
    }

    /**
     * メンバー設定
     * 
     * @param result SQL検索結果
     * @return エンティティに詰めなおした検索結果
     */
    private List<Member> getMembers(ResultSet result) throws SQLException {

        List<Member> members = new ArrayList<>();
        while (result.next()) {
            Member member = new Member();
            member.setId(result.getString(1));
            member.setName(result.getString(2));
            member.setAge(result.getInt(3));
            members.add(member);
        }

        return members;
    }
}
