package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataAccessObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalespersonDAO extends DataAccessObject<Salesperson> {
    private static final String INSERT = "INSERT INTO salesperson(first_name, last_name, email, phone, address, city, state, " +
            "zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ONE = "SELECT salesperson_id, first_name, last_name, email, phone, address, city, state, " +
            "zipcode FROM salesperson WHERE salesperson_id = ?";

    private static final String GET_ALL = "SELECT salesperson_id, first_name, last_name, email, phone, address, city, state, " +
            "zipcode FROM salesperson";

    private static final String GET_LMT = "SELECT salesperson_id, first_name, last_name, email, phone, address, city, state, " +
            "zipcode FROM salesperson LIMIT ?";

    private static final String GET_ALL_PAGED = "SELECT salesperson_id, first_name, last_name, email, phone, address, city, state, " +
            "zipcode FROM salesperson LIMIT ? OFFSET ?";

    private static final String UPDATE = "UPDATE salesperson SET first_name = ?, last_name = ?, email = ?, phone = ?, address = ?," +
            "city = ?, state = ?, zipcode = ? WHERE salesperson_id = ?";

    private static final String DELETE = "DELETE FROM salesperson WHERE salesperson_id = ?";

    public SalespersonDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Salesperson findById(long id) {
        Salesperson salesperson = new Salesperson();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                salesperson.setId(rs.getLong(1));
                salesperson.setFirstName(rs.getString(2));
                salesperson.setLastName(rs.getString(3));
                salesperson.setEmail(rs.getString(4));
                salesperson.setPhone(rs.getString(5));
                salesperson.setAddress(rs.getString(6));
                salesperson.setCity(rs.getString(7));
                salesperson.setState(rs.getString(8));
                salesperson.setZipcode(rs.getString(9));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return salesperson;
    }

    @Override
    public List<Salesperson> findAll() {
        List<Salesperson> salespeople = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL);){
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Salesperson salesperson = new Salesperson();
                salesperson.setId(rs.getLong(1));
                salesperson.setFirstName(rs.getString(2));
                salesperson.setLastName(rs.getString(3));
                salesperson.setEmail(rs.getString(4));
                salesperson.setPhone(rs.getString(5));
                salesperson.setAddress(rs.getString(6));
                salesperson.setCity(rs.getString(7));
                salesperson.setState(rs.getString(8));
                salesperson.setZipcode(rs.getString(9));
                salespeople.add(salesperson);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return salespeople;
    }

    @Override
    public List<Salesperson> findAllPaged(int limit, int pageNumber) {
        List<Salesperson> salespeople = new ArrayList<>();
        int offset = ((pageNumber - 1) * limit);
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED);){
            if(limit < 1){
                limit = 10;
            }
            statement.setInt(1, limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Salesperson salesperson = new Salesperson();
                salesperson.setId(rs.getLong(1));
                salesperson.setFirstName(rs.getString(2));
                salesperson.setLastName(rs.getString(3));
                salesperson.setEmail(rs.getString(4));
                salesperson.setPhone(rs.getString(5));
                salesperson.setAddress(rs.getString(6));
                salesperson.setCity(rs.getString(7));
                salesperson.setState(rs.getString(8));
                salesperson.setZipcode(rs.getString(9));
                salespeople.add(salesperson);
            }
        } catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return salespeople;
    }

    @Override
    public Salesperson create(Salesperson dto) {
        try{
            this.connection.setAutoCommit(false);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

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
            this.connection.commit();

            int key = this.getLastValue(SALESPERSON_SEQUENCE);
            return this.findById(key);
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
    }

    @Override
    public Salesperson update(Salesperson dto) {
        try{
            this.connection.setAutoCommit(false);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getEmail());
            statement.setString(4, dto.getPhone());
            statement.setString(5, dto.getAddress());
            statement.setString(6, dto.getCity());
            statement.setString(7, dto.getState());
            statement.setString(8, dto.getZipcode());
            statement.setLong(9, dto.getId());
            statement.execute();
            this.connection.commit();
            return this.findById(dto.getId());
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
    }

    @Override
    public void delete(long id) {
        try{
            this.connection.setAutoCommit(false);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1, id);
            statement.execute();
            this.connection.commit();
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
    }
}
