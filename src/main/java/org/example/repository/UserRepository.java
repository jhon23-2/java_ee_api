package org.example.repository;

import org.example.model.UserModel;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Stateless
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String QUERY_FIND_ALL = "SELECT u FROM UserModel u";
    private static final String QUERY_FIND_BY_EMAIL= "SELECT u FROM UserModel u WHERE u.email = :email";

    // @Transactional is not needed in EJB when you use @Stateless annotation, as methods are transactional by default
    public void save(UserModel user) {
        this.entityManager.persist(user);
    }

    public Optional<UserModel> findById(Long id) {
       return Optional.ofNullable(this.entityManager.find(UserModel.class, id));
    }

    public List<UserModel> findAll() {
        TypedQuery<UserModel> query = this.entityManager.createQuery(QUERY_FIND_ALL, UserModel.class);
        return query.getResultList();
    }

    public Optional<UserModel> findByEmail(String email) {
        TypedQuery<UserModel> query = this.entityManager.createQuery(
                QUERY_FIND_BY_EMAIL,
                UserModel.class
        );
        query.setParameter("email", email);
        return Optional.ofNullable(query.getSingleResult()); // test it
    }

    public void update(UserModel user) {
        this.entityManager.merge(user);
    }

    public void delete(Long id) {
        UserModel user = this.entityManager.find(UserModel.class, id);
        if(user != null) {
            this.entityManager.remove(user);
        }
    }

}
