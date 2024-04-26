package repo;

import main.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusRepo
{
    private static void createOpenStatus() throws SQLException
    {
        String sql = "INSERT INTO Status (open) VALUES(0)";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql))
        {
            pst.executeUpdate();
        }
    }

    public static boolean isOpen() throws SQLException
    {
        String sql = "SELECT * FROM Status";
        try(Connection con = DatabaseManager.getDataSource().getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery())
        {
            if(rs.next())
                return rs.getBoolean("open");
            else {
                createOpenStatus();
                return false;
            }
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
