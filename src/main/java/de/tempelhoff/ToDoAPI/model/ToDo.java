package de.tempelhoff.ToDoAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ToDo {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer ToDoID;
    private String Description;
    private boolean isDone;
    private Integer userID;

    protected ToDo(){}

    public ToDo(String description, boolean isDone, Integer userID){
        this.Description = description;
        this.isDone = isDone;
        this.userID = userID;
    }

    public ToDo(Integer toDoID, String description, boolean isDone, Integer userID){
        this(description, isDone, userID);
        this.ToDoID = toDoID;
    }

    public Integer getToDoID() {
        return ToDoID;
    }

    public String getDescription() {
        return Description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setToDoID(Integer toDoID) {
        ToDoID = toDoID;
    }

    public void setDescription(String description) {
        this.Description = description;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Integer getUserID() {
        return userID;
    }

    @JsonIgnore
    public void setUserID(Integer userID) {
        this.userID = userID;
    }
}
