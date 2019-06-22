/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import Helper.JdbcHelper;
import Model.ChuyenDe;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author MSI
 */
public class ChuyenDeDAO implements InterfaceDAO<ChuyenDe> {

    JdbcHelper jdbcHelper = new JdbcHelper();

    @Override
    public int insert(ChuyenDe cd) throws SQLException {
        String sql = "INSERT INTO dbo.ChuyenDe (id,tenCD,hocPhi,thoiLuong,MoTa) VALUES (?,?,?,?,?)";

        return jdbcHelper.executeUpdate(sql, cd.getId(), cd.getTenCD(), cd.getHocPhi(), cd.getThoiLuong(), cd.getMoTa());
    }

    @Override
    public int update(ChuyenDe cd) throws SQLException {
        String sql = "UPDATE dbo.ChuyenDe SET tenCD=?,hocPhi=?,thoiLuong=?,MoTa=? WHERE id=?";

        return jdbcHelper.executeUpdate(sql, cd.getTenCD(), cd.getHocPhi(), cd.getThoiLuong(), cd.getMoTa(), cd.getId());
    }

    @Override
    public int delete(String id) throws SQLException {
        String sql = "{call sp_xoaChuyenDe (?)}";

        ResultSet rs = jdbcHelper.executeQuery(sql, id);

        if (rs.next()) {
            return rs.getInt(1);
        }

        return -1;
    }

    @Override
    public List<ChuyenDe> get(Object... args) throws SQLException {
        List<ChuyenDe> listChuyenDe = new ArrayList<>();

        String sql = "SELECT * FROM dbo.ChuyenDe WHERE id LIKE ? OR tenCD LIKE ?";

        ResultSet rs = jdbcHelper.executeQuery(sql, "%" + args[0] + "%", "%" + args[0] + "%");

        while (rs.next()) {
            listChuyenDe.add(readResultSet(rs));
        }

        return listChuyenDe;
    }

    @Override
    public List<ChuyenDe> getAll() throws SQLException {
        List<ChuyenDe> listChuyenDe = new ArrayList<>();

        String sql = "SELECT * FROM dbo.ChuyenDe";

        ResultSet rs = jdbcHelper.executeQuery(sql);

        while (rs.next()) {
            listChuyenDe.add(readResultSet(rs));
        }

        return listChuyenDe;
    }

    @Override
    public ChuyenDe readResultSet(ResultSet rs) throws SQLException {
        return new ChuyenDe(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5));
    }

    public ChuyenDe getById(Object obj) throws SQLException {
        String sql = "SELECT * FROM dbo.ChuyenDe WHERE ID = ?";

        ResultSet rs = jdbcHelper.executeQuery(sql, obj);

        while (rs.next()) {
            return readResultSet(rs);
        }
        return null;
    }

    public ChuyenDe getByName(Object obj) throws SQLException {
        String sql = "SELECT * FROM dbo.ChuyenDe WHERE TenCD = ?";

        ResultSet rs = jdbcHelper.executeQuery(sql, obj);

        while (rs.next()) {
            return readResultSet(rs);
        }
        return null;
    }

}
