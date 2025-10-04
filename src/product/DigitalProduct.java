package product;

public class DigitalProduct extends Product {
    // Private fields
    private double downloadSizeMb;
    private String licenseKey;
    
    // No-args constructor - safe defaults via super()
    public DigitalProduct() {
        super();
        this.downloadSizeMb = 0.0;
        this.licenseKey = null;
    }
    
    // Constructor with id, name, price, download size
    public DigitalProduct(String id, String name, double price, double downloadSizeMb) {
        super(id, name, price);
        this.downloadSizeMb = 0.0;
        this.licenseKey = null;
        trySetDownloadSizeMb(downloadSizeMb);
    }
    
    // Full constructor with all parameters
    public DigitalProduct(String id, String name, String description, double price, 
                         int quantity, double downloadSizeMb, String licenseKey) {
        super(id, name, description, price, quantity, null);
        this.downloadSizeMb = 0.0;
        this.licenseKey = null;
        trySetDownloadSizeMb(downloadSizeMb);
        trySetLicenseKey(licenseKey);
    }
    
    // Getter methods
    public double getDownloadSizeMb() {
        return downloadSizeMb;
    }
    
    public String getLicenseKey() {
        return licenseKey;
    }
    
    // Guarded mutators
    public boolean trySetDownloadSizeMb(double size) {
        if (size >= 0.0 && size <= 1_000_000.0) {
            this.downloadSizeMb = size;
            return true;
        }
        return false;
    }
    
    public boolean trySetLicenseKey(String key) {
        if (key == null || (key.length() <= 64)) {
            this.licenseKey = key;
            return true;
        }
        return false;
    }
    
    // Business method to check if license is required
    public boolean isLicenseRequired() {
        return licenseKey != null && !licenseKey.isBlank();
    }
    
    @Override
    public String toString() {
        String licenseInfo = isLicenseRequired() ? 
            String.format("license='%s'", licenseKey) : "no license";
        return super.toString() + String.format(" [Digital: size=%.2fMB, %s]", 
                                               downloadSizeMb, licenseInfo);
    }
}
