# Practice 4 — Why Product Subtypes Beat Other Inheritance Options

> **Builds on:** Task 1–3 (`Product` with encapsulation, constructors, static factory).  
> **Scope:** Analysis of inheritance design choices in e-commerce systems.

---

## 🎯 Objective
Analyze **why Product subtypes (Physical/Digital) is the best inheritance choice** compared to other common e-commerce hierarchies:

1. **ShippingOption hierarchy** — needs logistics context
2. **PaymentMethod hierarchy** — needs Orders/Checkout system  
3. **Review hierarchy** — isn't great for inheritance
4. **Product subtypes** — you already implemented (Physical/Digital) ✅

---

## 🔍 Analysis: Why Product Subtypes Win

### ❌ Why Other Options Don't Work Well

#### 1. ShippingOption Hierarchy
```java
// PROBLEMATIC: Too much logistics context needed
abstract class ShippingOption {
    abstract double calculateCost(Address from, Address to, Package package);
    abstract int estimateDays(Address from, Address to);
    abstract boolean isAvailable(Address from, Address to);
}
class StandardShipping extends ShippingOption { ... }
class ExpressShipping extends ShippingOption { ... }
class OvernightShipping extends ShippingOption { ... }
```
**Issues:**
- Requires complex logistics system (Address, Package, routing)
- Needs external APIs for real-time shipping rates
- Too much business logic outside core product domain
- Hard to test without full shipping infrastructure

#### 2. PaymentMethod Hierarchy  
```java
// PROBLEMATIC: Needs complete checkout system
abstract class PaymentMethod {
    abstract boolean processPayment(double amount, String currency);
    abstract String getTransactionId();
    abstract boolean refund(String transactionId, double amount);
}
class CreditCard extends PaymentMethod { ... }
class PayPal extends PaymentMethod { ... }
class BankTransfer extends PaymentMethod { ... }
```
**Issues:**
- Requires Orders, Checkout, Transaction entities
- Needs payment gateway integrations
- Security concerns (PCI compliance)
- Complex state management across payment flow

#### 3. Review Hierarchy
```java
// PROBLEMATIC: Not good inheritance candidate
abstract class Review {
    String content;
    int rating;
    Date createdAt;
}
class ProductReview extends Review { ... }
class SellerReview extends Review { ... }
class ServiceReview extends Review { ... }
```
**Issues:**
- Very similar behavior across subclasses
- Better suited for composition (Review + Reviewable interface)
- Rating logic is identical across types
- Content validation is the same

### ✅ Why Product Subtypes Are Perfect

#### 4. Product Subtypes (Physical/Digital) - IMPLEMENTED ✅
```java
// EXCELLENT: Clear domain boundaries, distinct behaviors
public class PhysicalProduct extends Product {
    private double weightKg;
    private double lengthCm, widthCm, heightCm;
    
    public double estimateShippingCost() {
        // Physical-specific: volumetric calculation
        double volumetric = (lengthCm * widthCm * heightCm) / 5000.0;
        return Math.max(weightKg, volumetric) * 100;
    }
}

public class DigitalProduct extends Product {
    private double downloadSizeMb;
    private String licenseKey;
    
    public boolean isLicenseRequired() {
        // Digital-specific: license validation
        return licenseKey != null && !licenseKey.isBlank();
    }
}
```

**Why This Works Perfectly:**
- ✅ **Clear domain boundaries** — each type has distinct attributes
- ✅ **Different business logic** — shipping vs licensing
- ✅ **Self-contained** — no external dependencies
- ✅ **Easy to test** — simple validation rules
- ✅ **Extensible** — easy to add new product types
- ✅ **Real-world relevance** — matches actual e-commerce needs

---

## 📋 Implementation Example (Already Done)

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