import lockersystem.enums.PackageSize;
import lockersystem.models.*;
import lockersystem.errors.LockerSystemException;
import lockersystem.LockerSystem;

public class Main {
    public static void main(String[] args) {
        System.out.println("===== Locker System Demo =====\n");
        
        // Get singleton instance of LockerSystem
        LockerSystem lockerSystem = LockerSystem.getInstance();
        
        // Initialize compartments with different sizes
        System.out.println("1. Initializing Locker System with compartments...");
        lockerSystem.addCompartment(new Compartment(PackageSize.SMALL));
        lockerSystem.addCompartment(new Compartment(PackageSize.SMALL));
        lockerSystem.addCompartment(new Compartment(PackageSize.MEDIUM));
        lockerSystem.addCompartment(new Compartment(PackageSize.MEDIUM));
        lockerSystem.addCompartment(new Compartment(PackageSize.LARGE));
        System.out.println("   Added 5 compartments (2 Small, 2 Medium, 1 Large)\n");
        
        // Create assignment strategy
        AssignStrategy strategy = new DefaultSizeBasedStrategy();
        
        // Test Case 1: Successful package drop-off
        try {
            System.out.println("2. Test Case 1: Dropping off packages");
            
            // Create packages
            lockersystem.models.Package smallPackage = new lockersystem.models.Package(
                PackageSize.SMALL, 
                "Books and documents"
            );
            lockersystem.models.Package mediumPackage = new lockersystem.models.Package(
                PackageSize.MEDIUM, 
                "Laptop and accessories"
            );
            lockersystem.models.Package largePackage = new lockersystem.models.Package(
                PackageSize.LARGE, 
                "Winter clothing"
            );
            
            // Find compartments and assign packages
            Compartment comp1 = lockerSystem.findCompartment(smallPackage, strategy);
            String token1 = lockerSystem.assignCompartment(comp1, smallPackage);
            System.out.println("   Small package assigned. Token: " + token1.substring(0, 20) + "...");
            
            Compartment comp2 = lockerSystem.findCompartment(mediumPackage, strategy);
            String token2 = lockerSystem.assignCompartment(comp2, mediumPackage);
            System.out.println("   Medium package assigned. Token: " + token2.substring(0, 20) + "...");
            
            Compartment comp3 = lockerSystem.findCompartment(largePackage, strategy);
            String token3 = lockerSystem.assignCompartment(comp3, largePackage);
            System.out.println("   Large package assigned. Token: " + token3.substring(0, 20) + "...\n");
            
            // Test Case 2: Successful package pickup
            System.out.println("3. Test Case 2: Picking up a package");
            lockersystem.models.Package retrievedPackage = lockerSystem.pickPackage(token1);
            System.out.println("   Package retrieved: " + retrievedPackage.getData());
            System.out.println("   Package ID: " + retrievedPackage.getId().substring(0, 25) + "...\n");
            
            // Test Case 3: Attempt to use already used token
            System.out.println("4. Test Case 3: Trying to use an already used token");
            try {
                lockerSystem.pickPackage(token1);
                System.out.println("   ERROR: Should have thrown exception!");
            } catch (LockerSystemException e) {
                System.out.println("   Expected error: " + e.getMessage() + "\n");
            }
            
            // Test Case 4: Attempt to use invalid token
            System.out.println("5. Test Case 4: Trying to use an invalid token");
            try {
                lockerSystem.pickPackage("invalid-token-code");
                System.out.println("   ERROR: Should have thrown exception!");
            } catch (LockerSystemException e) {
                System.out.println("   Expected error: " + e.getMessage() + "\n");
            }
            
            // Test Case 5: Fill up all compartments
            System.out.println("6. Test Case 5: Filling remaining compartments");
            lockersystem.models.Package extraPackage1 = new lockersystem.models.Package(
                PackageSize.SMALL, 
                "Phone and charger"
            );
            Compartment comp4 = lockerSystem.findCompartment(extraPackage1, strategy);
            String token4 = lockerSystem.assignCompartment(comp4, extraPackage1);
            System.out.println("   Another small package assigned. Token: " + token4.substring(0, 20) + "...");
            
            lockersystem.models.Package extraPackage2 = new lockersystem.models.Package(
                PackageSize.MEDIUM, 
                "Sports equipment"
            );
            Compartment comp5 = lockerSystem.findCompartment(extraPackage2, strategy);
            String token5 = lockerSystem.assignCompartment(comp5, extraPackage2);
            System.out.println("   Another medium package assigned. Token: " + token5.substring(0, 20) + "...");
            
            lockersystem.models.Package extraPackage3 = new lockersystem.models.Package(
                PackageSize.SMALL, 
                "Headphones"
            );
            Compartment comp6 = lockerSystem.findCompartment(extraPackage3, strategy);
            String token6 = lockerSystem.assignCompartment(comp6, extraPackage3);
            System.out.println("   Last small package assigned. Token: " + token6.substring(0, 20) + "...");
            System.out.println("   All compartments are now full!\n");
            
            // Test Case 6: Try to assign when all compartments are full
            System.out.println("7. Test Case 6: Trying to assign when locker is full");
            try {
                lockersystem.models.Package fullPackage = new lockersystem.models.Package(
                    PackageSize.SMALL, 
                    "Extra item"
                );
                lockerSystem.findCompartment(fullPackage, strategy);
                System.out.println("   ERROR: Should have thrown exception!");
            } catch (LockerSystemException e) {
                System.out.println("   Expected error: " + e.getMessage() + "\n");
            }
            
            // Test Case 7: Pick up remaining packages
            System.out.println("8. Test Case 7: Picking up remaining packages");
            lockersystem.models.Package pkg2 = lockerSystem.pickPackage(token2);
            System.out.println("   Retrieved: " + pkg2.getData());
            
            lockersystem.models.Package pkg3 = lockerSystem.pickPackage(token3);
            System.out.println("   Retrieved: " + pkg3.getData());
            
            lockersystem.models.Package pkg4 = lockerSystem.pickPackage(token4);
            System.out.println("   Retrieved: " + pkg4.getData());
            
            lockersystem.models.Package pkg5 = lockerSystem.pickPackage(token5);
            System.out.println("   Retrieved: " + pkg5.getData());
            
            lockersystem.models.Package pkg6 = lockerSystem.pickPackage(token6);
            System.out.println("   Retrieved: " + pkg6.getData() + "\n");
            
            System.out.println("===== All tests completed successfully! =====");
            
        } catch (LockerSystemException e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
