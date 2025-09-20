package product;

import category.Category;

public class ShopDemo {
    public static void main(String[] args) {
        System.out.println("=== SHOP DEMO - ENCAPSULATION & VALIDATION ===\n");
        
        // Create category and product
        Category stationery = new Category("C001", "Stationery", "Office supplies and writing materials");
        Product pen = new Product();
        
        // Set up product using guarded mutators
        System.out.println("Setting up product:");
        System.out.println("trySetId('P001'): " + pen.trySetId("P001") + " | id=" + pen.getId());
        System.out.println("trySetName('Pen'): " + pen.trySetName("Pen") + " | name=" + pen.getName());
        System.out.println("trySetDescription('Blue ink ballpoint pen'): " + pen.trySetDescription("Blue ink ballpoint pen") + " | desc=" + pen.getDescription());
        System.out.println("trySetPrice(200.0): " + pen.trySetPrice(200.0) + " | price=" + pen.getPrice());
        System.out.println("trySetQuantity(5): " + pen.trySetQuantity(5) + " | qty=" + pen.getQuantity());
        System.out.println("trySetCategory(stationery): " + pen.trySetCategory(stationery) + " | category=" + (pen.getCategory() != null ? pen.getCategory().getName() : "null"));
        
        System.out.println("\nInitial product state:");
        System.out.println(pen);
        System.out.println("Status: " + pen.getStockStatus());
        
        // Accepted operations
        System.out.println("\n=== ACCEPTED OPERATIONS ===");
        System.out.println("trySetPrice(250.0): " + pen.trySetPrice(250.0) + " | price=" + pen.getPrice());
        System.out.println("addStock(20): " + pen.addStock(20) + " | qty=" + pen.getQuantity());
        System.out.println("Status after restock: " + pen.getStockStatus());
        
        // Rejected operations
        System.out.println("\n=== REJECTED OPERATIONS ===");
        System.out.println("trySetPrice(-1.0): " + pen.trySetPrice(-1.0) + " | price=" + pen.getPrice());
        System.out.println("trySetName('A'): " + pen.trySetName("A") + " | name=" + pen.getName());
        System.out.println("trySetDescription('" + "x".repeat(60) + "'): " + pen.trySetDescription("x".repeat(201)) + " | desc length=" + (pen.getDescription() != null ? pen.getDescription().length() : "null"));
        System.out.println("sellProduct(10000): " + pen.sellProduct(10000) + " | qty=" + pen.getQuantity());
        System.out.println("applyDiscount(200): " + pen.applyDiscount(200) + " | price=" + pen.getPrice());
        System.out.println("addStock(-5): " + pen.addStock(-5) + " | qty=" + pen.getQuantity());
        
        // Valid operations
        System.out.println("\n=== VALID OPERATIONS ===");
        System.out.println("sellProduct(10): " + pen.sellProduct(10) + " | qty=" + pen.getQuantity());
        System.out.println("Status after sale: " + pen.getStockStatus());
        System.out.println("applyDiscount(15): " + pen.applyDiscount(15) + " | price=" + pen.getPrice());
        
        // Category operations
        System.out.println("\n=== CATEGORY OPERATIONS ===");
        System.out.println("Add to category #1: " + stationery.addProduct(pen));
        System.out.println("Add to category #2 (duplicate): " + stationery.addProduct(pen));
        System.out.println("Add null product: " + stationery.addProduct(null));
        
        // Final state
        System.out.println("\n=== FINAL STATE ===");
        System.out.println(pen);
        System.out.println("Total value: $" + String.format("%.2f", pen.calculateTotalValue()));
        System.out.println("Category product count: " + stationery.getProductCount());
    }
}