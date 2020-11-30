package entities;

import org.eclipse.persistence.annotations.PrimaryKey;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findAccount",
                query = "SELECT a FROM Account a WHERE a.email=:email ORDER BY a.email" // JPQL
        )
})
public class Account {
    @Version
    private int version;

    @Id
    @NotNull
    @Email
    private String email;

    @NotNull
    private String group;

    public Account(@NotNull @Email String email, @NotNull String group) {
        this.email = email;
        this.group = group;
    }

    public Account() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
