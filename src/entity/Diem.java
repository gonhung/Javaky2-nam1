package entity;

public class Diem {
    private  String ma;
    private String ten;
    private float toan, van, anh, tongDiem;
    private String ketQua, ghiChu;


    public  Diem() {

    }

    public Diem(String ma, String ten, float toan, float van, float anh, float tongDiem, String ketQua, String ghiChu) {
        this.ma = ma;
        this.ten = ten;
        this.toan = toan;
        this.van = van;
        this.anh = anh;
        this.tongDiem = tongDiem;
        this.ketQua = ketQua;
        this.ghiChu = ghiChu;


    }
    public Diem(float toan, float van, float anh) {
        this.toan = toan;
        this.van = van;
        this.anh = anh;
    }



    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public float getToan() {
        return toan;
    }

    public void setToan(float toan) {
        this.toan = toan;
    }

    public float getVan() {
        return van;
    }

    public void setVan(float van) {
        this.van = van;
    }

    public float getAnh() {
        return anh;
    }

    public void setAnh(float anh) {
        this.anh = anh;
    }

    public float getTongdiem() {
        return this.getToan() + this.getVan()+ this.getAnh() ;
    }

    public String getKetQua() {
        if(getTongDiem() >=25)
            return "Giỏi";
        else if (getTongDiem() >=20 && getTongDiem() <=25)
            return "Khá";
        else
        return "Yếu";



    }



    public float getTongDiem() {
        return tongDiem;
    }

    public void setTongDiem(float tongDiem) {
        this.tongDiem = tongDiem;
    }


    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}
