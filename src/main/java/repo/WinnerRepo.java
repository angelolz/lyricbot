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
    public static List<Request> getWinners(int season) throws SQLException
    {
        List<Request> winners = new ArrayList<>();

        String sql = "SELECT * FROM Winner WHERE season = ? ORDER BY added";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setInt(1, season);

            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                Request request = new Request()
                    .setUserId(rs.getLong("user_id"))
                    .setTitle(rs.getString("title"));

                winners.add(request);
            }
        }

        return winners;
    }

    public static void addWinner(Request winningRequest, int season) throws SQLException
    {
        String sql = "INSERT INTO Winner (user_id, title, season) VALUES (?,?,?)";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, winningRequest.getUserId());
            pst.setString(2, winningRequest.getTitle());
            pst.setInt(3, season);

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
