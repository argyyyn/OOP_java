package product;

import category.Category;

public class ShopDemo {
    public static void main(String[] args) {
        System.out.println("=== SHOP DEMO - CONSTRUCTORS & STATIC FACTORY ===\n");
        
        // Create category for testing
        Category electronics = new Category("C001", "Electronics", "Electronic devices and accessories");
        
        // Test all constructors and static factories
        System.out.println("=== TESTING CONSTRUCTORS AND STATIC FACTORIES ===\n");
        
        // 1. No-args constructor
        Product p1 = new Product(); // no-args
        System.out.println("1. No-args constructor:");
        System.out.println(p1);
        
        // 2. Required-args constructor
        Product p2 = new Product("P100", "Notebook", 950.0); // required-args
        System.out.println("\n2. Required-args constructor:");
        System.out.println(p2);
        
        // 3. Full-args constructor
        Product p3 = new Product("P200", "Headphones", "BT 5.0", 14990.0, 5, electronics); // full-args
        System.out.println("\n3. Full-args constructor:");
        System.out.println(p3);
        
        // 4. Static factory - of()
        Product p4 = Product.of("P300", "Pencil", 120.0); // static factory
        System.out.println("\n4. Static factory - of():");
        System.out.println(p4);
        
        // 5. Static factory - freeSample()
        Product p5 = Product.freeSample("Sticker"); // static factory
        System.out.println("\n5. Static factory - freeSample():");
        System.out.println(p5);
        
        // Test invalid constructor arguments to show guards work
        System.out.println("\n=== TESTING GUARDS WITH INVALID ARGUMENTS ===");
        Product p6 = new Product("", "A", -5.0); // invalid id, name, price
        System.out.println("Invalid constructor args (empty id, short name, negative price):");
        System.out.println(p6);
        
        // Show auto-generated IDs
        System.out.println("\n=== AUTO-GENERATED IDs ===");
        System.out.println("p1 id: " + p1.getId());
        System.out.println("p5 id: " + p5.getId());
        
        // Show created count
        System.out.println("\n=== CREATED COUNT ===");
        System.out.println("Created count = " + Product.getCreatedCount());
        
        // Show default currency
        System.out.println("\n=== STATIC MEMBERS ===");
        System.out.println("Default currency: " + Product.DEFAULT_CURRENCY);
        
        // Final summary
        System.out.println("\n=== FINAL SUMMARY ===");
        System.out.println("All products created:");
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println(p4);
        System.out.println(p5);
        System.out.println(p6);
        System.out.println("\nTotal products created: " + Product.getCreatedCount());
    }
}