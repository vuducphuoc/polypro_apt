/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Helper;

import ConnectDatabase.ConnectDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author MSI
 */
public class JdbcHelper {
    public static Connection connection = ConnectDatabase.connectDB("TRAN-ANH", "dbPolypro_APT2", "sa", "123456");

    public PreparedStatement prepareStatement(String sql, Object... args) throws SQLException {
        PreparedStatement pstmt = null;
        
        if (sql.trim().startsWith("{")) {
            pstmt = this.connection.prepareCall(sql);
        } else {
            pstmt = this.connection.prepareStatement(sql);
        }
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }

        return pstmt;
    }

    public int executeUpdate(String sql, Object... args) throws SQLException {
        PreparedStatement stmt = this.prepareStatement(sql, args);
        return stmt.executeUpdate();
    }

    public ResultSet executeQuery(String sql, Object... args) throws SQLException {
        PreparedStatement stmt = this.prepareStatement(sql, args);
        return stmt.executeQuery();
    }
}
