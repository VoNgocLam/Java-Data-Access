package com.lam.vongoc.jdbc;

import java.beans.BeanInfo;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCExecutor {
    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost",
                "hplussport", "postgres", "password");

        try {
            Connection connection = dcm.getConnection();
            SalespersonDAO salespersonDAO = new SalespersonDAO(connection);
            salespersonDAO.findAll().forEach(System.out::println);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
