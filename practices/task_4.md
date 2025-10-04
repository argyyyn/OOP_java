# Practice 4 — Inheritance: Product Types (Physical, Digital)

> **Builds on:** Task 1–3 (`Product` with encapsulation, constructors, static factory).  
> **Scope Limit:** *Only products* — no new domain entities.

---

## 🎯 Objective
Introduce **inheritance** by creating specialized product types that extend your existing `product.Product`:

- `PhysicalProduct` — physical product (weight, dimensions, shipping).
- `DigitalProduct` — digital product (download size, license).

Keep it **simple**: private fields, guarded setters (`trySet...`), constructors with `super(...)`, and clear `toString()`.

---

## ✅ What to Implement

### Project Layout
```
src/
└── product/
    ├── Product.java            # from Task 2–3 (with guards and constructors)
    ├── PhysicalProduct.java    # Product subclass
    └── DigitalProduct.java     # Product subclass
```

### 1) `PhysicalProduct extends Product`
**Private fields:**
- `double weightKg` (0..1000)
- `double lengthCm, widthCm, heightCm` (each 0..1000)

**Methods:**
- `boolean trySetWeightKg(double v)`
- `boolean trySetDimensions(double l, double w, double h)` — all valid, otherwise `false`
- `double estimateShippingCost()`  
  Formula:  
  `volumetric = (l * w * h) / 5000.0` → `billable = max(weightKg, volumetric)` → `cost = billable * 100` (KZT)

**Constructors (chaining required):**
- `PhysicalProduct()` — safe defaults via `super()`
- `PhysicalProduct(String id, String name, double price, double weightKg)`
- `PhysicalProduct(String id, String name, String description, double price, int quantity, double weightKg, double l, double w, double h)`

Inside constructors use `trySet...` from `Product`; invalid values are **ignored** (defaults are preserved).

### 2) `DigitalProduct extends Product`
**Private fields:**
- `double downloadSizeMb` (0..1_000_000)
- `String licenseKey` (nullable, length ≤ 64)

**Methods:**
- `boolean trySetDownloadSizeMb(double v)`
- `boolean trySetLicenseKey(String key)`
- `boolean isLicenseRequired()` → `licenseKey != null && !licenseKey.isBlank()`

**Constructors:**
- `DigitalProduct()`
- `DigitalProduct(String id, String name, double price, double downloadSizeMb)`
- `DigitalProduct(String id, String name, String description, double price, int quantity, double downloadSizeMb, String licenseKey)`

### 3) Printing
Override `toString()` in each subclass: add subclass info to `super.toString()`.

---

## 🧪 Demo (`product.ShopDemo4`)
Show at minimum:

1) Creation using **three** types of constructors for each subclass (one example each).  
2) Valid and invalid updates:
```java
laptop.trySetDimensions(-1, 10, 10)   // false
ebook.trySetDownloadSizeMb(2048)      // true
```
3) Subclass method calls:
```java
laptop.estimateShippingCost();
ebook.isLicenseRequired();
```
4) Printing via `System.out.println(...)` — shows `toString()` of base class and subclass additions.

---

## ✅ Acceptance Criteria
- `PhysicalProduct` and `DigitalProduct` **extends Product**.
- New fields are **private**; modification only through `trySet...`.
- Constructors use `super(...)` and **constructor chaining**; guards maintain correct state.
- `toString()` of subclasses adds their fields to the base string.
- Demo contains ≥1 successful and ≥2 rejected updates, and subclass method calls.

---