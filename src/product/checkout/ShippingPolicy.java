package product.checkout;

import product.Product;

public interface ShippingPolicy {
    double shipping(Product p, int qty, double subtotal);
}

