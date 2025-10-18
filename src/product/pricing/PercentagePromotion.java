package product.pricing;
import product.Product;

public class PercentagePromotion extends Promotion {
    private final double percent; // 0..90

    public PercentagePromotion(String code, double percent) {
        super(code);
        this.percent = Math.max(0, Math.min(90, percent));
    }

    @Override protected double discountedUnitPrice(Product p, int qty) {
        return p.getPrice() * (1 - percent / 100.0);
    }

    @Override public String name() { return "Percent-" + percent + "%(" + code() + ")"; }
}
