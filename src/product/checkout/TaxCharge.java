package product.checkout;

import product.Product;
import product.tax.TaxPolicy;

public class TaxCharge implements Charge {
    private final TaxPolicy policy;
    public TaxCharge(TaxPolicy policy) { this.policy = policy; }
    @Override public String name() { return "Tax(" + policy.getClass().getSimpleName() + ")"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        return subtotal + policy.tax(p, qty, subtotal);
    }
}

