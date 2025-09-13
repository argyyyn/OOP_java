package product;

public class Product {
    // Product attributes
    private String productId;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String category;
    private boolean inStock;
    
    // Default constructor
    public Product() {
        this.productId = "P000";
        this.name = "Unknown Product";
        this.description = "No description available";
        this.price = 0.0;
        this.quantity = 0;
        this.category = "General";
        this.inStock = false;
    }
    
    // Parameterized constructor
    public Product(String productId, String name, String description, double price, int quantity, String category) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.inStock = quantity > 0;
    }
    
    // Getter methods
    public String getProductId() {
        return productId;
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
    
    public String getCategory() {
        return category;
    }
    
    public boolean isInStock() {
        return inStock;
    }
    
    // Setter methods
    public void setProductId(String productId) {
        this.productId = productId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setPrice(double price) {
        if (price >= 0) {
            this.price = price;
        } else {
            System.out.println("Price cannot be negative!");
        }
    }
    
    public void setQuantity(int quantity) {
        if (quantity >= 0) {
            this.quantity = quantity;
            this.inStock = quantity > 0;
        } else {
            System.out.println("Quantity cannot be negative!");
        }
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    // Business methods
    public void addStock(int amount) {
        if (amount > 0) {
            this.quantity += amount;
            this.inStock = true;
            System.out.println("Added " + amount + " units to stock. New quantity: " + this.quantity);
        } else {
            System.out.println("Cannot add negative or zero stock!");
        }
    }
    
    public boolean sellProduct(int amount) {
        if (amount > 0 && amount <= this.quantity) {
            this.quantity -= amount;
            this.inStock = this.quantity > 0;
            System.out.println("Sold " + amount + " units. Remaining quantity: " + this.quantity);
            return true;
        } else {
            System.out.println("Cannot sell " + amount + " units. Available: " + this.quantity);
            return false;
        }
    }
    
    public double calculateTotalValue() {
        return this.price * this.quantity;
    }
    
    public void applyDiscount(double discountPercent) {
        if (discountPercent > 0 && discountPercent <= 100) {
            this.price = this.price * (1 - discountPercent / 100);
            System.out.println("Applied " + discountPercent + "% discount. New price: $" + String.format("%.2f", this.price));
        } else {
            System.out.println("Invalid discount percentage!");
        }
    }
    
    public void displayProductInfo() {
        System.out.println("=== PRODUCT INFORMATION ===");
        System.out.println("Product ID: " + productId);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Price: $" + String.format("%.2f", price));
        System.out.println("Quantity: " + quantity);
        System.out.println("Category: " + category);
        System.out.println("In Stock: " + (inStock ? "Yes" : "No"));
        System.out.println("Total Value: $" + String.format("%.2f", calculateTotalValue()));
        System.out.println("==========================");
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", price=" + String.format("%.2f", price) +
                ", quantity=" + quantity +
                ", category='" + category + '\'' +
                ", inStock=" + inStock +
                '}';
    }
}
