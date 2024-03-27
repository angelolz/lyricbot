package repo;

import dataobjects.Request;
import lombok.Cleanup;
import main.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RequestRepo
{
    public static List<Request> getRequests() throws SQLException
    {
        List<Request> requests = new ArrayList<>();

        String sql = "SELECT * FROM Request";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery())
        {
            while(rs.next())
            {
                Request request = new Request()
                    .setUserId(rs.getLong("user_id"))
                    .setLink(rs.getString("link"))
                    .setTitle(rs.getString("title"));

                requests.add(request);
            }
        }

        return requests;
    }

    public static Request getRequest(long userId) throws SQLException
    {
        String sql = "SELECT * FROM Request WHERE user_id = ?";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);

            ResultSet rs = pst.executeQuery();

            if(!rs.next()) return null;

            return new Request()
                .setUserId(rs.getLong("user_id"))
                .setLink(rs.getString("link"))
                .setTitle(rs.getString("title"));
        }
    }

    public static void addRequest(Request request) throws SQLException
    {
        String sql = "INSERT INTO Request (user_id, link, title) VALUES (?,?,?)";

        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, request.getUserId());
            pst.setString(2, request.getLink());
            pst.setString(3, request.getTitle());

            pst.executeUpdate();
        }
    }

    public static boolean hasRequest(long userId) throws SQLException
    {
        String sql = "SELECT * FROM Request WHERE user_id = ? LIMIT 1";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);

            @Cleanup ResultSet rs = pst.executeQuery();

            return rs.next();
        }
    }

    public static void updateRequest(Request request) throws SQLException
    {
        String sql = "UPDATE Request SET link = ?, title = ? WHERE user_id = ?";

        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setString(1, request.getLink());
            pst.setString(2, request.getTitle());
            pst.setLong(3, request.getUserId());

            pst.executeUpdate();
        }
    }

    public static void deleteRequest(long userId) throws SQLException
    {
        String sql = "DELETE FROM Request WHERE user_id = ?";

        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);
            pst.executeUpdate();
        }
    }
}
