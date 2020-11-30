package dtos;

import ejbs.BaseBean;

import java.io.Serializable;

public class AccountDTO implements Serializable {
    private String code;
    private String email;
    private String group;

    public AccountDTO(String code, String email, String group) {
        this.code = code;
        this.email = email;
        this.group = group;
    }

    public AccountDTO() {
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
