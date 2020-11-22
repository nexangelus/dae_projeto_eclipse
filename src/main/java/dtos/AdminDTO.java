package dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AdminDTO extends TimestampDTO implements Serializable  {
    private String username;
    private String password;
    private String name;
    private String email;

    public AdminDTO(String username, String password, String name, String email, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public AdminDTO(String username,  String name, String email, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public AdminDTO() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
