
import enums.*;
import java.time.LocalDate;
import java.util.List;
import models.*;
import service.CarService;

public class Main {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   CAR RENTAL SYSTEM - LIVE DEMO");
        System.out.println("========================================\n");

        // Get the service instance (Singleton)
        System.out.println("Getting Service Instance (Singleton Pattern)...");
        CarService service = CarService.getInstance();
        System.out.println("✓ Service initialized with sample data\n");

        // 1. Register Users
        System.out.println("1. REGISTERING USERS");
        System.out.println("-------------------");
        service.registerUser("John Doe", "john@email.com", "123-456-7890");
        service.registerUser("Jane Smith", "jane@email.com", "098-765-4321");
        service.registerUser("Alice Johnson", "alice@email.com", "555-1234");
        System.out.println("✓ Registered 3 users\n");

        // 2. List all available cars
        System.out.println("2. LISTING ALL AVAILABLE CARS");
        System.out.println("-----------------------------");
        List<Car> allCars = service.listCars();
        for (Car car : allCars) {
            System.out.println(String.format("   • %s [%s] - $%.2f/day - Status: %s",
                    car.getModel(),
                    car.getCarType(),
                    car.getRentPerDay(),
                    car.getReservationStatus()));
        }
        System.out.println();

        // 3. Search for specific car types
        System.out.println("3. SEARCHING FOR SUV CARS");
        System.out.println("------------------------");
        SearchStrategy suvSearch = new CarTypeSearchStrategy(CarType.SUV);
        List<Car> suvCars = service.searchCars(suvSearch);
        System.out.println("Found " + suvCars.size() + " SUV(s):");
        for (Car car : suvCars) {
            System.out.println(String.format("   • %s - $%.2f/day", car.getModel(), car.getRentPerDay()));
        }
        System.out.println();

        // 4. Search for SEDAN cars
        System.out.println("4. SEARCHING FOR SEDAN CARS");
        System.out.println("---------------------------");
        SearchStrategy sedanSearch = new CarTypeSearchStrategy(CarType.SEDAN);
        List<Car> sedanCars = service.searchCars(sedanSearch);
        System.out.println("Found " + sedanCars.size() + " Sedan(s):");
        for (Car car : sedanCars) {
            System.out.println(String.format("   • %s - $%.2f/day", car.getModel(), car.getRentPerDay()));
        }
        System.out.println();

        // 5. Make a reservation
        System.out.println("5. CREATING A RESERVATION");
        System.out.println("------------------------");

        // Get a car and user for reservation
        Car selectedCar = allCars.get(0);
        String carId = selectedCar.getId();

        // Register a new user and get their ID
        service.registerUser("Bob Williams", "bob@email.com", "777-8888");

        // Get the registered user - we need to add a method to get all users
        // For now, we'll use the returned user from registration
        User registeredUser = new User("Bob Williams", "bob@email.com", "777-8888");
        // This won't work - we need the actual registered user
        // Let's just register and use a workaround

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5);

        System.out.println("   Car: " + selectedCar.getModel());
        System.out.println("   User: Bob Williams (newly registered)");
        System.out.println("   Period: " + startDate + " to " + endDate + " (5 days)");
        System.out.println("   Payment Mode: CREDIT_CARD");

        // We need to get the user ID from the service
        // Adding this feature to CarService temporarily
        List<User> allUsers = service.getAllUsers();
        User bobUser = allUsers.get(allUsers.size() - 1); // Get the last registered user

        String reservationId = service.reserveCar(carId, bobUser.getId(), startDate, endDate, PaymentMode.CREDIT_CARD);
        System.out.println("✓ Reservation created: " + reservationId);
        System.out.println();

        // 6. Check car status after reservation
        System.out.println("6. CHECKING CAR STATUS AFTER RESERVATION");
        System.out.println("---------------------------------------");
        System.out.println("   " + selectedCar.getModel() + " - Status: " + selectedCar.getReservationStatus());
        System.out.println();

        // 7. Complete the reservation with payment
        System.out.println("7. COMPLETING RESERVATION WITH PAYMENT");
        System.out.println("-------------------------------------");
        PaymentProcessor creditCard = new CreditCard(
                "1234-5678-9012-3456",
                "Bob Williams",
                "12/26",
                "123"
        );

        LocalDate returnDate = LocalDate.now().plusDays(3); // Returned after 3 days
        System.out.println("   Return date: " + returnDate + " (3 days used)");
        System.out.println("   Rate: $" + selectedCar.getRentPerDay() + "/day");
        System.out.println("   Total: $" + (3 * selectedCar.getRentPerDay()));

        service.completeReservation(reservationId, creditCard, returnDate);
        System.out.println("✓ Reservation completed and payment processed");
        System.out.println("✓ Car status: " + selectedCar.getReservationStatus());
        System.out.println();

        // 8. Make another reservation and cancel it
        System.out.println("8. TESTING RESERVATION CANCELLATION");
        System.out.println("----------------------------------");
        Car anotherCar = allCars.get(1);
        String reservationId2 = service.reserveCar(
                anotherCar.getId(),
                bobUser.getId(),
                LocalDate.now(),
                LocalDate.now().plusDays(2),
                PaymentMode.CASH
        );
        System.out.println("   Created reservation: " + reservationId2);
        System.out.println("   Car: " + anotherCar.getModel() + " - Status: " + anotherCar.getReservationStatus());

        service.cancelReservation(reservationId2);
        System.out.println("✓ Reservation cancelled");
        System.out.println("   Car: " + anotherCar.getModel() + " - Status: " + anotherCar.getReservationStatus());
        System.out.println();

        // 9. Final summary
        System.out.println("========================================");
        System.out.println("   DEMO COMPLETE!");
        System.out.println("========================================");
        System.out.println("Successfully demonstrated:");
        System.out.println("  ✓ User registration");
        System.out.println("  ✓ Car listing");
        System.out.println("  ✓ Search by car type (Strategy Pattern)");
        System.out.println("  ✓ Reservation creation");
        System.out.println("  ✓ Payment processing");
        System.out.println("  ✓ Reservation completion");
        System.out.println("  ✓ Reservation cancellation");
        System.out.println("========================================\n");
    }
}
