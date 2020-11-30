package entities;

import org.eclipse.persistence.annotations.PrimaryKey;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Random;
import java.util.UUID;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findAccount",
                query = "SELECT a FROM Account a WHERE a.code=:code ORDER BY a.email" // JPQL
        )
})
public class Account {
    @Version
    private int version;

    @Id
    @NotNull
    private String code;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String group;

    public Account(@NotNull String code, @NotNull @Email String email, @NotNull String group) {
        this.code = code;
        this.email = email;
        this.group = group;
    }

    public Account() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
