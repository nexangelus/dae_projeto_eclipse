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
    private String contact;

    @NotNull
    private String address;

    @OneToMany
    Set<Project> projects;

    @OneToMany
    Set<Upload> uploads;

    public Client(String username, String password, String name, String email, String contact, String address) {
        super(username, password, name, email);
        this.contact = contact;
        this.address = address;
        this.projects = new LinkedHashSet<>();
        this.uploads = new LinkedHashSet<>();
    }

    public Client() {
        super();
        this.projects = new LinkedHashSet<>();
        this.uploads = new LinkedHashSet<>();
    }

    public String getContact() {
        return this.contact;
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

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    public void addProject(Project project){
        this.projects.add(project);
    }

    public void removeProject(Project project){
        this.projects.remove(project);
    }

    public Set<Upload> getUploads() {
        return uploads;
    }

    public void setUploads(Set<Upload> uploads) {
        this.uploads = uploads;
    }

    public void addUpload(Upload upload){
        this.uploads.add(upload);
    }

    public void removeUpload(Upload upload){
        this.uploads.remove(upload);
    }
}
