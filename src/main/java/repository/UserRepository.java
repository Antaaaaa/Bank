package repository;

import entity.User;

import java.util.List;

public interface UserRepository {
    void insertUser(String name, String phone, String email);
    List<User> findAllUsers();
    User getUserByNumber(String phone);
}
