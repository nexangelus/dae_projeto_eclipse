package ejbs;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class BaseBean {
    @PersistenceContext
    EntityManager em;
}
