/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Helper.DateHelper;
import Helper.JdbcHelper;
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
public class KhoaHocDAO implements InterfaceDAO<KhoaHoc>{
    JdbcHelper jdbcHelper   = new JdbcHelper();
    DateHelper dateHelper   = new DateHelper();

    @Override
    public int insert(KhoaHoc kh) throws SQLException {
        String sql = "INSERT INTO dbo.KhoaHoc (maCD,hocPhi,thoiLuong,ngayKG,ghiChu,ngayTao) VALUES (?,?,?,?,?,GETDATE())";
        
        return jdbcHelper.executeUpdate(sql, kh.getMaCD(),kh.getHocPhi(),kh.getThoiLuong(),kh.getNgayKG(),kh.getGhiChu());
    }

    @Override
    public int update(KhoaHoc kh) throws SQLException {
        String sql = "UPDATE dbo.KhoaHoc SET maCD=?,hocPhi=?,thoiLuong=?,ngayKG=?,ghiChu=? WHERE id=?";
        
        return jdbcHelper.executeUpdate(sql, kh.getMaCD(),kh.getHocPhi(),kh.getThoiLuong(),kh.getNgayKG(),kh.getGhiChu(),kh.getId());
    }

    @Override
    public int delete(String id) throws SQLException {
        String sql = "{call sp_xoaKhoaHoc (?)}";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, id);
        
        if (rs.next()) {
            return rs.getInt(1);
        }
        
        return -1;
    }

    @Override
    public List<KhoaHoc> get(Object...args) throws SQLException {
        List<KhoaHoc> listKhoaHoc = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.KhoaHoc WHERE maCD=? and YEAR(ngayKG)= ? ORDER BY ngayKG";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, args[0],args[1]);
        
        while (rs.next()) {            
            listKhoaHoc.add(readResultSet(rs));
        }
        
        return listKhoaHoc;
    }

    @Override
    public List<KhoaHoc> getAll() throws SQLException {
        List<KhoaHoc> listKhoaHoc = new ArrayList<>();
        
        String sql = "SELECT * FROM dbo.KhoaHoc ORDER BY ngayKG DESC";
        
        ResultSet rs = jdbcHelper.executeQuery(sql);
        
        while (rs.next()) {            
            listKhoaHoc.add(readResultSet(rs));
        }
        
        return listKhoaHoc;
    }

    @Override
    public KhoaHoc readResultSet(ResultSet rs) throws SQLException {
        try {
            return new KhoaHoc(rs.getInt(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    dateHelper.castDateForm3ToForm1(rs.getString(5)),
                    rs.getString(6),
                    dateHelper.castDateForm3ToForm1(rs.getString(7)),
                    rs.getBoolean(8));
        } catch (ParseException ex) {
            Logger.getLogger(KhoaHocDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Get khóa học từ mã khóa học ">
    public KhoaHoc getKhoaHocByID(int id) throws SQLException{
        KhoaHoc kh = null;
        String sql = "SELECT * FROM dbo.KhoaHoc WHERE id = ?";
        
        ResultSet rs = jdbcHelper.executeQuery(sql, id);
        
        while (rs.next()) {            
            kh = readResultSet(rs);
        }
        
        return kh;
    }
    // </editor-fold>
}
