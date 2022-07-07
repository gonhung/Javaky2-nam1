package dao;

import entity.TaiKhoan;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaoTaiKhoanDAO {
    public TaoTaiKhoanDAO() {
        ConnectDatabase.getInstance().connect();
    }
    public void taoTaiKhoan(String tenTK, String matKhau) {
        Connection con = ConnectDatabase.getConnection();
        String sql = "INSERT TaiKhoan (TenTaiKhoan, MatKhau) VALUES (?, ?)";
        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, tenTK);
            stmt.setString(2, matKhau);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


}
