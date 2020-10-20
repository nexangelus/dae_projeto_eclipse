package entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "getAllClients",
                query = "SELECT c FROM Client c ORDER BY c.name" // JPQL
        )
})
public class Client extends User{

    @NotNull
    private String Contact;

    @NotNull
    private String address;

    @OneToMany
    Set<Project> projects;

    public Client() {
        super();
        this.projects = new LinkedHashSet<Project>();
    }

    public Client(String username, String password, String name, String email, String contact, String address) {
        super(username, password, name, email);
        Contact = contact;
        this.address = address;
        this.projects = new LinkedHashSet<Project>();
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    //add + remove from list
}
