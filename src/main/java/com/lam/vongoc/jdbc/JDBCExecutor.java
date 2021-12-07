package com.lam.vongoc.jdbc;

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
            CustomerDAO customerDAO = new CustomerDAO(connection);
            for (int i = 1; i < 102; i++) {
                System.out.println("Page Number " + i);
                customerDAO.findAllPaged(10,i).forEach(System.out::println);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
