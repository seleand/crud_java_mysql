package net.usermanager.dao;

import net.usermanager.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by x555l on 15.08.2016.
 */
@Repository
public class UserDaoImpl implements UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
        LOGGER.info("User successfully added. User details: "+user);
    }

    @Override
    public void updateUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(user);
        LOGGER.info("User successfully updated. User details: "+user);

    }

    @Override
    public void removeUser(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class, new Integer(id));
        if (user!=null){
            session.delete(user);
        }
        LOGGER.info("User successfully deleted. User details: "+user);
    }

    @Override
    public User getUserById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class, new Integer(id));
        LOGGER.info("User successfully loaded. User details: "+user);

        return user;
    }

    @Override
    public List<User> findUsersByName(String name) {
        String query = String.format("from User where name = '%s'", name);

        Session session = this.sessionFactory.getCurrentSession();
        List<User> userList = session.createQuery(query).list();
        for (User user : userList) {
            LOGGER.info("User list by name: "+user);
        }
        return userList;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        Session session = this.sessionFactory.getCurrentSession();
        List<User> userList = session.createQuery("from User").list();
        for (User user : userList) {
            LOGGER.info("User list: "+user);
        }
        return userList;
    }
}
