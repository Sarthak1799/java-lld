package lockersystem.models;
import java.util.*;

public interface AssignStrategy {
    public Compartment getFreeCompartment(List<Compartment> compartments, lockersystem.models.Package pack);
}
