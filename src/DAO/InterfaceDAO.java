/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author MSI
 */
public interface InterfaceDAO <T>{
    public int insert(T t) throws SQLException;
    public int update(T t) throws SQLException;
    public int delete(String id) throws SQLException;
    public List<T> get(Object... args) throws SQLException;
    public List<T> getAll() throws SQLException;
    public T readResultSet(ResultSet rs) throws SQLException;
}
