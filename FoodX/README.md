# FoodX - Food Delivery System Implementation

## Overview
A complete Low-Level Design (LLD) implementation of a food delivery system following SOLID principles and design patterns.

## Architecture

### Design Patterns Used
1. **Singleton Pattern** - All services (CustomerService, RestaurantService, OrderService, DeliveryService)
2. **Observer Pattern** - Order status notifications to Customer, Restaurant, and DeliveryAgent
3. **Strategy Pattern** - PaymentProcessor interface with CardPaymentProcessor implementation

## Entity Models

### Base Classes
- **Person** (abstract) - Base class with UUID generation (prefix: CUST, AGENT)
  - Customer (extends Person, implements OrderObserver)
  - DeliveryAgent (extends Person, implements OrderObserver)

### Core Entities
- **Restaurant** (implements OrderObserver)
  - UUID prefix: REST_
  - Has-One: Menu
  
- **Menu**
  - UUID prefix: MENU_
  - Has-Many: MenuItem
  
- **MenuItem**
  - UUID prefix: ITEM_
  
- **Cart**
  - UUID prefix: CART_
  - Has-Many: CartItem
  - Belongs-To: Customer (1:1)
  
- **Order**
  - UUID prefix: ORD_
  - Has-Many: OrderItem
  - Belongs-To: Customer, Restaurant, DeliveryAgent
  
### Enums
- **OrderStatus**: PENDING, CONFIRMED, PREPARING, READY_FOR_PICKUP, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
- **PaymentMethod**: CREDIT_CARD, DEBIT_CARD, DIGITAL_WALLET, UPI, CASH

## Services (Singleton)

### CustomerService
- Manages customer registration
- Cart operations (add/remove items, update quantity)
- Order history management

### RestaurantService (implements OrderObserver)
- Restaurant management
- Menu item CRUD operations
- Restaurant search and filtering
- Notifies restaurants of order status updates

### DeliveryService (implements OrderObserver)
- Delivery agent management
- Agent assignment logic (finds least busy agent)
- Order-agent mapping
- Notifies agents of order assignments

### OrderService
- Order creation and management
- Payment processing integration
- Observer pattern implementation (notifies all subscribed observers)
- Order status tracking and updates

## Key Features

### Thread Safety
- Regular HashMap with synchronized methods (not Collections.synchronizedMap)
- All service methods are synchronized
- Cart and Menu operations are synchronized

### Observer Pattern Implementation
- Customer, Restaurant, and DeliveryAgent implement OrderObserver
- Services (RestaurantService, DeliveryService) implement OrderObserver to delegate to entities
- OrderService notifies all observers on order status changes
- Real-time notifications for:
  - Customers: Order status updates
  - Restaurants: New orders and status changes
  - Delivery Agents: Order assignments

### UUID Generation
- All entities auto-generate UUIDs with prefixes
- No ID accepted in constructors
- Format: PREFIX_UUID

### Payment Processing
- PaymentProcessor interface accepts PaymentMethod
- Supports multiple payment methods
- Returns success/failure status

## Usage Example

```java
// Get singleton instance
FoodXSystem system = FoodXSystem.getInstance();

// Create restaurant and menu items
Restaurant restaurant = new Restaurant("Pizza Palace", "123 Main St", "555-1234");
MenuItem pizza = new MenuItem("Margherita Pizza", "Classic pizza", 12.99);
restaurant.getMenu().addItem(pizza);
system.getRestaurantService().addRestaurant(restaurant);

// Create customer and add items to cart
Customer customer = new Customer("John Doe", "john@example.com", "555-1111", "789 Elm St");
system.getCustomerService().registerCustomer(customer);
system.getCustomerService().addItemToCart(customer.getId(), pizza, 2);

// Place order (automatically assigns delivery agent)
Order order = system.placeOrder(
    customer.getId(), 
    restaurant.getId(), 
    PaymentMethod.CREDIT_CARD, 
    "789 Elm St"
);

// Update order status (triggers notifications)
system.updateOrderStatus(order.getId(), OrderStatus.DELIVERED);
```

## Concurrency Handling
- All HashMap operations wrapped in synchronized methods
- Service instances use singleton pattern with synchronized getInstance()
- Order counter is atomic to ensure unique order IDs
- Thread-safe collections avoided in favor of synchronized methods on regular HashMaps

## Concurrent Testing
The system includes comprehensive concurrent tests using ExecutorService and CountDownLatch:

### Test Scenarios
1. **Concurrent Order Placement** - 10 customers placing orders simultaneously
2. **Concurrent Order Status Updates** - Multiple orders being updated across different stages
3. **Concurrent Cart Operations** - 50 parallel operations (add, update, remove)

### Test Results
- All operations complete successfully without race conditions
- Data consistency maintained across concurrent operations
- Observer pattern correctly notifies all subscribers in multi-threaded environment
- No deadlocks or thread safety issues

### Running Tests
```bash
javac -d bin src/models/*.java src/services/*.java src/*.java
java -cp bin Main
```

The test will run:
1. **Normal Test (Sequential)** - Validates basic functionality
2. **Concurrent Test (Multi-threaded)** - Validates thread-safety with ExecutorService

### Verification
- All orders maintain unique UUIDs
- Order status transitions are atomic
- No data corruption in concurrent cart updates
- Payment processing handles concurrent requests
- Delivery agent assignment works correctly under load

## Scalability Considerations
- Singleton services can be converted to dependency injection for better testing
- Observer pattern allows easy addition of new notification channels
- Payment processor interface allows multiple payment gateway implementations
- Service layer separation enables horizontal scaling
