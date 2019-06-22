/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Helper.DateHelper;
import Helper.JdbcHelper;
import Model.HocVien;
import Model.HocVienEnjoyKhoaHoc;
import Model.KhoaHoc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MSI
 */
public class HocVienDAO implements InterfaceDAO<HocVien>{
    JdbcHelper jdbcHelper = new JdbcHelper();
    DateHelper dateHelper = new DateHelper();

    @Override
    public int insert(HocVien hv) throws SQLException {
        String sql = "INSERT INTO dbo.HocVien (id,hoTen,ngaySinh,gioiTinh,dienThoai,email,ghiChu,ngayDK) VALUES (?,?,?,?,?,?,?,GETDATE())";
        
        return jdbcHelper.executeUpdate(sql, hv.getId(),hv.getHoTen(),hv.getNgaySinh(),hv.isGioiTinh(),hv.getDienThoai(),hv.getEmail(),hv.getGhiChu());
    }

    @Override
    public int update(HocVien hv) throws SQLException {
        String sql = "UPDATE dbo.HocVien SET hoTen=?,ngaySinh=?,gioiTinh=?,dienThoai=?,email=?,ghiChu=? WHERE id=?";
        
        return jdbcHelper.executeUpdate(sql, hv.getHoTen(),hv.getNgaySinh(),hv.isGioiTinh(),hv.getDienThoai(),hv.getEmail(),hv.getGhiChu(),hv.getId());
    }

