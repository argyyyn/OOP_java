package category;

import java.util.ArrayList;
import java.util.List;
import product.Product;

public class Category {
    private String categoryId;
    private String name;
    private String description;
    private List<Product> products;
    
    public Category() {
        this.categoryId = "C000";
        this.name = "Unknown";
        this.description = "No description";
        this.products = new ArrayList<>();
    }
    
    public Category(String categoryId, String name, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.products = new ArrayList<>();
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public List<Product> getProducts() {
        return products;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void addProduct(Product product) {
        if (product != null) {
            product.setCategory(this.name);
            this.products.add(product);
        }
    }
    
    public void removeProduct(Product product) {
        this.products.remove(product);
    }
    
    public int getProductCount() {
        return products.size();
    }
    
    public double getTotalValue() {
        double total = 0;
        for (Product product : products) {
            total += product.calculateTotalValue();
        }
        return total;
    }
    
    public void displayCategoryInfo() {
        System.out.println("=== CATEGORY: " + name.toUpperCase() + " ===");
        System.out.println("ID: " + categoryId);
        System.out.println("Description: " + description);
        System.out.println("Products: " + getProductCount());
        System.out.println("Total Value: $" + String.format("%.2f", getTotalValue()));
        System.out.println();
        
        for (Product product : products) {
            System.out.println("- " + product.getName() + " (ID: " + product.getProductId() + ") - $" + String.format("%.2f", product.getPrice()));
        }
        System.out.println("=====================================");
    }
}
