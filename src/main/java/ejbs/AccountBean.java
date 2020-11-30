package ejbs;

import entities.Account;
import entities.Admin;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;

@Stateless
public class AccountBean extends BaseBean {

    public Account findAccount(String email) {
        return (Account) em.createNamedQuery("findAccount").setParameter("email", email).getResultList();
    }

    public void create(String email, String group)
            throws MyEntityExistsException, MyConstraintViolationException {
        User user = em.find(User.class, email);
        if (user == null)
            throw new MyEntityExistsException("Account with email: " + email + " already exists");
        try {
            Account account = new Account(email, group);
            em.persist(account);
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

}
