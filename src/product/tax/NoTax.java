package product.tax;
import product.Product;

public class NoTax implements TaxPolicy {
    @Override public double tax(Product p, int qty, double subtotal) { return 0.0; }
}
