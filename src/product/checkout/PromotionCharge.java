package product.checkout;

import product.Product;
import product.pricing.PricePolicy;

public class PromotionCharge implements Charge {
    private final PricePolicy policy;
    public PromotionCharge(PricePolicy policy) { this.policy = policy; }
    @Override public String name() { return "Promotion(" + policy.name() + ")"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        // price with selected policy (ignores previous subtotal)
        return policy.apply(p, qty);
    }
}

