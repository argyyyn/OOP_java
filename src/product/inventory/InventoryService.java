package product.inventory;

import product.Product;

public class InventoryService {
    /**
     * Increase quantity by amount (>0). Return true if applied.
     */
    public boolean addStock(Product p, int amount) {
        if (p == null || amount <= 0) return false;
        long next = (long) p.getQuantity() + amount;
        if (next > 1_000_000L) return false;
        return p.trySetQuantity((int) next);
    }

    /**
     * Sell (reduce) quantity by amount (>0 and <= current). Return true if applied.
     */
    public boolean sell(Product p, int amount) {
        if (p == null || amount <= 0 || amount > p.getQuantity()) return false;
        return p.trySetQuantity(p.getQuantity() - amount);
    }
}

