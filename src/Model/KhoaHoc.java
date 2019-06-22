/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Helper.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI
 */
public class KhoaHoc {

    int id;
    String maCD;
    int hocPhi;
    int thoiLuong;
    String ngayKG;
    String ghiChu;
    String ngayTao;
    boolean sendResult;

    JdbcHelper jdbcHelper = new JdbcHelper();

    public KhoaHoc() {
    }

    public KhoaHoc(int id, String maCD, String ngayKG) {
        this.id = id;
        this.maCD = maCD;
        this.ngayKG = ngayKG;
    }

    public KhoaHoc(String maCD, int hocPhi, int thoiLuong, String ngayKG, String ghiChu) {
        this.maCD = maCD;
        this.hocPhi = hocPhi;
        this.thoiLuong = thoiLuong;
        this.ngayKG = ngayKG;
        this.ghiChu = ghiChu;
    }

    public KhoaHoc(int id, String maCD, int hocPhi, int thoiLuong, String ngayKG, String ghiChu, String ngayTao, boolean sendResult) {
        this.id = id;
        this.maCD = maCD;
        this.hocPhi = hocPhi;
        this.thoiLuong = thoiLuong;
        this.ngayKG = ngayKG;
        this.ghiChu = ghiChu;
        this.ngayTao = ngayTao;
        this.sendResult = sendResult;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaCD() {
        return maCD;
    }

    public void setMaCD(String maCD) {
        this.maCD = maCD;
    }

    public int getHocPhi() {
        return hocPhi;
    }

    public void setHocPhi(int hocPhi) {
        this.hocPhi = hocPhi;
    }

    public int getThoiLuong() {
        return thoiLuong;
    }

    public void setThoiLuong(int thoiLuong) {
        this.thoiLuong = thoiLuong;
    }

    public String getNgayKG() {
        return ngayKG;
    }

    public void setNgayKG(String ngayKG) {
        this.ngayKG = ngayKG;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public boolean isSendResult() {
        return sendResult;
    }

    public void setSendResult(boolean sendResult) {
        this.sendResult = sendResult;
    }

    @Override
    public String toString() {
        String sql = "SELECT tenCD FROM dbo.ChuyenDe WHERE id=?";
        String tenCD = "";
        try {
            ResultSet rs = jdbcHelper.executeQuery(sql, this.getMaCD());

            if (rs.next()) {
                tenCD = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(KhoaHoc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tenCD + " (" + this.getNgayKG() + ")";
    }
}
