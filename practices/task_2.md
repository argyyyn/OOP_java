# Practice 2 — Encapsulation & Validation Guard (Tech Requirement)

> **Theme:** Keep your objects in a **valid state**.  
> **Builds on:** Task 1 (`Product`, `Category`, `ShopDemo`).  
> **Rule of thumb:** *No invalid data gets inside `Product`.*

---

## 🎯 Goal
Refactor **`product.Product`** (and a tiny bit of **`category.Category`**) to enforce **encapsulation** and add **simple validation guards**.  
No exceptions yet, no complex patterns. Just **private fields + guarded updates** that return `boolean`.

---

## ✅ What to change (minimal & easy)

### 1) Encapsulation
- All fields in `Product` must be **`private`**.
- Expose state via **getters** only.
- Replace plain setters with **guarded mutators** that **return `boolean`** and **do nothing on invalid input**.

### 2) Validation rules (keep it simple)
- `id`: not `null`, trimmed length `>= 2`.
- `name`: not `null`, trimmed length `>= 2`.
- `description`: can be `null` or trimmed; if not null, length `<= 200`.
- `price`: `>= 0.0` and `<= 1_000_000.0`.
- `quantity`: `>= 0` and `<= 1_000_000`.
- `category`: not `null`.
- **Discounts**: percent must be `0..90`. If outside → ignore and return original price in preview.

### 3) Guarded mutators (proposed signatures)
```java
// inside product.Product
public boolean trySetId(String id);
public boolean trySetName(String name);
public boolean trySetDescription(String description);
public boolean trySetPrice(double price);
public boolean trySetQuantity(int quantity);
public boolean trySetCategory(category.Category category);
```
- Return `true` **only if** the field is updated.
- On invalid input: return `false` and **do not** change the field.

### 4) Use guards in inventory methods
- `addStock(int amount)`: accept only `amount > 0`; update `quantity`; return `true/false`.
- `sellProduct(int amount)`: only if `amount > 0` and `amount <= quantity`; update `quantity`; return `true/false`.
- `applyDiscount(double percent)`: if `percent` not in `0..90`, **do nothing** and return `false`; if valid, apply new price and return `true`.
  - *(If your Task 1 had a “preview” method instead of mutation, keep that; just enforce the `0..90` rule.)*
- `calculateTotalValue()`: unchanged (but now it relies on guarded values).

### 5) Computed stock status (no direct setter)
- Remove any public setter for “stock status”.  
- Provide a **derived** read-only method:
```java
public String getStockStatus(); // returns "OUT_OF_STOCK" (0), "LOW" (1..10), or "IN_STOCK" (>10)
```
- Thresholds: `0 → OUT_OF_STOCK`, `1..10 → LOW`, `>10 → IN_STOCK`.

### 6) Category safety (tiny change)
- In `category.Category`, make `addProduct(Product p)` return `boolean`:
  - Reject `null`.
  - Reject duplicates of the **same object reference** (simple `contains` check).
- `removeProduct(Product p)` can stay as in Task 1 (or return `boolean` if you prefer).

---

## 🧪 Demo requirements (`product.ShopDemo`)
Update your demo to prove guards work:

1. Create a `Category` (e.g., “Stationery”) and a `Product` (e.g., Pen).
2. **Accepted updates:**
   - `trySetPrice(250.0)` → `true`
   - `addStock(20)` when quantity was, say, `5` → `true` → quantity becomes `25`
3. **Rejected updates:**
   - `trySetPrice(-1.0)` → `false` (price unchanged)
   - `trySetName("A")` → `false` (name unchanged)
   - `sellProduct(10_000)` if quantity is lower → `false` (quantity unchanged)
   - `applyDiscount(200)` → `false` (ignored)
4. Show `getStockStatus()` before/after restocks/sales.
5. Add the product to the category twice; the **second add** must be rejected.

**Print** each action and the resulting state so it’s obvious what passed/failed.

---

## ✅ Acceptance Criteria
- All `Product` fields are `private`; external code cannot mutate them directly.
- Guarded mutators above exist and **preserve old values** on invalid input.
- Inventory methods (`addStock`, `sellProduct`, `applyDiscount`) validate inputs and return `boolean`.
- `getStockStatus()` is computed from `quantity` (no direct setter).
- `Category.addProduct` rejects `null` and duplicates (by reference).
- `ShopDemo` prints at least **2 accepted** and **3 rejected** actions.

---

## 📊 Suggested Grading ( /10 )
- Encapsulation (private fields + getters) — **2**
- Guarded mutators work (return `boolean`, no state corruption) — **4**
- Inventory methods validate correctly — **2**
- Demo proves both accepted/rejected flows + stock status — **2**

---

## ✂️ Starter patch (copy/paste)

