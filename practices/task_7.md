# Practice 7 — SOLID (Part 1): Single‑Responsibility & Open‑Closed

> **Builds on:** Tasks 1–6 (`Product`, `PhysicalProduct`, `DigitalProduct`, Promotions, Tax, Shipping).  
> **Focus:** Keep code simple, but **separate responsibilities (SRP)** and design the pricing flow to be **extensible without modification (OCP)**.

---

## 🎯 Learning Goals
- **SRP:** Each class has one clear reason to change.
- **OCP:** Add new pricing rules/fees **by adding new classes**, not editing existing ones.

---

## What you will do
1) **Refactor responsibilities**: keep `Product` as **data + guards only**; move stock operations to `InventoryService`.  
2) **Introduce an extensible checkout pipeline** using small **charges** (promotion/tax/shipping/etc.) that can be composed in order.  
3) **Prove OCP** by adding a **new fee** without touching existing classes.

> Keep it minimal. Don’t rewrite old tasks—just add the missing structure and route the new demo through it.

---

## 📁 Project Structure (suggested)
```
src/
└── product/
    ├── Product.java
    ├── PhysicalProduct.java
    ├── DigitalProduct.java
    ├── pricing/
    │   ├── PricePolicy.java
    │   ├── Promotion.java
    │   ├── PercentagePromotion.java
    │   ├── FixedPromotion.java
    │   └── BogoHalfPromotion.java
    ├── tax/
    │   ├── TaxPolicy.java
    │   ├── NoTax.java
    │   ├── FlatVat.java
    │   └── ReducedDigitalVat.java
    ├── shipping/
    │   └── Shippable.java
    ├── inventory/
    │   └── InventoryService.java            # NEW (SRP: quantity changes)
    └── checkout/
        ├── Charge.java                      # NEW (OCP: stage in pipeline)
        ├── PromotionCharge.java             # wraps a PricePolicy/Promotion
        ├── TaxCharge.java                   # wraps a TaxPolicy
        ├── ShippingPolicy.java              # NEW – strategy for shipping
        ├── ShippingCharge.java              # wraps a ShippingPolicy
        ├── EnvironmentalFeeCharge.java      # NEW – prove OCP
        ├── CheckoutCalculator.java          # runs the pipeline
        └── Receipt.java                     # breakdown + total
```

---

## 1) SRP — InventoryService

**Move stock mutations out of `Product`:**
```java
// product/inventory/InventoryService.java
package product.inventory;

import product.Product;

public class InventoryService {
    /**
     * Increase quantity by amount (>0). Return true if applied.
     */
    public boolean addStock(Product p, int amount) {
        if (p == null || amount <= 0) return false;
        long next = (long) p.getQuantity() + amount;
        if (next > 1_000_000L) return false;
        return p.trySetQuantity((int) next);
    }

    /**
     * Sell (reduce) quantity by amount (>0 and <= current). Return true if applied.
     */
    public boolean sell(Product p, int amount) {
        if (p == null || amount <= 0 || amount > p.getQuantity()) return false;
        return p.trySetQuantity(p.getQuantity() - amount);
    }
}
```
> From now on, **client code calls `InventoryService`** for stock changes. `Product` remains **state + validation** only.

---

## 2) OCP — Extensible Checkout Pipeline

### 2.1 Charge interface (single stage)
```java
// product/checkout/Charge.java
package product.checkout;

import product.Product;

/** One stage in the pricing pipeline. */
public interface Charge {
    String name();
    /**
     * Apply a charge/discount to the running subtotal and return the new subtotal.
     * Must NOT mutate Product.
     */
    double apply(Product p, int qty, double subtotal);
}
```

### 2.2 Built‑in charges

**PromotionCharge** (wrap any `PricePolicy/Promotion`; replaces subtotal with discounted price for qty):
```java
// product/checkout/PromotionCharge.java
package product.checkout;

import product.Product;
import product.pricing.PricePolicy;

public class PromotionCharge implements Charge {
    private final PricePolicy policy;
    public PromotionCharge(PricePolicy policy) { this.policy = policy; }
    @Override public String name() { return "Promotion(" + policy.name() + ")"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        // price with selected policy (ignores previous subtotal)
        return policy.apply(p, qty);
    }
}
```

