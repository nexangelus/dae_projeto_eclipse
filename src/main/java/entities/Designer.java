package entities;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NamedQueries({
        @NamedQuery(name = "getAllDesigners", query = "SELECT d FROM Designer d ORDER BY d.name"),
        @NamedQuery(name = "getDesignerProjects", query = "SELECT p FROM Project p JOIN Designer d ON d = p.designer WHERE d.username = :username")
})
public class Designer extends User{

    @OneToMany(mappedBy = "designer")
    private Set<Project> projects;

    public Designer() {
        super();
        this.projects = new LinkedHashSet<>();
    }

    public Designer(String username, String password, String name, String email) {
        super(username, password, name, email);
        this.projects = new LinkedHashSet<>();
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
}
