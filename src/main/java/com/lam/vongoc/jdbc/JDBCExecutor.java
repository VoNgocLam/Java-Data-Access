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
            ProductDAO productDAO = new ProductDAO(connection);
            Product product = new Product();
            product.setCode("MWAPP20");
            product.setName("Mineral Water");
            product.setSize(20);
            product.setVariety("Apple");
            product.setPrice(BigDecimal.valueOf(1.79));
            product.setStatus("DISCONTINUED");

            Product productData = productDAO.create(product);
            System.out.println("After insert: ");
            System.out.println(productData);

            productData.setStatus("ACTIVE");
            productData = productDAO.update(productData);
            System.out.println("After update: ");
            System.out.println(productData);

            System.out.println("Find product has id is 1");
            System.out.println(productDAO.findById(1));

            productDAO.delete(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