**TaxCharge** (wrap any `TaxPolicy`):
```java
// product/checkout/TaxCharge.java
package product.checkout;

import product.Product;
import product.tax.TaxPolicy;

public class TaxCharge implements Charge {
    private final TaxPolicy policy;
    public TaxCharge(TaxPolicy policy) { this.policy = policy; }
    @Override public String name() { return "Tax(" + policy.getClass().getSimpleName() + ")"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        return subtotal + policy.tax(p, qty, subtotal);
    }
}
```

**ShippingPolicy** and **ShippingCharge** (strategy + charge)
```java
// product/checkout/ShippingPolicy.java
package product.checkout;

import product.Product;

public interface ShippingPolicy {
    double shipping(Product p, int qty, double subtotal);
}
```
```java
// product/checkout/ShippingCharge.java
package product.checkout;

import product.Product;

public class ShippingCharge implements Charge {
    private final ShippingPolicy policy;
    public ShippingCharge(ShippingPolicy policy) { this.policy = policy; }
    @Override public String name() { return "Shipping(" + policy.getClass().getSimpleName() + ")"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        return subtotal + policy.shipping(p, qty, subtotal);
    }
}
```

**Example shipping policy** using existing `PhysicalProduct` logic:
```java
// product/checkout/Policies.java (optional helpers)
package product.checkout;

import product.Product;
import product.PhysicalProduct;

public class Policies {
    /** Flat shipping for physical items based on product's estimate; free for digital. */
    public static final ShippingPolicy SIMPLE = (Product p, int qty, double s) -> {
        if (p instanceof PhysicalProduct pp) {
            return pp.estimateShippingCost();
        }
        return 0.0;
    };
}
```

### 2.3 New fee (prove OCP)
```java
// product/checkout/EnvironmentalFeeCharge.java
package product.checkout;

import product.Product;

public class EnvironmentalFeeCharge implements Charge {
    private final double feePerUnit; // >= 0
    public EnvironmentalFeeCharge(double feePerUnit) { this.feePerUnit = Math.max(0, feePerUnit); }
    @Override public String name() { return "EnvFee(" + feePerUnit + "/unit)"; }
    @Override public double apply(Product p, int qty, double subtotal) {
        return subtotal + feePerUnit * Math.max(0, qty);
    }
}
```

### 2.4 CheckoutCalculator + Receipt

```java
// product/checkout/Receipt.java
package product.checkout;

import java.util.ArrayList;
import java.util.List;

public class Receipt {
    public static class Line {
        public final String name;
        public final double value;
        public Line(String name, double value){ this.name=name; this.value=value; }
    }
    private final List<Line> lines = new ArrayList<>();
    private double total;

    public void add(String name, double value) { lines.add(new Line(name, value)); }
    public void setTotal(double total) { this.total = total; }
    public List<Line> lines(){ return lines; }
    public double total(){ return total; }
}
```

```java
// product/checkout/CheckoutCalculator.java
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
```

> **OCP proof:** To add a new fee, create a new `Charge` implementation and put it in the pipeline. No edits to `CheckoutCalculator` or other charges.

---

## 3) Demo — `ShopDemo7`

```java
// product/ShopDemo7.java
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
```

**What to notice**
- `InventoryService` holds **all** stock change logic (SRP).  
- `CheckoutCalculator` is unchanged when adding `EnvironmentalFeeCharge` (OCP).  
- New charges are plug‑and‑play and independent.

---

## ✅ Acceptance Criteria
- `InventoryService` exists and is used by the demo to mutate quantity; `Product` is not mutated directly in demo.  
- `Charge` interface and `CheckoutCalculator` exist; pipeline order is respected.  
- At least **three charges** implemented: `PromotionCharge`, `TaxCharge`, `ShippingCharge`; plus **one new** `EnvironmentalFeeCharge`.  
- Adding a new fee requires **no edits** to existing charges or calculator (just compose).  
- Demo prints a receipt with per‑stage deltas and a final total for one physical and one digital product.

---

## ▶️ How to Run
```bash
cd /Users/argo/OOP_java/src
javac product/inventory/*.java product/checkout/*.java product/pricing/*.java product/tax/*.java product/*.java
java product.ShopDemo7
```

---

## (Optional) Extensions
- Add `RoundingCharge` as a new stage (round to nearest 1 KZT).  
- Add `BulkThresholdPromotion` by writing a new `Promotion` subclass and swapping in the pipeline.  
- Add `InventoryAuditService` (read‑only report) to further separate responsibilities.
