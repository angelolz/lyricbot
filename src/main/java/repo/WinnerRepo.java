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

public class WinnerRepo
{
    public static List<Request> getWinners() throws SQLException
    {
        List<Request> winners = new ArrayList<>();

        String sql = "SELECT * FROM Winner ORDER BY added";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery())
        {
            while(rs.next())
            {
                Request request = new Request()
                    .setUserId(rs.getLong("user_id"))
                    .setName(rs.getString("name"))
                    .setTitle(rs.getString("title"));

                winners.add(request);
            }
        }

        return winners;
    }

    public static void addWinner(Request winningRequest) throws SQLException
    {
        String sql = "INSERT INTO Winner (user_id, name, title) VALUES (?,?,?)";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, winningRequest.getUserId());
            pst.setString(2, winningRequest.getName());
            pst.setString(3, winningRequest.getTitle());

            pst.executeUpdate();
        }
    }

    public static boolean isWinner(long userId) throws SQLException
    {
        String sql = "SELECT * FROM Winner WHERE user_id = ? LIMIT 1";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);

            @Cleanup ResultSet rs = pst.executeQuery();

            return rs.next();
        }
    }
}
