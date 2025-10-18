package product.pricing;
import product.Product;

public class FixedPromotion extends Promotion {
    private final double amount; // >=0

    public FixedPromotion(String code, double amount) {
        super(code);
        this.amount = Math.max(0, amount);
    }

    @Override protected double discountedUnitPrice(Product p, int qty) {
        return Math.max(0.0, p.getPrice() - amount);
    }
}
