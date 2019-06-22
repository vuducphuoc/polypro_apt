/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Helper.JdbcHelper;
import Model.NhanVien;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class NhanVienDAO implements InterfaceDAO<NhanVien>{

    JdbcHelper jdbcHelper = new JdbcHelper();

    @Override
    public int insert(NhanVien nv) throws SQLException {
        String sql = "{call sp_insertNV (?,?,?,?,?)}";
        
        return jdbcHelper.executeUpdate(sql,nv.getID(),nv.getMatKhau(),nv.getHoTen(),nv.getEmail(),nv.getVaiTro());
    }

    @Override
    public int update(NhanVien nv) throws SQLException {
        String sql = "{call sp_updateNV (?,?,?,?)}";
        
        return jdbcHelper.executeUpdate(sql, nv.getMatKhau(),nv.getHoTen(),nv.getVaiTro(),nv.getID());
    }

    @Override
    public int delete(String id) throws SQLException {
        String sql = "DELETE dbo.NhanVien WHERE id=?";
        
        return jdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public List<NhanVien> get(Object...args) throws SQLException{
        List<NhanVien> listNhanVien = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.NhanVien WHERE id LIKE ? OR hoTen LIKE ? ORDER BY id";
        
        ResultSet rs = jdbcHelper.executeQuery(sql,"%" + args[0] + "%","%" + args[0] + "%");
        
        while (rs.next()) {            
            listNhanVien.add(readResultSet(rs));
        }
        
        return listNhanVien;
    }

    @Override
    public List<NhanVien> getAll() throws SQLException {
        List<NhanVien> listNhanVien = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.NhanVien ORDER BY id";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        while (rs.next()) {            
            listNhanVien.add(readResultSet(rs));
        }
        
        return listNhanVien;
    }

    @Override
    public NhanVien readResultSet(ResultSet rs) throws SQLException{
        return new NhanVien(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
    }

   
    public String getLastIdNhanVien() throws SQLException{
        String lastID = null;
        
        String sql = "SELECT TOP 1 * FROM dbo.NhanVien WHERE id LIKE 'NV%' ORDER BY id DESC";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        if (rs.next()) {
            lastID = rs.getString(1);
        }
        
        return lastID;
    }
    
    public NhanVien checkTpExists() throws SQLException{
        String sql = "SELECT * FROM dbo.NhanVien WHERE vaiTro = 1";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        NhanVien tp = null;
        
        if (rs.next()) {            
            tp = readResultSet(rs);
        }
        
        return tp;
    }
    
    public boolean checkEmailExistsInDb(String email) throws SQLException{
        String sql = "SELECT * FROM dbo.NhanVien WHERE email = ?";
        
        ResultSet rs  = jdbcHelper.executeQuery(sql, email.trim());
        
        if (rs.next()) {
            return true;
        }
        return false;
    }
    
    public int resetPass(String newPass, String id) throws SQLException{
        String sql = "UPDATE dbo.NhanVien SET matKhau = ? WHERE id = ?";
        
        return jdbcHelper.executeUpdate(sql, newPass,id);
    }
}
