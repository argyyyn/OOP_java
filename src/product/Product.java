package product;

import category.Category;

public class Product {
    // Product attributes
    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private Category category;
    
    // Default constructor
    public Product() {
        this.id = "P000";
        this.name = "Unknown Product";
        this.description = "No description available";
        this.price = 0.0;
        this.quantity = 0;
        this.category = null;
    }
    
    // Parameterized constructor
    public Product(String id, String name, String description, double price, int quantity, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
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
    
    @Override
    public String toString() {
        return "Product{id='%s', name='%s', price=%.2f, qty=%d, status=%s, category=%s}"
            .formatted(id, name, price, quantity, getStockStatus(),
                       category == null ? "NONE" : category.getName());
    }
}
