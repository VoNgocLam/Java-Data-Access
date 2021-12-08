package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataAccessObject;
import com.lam.vongoc.jdbc.utils.DataTransferObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO extends DataAccessObject<Product> {

    private static final String INSERT = "INSERT INTO product(code, name, size, variety, price, status) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_ONE = "SELECT product_id, code, name, size, variety, price, status FROM product WHERE product_id = ?";
    private static final String GET_ALL = "SELECT product_id, code, name, size, variety, price, status FROM product";
    private static final String GET_ALL_LMT = "SELECT product_id, code, name, size, variety, price, status FROM product LIMIT ?";
    private static final String GET_ALL_PAGED = "SELECT product_id, code, name, size, variety, price, status FROM product LIMIT ? OFFSET ?";
    private static final String UPDATE = "UPDATE product SET code = ?, name = ?, size = ?, variety = ?, price = ?, status = ? WHERE product_id = ?";
    private static final String DELETE = "DELETE FROM product WHERE product_id = ?";

    public ProductDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Product findById(long id) {
        Product product = new Product();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);

            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                product.setID(rs.getLong(1));
                product.setCode(rs.getString(2));
                product.setName(rs.getString(3));
                product.setSize(rs.getInt(4));
                product.setVariety(rs.getString(5));
                product.setPrice(rs.getBigDecimal(6));
                product.setStatus(rs.getString(7));
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return product;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL);){
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                Product product = new Product();
                product.setID(rs.getLong(1));
                product.setCode(rs.getString(2));
                product.setName(rs.getString(3));
                product.setSize(rs.getInt(4));
                product.setVariety(rs.getString(5));
                product.setPrice(rs.getBigDecimal(6));
                product.setStatus(rs.getString(7));
                products.add(product);
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public List<Product> findAllPaged(int limit, int pageNumber) {
        List<Product> products = new ArrayList<>();
        int offset = ((pageNumber-1) * limit);
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ALL_PAGED);){
            if(limit < 1){
                limit = 10;
            }
            statement.setInt(1,limit);
            statement.setInt(2, offset);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Product product = new Product();
                product.setID(rs.getLong(1));
                product.setCode(rs.getString(2));
                product.setName(rs.getString(3));
                product.setSize(rs.getInt(4));
                product.setVariety(rs.getString(5));
                product.setPrice(rs.getBigDecimal(6));
                product.setStatus(rs.getString(7));
                products.add(product);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return products;
    }

    @Override
    public Product create(Product dto) {
        try{
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setString(1, dto.getCode());
            statement.setString(2, dto.getName());
            statement.setInt(3, dto.getSize());
            statement.setString(4, dto.getVariety());
            statement.setBigDecimal(5, dto.getPrice());
            statement.setString(6, dto.getStatus());
            statement.execute();

            int key = this.getLastValue(PRODUCT_SEQUENCE);
            this.connection.commit();
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
    public Product update(Product dto) {
        Product product = null;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setString(1, dto.getCode());
            statement.setString(2, dto.getName());
            statement.setInt(3, dto.getSize());
            statement.setString(4, dto.getVariety());
            statement.setBigDecimal(5, dto.getPrice());
            statement.setString(6, dto.getStatus());
            statement.setLong(7, dto.getId());
            statement.execute();
            this.connection.commit();
            product = this.findById(dto.getId());
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
        return product;
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
