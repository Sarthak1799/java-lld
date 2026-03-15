package lockersystem.models;

import java.util.UUID;

import lockersystem.enums.PackageSize;
import lockersystem.enums.TokenStatus;

public class Compartment {
    private String id;
    private PackageSize size;
    private Package pack;
    private boolean isOccupied;

    public Compartment(PackageSize size){
        this.id = "compartment " + UUID.randomUUID().toString();
        this.size = size;
        this.isOccupied = false;
    }

    public void assignPackage(Package pack){
        this.pack = pack;
    }

    public boolean isOccupied(){
        return this.isOccupied;
    }

    public void markOccupied(){
        this.isOccupied = true;
    }

    public void markFree(){
        this.isOccupied = false;
        this.pack = null;
    }

    public Package getPackage(){
        return this.pack;
    }

    public PackageSize getSize(){
        return this.size;
    }

}
