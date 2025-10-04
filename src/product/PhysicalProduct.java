package product;

public class PhysicalProduct extends Product {
    // Private fields
    private double weightKg;
    private double lengthCm;
    private double widthCm;
    private double heightCm;
    
    // No-args constructor - safe defaults via super()
    public PhysicalProduct() {
        super();
        this.weightKg = 0.0;
        this.lengthCm = 0.0;
        this.widthCm = 0.0;
        this.heightCm = 0.0;
    }
    
    // Constructor with id, name, price, weight
    public PhysicalProduct(String id, String name, double price, double weightKg) {
        super(id, name, price);
        this.weightKg = 0.0;
        this.lengthCm = 0.0;
        this.widthCm = 0.0;
        this.heightCm = 0.0;
        trySetWeightKg(weightKg);
    }
    
    // Full constructor with all parameters
    public PhysicalProduct(String id, String name, String description, double price, 
                          int quantity, double weightKg, double lengthCm, double widthCm, double heightCm) {
        super(id, name, description, price, quantity, null);
        this.weightKg = 0.0;
        this.lengthCm = 0.0;
        this.widthCm = 0.0;
        this.heightCm = 0.0;
        trySetWeightKg(weightKg);
        trySetDimensions(lengthCm, widthCm, heightCm);
    }
    
    // Getter methods
    public double getWeightKg() {
        return weightKg;
    }
    
    public double getLengthCm() {
        return lengthCm;
    }
    
    public double getWidthCm() {
        return widthCm;
    }
    
    public double getHeightCm() {
        return heightCm;
    }
    
    // Guarded mutators
    public boolean trySetWeightKg(double weight) {
        if (weight >= 0.0 && weight <= 1000.0) {
            this.weightKg = weight;
            return true;
        }
        return false;
    }
    
    public boolean trySetDimensions(double length, double width, double height) {
        if (length >= 0.0 && length <= 1000.0 &&
            width >= 0.0 && width <= 1000.0 &&
            height >= 0.0 && height <= 1000.0) {
            this.lengthCm = length;
            this.widthCm = width;
            this.heightCm = height;
            return true;
        }
        return false;
    }
    
    // Business method for shipping cost calculation
    public double estimateShippingCost() {
        double volumetric = (lengthCm * widthCm * heightCm) / 5000.0;
        double billable = Math.max(weightKg, volumetric);
        return billable * 100; // Cost in KZT
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(" [Physical: weight=%.2fkg, dims=%.1fx%.1fx%.1fcm, shipping=%.2fKZT]", 
                                               weightKg, lengthCm, widthCm, heightCm, estimateShippingCost());
    }
}
