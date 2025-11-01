package product.checkout;

import product.Product;
import product.PhysicalProduct;

public class Policies {
    /** Flat shipping for physical items based on product's estimate; free for digital. */
    public static final ShippingPolicy SIMPLE = (Product p, int qty, double s) -> {
        if (p instanceof PhysicalProduct pp) {
            return pp.estimateShippingCost();
        }
        return 0.0;
    };
}

