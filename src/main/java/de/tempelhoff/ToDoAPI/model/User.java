package de.tempelhoff.ToDoAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
public class User {
    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Integer id;
    private @Column(unique = true, nullable = false) String email;
    private @Column(nullable = false)@JsonProperty("password") @JsonIgnore String password;
    @OneToMany
    @JoinColumn(name = "userID")
    @Column(nullable = true)
    private Set<ToDo> todos = null;
    private String secret;

    protected User(){}

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<ToDo> getTodos() {
        return todos;
    }

    public void setTodos(Set<ToDo> todos) {
        this.todos = todos;
    }

    public String getSecret() {
        return secret;
    }


    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        User user = (User) object;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", Email='" + email + '\'' +
                ", Password='" + password + '\'' +
                '}';
    }
}
