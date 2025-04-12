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
    private final Caching<String,Product> caching;

    public ProductController(Caching<String,Product> caching) {
        this.caching = caching;
        this.productRepository = new ProductRepository();
    }

    public Product getProductByName(String name) {
        if (currentState != SessionState.SESSION_OPEN) {
            throw new IllegalStateException("Session ist nicht geöffnet – Zugriff nicht erlaubt.");
        }

        Product cachedProduct = caching.get(name);

        if (cachedProduct != null) {
            return cachedProduct;
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
        caching.put(product.getName(), product);
        productRepository.save(product);
    }

    public void openSession(){
        currentState = SessionState.SESSION_OPEN;
    }

    public void closeSession(){
        currentState = SessionState.SESSION_CLOSED;
    }

}
