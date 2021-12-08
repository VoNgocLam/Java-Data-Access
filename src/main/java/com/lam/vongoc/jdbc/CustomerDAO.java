package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO extends DataAccessObject<Customer> {
    private static final String INSERT = "INSERT INTO customer (first_name, last_name, email, phone, address, city, state, zipcode) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ONE = "SELECT customer_id, first_name, last_name, email, phone, address, city, state, zipcode " +
            "FROM customer WHERE customer_id = ?";

    private static final String GET_ALL = "SELECT customer_id, first_name, last_name, email, phone, address, city, state, zipcode " +
            "FROM customer";

    private static final String UPDATE = "UPDATE customer SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?," +
            "city = ?, state = ?, zipcode = ? WHERE customer_id = ?";

    private static final String DELETE = "DELETE FROM customer WHERE customer_id = ?";

    private static final String GET_ALL_LMT = "SELECT customer_id, first_name, last_name, email, phone, address, city, state, zipcode " +
            "FROM customer ORDER BY first_name, last_name LIMIT ?";

    private static final String GET_ALL_PAGED = "SELECT customer_id, first_name, last_name, email, phone, address, city, state, zipcode " +
            "FROM customer LIMIT ? OFFSET ?";

    public CustomerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Customer findById(long id) {
        Customer customer = new Customer();

        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE)){
            statement.setLong(1,id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                customer.setId(rs.getLong(1));
                customer.setFirstName(rs.getString(2));
                customer.setLastName(rs.getString(3));
                customer.setEmail(rs.getString(4));
                customer.setPhone(rs.getString(5));
                customer.setAddress(rs.getString(6));
                customer.setCity(rs.getString(7));
                customer.setState(rs.getString(8));
                customer.setZipcode(rs.getString(9));
            }
            return customer;
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL);)
        {
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong(1));
                customer.setFirstName(rs.getString(2));
                customer.setLastName(rs.getString(3));
                customer.setEmail(rs.getString(4));
                customer.setPhone(rs.getString(5));
                customer.setAddress(rs.getString(6));
                customer.setCity(rs.getString(7));
                customer.setState(rs.getString(8));
                customer.setZipcode(rs.getString(9));
                customers.add(customer);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return customers;
    }

    public List<Customer> findAllSorted(int limit){
        List<Customer> customers = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_LMT);){
            statement.setInt(1, limit);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong(1));
                customer.setFirstName(rs.getString(2));
                customer.setLastName(rs.getString(3));
                customer.setEmail(rs.getString(4));
                customer.setPhone(rs.getString(5));
                customer.setAddress(rs.getString(6));
                customer.setCity(rs.getString(7));
                customer.setState(rs.getString(8));
                customer.setZipcode(rs.getString(9));
                customers.add(customer);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return customers;
    }

    public List<Customer> findAllPaged(int limit, int pageNumber){
        List<Customer> customers = new ArrayList<>();
        int offset = (limit * (pageNumber - 1));
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED);){
            if (limit < 1){
                limit = 10;
            }
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                Customer customer = new Customer();
                customer.setId(rs.getLong(1));
                customer.setFirstName(rs.getString(2));
                customer.setLastName(rs.getString(3));
                customer.setEmail(rs.getString(4));
                customer.setPhone(rs.getString(5));
                customer.setAddress(rs.getString(6));
                customer.setCity(rs.getString(7));
                customer.setState(rs.getString(8));
                customer.setZipcode(rs.getString(9));
                customers.add(customer);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return customers;
    }
    @Override
    public Customer create(Customer dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getEmail());
            statement.setString(4, dto.getPhone());
            statement.setString(5, dto.getAddress());
            statement.setString(6, dto.getCity());
            statement.setString(7, dto.getState());
            statement.setString(8, dto.getZipcode());
            statement.execute();
            long key = this.getLastValue(CUSTOMER_SEQUENCE);
            return this.findById(key);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer update(Customer dto) {
        Customer customer = null;
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setString(1,dto.getFirstName());
            statement.setString(2,dto.getLastName());
            statement.setString(3,dto.getEmail());
            statement.setString(4,dto.getPhone());
            statement.setString(5,dto.getAddress());
            statement.setString(6,dto.getCity());
            statement.setString(7,dto.getState());
            statement.setString(8,dto.getZipcode());
            statement.setLong(9,dto.getId());
            statement.execute();
            customer = this.findById(dto.getId());
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return customer;
    }

    @Override
    public void delete(long id) {
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1,id);
            statement.execute();
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
