package product.pricing;

import product.Product;

/**
 * Interface for price calculation policies.
 * Used by promotions and other pricing rules.
 */
public interface PricePolicy {
    /**
     * Calculate the total price for given product and quantity.
     * @param p the product
     * @param qty the quantity
     * @return the total price after applying this policy
     */
    double apply(Product p, int qty);
    
    /**
     * Get a human-readable name for this policy.
     * @return the policy name
     */
    String name();
    
    /**
     * Check if this policy is applicable to the given product.
     * @param p the product to check
     * @return true if applicable, false otherwise
     */
    boolean applicableTo(Product p);
}
