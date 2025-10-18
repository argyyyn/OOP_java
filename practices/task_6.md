# Practice 6 — Abstract Classes & Interfaces: Promotions (Template Method) + Tax & Shipping Interfaces

> **Builds on:** Task 1–5 (`Product`, `PhysicalProduct`, `DigitalProduct`, price rules).  
> **Scope:** Keep the domain minimal (Products only). We will add **one abstract class** for promotions and **two small interfaces** for cross-cutting behavior.

---

## 🎯 Goal
1) Refactor pricing rules to showcase an **abstract class** with a **Template Method**.  
2) Add **interfaces** for tax and shipping that different product types can implement/use.

You will:
- Create `abstract class Promotion` that implements `PricePolicy` and factors shared logic.  
- Implement three concrete promotions by **extending** `Promotion`.  
- Add interfaces `TaxPolicy` and `Shippable` and wire them into price calculation.  
- Demonstrate polymorphism with lists of `Promotion` and different `TaxPolicy` on `PhysicalProduct` vs `DigitalProduct`.

---

## 📁 Project Structure
```
src/
└── product/
    ├── Product.java
    ├── PhysicalProduct.java
    ├── DigitalProduct.java
    ├── pricing/
    │   ├── PricePolicy.java            # from Task 5 (keep it)
    │   ├── Promotion.java              # NEW (abstract class)
    │   ├── PercentagePromotion.java    # extends Promotion
    │   ├── FixedPromotion.java         # extends Promotion
    │   └── BogoHalfPromotion.java      # extends Promotion (pair logic)
    ├── tax/
    │   ├── TaxPolicy.java              # NEW (interface)
    │   ├── NoTax.java                  # implements TaxPolicy
    │   ├── FlatVat.java                # implements TaxPolicy (e.g., 12%)
    │   └── ReducedDigitalVat.java      # implements TaxPolicy (e.g., 5% only for DigitalProduct)
    ├── shipping/
    │   └── Shippable.java              # NEW (interface)
    └── ShopDemo6.java
```

---

## 1) Abstract Class: `Promotion` (Template Method over `PricePolicy`)

### Why
In Task 5 each rule computed totals independently. Now we **centralize common logic** (validation, qty clamp, naming) in an **abstract base** and let subclasses provide only the delta.

```java
// product/pricing/Promotion.java
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
    public final double apply(Product p, int qty) {
        int q = Math.max(0, qty);
        double unit = discountedUnitPrice(p, q);
        return Math.max(0.0, unit) * q;
    }

    /** Hook for subclasses: return *unit* price after discount for given qty/product. */
    protected abstract double discountedUnitPrice(Product p, int qty);

    /** Promotions are applicable to any product by default. */
    @Override public boolean applicableTo(Product p) { return true; }
}
```

### Concrete Promotions

```java
// product/pricing/PercentagePromotion.java
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
```

```java
// product/pricing/FixedPromotion.java
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
```

```java
// product/pricing/BogoHalfPromotion.java
// For every pair: second is -50%. We override apply() because logic is per *pair*, not per unit.
package product.pricing;
import product.Product;

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
```

> You still keep `PricePolicy` interface (from Task 5), but now every promotion is both a `PricePolicy` **and** a `Promotion` subclass — showing interface + abstract class together.

---

## 2) Interfaces: `TaxPolicy` and `Shippable`

### `TaxPolicy` (strategy interface for tax calculation)
```java
// product/tax/TaxPolicy.java
package product.tax;

import product.Product;

public interface TaxPolicy {
    /**
     * Calculate tax for given product, qty, and subtotal (after promotions, before shipping).
     * Must NOT mutate Product.
     */
    double tax(Product p, int qty, double subtotal);
}
```

Implementations:
```java
// NoTax.java
package product.tax;
import product.Product;

public class NoTax implements TaxPolicy {
    @Override public double tax(Product p, int qty, double subtotal) { return 0.0; }
}
```

```java
// FlatVat.java  (e.g., 12% VAT for all products)
package product.tax;
import product.Product;

public class FlatVat implements TaxPolicy {
    private final double percent; // e.g., 12.0
    public FlatVat(double percent) { this.percent = Math.max(0, percent); }
    @Override public double tax(Product p, int qty, double subtotal) { return subtotal * (percent / 100.0); }
}
```

```java
// ReducedDigitalVat.java (e.g., 5% only for DigitalProduct)
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
```

### `Shippable` (marker interface for shipping cost)
```java
// product/shipping/Shippable.java
package product.shipping;

public interface Shippable {
    double shippingCost();
}
```

Make `PhysicalProduct` implement `Shippable` by delegating to its existing shipping logic:
```java
// PhysicalProduct.java (excerpt)
import product.shipping.Shippable;

public class PhysicalProduct extends Product implements Shippable {
    @Override public double shippingCost() { return estimateShippingCost(); }
}
```

---

## 3) Putting It Together — Flow to compute order-like total (without Orders)
For a given `Product p`, `qty`, a chosen `Promotion promo`, and a chosen `TaxPolicy tax`:
1. **Subtotal before shipping:** `subtotal = p.finalPrice(qty, promo)` (from Task 5).  
2. **Tax:** `taxAmount = tax.tax(p, qty, subtotal)`.  
3. **Shipping (if shippable):**
   ```java
   double shipping = (p instanceof product.shipping.Shippable s) ? s.shippingCost() : 0.0;
   ```
4. **Grand total:** `total = subtotal + taxAmount + shipping`.

This shows abstract class (Promotion), interface (TaxPolicy, Shippable), and polymorphism.

---

## 4) Demo — `ShopDemo6`
```java
// product/ShopDemo6.java
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
```

**What to observe**
- `Promotion` provides a **shared apply flow**; subclasses only change discount logic.  
- `PhysicalProduct` implements `Shippable`; `DigitalProduct` does not.  
- `TaxPolicy` changes totals without touching product classes.  
- You can swap promotions/taxes at runtime → **interface polymorphism** + **abstract base reuse**.

---

## ✅ Acceptance Criteria
- `Promotion` (abstract class) exists and implements `PricePolicy` using a **Template Method**.  
- `PercentagePromotion`, `FixedPromotion`, `BogoHalfPromotion` extend `Promotion` and work as expected.  
- `TaxPolicy` interface + at least two implementations (`NoTax`, `FlatVat`), and a conditional one (`ReducedDigitalVat`).  
- `Shippable` interface implemented by `PhysicalProduct`.  
- `ShopDemo6` produces totals that reflect promo + tax + shipping for both product types and for `qty=1` and `qty=2`.  
- No mutation of product state during calculations; guards/encapsulation from previous tasks remain.

---

## ▶️ How to Run
```bash
cd /Users/argo/OOP_java/src
javac product/pricing/*.java product/tax/*.java product/shipping/*.java product/*.java
java product.ShopDemo6
```

---

## (Optional) Extensions
- Add `CompositePromotion` that applies multiple promotions in sequence.  
- Add `CappedFixedPromotion` (fixed off up to a cap percentage).  
- Add `Shippable` default method `default double shippingCostForQty(int qty)` that scales cost.
