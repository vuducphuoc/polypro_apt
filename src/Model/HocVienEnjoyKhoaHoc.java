/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * Khóa học mà học viên đang tham gia
 */
public class HocVienEnjoyKhoaHoc {
    int maHV_KH;
    int maKH;
    String tenCĐ;
    int thoiLuong;
    int hocPhi;
    String ngayKG;
    double diem;

    public HocVienEnjoyKhoaHoc() {
    }

//    public HocVienEnjoyKhoaHoc(int maHV_KH, String tenCĐ, int thoiLuong, int hocPhi, String ngayKG, double diem) {
//        this.maHV_KH = maHV_KH;
//        this.tenCĐ = tenCĐ;
//        this.thoiLuong = thoiLuong;
//        this.hocPhi = hocPhi;
//        this.ngayKG = ngayKG;
//        this.diem = diem;
//    }

    public HocVienEnjoyKhoaHoc(int maHV_KH, int maKH, String tenCĐ, int thoiLuong, int hocPhi, String ngayKG, double diem) {
        this.maHV_KH = maHV_KH;
        this.maKH = maKH;
        this.tenCĐ = tenCĐ;
        this.thoiLuong = thoiLuong;
        this.hocPhi = hocPhi;
        this.ngayKG = ngayKG;
        this.diem = diem;
    }
    

    public int getMaHV_KH() {
        return maHV_KH;
    }

    public void setMaHV_KH(int maHV_KH) {
        this.maHV_KH = maHV_KH;
    }

    public String getTenCĐ() {
        return tenCĐ;
    }

    public void setTenCĐ(String tenCĐ) {
        this.tenCĐ = tenCĐ;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public int getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(int hocPhi) {
        this.hocPhi = hocPhi;
    }

    public String getNgayKG() {
        return ngayKG;
    }

    public void setNgayKG(String ngayKG) {
        this.ngayKG = ngayKG;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }
    
    
}
