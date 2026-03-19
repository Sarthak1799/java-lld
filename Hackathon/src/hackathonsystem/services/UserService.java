package hackathonsystem.services;
import hackathonsystem.models.*;
import hackathonsystem.enums.*;

import java.util.*;

public class UserService {
    private Map<String, User> users;
    private Map<String, List<Problem>> problemList;
    private static UserService instance = null;

    public UserService(){
        this.users = new HashMap<>();
        this.problemList = new HashMap<>();
    }

    public static UserService getInstance(){
        if(instance == null)
            instance = new UserService();
        return instance;
    }

    public void registerUser(String name, Department department){
        User user = new User(department, name);
        this.users.put(user.getId(), user);
    }
    
    public String registerUserAndGetId(String name, Department department){
        User user = new User(department, name);
        this.users.put(user.getId(), user);
        return user.getId();
    }

    public void registerProblem(String userId, Problem problem){
        this.problemList.putIfAbsent(userId, new ArrayList<>());
        this.problemList.get(userId).add(problem);
    }

    public boolean doesProblemExist(String userId, Problem prob){
        this.problemList.putIfAbsent(userId, new ArrayList<>());
        return this.problemList.get(userId).contains(prob);
    }

    public User getUser(String userId){
        return users.get(userId);
    }

    public List<Problem> getProblems(String userId){
        return this.problemList.get(userId);
    }
}
