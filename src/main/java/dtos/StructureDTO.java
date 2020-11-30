package dtos;

import entities.Project;

import java.io.Serializable;
import java.time.LocalDateTime;

public class StructureDTO extends TimestampDTO implements Serializable {
    private long id;
    private String name;
    private String parameters;
    private String clientObservations;
    private Project project;
    private boolean visibleToClient;
    private boolean clientAccepted;

    public StructureDTO(String name, String parameters, String clientObservations, Project project, boolean visibleToClient, boolean clientAccepted, LocalDateTime created, LocalDateTime updated) {
        super(created, updated);
        this.name = name;
        this.parameters = parameters;
        this.clientObservations = clientObservations;
        this.project = project;
        this.visibleToClient = visibleToClient;
        this.clientAccepted = clientAccepted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getClientObservations() {
        return clientObservations;
    }

    public void setClientObservations(String clientObservations) {
        this.clientObservations = clientObservations;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public boolean isVisibleToClient() {
        return visibleToClient;
    }

    public void setVisibleToClient(boolean visibleToClient) {
        this.visibleToClient = visibleToClient;
    }

    public boolean isClientAccepted() {
        return clientAccepted;
    }

    public void setClientAccepted(boolean clientAccepted) {
        this.clientAccepted = clientAccepted;
    }
}
