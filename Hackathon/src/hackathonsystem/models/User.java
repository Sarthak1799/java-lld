package hackathonsystem.models;
import hackathonsystem.enums.*;
import java.util.UUID;

public class User{
    private String id;
    private Department department;
    private String name;

    public User(Department department, String name){
        this.name = name;
        this.department = department;
        this.id = "User-" + UUID.randomUUID().toString();
    }

    public String getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public Department getDepartment(){
        return department;
    }
}