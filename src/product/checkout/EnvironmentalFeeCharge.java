package product.checkout;

import product.Product;

public class EnvironmentalFeeCharge implements Charge {
    private final double feePerUnit; // >= 0
    public EnvironmentalFeeCharge(double feePerUnit) { this.feePerUnit = Math.max(0, feePerUnit); }
    @Override public String name() { return "EnvFee(" + feePerUnit + "/unit)"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        return subtotal + feePerUnit * Math.max(0, qty);
    }
}

