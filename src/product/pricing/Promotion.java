package product.pricing;

import product.Product;

/**
 * Base for all promotions. Implements PricePolicy with a Template Method.
 * Subclasses typically implement 'discountedUnitPrice(...)'.
 */
public abstract class Promotion implements PricePolicy {
    private final String code; // id/code (non-null, len>=1)

    protected Promotion(String code) {
        this.code = (code == null || code.isBlank()) ? "PROMO" : code.trim();
    }

    public final String code() { return code; }

    /** Default label uses the class + code. Subclasses may override. */
    @Override public String name() { return getClass().getSimpleName() + "(" + code + ")"; }

    /** Template Method: total price for qty units; clamps qty and calls a hook. */
    @Override
    public double apply(Product p, int qty) {
        int q = Math.max(0, qty);
        double unit = discountedUnitPrice(p, q);
        return Math.max(0.0, unit) * q;
    }

    /** Hook for subclasses: return *unit* price after discount for given qty/product. */
    protected abstract double discountedUnitPrice(Product p, int qty);

    /** Promotions are applicable to any product by default. */
    @Override public boolean applicableTo(Product p) { return true; }
}
