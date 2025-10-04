package product;

import category.Category;

public class ShopDemo4 {
    public static void main(String[] args) {
        System.out.println("=== Shop Demo 4: Inheritance Demo ===\n");
        
        // Create categories
        Category electronics = new Category("ELEC", "Electronics", "Electronic devices and gadgets");
        Category software = new Category("SOFT", "Software", "Software applications and programs");
        
        System.out.println("1. Creating Physical Products with different constructors:");
        System.out.println("--------------------------------------------------------");
        
        // PhysicalProduct - no-args constructor
        PhysicalProduct laptop1 = new PhysicalProduct();
        System.out.println("Laptop1 (no-args): " + laptop1);
        
        // PhysicalProduct - id, name, price, weight constructor
        PhysicalProduct laptop2 = new PhysicalProduct("LAPTOP-001", "Gaming Laptop", 150000.0, 2.5);
        System.out.println("Laptop2 (4-args): " + laptop2);
        
        // PhysicalProduct - full constructor
        PhysicalProduct laptop3 = new PhysicalProduct("LAPTOP-002", "Business Laptop", 
            "High-performance laptop for business use", 120000.0, 5, 2.0, 35.0, 25.0, 2.5);
        System.out.println("Laptop3 (full): " + laptop3);
        
        System.out.println("\n2. Creating Digital Products with different constructors:");
        System.out.println("--------------------------------------------------------");
        
        // DigitalProduct - no-args constructor
        DigitalProduct software1 = new DigitalProduct();
        System.out.println("Software1 (no-args): " + software1);
        
        // DigitalProduct - id, name, price, download size constructor
        DigitalProduct software2 = new DigitalProduct("SOFT-001", "Photo Editor", 5000.0, 250.0);
        System.out.println("Software2 (4-args): " + software2);
        
        // DigitalProduct - full constructor
        DigitalProduct software3 = new DigitalProduct("SOFT-002", "Video Editor Pro", 
            "Professional video editing software", 15000.0, 10, 1024.0, "LICENSE-ABC123");
        System.out.println("Software3 (full): " + software3);
        
        System.out.println("\n3. Testing valid and invalid updates:");
        System.out.println("------------------------------------");
        
        // Valid updates
        System.out.println("Valid updates:");
        boolean result1 = laptop2.trySetDimensions(40.0, 30.0, 3.0);
        System.out.println("laptop2.trySetDimensions(40, 30, 3): " + result1);
        
        boolean result2 = software2.trySetDownloadSizeMb(500.0);
        System.out.println("software2.trySetDownloadSizeMb(500): " + result2);
        
        boolean result3 = software2.trySetLicenseKey("NEW-LICENSE-456");
        System.out.println("software2.trySetLicenseKey('NEW-LICENSE-456'): " + result3);
        
        // Invalid updates
        System.out.println("\nInvalid updates:");
        boolean result4 = laptop2.trySetDimensions(-1, 10, 10);
        System.out.println("laptop2.trySetDimensions(-1, 10, 10): " + result4 + " (negative length)");
        
        boolean result5 = laptop2.trySetWeightKg(1500.0);
        System.out.println("laptop2.trySetWeightKg(1500): " + result5 + " (exceeds 1000kg limit)");
        
        boolean result6 = software2.trySetDownloadSizeMb(2000000.0);
        System.out.println("software2.trySetDownloadSizeMb(2000000): " + result6 + " (exceeds 1M limit)");
        
        boolean result7 = software2.trySetLicenseKey("A".repeat(100));
        System.out.println("software2.trySetLicenseKey(100-char string): " + result7 + " (exceeds 64 char limit)");
        
        System.out.println("\n4. Testing subclass-specific methods:");
        System.out.println("------------------------------------");
        
        // Physical product methods
        System.out.println("Physical product methods:");
        System.out.println("laptop2.estimateShippingCost(): " + laptop2.estimateShippingCost() + " KZT");
        System.out.println("laptop3.estimateShippingCost(): " + laptop3.estimateShippingCost() + " KZT");
        
        // Digital product methods
        System.out.println("\nDigital product methods:");
        System.out.println("software2.isLicenseRequired(): " + software2.isLicenseRequired());
        System.out.println("software3.isLicenseRequired(): " + software3.isLicenseRequired());
        
        System.out.println("\n5. Updated product information:");
        System.out.println("-------------------------------");
        System.out.println("Updated laptop2: " + laptop2);
        System.out.println("Updated software2: " + software2);
        
        System.out.println("\n6. Testing inheritance - calling parent methods:");
        System.out.println("------------------------------------------------");
        System.out.println("laptop2.getStockStatus(): " + laptop2.getStockStatus());
        System.out.println("software3.calculateTotalValue(): " + software3.calculateTotalValue() + " KZT");
        
        // Test selling products
        boolean sold1 = laptop2.sellProduct(1);
        boolean sold2 = software3.sellProduct(2);
        System.out.println("laptop2.sellProduct(1): " + sold1);
        System.out.println("software3.sellProduct(2): " + sold2);
        
        System.out.println("\nAfter selling:");
        System.out.println("laptop2: " + laptop2);
        System.out.println("software3: " + software3);
        
        System.out.println("\n=== Demo Complete ===");
    }
}