    @Override
    public int delete(String id) throws SQLException {
        String sql = "{call sp_xoaHocVien (?)}";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, id);
        
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return -1;
    }

    @Override
    public List<HocVien> get(Object...args) throws SQLException {
        List<HocVien> listHocVien = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.HocVien WHERE id LIKE ? OR hoTen LIKE ? OR dienThoai LIKE ? OR email LIKE ? ORDER BY id";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, "%"+args[0]+"%", "%"+args[0]+"%", "%"+args[0]+"%", "%"+args[0]+"%");
        
        while (rs.next()) {            
            listHocVien.add(readResultSet(rs));
        }
        
        return listHocVien;
    }

    @Override
    public List<HocVien> getAll() throws SQLException {
        List<HocVien> listHocVien = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.HocVien";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        while (rs.next()) {            
            listHocVien.add(readResultSet(rs));
        }
        
        return listHocVien;
    }

    @Override
    public HocVien readResultSet(ResultSet rs) throws SQLException {
        String ngaySinh = "";
        try {
            ngaySinh = dateHelper.castDateForm3ToForm1(rs.getString(3));
        } catch (ParseException ex) {
            Logger.getLogger(HocVienDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new HocVien(rs.getString(1), 
                rs.getString(2), 
                ngaySinh, 
                rs.getBoolean(4), 
                rs.getString(5), 
                rs.getString(6), 
                rs.getString(7), 
                rs.getString(8));
    }
    
    //get list năm học viên đăng ký
    public List<Integer> getListYearDK() throws SQLException{
        List<Integer> listYear = new ArrayList<>();
        
        String sql = "SELECT DISTINCT YEAR(ngayDK) FROM dbo.HocVien ORDER BY YEAR(ngayDK) DESC";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        while (rs.next()) {            
            listYear.add(rs.getInt(1));
        }
        
        return listYear;
    }
    
    //get list học viên theo năm
    public List<HocVien> getByYearDK(int year) throws SQLException{
        List<HocVien> listHocVien = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.HocVien WHERE YEAR(ngayDK) = ? ORDER BY id ";
        
        ResultSet rs = jdbcHelper.executeQuery(sql,year);
        
        while (rs.next()) {            
            listHocVien.add(readResultSet(rs));
        }
        
        return listHocVien;
    }
    
    public String getLastIdHocVien() throws SQLException{
        String lastID = null;
        
        String sql = "SELECT TOP 1 * FROM dbo.HocVien ORDER BY id DESC";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        if (rs.next()) {
            lastID = rs.getString(1);
        }
        
        return lastID;
    }
    
    public boolean checkEmailExistsInDb(String email) throws SQLException{
        String sql = "SELECT * FROM dbo.HocVien WHERE email = ?";
        
        ResultSet rs  = jdbcHelper.executeQuery(sql, email.trim());
        
        if (rs.next()) {
            return true;
        }
        return false;
    }
    
    public boolean checkPhoneExistsInDb(String phone) throws SQLException{
        String sql = "SELECT * FROM dbo.HocVien WHERE dienThoai = ?";
        
        ResultSet rs  = jdbcHelper.executeQuery(sql, phone.trim());
        
        if (rs.next()) {
            return true;
        }
        return false;
    }
    
    public List<Integer> getListYearSearch(Object...args) throws SQLException {
        List<Integer> listYearSearch = new ArrayList<>();
        
        String sql = "SELECT DISTINCT YEAR(ngayDK) FROM dbo.HocVien WHERE id LIKE ? OR hoTen LIKE ? OR dienThoai LIKE ? OR email LIKE ?  ORDER BY YEAR(ngayDK) DESC";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, "%"+args[0]+"%", "%"+args[0]+"%", "%"+args[0]+"%", "%"+args[0]+"%");
        
        while (rs.next()) {            
            listYearSearch.add(rs.getInt(1));
        }
        
        return listYearSearch;
    }
    
    
    //get list khóa học mà học viên đã tham gia
    public List<HocVienEnjoyKhoaHoc> getListKhByHV(String maHV) throws SQLException{
        List<HocVienEnjoyKhoaHoc> list = new ArrayList<>();
        String sql = "{call sp_getListKhByHv (?)}";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, maHV);
        
        while (rs.next()) {            
            HocVienEnjoyKhoaHoc hvekh = new HocVienEnjoyKhoaHoc(rs.getInt(1),
                                                                rs.getInt(2),
                                                                rs.getString(3), 
                                                                rs.getInt(4), 
                                                                rs.getInt(5), 
                                                                rs.getString(6),
                                                                rs.getDouble(7));
            list.add(hvekh);
        }
        
        return list;
    }
    
    public List<HocVien> getByYear(Object...args) throws SQLException {
        List<HocVien> listHocVien = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.HocVien WHERE (id LIKE ? OR hoTen LIKE ? OR dienThoai LIKE ? OR email LIKE ?) AND YEAR(ngayDK) = ? ORDER BY id,YEAR(ngayDK) DESC";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, "%"+args[0]+"%", "%"+args[0]+"%", "%"+args[0]+"%", "%"+args[0]+"%", args[1]);
        
        while (rs.next()) {            
            listHocVien.add(readResultSet(rs));
        }
        
        return listHocVien;
    }
    
    public List<KhoaHoc> getListKhForHV(String maHV) throws SQLException, ParseException{
        List<KhoaHoc> listKH = new ArrayList<>();
        
        String sql = "{call sp_getListKhForHv (?)}";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, maHV);
        
        while (rs.next()) {            
            KhoaHoc kh = new KhoaHoc(rs.getInt(1), rs.getString(2), dateHelper.castDateForm3ToForm1(rs.getString(3)));
            
            listKH.add(kh);
        }
        
        return listKH;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Kiểm tra học viên có tên - giới tính - ngày sinh đã tồn tại hay chưa ">
    public boolean checkHvExists(String hoTen,boolean gioiTinh,String ngaySinh) throws SQLException{
        String sql = "SELECT * FROM dbo.HocVien WHERE hoTen = ? AND gioiTinh = ? AND ngaySinh = ?";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, hoTen, gioiTinh, ngaySinh);
        
        if (rs.next()) {
            return true;
        }
        return false;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="get học viên từ id ">
    public HocVien getById(String id) throws SQLException{
        HocVien hv = null;
        
        String sql = "SELECT * FROM dbo.HocVien WHERE id = ?";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, id);
       
        if (rs.next()) {
            hv = readResultSet(rs);
        }
        
        return hv;
    }
    // </editor-fold>
}
