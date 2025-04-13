package org.hbrs.ooka.uebung1;

import org.hbrs.ooka.uebung1.entities.Product;

import java.util.List;

public class ProductControllerPort implements ProductManagementInt {

    private final ProductController productController;

    public ProductControllerPort(Caching caching) {
        if (caching == null) {
            // man könnte eine Standardimplementierung nutzen oder caching deaktivieren
            this.productController = new ProductController();
        }else {
            this.productController = new ProductController(caching);
        }

    }

    @Override
    public Product getProductByName(String name) {
        return productController.getProductByName(name);
    }

    @Override
    public List<Product> getAllProducts() {
        return productController.getAllProducts();
    }

    @Override
    public void saveProduct(Product product) {
        productController.saveProduct(product);
    }

    @Override
    public void deleteProduct(Product product) {
        // TODO
    }

    @Override
    public void openSession() {
        productController.openSession();
    }

    @Override
    public void closeSession() {
        productController.closeSession();
    }
}
