package dtos;

import ejbs.BaseBean;

import java.io.Serializable;

public class AccountDTO implements Serializable {
    private String email;
    private String group;

    public AccountDTO(String email, String group) {
        this.email = email;
        this.group = group;
    }

    public AccountDTO() {
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
