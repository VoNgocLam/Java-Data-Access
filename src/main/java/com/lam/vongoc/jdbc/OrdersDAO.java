package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO extends DataAccessObject<Order> {
    private static final String GET_BY_ID =
            "SELECT c.first_name, c.last_name, c.email, o.order_id, o.creation_date, o.total_due, o.status, " +
                    "s.first_name, s.last_name, s.email, oi.quantity, p.code, p.name, p.size, p.variety, p.price " +
                    "FROM orders o JOIN customer c ON o.customer_id = c.customer_id " +
                    "JOIN salesperson s ON o.salesperson_id = s.salesperson_id " +
                    "JOIN order_item oi ON oi.order_id = o.order_id " +
                    "JOIN product p ON oi.product_id = p.product_id " +
                    "WHERE o.order_id = ?";

    private static final String GET_BY_CUS = "SELECT * FROM get_orders_by_customer(?)";

    public OrdersDAO(Connection connection) {
        super(connection);
    }

    //Find Order Details by Order ID
    @Override
    public Order findById(long id) {
        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<OrderLine>();

        try(PreparedStatement statement = this.connection.prepareStatement(GET_BY_ID);){
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            boolean flag = true;
            while(rs.next()){
                if (flag){
                    order.setCustomerFirstName(rs.getString(1));
                    order.setCustomerLastName(rs.getString(2));
                    order.setCustomerEmail(rs.getString(3));
                    order.setId(rs.getLong(4));
                    order.setCreationDate(rs.getDate(5));
                    order.setTotalDue(rs.getBigDecimal(6));
                    order.setStatus(rs.getString(7));
                    order.setSalespersonFirstName(rs.getString(8));
                    order.setSalespersonLastName(rs.getString(9));
                    order.setSalespersonEmail(rs.getString(10));
                    flag = false;
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt(11));
                orderLine.setProductCode(rs.getString(12));
                orderLine.setProductName(rs.getString(13));
                orderLine.setProductSize(rs.getInt(14));
                orderLine.setProductVariety(rs.getString(15));
                orderLine.setProductPrice(rs.getBigDecimal(16));

                orderLines.add(orderLine);
            }
            order.setOrderLine(orderLines);
            return order;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public List<Order> findAllPaged(int limit, int pageNumber) {
        return null;
    }

    @Override
    public Order create(Order dto) {
        return null;
    }

    @Override
    public Order update(Order dto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    //Find Customer orders
    public List<Order> findByCustomerId(long id){
        List<Order> orders = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_BY_CUS);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            long currentKey = 0;
            Order order = null;
            List<OrderLine> orderLines = null;
            while(rs.next()){
                long localKey = rs.getLong(4);
                if (currentKey != localKey){
                    order = new Order();
                    currentKey = localKey;
                    order.setCustomerFirstName(rs.getString(1));
                    order.setCustomerLastName(rs.getString(2));
                    order.setCustomerEmail(rs.getString(3));
                    order.setId(currentKey);
                    order.setCreationDate(rs.getDate(5));
                    order.setTotalDue(rs.getBigDecimal(6));
                    order.setStatus(rs.getString(7));
                    order.setSalespersonFirstName(rs.getString(8));
                    order.setSalespersonLastName(rs.getString(9));
                    order.setSalespersonEmail(rs.getString(10));
                    orderLines = new ArrayList<>();
                    orders.add(order);
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt(11));
                orderLine.setProductCode(rs.getString(12));
                orderLine.setProductName(rs.getString(13));
                orderLine.setProductSize(rs.getInt(14));
                orderLine.setProductVariety(rs.getString(15));
                orderLine.setProductPrice(rs.getBigDecimal(16));
                orderLines.add(orderLine);
                order.setOrderLine(orderLines);
            }
            return orders;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
