package product.tax;

import product.Product;

public interface TaxPolicy {
    /**
     * Calculate tax for given product, qty, and subtotal (after promotions, before shipping).
     * Must NOT mutate Product.
     */
    double tax(Product p, int qty, double subtotal);
}
