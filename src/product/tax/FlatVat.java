package product.tax;
import product.Product;

public class FlatVat implements TaxPolicy {
    private final double percent; // e.g., 12.0
    public FlatVat(double percent) { this.percent = Math.max(0, percent); }
    @Override public double tax(Product p, int qty, double subtotal) { return subtotal * (percent / 100.0); }
}
