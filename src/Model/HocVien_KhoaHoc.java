/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author MSI
 */
public class HocVien_KhoaHoc {

    int id;
    int maKH;
    String maHV;
    double diem;
    boolean sendResult;

    public HocVien_KhoaHoc() {
    }

    public HocVien_KhoaHoc(int maKH, String maHV) {
        this.maKH = maKH;
        this.maHV = maHV;
    }

    public HocVien_KhoaHoc(int maKH, String maHV, double diem) {
        this.maKH = maKH;
        this.maHV = maHV;
        this.diem = diem;
    }

    public HocVien_KhoaHoc(int id, int maKH, String maHV, double diem, boolean sendResult) {
        this.id = id;
        this.maKH = maKH;
        this.maHV = maHV;
        this.diem = diem;
        this.sendResult = sendResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaHV() {
        return maHV;
    }

    public void setMaHV(String maHV) {
        this.maHV = maHV;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public boolean isSendResult() {
        return sendResult;
    }

    public void setSendResult(boolean sendResult) {
        this.sendResult = sendResult;
    }

    public String getXepLoai() {
        if (this.getDiem() >= 8) {
            return "Giỏi";
        } else if (this.getDiem() >= 6.5) {
            return "Khá";
        } else if (this.getDiem() >= 5) {
            return "Trung bình";
        } else if (this.getDiem() >= 3.5) {
            return "Yếu";
        } else if (this.getDiem() >= 0) {
            return "Kém";
        } else {
            return "";
        }
    }
}
