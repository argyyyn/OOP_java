package product.checkout;

import product.Product;

public class ShippingCharge implements Charge {
    private final ShippingPolicy policy;
    public ShippingCharge(ShippingPolicy policy) { this.policy = policy; }
    @Override public String name() { return "Shipping(" + policy.getClass().getSimpleName() + ")"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        return subtotal + policy.shipping(p, qty, subtotal);
    }
}

