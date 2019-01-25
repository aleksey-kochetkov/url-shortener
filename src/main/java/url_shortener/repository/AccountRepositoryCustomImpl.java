package url_shortener.repository;

import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;

public class AccountRepositoryCustomImpl
                                     implements AccountRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(Object object) {
        this.entityManager.persist(object);
    }
}
