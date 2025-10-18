package product.tax;
import product.Product;
import product.DigitalProduct;

public class ReducedDigitalVat implements TaxPolicy {
    private final double percent; // e.g., 5.0
    public ReducedDigitalVat(double percent) { this.percent = Math.max(0, percent); }
    @Override public double tax(Product p, int qty, double subtotal) {
        if (p instanceof DigitalProduct) return subtotal * (percent / 100.0);
        return 0.0;
    }
}
