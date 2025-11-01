package product.checkout;

import product.Product;

/** One stage in the pricing pipeline. */
public interface Charge {
    String name();
    /**
     * Apply a charge/discount to the running subtotal and return the new subtotal.
     * Must NOT mutate Product.
     */
    double apply(Product p, int qty, double subtotal);
}

