package product;

import product.pricing.*;
import product.tax.*;
import product.shipping.*;
import java.util.List;

public class ShopDemo6 {
    public static void main(String[] args) {
        PhysicalProduct laptop = new PhysicalProduct("P-LAP-1","Laptop",450_000.0,1.8);
        laptop.trySetDimensions(35,24,2);

        DigitalProduct ebook = new DigitalProduct("P-EBK-1","E-Book",1_500.0,12.5);

        List<Promotion> promos = List.of(
            new PercentagePromotion("P10", 10),
            new FixedPromotion("F50", 50),
            new BogoHalfPromotion("BOGO")
        );

        TaxPolicy noTax = new NoTax();
        TaxPolicy vat12 = new FlatVat(12.0);
        TaxPolicy digital5 = new ReducedDigitalVat(5.0);

        for (Product p : List.of(laptop, ebook)) {
            for (int qty : new int[]{1, 2}) {
                for (Promotion promo : promos) {
                    double subtotal = p.finalPrice(qty, promo); // Task 5 overloading/overriding
                    double tax = (p instanceof DigitalProduct) ? digital5.tax(p, qty, subtotal)
                                                               : vat12.tax(p, qty, subtotal);
                    double shipping = (p instanceof Shippable s) ? s.shippingCost() : 0.0;
                    double total = subtotal + tax + shipping;

                    System.out.printf("%n%s | qty=%d | %s -> subtotal=%.2f, tax=%.2f, ship=%.2f, TOTAL=%.2f%n",
                        p.getName(), qty, promo.name(), subtotal, tax, shipping, total);
                }
            }
        }
    }
}
