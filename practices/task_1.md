# Java OOP Practice - Shop Management System

## Task Description
Create a simple shop management system using Object-Oriented Programming concepts in Java. The system should include Product and Category classes that work together to manage inventory.

## Project Structure
```
src/
├── product/
│   ├── Product.java          # Product class with inventory management
│   └── ShopDemo.java         # Demo showing Product functionality
└── category/
    └── Category.java         # Category class for organizing products
```

## Classes Overview

### Product Class (`product.Product`)
**Purpose**: Represents individual products in the shop

**Key Features**:
- Product attributes: ID, name, description, price, quantity, category, stock status
- Constructors: Default and parameterized
- Inventory management: add stock, sell products
- Business operations: apply discounts, calculate total value
- Data validation for price and quantity

**Main Methods**:
- `addStock(int amount)` - Add inventory
- `sellProduct(int amount)` - Sell items with validation
- `applyDiscount(double percent)` - Apply percentage discount
- `calculateTotalValue()` - Calculate inventory value
- `displayProductInfo()` - Show detailed product information

### Category Class (`category.Category`)
**Purpose**: Organizes products into categories

**Key Features**:
- Category attributes: ID, name, description
- Product collection management
- Category-level calculations

**Main Methods**:
- `addProduct(Product product)` - Add product to category
- `removeProduct(Product product)` - Remove product from category
- `getTotalValue()` - Calculate total value of all products in category
- `displayCategoryInfo()` - Show category and its products

## How to Run

### Compile and Run
```bash
cd /Users/argo/OOP_java/src
javac category/*.java product/*.java
java product.ShopDemo
```
