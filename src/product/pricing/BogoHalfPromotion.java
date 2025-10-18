package product.pricing;
import product.Product;

// For every pair: second is -50%. We override apply() because logic is per *pair*, not per unit.
public class BogoHalfPromotion extends Promotion {
    public BogoHalfPromotion(String code) { super(code); }

    @Override protected double discountedUnitPrice(Product p, int qty) {
        // Not used; apply(...) overridden for pairs; return base price to keep non-negative.
        return p.getPrice();
    }

    @Override
    public double apply(Product p, int qty) {
        int q = Math.max(0, qty);
        double price = p.getPrice();
        int pairs = q / 2;
        int singles = q % 2;
        return pairs * (price * 1.5) + singles * price;
    }
}