```java
// product/Product.java  (only signatures & tiny bodies shown; fill in validation)
package product;

import category.Category;

public class Product {
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category;

    // ---- Getters (read-only access for now) ----
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public Category getCategory() { return category; }

    // ---- Guarded mutators ----
    public boolean trySetId(String id) {
        if (id != null && id.trim().length() >= 2) { this.id = id.trim(); return true; }
        return false;
    }
    public boolean trySetName(String name) {
        if (name != null && name.trim().length() >= 2) { this.name = name.trim(); return true; }
        return false;
    }
    public boolean trySetDescription(String description) {
        if (description == null || description.trim().length() <= 200) { this.description = description == null ? null : description.trim(); return true; }
        return false;
    }
    public boolean trySetPrice(double price) {
        if (price >= 0.0 && price <= 1_000_000.0) { this.price = price; return true; }
        return false;
    }
    public boolean trySetQuantity(int quantity) {
        if (quantity >= 0 && quantity <= 1_000_000) { this.quantity = quantity; return true; }
        return false;
    }
    public boolean trySetCategory(Category category) {
        if (category != null) { this.category = category; return true; }
        return false;
    }

    // ---- Inventory & business ops (guarded) ----
    public boolean addStock(int amount) {
        if (amount <= 0) return false;
        long next = (long) quantity + amount; // avoid overflow
        if (next > 1_000_000L) return false;
        quantity += amount;
        return true;
    }
    public boolean sellProduct(int amount) {
        if (amount <= 0 || amount > quantity) return false;
        quantity -= amount;
        return true;
    }
    public boolean applyDiscount(double percent) {
        if (percent < 0 || percent > 90) return false;
        double factor = 1 - percent / 100.0;
        double next = price * factor;
        // keep within allowed bounds
        if (next < 0.0 || next > 1_000_000.0) return false;
        price = next;
        return true;
    }

    public double calculateTotalValue() {
        return price * quantity;
    }

    public String getStockStatus() {
        if (quantity == 0) return "OUT_OF_STOCK";
        if (quantity <= 10) return "LOW";
        return "IN_STOCK";
    }

    public void displayProductInfo() {
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return "Product{id='%s', name='%s', price=%.2f, qty=%d, status=%s, category=%s}"
            .formatted(id, name, price, quantity, getStockStatus(),
                       category == null ? "NONE" : category.getName());
    }
}
```

```java
// category/Category.java  (only the tiny change shown)
package category;

import product.Product;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private String id;
    private String name;
    private String description;
    private final List<Product> products = new ArrayList<>();

    public String getName() { return name; }

    public boolean addProduct(Product p) {
        if (p == null) return false;
        if (products.contains(p)) return false; // reject duplicate reference
        products.add(p);
        return true;
    }

    public boolean removeProduct(Product p) {
        return products.remove(p);
    }

    // ... other methods from Task 1 stay as-is
}
```

```java
// product/ShopDemo.java  (show accepted & rejected flows)
package product;

import category.Category;

public class ShopDemo {
    public static void main(String[] args) {
        Category stationery = new Category();
        // assume Category has simple setters or constructor from Task 1
        // (you can keep them or convert to guards similarly if you want)

        Product pen = new Product();
        pen.trySetId("P001");
        pen.trySetName("Pen");
        pen.trySetDescription("Blue ink ballpoint pen");
        pen.trySetPrice(200.0);
        pen.trySetQuantity(5);
        pen.trySetCategory(stationery);

        System.out.println(pen);
        System.out.println("Status: " + pen.getStockStatus());

        // Accepted
        System.out.println("trySetPrice(250.0): " + pen.trySetPrice(250.0) + " | price=" + pen.getPrice());
        System.out.println("addStock(20): " + pen.addStock(20) + " | qty=" + pen.getQuantity());

        // Rejected
        System.out.println("trySetPrice(-1.0): " + pen.trySetPrice(-1.0) + " | price=" + pen.getPrice());
        System.out.println("trySetName("A"): " + pen.trySetName("A") + " | name=" + pen.getName());
        System.out.println("sellProduct(10000): " + pen.sellProduct(10000) + " | qty=" + pen.getQuantity());
        System.out.println("applyDiscount(200): " + pen.applyDiscount(200) + " | price=" + pen.getPrice());

        // Category duplicate check
        System.out.println("Add to category #1: " + stationery.addProduct(pen));
        System.out.println("Add to category #2 (duplicate): " + stationery.addProduct(pen));

        // Final view
        System.out.println(pen);
        System.out.println("Total value: " + pen.calculateTotalValue());
    }
}
```

---

## ▶️ How to Run
```bash
cd /Users/argo/OOP_java/src
javac category/*.java product/*.java
java product.ShopDemo
```

> **Tip:** If your IDE auto-generates setters, rename them to `trySetX` and adapt the body to follow the guards above.
