package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataAccessObject;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO extends DataAccessObject<Order> {

    private static final String INSERT_ORDER = "INSERT INTO orders(creation_date, total_due, status, customer_id, salesperson_id)" +
            "VALUES(?, ?, ?, ?, ?)";

    private static final String INSERT_ORDER_LINE = "INSERT INTO order_item(order_id, product_id, quantity) " +
            "VALUES(?, ?, ?)";
    private static final String UPDATE = "UPDATE orders SET status = ? WHERE order_id = ?";

    private static final String GET_BY_ID =
            "SELECT c.first_name, c.last_name, c.email, o.order_id, o.creation_date, o.total_due, o.status, " +
                    "s.first_name, s.last_name, s.email, oi.quantity, p.code, p.name, p.size, p.variety, p.price " +
                    "FROM orders o JOIN customer c ON o.customer_id = c.customer_id " +
                    "JOIN salesperson s ON o.salesperson_id = s.salesperson_id " +
                    "JOIN order_item oi ON oi.order_id = o.order_id " +
                    "JOIN product p ON oi.product_id = p.product_id " +
                    "WHERE o.order_id = ?";

    private static final String GET_ALL =
            "SELECT c.first_name, c.last_name, c.email, o.order_id, o.creation_date, o.total_due, o.status, " +
                    "s.first_name, s.last_name, s.email, oi.quantity, p.code, p.name, p.size, p.variety, p.price " +
                    "FROM orders o JOIN customer c ON o.customer_id = c.customer_id " +
                    "JOIN salesperson s ON o.salesperson_id = s.salesperson_id " +
                    "JOIN order_item oi ON oi.order_id = o.order_id " +
                    "JOIN product p ON oi.product_id = p.product_id " +
                    "ORDER BY o.order_id ASC";

    private static final String GET_ALL_PAGED =
            "SELECT order_id FROM orders LIMIT ? OFFSET ?";

    private static final String GET_CUS_ID = "SELECT get_customer_id(?)";

    private static final String GET_PRODUCT_ID = "SELECT get_product_id(?)";

    private static final String GET_PRODUCT_PRICE = "SELECT get_product_price(?)";

    private static final String GET_SALESPERSON_ID = "SELECT get_salesperson_id(?)";

    private static final String GET_BY_CUS = "SELECT * FROM get_orders_by_customer(?)";

    public OrderDAO(Connection connection) {
        super(connection);
    }

    //Find Order Details by Order ID
    @Override
    public Order findById(long id) {
        Order order = new Order();
        List<OrderLine> orderLines = new ArrayList<>();

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
        List<Order> orders = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL);){
            Order order = null;
            ResultSet rs = statement.executeQuery();
            long currentKey = 0;
            while(rs.next()){
                long localKey = rs.getLong(4);
                if(currentKey != localKey){
                    order = new Order();
                    List<OrderLine> orderLines = new ArrayList<>();
                    orders.add(order);
                    order.setCustomerFirstName(rs.getString(1));
                    order.setCustomerLastName(rs.getString(2));
                    order.setCustomerEmail(rs.getString(3));
                    currentKey = localKey;
                    order.setId(rs.getLong(4));
                    order.setCreationDate(rs.getDate(5));
                    order.setTotalDue(rs.getBigDecimal(6));
                    order.setStatus(rs.getString(7));
                    order.setSalespersonFirstName(rs.getString(8));
                    order.setSalespersonLastName(rs.getString(9));
                    order.setSalespersonEmail(rs.getString(10));
                    order.setOrderLine(orderLines);
                }
                OrderLine orderLine = new OrderLine();
                orderLine.setQuantity(rs.getInt(11));
                orderLine.setProductCode(rs.getString(12));
                orderLine.setProductName(rs.getString(13));
                orderLine.setProductSize(rs.getInt(14));
                orderLine.setProductVariety(rs.getString(15));
                orderLine.setProductPrice(rs.getBigDecimal(16));
                order.getOrderLine().add(orderLine);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public List<Order> findAllPaged(int limit, int pageNumber) {
        List<Order> orders = new ArrayList<>();
        int offset = ((pageNumber - 1) * limit);
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED);){
            if(limit < 1){
                limit = 10;
            }
            Order order = null;
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                long currentKey = rs.getLong(1);
                order = this.findById(currentKey);
                orders.add(order);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return orders;
    }

    @Override
    public Order create(Order dto) {
        double total_due = 0;
        long customerKey = 0;
        long salespersonKey = 0;
        long orderKey = 0;
        long productKey = 0;
        for (OrderLine orderline: dto.getOrderLine()
             ) {
            try(PreparedStatement statement = this.connection.prepareStatement(GET_PRODUCT_PRICE);){
                statement.setString(1, orderline.getProductCode());
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    total_due += orderline.getQuantity() * rs.getBigDecimal(1).doubleValue();
                }
            } catch (SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        dto.setTotalDue(BigDecimal.valueOf(total_due));

        try(PreparedStatement statement = this.connection.prepareStatement(GET_CUS_ID);){
            statement.setString(1, dto.getCustomerEmail());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                customerKey = rs.getLong(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(GET_SALESPERSON_ID);){
            statement.setString(1, dto.getSalespersonEmail());
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                salespersonKey = rs.getLong(1);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(INSERT_ORDER);){
            statement.setDate(1, Date.valueOf(LocalDate.now()));
            statement.setBigDecimal(2, dto.getTotalDue());
            statement.setString(3, dto.getStatus());
            statement.setLong(4, customerKey);
            statement.setLong(5, salespersonKey);
            statement.execute();

            orderKey = this.getLastValue(ORDER_SEQUENCE);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        for (OrderLine line: dto.getOrderLine()
             ) {
            try(PreparedStatement statement = this.connection.prepareStatement(GET_PRODUCT_ID);){
                statement.setString(1, line.getProductCode());
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                    productKey = rs.getLong(1);
                }
            } catch (SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            try(PreparedStatement statement = this.connection.prepareStatement(INSERT_ORDER_LINE);){
                statement.setLong(1, orderKey);
                statement.setLong(2, productKey);
                statement.setInt(3, line.getQuantity());
                statement.execute();
            } catch (SQLException e){
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        return this.findById(orderKey);
    }


    @Override
    public Order update(Order dto) {
        Order order = new Order();
        try{
            this.connection.setAutoCommit(false);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);)
        {
            statement.setString(1, dto.getStatus());
            statement.setLong(2, dto.getId());
            statement.execute();
            this.connection.commit();
            order = this.findById(dto.getId());
        } catch (SQLException e){
            try{
                this.connection.rollback();
            } catch (SQLException sqle){
                sqle.printStackTrace();
                throw new RuntimeException(sqle);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return order;
    }


    @Override
    public void delete(long id) {}

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
