package dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ManufacturerDTO extends TimestampDTO implements Serializable {
    private String username;
    private String newPassword;
    private String oldPassword;
    private String name;
    private String email;
    private String address;
    private String website;
    private String contact;

    public ManufacturerDTO(String username, String newPassword, String oldPassword, String name, String email, String address, String website, String contact, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.username = username;
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
        this.name = name;
        this.email = email;
        this.address = address;
        this.website = website;
        this.contact = contact;
    }

    public ManufacturerDTO(String username, String name, String email, String address, String website, String contact, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.username = username;
        this.name = name;
        this.email = email;
        this.address = address;
        this.website = website;
        this.contact = contact;
    }

    public ManufacturerDTO() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
