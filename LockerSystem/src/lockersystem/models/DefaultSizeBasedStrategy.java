package lockersystem.models;
import java.util.*;

public class DefaultSizeBasedStrategy implements AssignStrategy {
    @Override
    public Compartment getFreeCompartment(List<Compartment> compartments, lockersystem.models.Package pack) {
        
        for(Compartment compartment : compartments){
            if(!compartment.isOccupied() && compartment.getSize().ordinal() >= pack.getSize().ordinal()) 
                return compartment;
        }

        return null;
    }
}
