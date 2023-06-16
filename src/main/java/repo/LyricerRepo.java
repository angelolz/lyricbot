package repo;

import dataobjects.Lyricer;
import lombok.Cleanup;
import main.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LyricerRepo
{
    public static boolean doesExist(long userId) throws SQLException
    {
        String sql = "SELECT * FROM Lyricer WHERE user_id = ? LIMIT 1";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);

            @Cleanup ResultSet rs = pst.executeQuery();

            return rs.next();
        }
    }

    public static Lyricer getLyricer(long userId) throws SQLException
    {
        String sql = "SELECT * FROM Lyricer WHERE user_id = ? LIMIT 1";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);

            @Cleanup ResultSet rs = pst.executeQuery();

            if(!rs.next())
                return null;

            return new Lyricer()
                .setUserId(rs.getLong("user_id"))
                .setLink(rs.getString("link"))
                .setBanned(rs.getBoolean("banned"));
        }
    }

    public static void addLyricer(long userId) throws SQLException
    {
        String sql = "INSERT INTO Lyricer (user_id) VALUES (?)";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, userId);
            pst.executeUpdate();
        }
    }

    public static void addLyricer(Lyricer lyricer) throws SQLException
    {
        String sql = "INSERT INTO Lyricer (user_id, link) VALUES (?,?)";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, lyricer.getUserId());
            pst.setString(2, lyricer.getLink());
            pst.executeUpdate();
        }
    }

    public static void updateBanned(long userId, boolean banned) throws SQLException
    {
        String sql = "UPDATE Lyricer SET banned = ? WHERE user_id = ?";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setBoolean(1, banned);
            pst.setLong(2, userId);
            pst.executeUpdate();
        }
    }
}
