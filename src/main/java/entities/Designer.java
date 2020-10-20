package entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class Designer extends User{

    @OneToMany
    private Set<Project> projects;

    public Designer() {
        super();
        this.projects = new LinkedHashSet<Project>();
    }

    public Designer(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.projects = new LinkedHashSet<Project>();
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    // add + remove from set project void or bollean
}
