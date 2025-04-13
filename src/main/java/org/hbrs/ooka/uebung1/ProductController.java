package org.hbrs.ooka.uebung1;

import org.hbrs.ooka.uebung1.entities.Product;

import java.util.List;

public class ProductController {

    private enum SessionState {
        SESSION_CLOSED,
        SESSION_OPEN
    }

    private SessionState currentState = SessionState.SESSION_CLOSED;

    private final ProductRepository productRepository;
    private final DatabaseConnection databaseConnection;
    private final Caching caching;

    public ProductController(Caching caching) {
        this.caching = caching;
        this.databaseConnection = new DatabaseConnection();
        this.productRepository = new ProductRepository(databaseConnection);
    }

    public ProductController() {
        this.caching = null; // Caching deaktiviert
        this.databaseConnection = new DatabaseConnection();
        this.productRepository = new ProductRepository(databaseConnection);
    }

    public Product getProductByName(String name) {
        if (currentState != SessionState.SESSION_OPEN) {
            throw new IllegalStateException("Session ist nicht geöffnet – Zugriff nicht erlaubt.");
        }

        if (caching != null) {
            Product cachedProduct = caching.get(name);

            if (cachedProduct != null) {
                return cachedProduct;
            }
        }
        
        return productRepository.readProductByName(name);
    }

    public List<Product> getAllProducts() {
        if (currentState != SessionState.SESSION_OPEN) {
            throw new IllegalStateException("Session ist nicht geöffnet – Zugriff nicht erlaubt.");
        }

        //

        return productRepository.readProducts();
    }

    public void saveProduct(Product product) {
        if (currentState != SessionState.SESSION_OPEN) {
            throw new IllegalStateException("Session ist nicht geöffnet – Zugriff nicht erlaubt.");
        }

        if (caching != null) {
            caching.put(product.getName(), product);
        }

        productRepository.save(product);
    }

    public void openSession() {
        currentState = SessionState.SESSION_OPEN;
        databaseConnection.openConnection();
    }

    public void closeSession() {
        currentState = SessionState.SESSION_CLOSED;
        databaseConnection.closeConnection();
    }

}
