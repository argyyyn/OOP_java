package product.checkout;

import product.Product;
import java.util.List;

public class CheckoutCalculator {
    private final List<Charge> pipeline;
    public CheckoutCalculator(List<Charge> pipeline) { this.pipeline = List.copyOf(pipeline); }

    public Receipt checkout(Product p, int qty) {
        double subtotal = p.getPrice() * Math.max(0, qty); // base
        Receipt r = new Receipt();
        r.add("Base(qty×price)", subtotal);
        for (Charge c : pipeline) {
            double next = c.apply(p, qty, subtotal);
            r.add(c.name(), next - subtotal); // delta of this stage
            subtotal = next;
        }
        r.setTotal(subtotal);
        return r;
    }
}

