import org.hbrs.ooka.uebung1.DatabaseConnection;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import org.hbrs.ooka.uebung1.entities.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionTest {

    private Connection connection;

    @BeforeEach
    public void setup() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        try {
            databaseConnection.openConnection();

            connection = databaseConnection.getConnection();

            String sql = "CREATE TABLE IF NOT EXISTS products ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT, "
                    + "name VARCHAR(255) NOT NULL, "
                    + "price DOUBLE NOT NULL)";

            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            System.out.println("Table created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void roundTrip() {
        // Erzeugung eines neuen Produkts (CREATE)
        Product productTarget = insertProduct();

        // Lesen des Produkts aus der Datenbank (READ)
        Product productActual = readProductByName("My Motor 1.0");

        // Vergleich des Produkts mit dem erwarteten Produkt (Assertion)
        assertEquals(productTarget, productActual);

        deleteProductByName("My Motor 1.0");

        productActual = readProductByName("My Motor 1.0");

        assertNull(productActual);

    }

    @AfterEach
    public void deleteStuff() {
        // SQL für die Löschung der Tabelle (Vermeidung von Datenmüll)
        // String sql = "DROP TABLE products"; --> ToDo bei Ihnen ;-)
        try{
//            String sql = "DROP TABLE products";
            String sql = "TRUNCATE TABLE products";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void deleteProductByName(String name) {
        try{
            String sql = "DELETE FROM products WHERE name = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<Product> readProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                products.add(new Product(
                                resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getDouble("price")
                        )
                );
            }
            System.out.println("Product inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product readProductByName(String name) {
        Product product = null;
        String sql = "SELECT * FROM products WHERE name = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql);) {

            pstmt.setString(1, name);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getDouble("price")
                );
            }
            System.out.println("Product inserted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }

    private Product insertProduct() {
        String sql1 = "INSERT INTO products (name, price) VALUES (?, ?)";
        Product productTarget = new Product(1, "My Motor 1.0", 100.0);

        try {
            PreparedStatement pstmt = this.connection.prepareStatement(sql1);
            pstmt.setString(1, productTarget.getName());
            pstmt.setDouble(2, productTarget.getPrice());
            pstmt.executeUpdate();
            System.out.println("Product inserted successfully.");
        } catch (SQLException e2) {
            e2.printStackTrace();
        }

        return productTarget;
    }

    @AfterAll
    public void closeConnection() throws SQLException {
        connection.close();
    }
}