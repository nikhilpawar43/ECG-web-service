package easycommuteguide.model.dao;

import java.util.List;

import easycommuteguide.model.User;

public interface UserDao {

    User getUser( Integer id );

    List<User> getUsers();

}