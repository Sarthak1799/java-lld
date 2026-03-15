package lockersystem;
import lockersystem.enums.*;
import lockersystem.models.*;
import lockersystem.errors.*;

import java.util.*;

public class LockerSystem {
    private List<Compartment> compartments;
    private Map<String, AccessToken> tokenMap;
    private static LockerSystem instance = null;


    public LockerSystem(){
        this.compartments = new ArrayList<>();
        this.tokenMap = new HashMap<>();
    }

    public void addCompartment(Compartment compartment){
        this.compartments.add(compartment);
    }

    public static LockerSystem getInstance(){
        if(instance == null)
            return instance = new LockerSystem();
        return instance;
    }

    public synchronized String assignCompartment(Compartment compartment, lockersystem.models.Package pack) throws LockerSystemException {
        if(compartment.isOccupied())
            throw new LockerSystemException("Compartment is already occupied");

        AccessToken token = new AccessToken(compartment);
        compartment.assignPackage(pack);
        compartment.markOccupied();
        pack.assignToken(token);

        this.tokenMap.put(token.getCode(), token);

        return token.getCode();
    }

    public synchronized lockersystem.models.Package pickPackage(String code) throws LockerSystemException {
        if(!tokenMap.containsKey(code))
            throw new LockerSystemException("Token does not exist");

        AccessToken token = tokenMap.get(code);
        if(token.isExpired())
            throw new LockerSystemException("Token is Expired");
        if(token.isUsed())
            throw new LockerSystemException("Token is already used");

        Compartment compartment = token.getCompartment();
        lockersystem.models.Package pack = compartment.getPackage();
        compartment.markFree();
        token.markUsed();

        return pack;
    }

    public Compartment findCompartment(lockersystem.models.Package pack, AssignStrategy strategy) throws LockerSystemException {
        Compartment compartment =  strategy.getFreeCompartment(this.compartments, pack);

        if(compartment == null)
            throw new LockerSystemException("No compartment is available");
        
        return compartment;
    }
}