package dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ClientDTO extends TimestampDTO implements Serializable {
    private String username;
    private String password;
    private String name;
    private String email;
    private String contact;
    private String address;

    public ClientDTO(String username, String password, String name, String email, String contact, String address, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }

    public ClientDTO(String username, String name, String email, String contact, String address, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.username = username;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.address = address;
    }

    public ClientDTO(String username, String name) {
        super();
        this.username = username;
        this.name = name;
    }

    public ClientDTO() {
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
