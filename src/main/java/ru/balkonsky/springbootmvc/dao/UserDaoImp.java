package ru.balkonsky.springbootmvc.dao;

import org.springframework.stereotype.Repository;
import ru.balkonsky.springbootmvc.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createNativeQuery("SELECT * from users", User.class);

        return query.getResultList();
    }

    public User showUserById(int id){
        return entityManager.find(User.class, id);
    }

    public void saveUser(User user) {
        entityManager.persist(user);
    }

    public void updateUser(User updatedUser, int id) {
        updatedUser.setId(id);
        entityManager.merge(updatedUser);
    }

    public void deleteUser(int id) {
        entityManager.remove(entityManager.find(User.class, id));
    }

    @Override
    public User getUserByUsername(String username) {
        TypedQuery<User> query = (TypedQuery<User>) entityManager.createNativeQuery("SELECT * FROM users where username = :username", User.class);
        query.setParameter("username", username);
        return query.getSingleResult();
    }

}
