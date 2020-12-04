package ejbs;

import entities.Account;
import entities.Admin;
import entities.Project;
import entities.User;
import exceptions.MyConstraintViolationException;
import exceptions.MyEntityExistsException;

import javax.ejb.Stateless;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;

@Stateless
public class AccountBean extends BaseBean {

    public Account findAccount(String code) {
        return em.find(Account.class, code);
    }

    public Account getAccountEmail(String email) {
        List<Account> resultList = em.createNamedQuery("getAccountEmail").setParameter("email", email).getResultList();
        if(resultList.isEmpty())
            return null;
        return resultList.get(0);
    }

    public String create(String email, String group)
            throws MyEntityExistsException, MyConstraintViolationException {
        User user = em.find(User.class, email);
        Account accountCheck = getAccountEmail(email);
        if (user != null)
            throw new MyEntityExistsException("Account with email: " + email + " already exists");
        if (accountCheck != null)
            throw new MyEntityExistsException("Account for creation with email: " + email + " already exists");
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
