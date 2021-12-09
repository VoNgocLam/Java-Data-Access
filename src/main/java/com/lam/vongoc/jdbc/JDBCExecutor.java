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
            Order order = new Order();
            List<OrderLine> lines = new ArrayList<>();
            OrderLine firstLine = new OrderLine();
            OrderLine secondLine = new OrderLine();
            OrderLine thirdLine = new OrderLine();
            OrderLine fourthLine = new OrderLine();

            order.setOrderLine(lines);
            lines.add(firstLine);
            lines.add(secondLine);
            lines.add(thirdLine);
            lines.add(fourthLine);

            order.setCustomerEmail("tknight2g@friendfeed.com");
            order.setSalespersonEmail("rsullivan1b@hplussport.com");
            order.setStatus("cancelled");

            firstLine.setProductCode("MWBLU32");
            firstLine.setQuantity(5);

            secondLine.setProductCode("MWCRA20");
            secondLine.setQuantity(7);

            thirdLine.setProductCode("MWORG32");
            thirdLine.setQuantity(10);

            fourthLine.setProductCode("MWSTR32");
            fourthLine.setQuantity(2);
            System.out.println(orderDAO.create(order));

//            for(int i = 1; i < 21 ; i++){
//                System.out.println("Page Number " + i);
//                orderDAO.findAllPaged(10, i).forEach(System.out::println);
//            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
