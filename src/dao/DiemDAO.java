package dao;

import entity.Diem;

import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class DiemDAO {
    public DiemDAO() {
        // TODO Auto-generated constructor stub
        ConnectDatabase.getInstance().connect();
    }

    /**
     * Lấy dữ liệu từ cơ sở dữ liệu
     *
     * @return danh sách điểm
     */
    public List<Diem> getDiem() {
        List<Diem> list = new ArrayList<>();
        try {
            Connection con = ConnectDatabase.getConnection();
            String sql = "SELECT * FROM Diem";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String ma = rs.getString(1);
                String ten = rs.getString(2);
                Float toan = rs.getFloat(3);
                Float van = rs.getFloat(4);
                Float anh = rs.getFloat(5);
                Float tongDiem = rs.getFloat(6);
                String ketQua = rs.getString(7);
                String ghiChu = rs.getString(8);
                Diem tg = new Diem(toan, van, anh);

                Diem diem = new Diem(ma, ten, toan, van, anh, tg.getTongdiem(), tg.getKetQua(), ghiChu);
                list.add(diem);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Thêm dữ liệu vào cơ sở dữ liệu
     *
     * @param ten
     * @param toan
     * @param van
     * @param anh

     * @throws ParseException
     */
    public void them(String Ma,String ten, float toan, float van, float anh) throws ParseException {
        Connection con = ConnectDatabase.getConnection();
        String sql = "INSERT Diem(Ma,TenHocSinh, Toan, Van, Anh) VALUES (?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            stmt.setString(1, Ma);
            stmt.setString(2, ten);
            stmt.setFloat(3, toan);
            stmt.setFloat(4, van);
            stmt.setFloat(5, anh);

            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Sua du lieu
     *
     * @param ma
     * @param ten
     * @param toan
     * @param van
     * @param anh
     * @param tongDiem
     * @param ketQua
     * @param ghiChu
     */
    public void sua(String ma, String ten, float toan, float van, float anh, float tongDiem, String ketQua, String ghiChu) {
        Connection con = ConnectDatabase.getConnection();
        String sql = "UPDATE Diem\r\n" +
                "SET TenHocSinh = ?, Toan = ?, Van = ?, Anh = ?, TongDiem = ?, KetQua = ?, GhiChu = ?\r\n" +
                "WHERE Ma = ?";
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);
            System.out.println(sql);
            stmt.setString(1, ten);
            stmt.setFloat(2, toan);
            stmt.setFloat(3, van);
            stmt.setFloat(4, anh);
            stmt.setFloat(5, tongDiem);
            stmt.setString(6, ketQua);
            stmt.setString(7, ghiChu);
            stmt.setString(8, ma);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void Xoa(String ma){
        Connection con = ConnectDatabase.getConnection();
        String sql = "DELETE FROM Diem WHERE Ma = ?" ;

        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement(sql);


            stmt.setString(1, ma);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    /**
     * Lấy dữ liệu điểm bằng mã
     *
     * @param maTim
     * @return
     */
    public Diem getDiembyMa(String maTim) {
        Diem d = new Diem();
        try {
            Connection con = ConnectDatabase.getConnection();
            String sql = "SELECT *\r\n" +
                    "FROM Diem\r\n" +
                    "WHERE Ma = '" + maTim + "'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String ma = rs.getString(1);
                String ten = rs.getString(2);
                Float toan = rs.getFloat(3);
                Float van = rs.getFloat(4);
                Float anh = rs.getFloat(5);
                Float tongDiem = rs.getFloat(6);
                String ketQua = rs.getString(7);
                String ghiChu = rs.getString(8);
                Diem diem = new Diem(ma, ten, toan, van, anh, tongDiem, ketQua, ghiChu);
                d = diem;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return d;
    }

    /**
     * Lấy dữ liệu điểm bằng tên
     *
     * @param tenTim
     * @return danh sách điểm thỏa mãn điều kiện
     */
    public List<Diem> getDiembyTen(String tenTim) {
        List<Diem> list = new ArrayList<Diem>();
        try {
            Connection con = ConnectDatabase.getConnection();
            String sql = "SELECT *\r\n" +
                    "FROM Diem\r\n" +
                    "where TenHocSinh LIKE N'%" + tenTim + "%'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String ma = rs.getString(1);
                String ten = rs.getString(2);
                Float toan = rs.getFloat(3);
                Float van = rs.getFloat(4);
                Float anh = rs.getFloat(5);
                Float tongDiem = rs.getFloat(6);
                String ketQua = rs.getString(7);
                String ghiChu = rs.getString(8);
                Diem diem = new Diem(ma, ten, toan, van, anh, tongDiem, ketQua, ghiChu);
                list.add(diem);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Lấy dữ liệu điểm bằng kết quả
     * @param ketQuaTim
     * @return
     *
     */
    public List<Diem> getDiembyKetQua(String ketQuaTim) {
        List<Diem> list= new ArrayList<Diem>();
        try {
            Connection con = ConnectDatabase.getConnection();
            String sql = "SELECT *\r\n" +
                    "FROM Diem" +
                    "" +
                    "" +
                    "" +
                    "*r\n" +
                    "WHERE KetQua LIKE N'%" + ketQuaTim + "%'";
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                String ma = rs.getString(1);
                String ten = rs.getString(2);
                Float toan = rs.getFloat(3);
                Float van = rs.getFloat(4);
                Float anh = rs.getFloat(5);
                Float tongDiem = rs.getFloat(6);
                String ketQua = rs.getString(7);
                String ghiChu = rs.getString(8);
                Diem diem = new Diem(ma, ten, toan, van, anh, tongDiem, ketQua, ghiChu);
                list.add(diem);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return list;
    }
}

