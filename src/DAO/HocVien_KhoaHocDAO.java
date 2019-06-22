/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Helper.JdbcHelper;
import Model.HocVien_KhoaHoc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class HocVien_KhoaHocDAO implements InterfaceDAO<HocVien_KhoaHoc> {

    JdbcHelper jdbcHelper = new JdbcHelper();
    
    @Override
    public int insert(HocVien_KhoaHoc hvkh) throws SQLException {
        String sql = "INSERT INTO dbo.HocVien_KhoaHoc (maKH,maHV,diem) VALUES (?,?,?)";
        
        return jdbcHelper.executeUpdate(sql, hvkh.getMaKH(), hvkh.getMaHV(), hvkh.getDiem());
    }
    
    @Override
    public int update(HocVien_KhoaHoc hvkh) throws SQLException {
        String sql = "UPDATE dbo.HocVien_KhoaHoc SET diem=? WHERE id=?";
        
        return jdbcHelper.executeUpdate(sql, hvkh.getDiem(), hvkh.getId());
    }
    
    @Override
    public int delete(String id) throws SQLException {
        String sql = "{call sp_xoaHVKH (?)}";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, id);
        
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }
    
    @Override
    public List<HocVien_KhoaHoc> get(Object... args) throws SQLException {
        List<HocVien_KhoaHoc> listHocVien_KhoaHoc = new ArrayList<>();
        
        String sql = "{call sp_getHvkhByKhAndYear (?,?)}";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, args[0], args[1]);
        
        while (rs.next()) {            
            listHocVien_KhoaHoc.add(readResultSet(rs));
        }
        
        return listHocVien_KhoaHoc;
    }
    
    @Override
    public List<HocVien_KhoaHoc> getAll() throws SQLException {
        List<HocVien_KhoaHoc> listHocVien_KhoaHoc = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.HocVien_KhoaHoc";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        while (rs.next()) {            
            listHocVien_KhoaHoc.add(readResultSet(rs));
        }
        
        return listHocVien_KhoaHoc;
    }
    
    @Override
    public HocVien_KhoaHoc readResultSet(ResultSet rs) throws SQLException {
        return new HocVien_KhoaHoc(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDouble(4), rs.getBoolean(5));
    }
    
}
