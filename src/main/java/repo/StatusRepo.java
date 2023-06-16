package repo;

import main.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusRepo
{
    public static long getTime() throws SQLException
    {
        String sql = "SELECT * FROM Status";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery())
        {
            rs.next();

            return rs.getLong("time");
        }
    }

    public static boolean isOpen() throws SQLException
    {
        String sql = "SELECT * FROM Status";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery())
        {
            rs.next();

            return rs.getBoolean("open");
        }
    }

    public static void setTime(long time) throws SQLException
    {
        String sql = "UPDATE Status set time = ?";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setLong(1, time);
            pst.executeUpdate();
        }
    }

    public static void setOpen(boolean open) throws SQLException
    {
        String sql = "UPDATE Status set open = ?";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.setBoolean(1, open);
            pst.executeUpdate();
        }
    }
}
