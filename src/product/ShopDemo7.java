package product;

import product.inventory.InventoryService;
import product.pricing.*;
import product.tax.*;
import product.checkout.*;
import java.util.List;

public class ShopDemo7 {
    public static void main(String[] args) {
        // SRP: use InventoryService for stock changes
        InventoryService inv = new InventoryService();

        PhysicalProduct laptop = new PhysicalProduct("P-LAP-1","Laptop",450_000.0,1.8);
        laptop.trySetDimensions(35,24,2);
        inv.addStock(laptop, 10);
        inv.sell(laptop, 2);

        DigitalProduct ebook = new DigitalProduct("P-EBK-1","E-Book",1_500.0,12.5);

        // OCP: assemble pipeline
        var promo = new PercentagePromotion("P10", 10);
        var tax   = new FlatVat(12.0);
        var ship  = new ShippingCharge(Policies.SIMPLE);
        var env   = new EnvironmentalFeeCharge(30.0); // new feature w/o touching existing classes

        CheckoutCalculator calc = new CheckoutCalculator(List.of(
            new PromotionCharge(promo),
            new TaxCharge(tax),
            ship,
            env
        ));

        for (Product p : List.of(laptop, ebook)) {
            var receipt = calc.checkout(p, 2);
            System.out.println("\n== " + p.getName() + " ==");
            for (var line : receipt.lines()) {
                System.out.printf("%-22s %+10.2f%n", line.name, line.value);
            }
            System.out.printf("%-22s %10.2f%n", "TOTAL", receipt.total());
        }
    }
}

