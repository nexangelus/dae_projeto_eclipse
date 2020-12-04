package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAccountEmail",
                query = "SELECT a FROM Account a WHERE a.email= :email ORDER BY a.groupType" // JPQL
        )
})
public class Account {
    @Version
    private int version;

    @Id
    private String code;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String groupType;

    public Account(String code, @NotNull @Email String email, @NotNull String groupType) {
        this.code = code;
        this.email = email;
        this.groupType = groupType;
    }

    public Account() {
    }

    public int getVersion() {
        return version;
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

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
