package product;

import category.Category;

public class Product {
    // Static members
    public static final String DEFAULT_CURRENCY = "KZT";
    private static int SEQ = 1;
    private static int createdCount = 0;


    // Product attributes
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category;   
    
    // No-args constructor - sets safe defaults
    public Product() {
        this.id = "AUTO-" + nextSeq();
        this.name = "Unnamed";
        this.description = null;
        this.price = 0.0;
        this.quantity = 0;
        this.category = null;
        createdCount++;
    }
    
    // Required-args constructor - id, name, price
    public Product(String id, String name, double price) {
        this(); // chain to no-args constructor for defaults
        trySetId(id);
        trySetName(name);
        trySetPrice(price);
    }
    
    // Full-args constructor - id, name, description, price, quantity, category
    public Product(String id, String name, String description, double price, int quantity, Category category) {
        this(); // chain to no-args constructor for defaults
        trySetId(id);
        trySetName(name);
        trySetDescription(description);
        trySetPrice(price);
        trySetQuantity(quantity);
        trySetCategory(category);
    }
    
    // Static helper method
    private static String nextSeq() {
        return String.valueOf(SEQ++);
    }
    
    // Static factory method to get created count
    public static int getCreatedCount() {
        return createdCount;
    }
    
    // Static factory methods
    public static Product of(String id, String name, double price) {
        return new Product(id, name, price);
    }
    
    public static Product freeSample(String name) {
        Product product = new Product();
        product.trySetName(name);
        product.trySetPrice(0.0);
        product.trySetQuantity(1);
        return product;
    }
    
    // Getter methods
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public double getPrice() {
        return price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public Category getCategory() {
        return category;
    }
    
    // Guarded mutators
    public boolean trySetId(String id) {
        if (id != null && id.trim().length() >= 2) {
            this.id = id.trim();
            return true;
        }
        return false;
    }
    
    public boolean trySetName(String name) {
        if (name != null && name.trim().length() >= 2) {
            this.name = name.trim();
            return true;
        }
        return false;
    }
    
    public boolean trySetDescription(String description) {
        if (description == null || description.trim().length() <= 200) {
            this.description = description == null ? null : description.trim();
            return true;
        }
        return false;
    }
    
    public boolean trySetPrice(double price) {
        if (price >= 0.0 && price <= 1_000_000.0) {
            this.price = price;
            return true;
        }
        return false;
    }
    
    public boolean trySetQuantity(int quantity) {
        if (quantity >= 0 && quantity <= 1_000_000) {
            this.quantity = quantity;
            return true;
        }
        return false;
    }
    
    public boolean trySetCategory(Category category) {
        if (category != null) {
            this.category = category;
            return true;
        }
        return false;
    }
    
    // Inventory & business operations (guarded)
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
    
    public double calculateTotalValue() {
        return price * quantity;
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
    
    public String getStockStatus() {
        if (quantity == 0) return "OUT_OF_STOCK";
        if (quantity <= 10) return "LOW";
        return "IN_STOCK";
    }
    
    public void displayProductInfo() {
        System.out.println(toString());
    }
    
    // Method to calculate final price with promotion (for Task 6)
    public double finalPrice(int qty, product.pricing.PricePolicy policy) {
        if (policy == null) {
            return getPrice() * qty;
        }
        return policy.apply(this, qty);
    }
    
    @Override
    public String toString() {
        return "Product{id='%s', name='%s', price=%.2f, qty=%d, status=%s, category=%s}"
            .formatted(id, name, price, quantity, getStockStatus(),
                       category == null ? "NONE" : category.getName());
    }
}




