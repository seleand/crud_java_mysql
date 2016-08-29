package net.usermanager.service;

import net.usermanager.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by x555l on 15.08.2016.
 */
public interface UserService {
    public void addUser(User user);

    public void updateUser(User user);

    public void removeUser(int id);

    public User getUserById(int id);

    public List<User> listUsers();

    public List<User> findUsersByName(String name);

}
