package entity;

public class KetQua {
    private float tongdiem;
    private String ketqua;
    public float getTongdiem() {
        return tongdiem;
    }
    public void setTongdiem(float tongdiem) {
        this.tongdiem = tongdiem;
    }
    public String getKetqua() {
        return ketqua;
    }
    public void setKetqua(String ketqua) {
        this.ketqua = ketqua;
    }
    public KetQua() {

    }

    public KetQua(float tongdiem, String ketQua) {
        this.tongdiem = tongdiem;
        this.ketqua = ketQua;
    }
    @Override
    public String toString() {
        return "Ketqua [tongdiem=" + tongdiem + ", ketqua=" + ketqua + ", getTongdiem()=" + getTongdiem()
                + ", getKetqua()=" + getKetqua() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }

}
