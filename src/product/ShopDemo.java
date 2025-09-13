package product;

public class ShopDemo {
    public static void main(String[] args) {
        System.out.println("=== SHOP PRODUCT DEMO ===\n");
        
        // Create products using different constructors
        Product product1 = new Product();
        Product product2 = new Product("P001", "Laptop", "High-performance gaming laptop", 1299.99, 5, "Electronics");
        Product product3 = new Product("P002", "Coffee Mug", "Ceramic coffee mug with logo", 12.50, 20, "Kitchen");
        
        // Display initial product information
        System.out.println("Initial Products:");
        product1.displayProductInfo();
        System.out.println();
        
        product2.displayProductInfo();
        System.out.println();
        
        product3.displayProductInfo();
        System.out.println();
        
        // Modify product1 using setters
        System.out.println("Modifying Product 1:");
        product1.setProductId("P003");
        product1.setName("Smartphone");
        product1.setDescription("Latest model smartphone");
        product1.setPrice(699.99);
        product1.setQuantity(10);
        product1.setCategory("Electronics");
        product1.displayProductInfo();
        System.out.println();
        
        // Demonstrate selling products
        System.out.println("Selling Products:");
        product2.sellProduct(2);
        product3.sellProduct(5);
        product1.sellProduct(15); // This should fail - not enough stock
        System.out.println();
        
        // Add stock
        System.out.println("Adding Stock:");
        product2.addStock(3);
        product3.addStock(10);
        System.out.println();
        
        // Apply discounts
        System.out.println("Applying Discounts:");
        product2.applyDiscount(10); // 10% discount
        product3.applyDiscount(15); // 15% discount
        System.out.println();
        
        // Display updated information
        System.out.println("Updated Product Information:");
        product2.displayProductInfo();
        System.out.println();
        
        product3.displayProductInfo();
        System.out.println();
        
        // Demonstrate toString method
        System.out.println("Using toString method:");
        System.out.println(product1.toString());
        System.out.println(product2.toString());
        System.out.println(product3.toString());
        System.out.println();
        
        // Calculate total inventory value
        System.out.println("=== INVENTORY SUMMARY ===");
        double totalValue = product1.calculateTotalValue() + 
                           product2.calculateTotalValue() + 
                           product3.calculateTotalValue();
        System.out.println("Total inventory value: $" + String.format("%.2f", totalValue));
        System.out.println("=========================");
    }
}
