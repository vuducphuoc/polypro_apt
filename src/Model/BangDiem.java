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
public class BangDiem {
    int stt;
    String maHV;
    String hoTen;
    String diem;
    String xepLoai;

    public BangDiem() {
    }

    public BangDiem(int stt, String maHV, String hoTen, String diem, String xepLoai) {
        this.stt = stt;
        this.maHV = maHV;
        this.hoTen = hoTen;
        this.diem = diem;
        this.xepLoai = xepLoai;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getMaHV() {
        return maHV;
    }

    public void setMaHV(String maHV) {
        this.maHV = maHV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getDiem() {
        return diem;
    }

    public void setDiem(String diem) {
        this.diem = diem;
    }

    public String getXepLoai() {
        return xepLoai;
    }

    public void setXepLoai(String xepLoai) {
        this.xepLoai = xepLoai;
    }
    
    
}
