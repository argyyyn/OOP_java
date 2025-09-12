# Practice 1 — Product Class Bootcamp (Tech Requirement)

## Goal
Model a basic **Product** for our Internet Shop using only *objects & classes*. No advanced topics yet (no validation rules, no exceptions, no collections magic).

## Scope (what to implement)
1. **Class `Product`**
   - **Fields (all `private`):**
     - `String id`
     - `String name`
     - `double price`  *(in KZT or your preferred currency)*
     - `int stock`
     - `Category category`
   - **Enum `Category`** with a few values: `STATIONERY, ELECTRONICS, GROCERY, CLOTHING, OTHER`.
   - **Constructors:**
     - Empty (no-args).
     - All-args (set every field).
     *(Validation comes in Practice 2; here you can assign directly.)*
   - **Getters/Setters** for all fields. *(Keep them simple—no checks yet.)*
   - **Behavior methods:**
     - `double priceAfterDiscount(double percent)` – returns the price *after* applying `percent` (e.g., `10` → 10%). **Does not change** the field `price`.
     - `boolean canPurchase(int qty)` – returns `true` if `qty <= stock` and `qty > 0`.
     - `boolean purchase(int qty)` – if `canPurchase(qty)` then decrease `stock` and return `true`, else return `false`.
     - `void restock(int qty)` – increases `stock` if `qty > 0`, else do nothing.
   - **Utility:**
     - `String toString()` – readable single-line summary of the product.

2. **Demo class `App` (with `main`)**
   - Create 3 sample products, e.g.:
     - `new Product("P001","Pen", 200.0, 100, Category.STATIONERY)`
     - `new Product("P002","Notebook", 950.0, 50, Category.STATIONERY)`
     - `new Product("P003","Headphones", 14990.0, 10, Category.ELECTRONICS)`
   - Print them.
   - Demonstrate:
     - Discount preview: show original price vs `priceAfterDiscount(10)`.
     - Purchase attempts (valid & invalid).
     - Restock and show updated stock.

## Non-Goals (for later practices)
- No input from files/DB, no JSON/CSV.
- No exceptions/validation rules (Practice 2).
- No equals/hashCode or comparators (Practice 10).
- No patterns, tests, or multithreading (later topics).

## Acceptance Criteria
- `Product` and `Category` compile without errors.
- All specified methods exist with exact signatures and work as described.
- `App.main` prints a short, readable demo showing:
  - product info,
  - discount calculation (without mutating `price`),
  - at least one successful and one failed `purchase`,
  - a `restock` followed by a print verifying the new stock.

## Grading (suggested, /10)
- Class & fields private, enum present — **2**
- Constructors + getters/setters — **2**
- Methods (`priceAfterDiscount`, `canPurchase`, `purchase`, `restock`) — **4**
- `toString` readable + `App` demo output shows all scenarios — **2**

## Hints (keep it simple)
- Discount formula: `price * (1 - percent / 100.0)`.
- Do not let `purchase` go below zero stock: always call `canPurchase` first.
- Use `System.out.println(product)` to trigger `toString()`.

---

## Starter Skeleton (optional)

```java
// Category.java
public enum Category {
    STATIONERY, ELECTRONICS, GROCERY, CLOTHING, OTHER
}
```

```java
// Product.java
public class Product {
    private String id;
    private String name;
    private double price;
    private int stock;
    private Category category;

    public Product() { }

    public Product(String id, String name, double price, int stock, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    // Getters & Setters (no validation yet)
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    // Behavior
    public double priceAfterDiscount(double percent) {
        return price * (1 - percent / 100.0);
    }

    public boolean canPurchase(int qty) {
        return qty > 0 && qty <= stock;
    }

    public boolean purchase(int qty) {
        if (!canPurchase(qty)) return false;
        stock -= qty;
        return true;
    }

    public void restock(int qty) {
        if (qty > 0) stock += qty;
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price +
               ", stock=" + stock + ", category=" + category + "}";
    }
}
```

```java
// App.java
public class App {
    public static void main(String[] args) {
        Product pen = new Product("P001","Pen", 200.0, 100, Category.STATIONERY);
        Product notebook = new Product("P002","Notebook", 950.0, 50, Category.STATIONERY);
        Product headphones = new Product("P003","Headphones", 14990.0, 10, Category.ELECTRONICS);

        System.out.println(pen);
        System.out.println(notebook);
        System.out.println(headphones);

        System.out.println("Pen after 10% discount: " + pen.priceAfterDiscount(10));

        System.out.println("Buy 5 notebooks: " + (notebook.purchase(5) ? "OK" : "FAIL"));
        System.out.println("Buy 60 notebooks: " + (notebook.purchase(60) ? "OK" : "FAIL"));

        System.out.println("Headphones stock before restock: " + headphones.getStock());
        headphones.restock(5);
        System.out.println("Headphones stock after restock: " + headphones.getStock());
    }
}
```

---

### Stretch (optional, +bonus)
- Add `String sku` with a simple pattern like `"CAT-0001"` (hardcode for now).
- Add `String shortInfo()` that returns `"Pen (P001): 200.0 KZT"`.
