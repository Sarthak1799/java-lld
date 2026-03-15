package lockersystem.models;
import java.time.LocalDateTime;
import java.util.UUID;
import lockersystem.enums.TokenStatus;

public class AccessToken {
    private String code;
    private TokenStatus status;
    private LocalDateTime expiry;
    private Compartment compartment;

    public AccessToken(Compartment compartment) {
        code = "token " + UUID.randomUUID().toString();
        status = TokenStatus.ACTIVE;
        expiry = LocalDateTime.now().plusDays(7); 
        this.compartment = compartment;
    }

    public String getCode(){
        return this.code;
    }

    public boolean isExpired(){
        if(!this.expiry.isBefore(LocalDateTime.now()))
            return false;

        this.status = TokenStatus.EXPIRED;
        return true;
    }

    public TokenStatus getStatus(){
        return this.status;
    }

    public void markUsed(){
       this.status = TokenStatus.USED;
    }

    public boolean isUsed() {
        return this.status.equals(TokenStatus.USED);
    }

    public Compartment getCompartment(){
        return this.compartment;
    }

}
