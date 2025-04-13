package org.hbrs.ooka.uebung1;

import org.hbrs.ooka.uebung1.entities.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    DatabaseConnection databaseConnection;

    public ProductRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public void save(Product product) {
        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Speichern des Produkts", e);
        }
    }

    public List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen der Produkte", e);
        }

        return products;
    }

    public Product readProductById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Product product = null;

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen des Produkts mit ID: " + id, e);
        }

        return product;
    }

    public Product readProductByName(String name) {
        String sql = "SELECT * FROM products WHERE name = ?";
        Product product = null;

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql)) {

            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                product = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Lesen des Produkts mit Name: " + name, e);
        }

        return product;
    }

    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql)) {

            pstmt.setString(1, product.getName());
            pstmt.setDouble(2, product.getPrice());
            pstmt.setInt(3, product.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Aktualisieren des Produkts", e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";

        try (PreparedStatement pstmt = databaseConnection.getConnection().prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Fehler beim Löschen des Produkts mit ID: " + id, e);
        }
    }
}
