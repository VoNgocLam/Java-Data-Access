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
            OrderDAO orderDAO = new OrderDAO(connection);
//            for(int i = 1; i < 21 ; i++){
//                System.out.println("Page Number " + i);
//                orderDAO.findAllPaged(10, i).forEach(System.out::println);
//            }
            orderDAO.test();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
