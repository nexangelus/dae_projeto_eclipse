package entities;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllAdmins",
                query = "SELECT a FROM Admin a ORDER BY a.name" // JPQL
        )
})
public class Admin extends User{

    public Admin(String username, String password, String name, String email) {
        super(username, password, name, email);
    }

    public Admin() {
    }
}
