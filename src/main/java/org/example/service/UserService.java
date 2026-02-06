package org.example.service;

import org.example.model.UserModel;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserService {

    @PersistenceContext
    private EntityManager em;

    private static final String QUERY_FIND_ALL = "SELECT u FROM UserModel u";

    @Transactional
    public void createUser(UserModel user){
        em.persist(user);
    }

    public UserModel findByUserId(Long id) {
        return em.find(UserModel.class, id);
    }

    public List<UserModel> findAllUsers () {
        return em.createQuery(QUERY_FIND_ALL, UserModel.class).getResultList();
    }

    @Transactional
    public void deleteUser(Long id) {
        UserModel user = findByUserId(id);
        if (user != null) {
            em.remove(user);
        }
    }
}
