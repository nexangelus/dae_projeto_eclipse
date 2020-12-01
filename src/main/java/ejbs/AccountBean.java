package ejbs;

import entities.Account;
import entities.Admin;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.UUID;

@Stateless
public class AccountBean extends BaseBean {

    public Account findAccount(String code) {
        return em.find(Account.class, code);
    }

    public String create(String email, String group)
            throws MyEntityExistsException, MyConstraintViolationException {
        User user = em.find(User.class, email);
        if (user != null)
            throw new MyEntityExistsException("Account with email: " + email + " already exists");
        try {
            String code;
            do {
                code = UUID.randomUUID().toString().replace("-","");
            }while (findAccount(code)!=null);
            Account account = new Account(code,email, group);
            em.persist(account);
            return account.getCode();
        } catch (ConstraintViolationException e) {
            throw new MyConstraintViolationException(e);
        }
    }

}
