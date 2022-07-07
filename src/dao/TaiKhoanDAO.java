package dao;

import entity.TaiKhoan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {
    public static TaiKhoan taiKhoan;
    public static Connection con;

    // Phương thức KiemTraDangNhap() dùng để kiểm tra thông tin đăng nhập
    public TaiKhoan kiemTraDangNhap(String tenTK, String matKhau) {
        ConnectDatabase dbDAO = new ConnectDatabase();
        dbDAO.connect();
        con = ConnectDatabase.getConnection();
        PreparedStatement stmt = null;
        String sql = "select * from TaiKhoan where tentaikhoan like ? and matkhau like ?";

        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, tenTK);
            stmt.setString(2, matKhau);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                taiKhoan = new TaiKhoan();
                taiKhoan.setTenTaiKhoan(rs.getString("tentaikhoan"));
                taiKhoan.setMatKhau(rs.getString("matkhau"));
            }
            stmt.close();
            con.close();
            return taiKhoan;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return null;
    }
}