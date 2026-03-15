package lockersystem.models;

import java.util.UUID;

import lockersystem.enums.PackageSize;
import lockersystem.enums.TokenStatus;

public class Package {
    private String id;
    private PackageSize size;
    private String metaData;
    private AccessToken token;
    
    public Package(PackageSize size, String metaData){
        id = "package " + UUID.randomUUID().toString();
        this.size = size;
        this.metaData = metaData;
    }

    public String getTokenCode() {
        return this.token.getCode();
    }

    public void assignToken(AccessToken token){
        this.token = token;
    }

    public TokenStatus getTokenStatus(){
        return this.token.getStatus();
    }

    public String getId(){
        return this.id;
    }

    public String getData(){
        return this.metaData;
    }

    public PackageSize getSize(){
        return this.size;
    }
}
