package ch.hevs.aislab.intro.database.entity;

import com.google.firebase.database.Exclude;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ClientEntity {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private long createdAt;


    public ClientEntity() {
    }

    public ClientEntity(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof ClientEntity)) return false;
        ClientEntity o = (ClientEntity) obj;
        return o.getEmail().equals(this.getEmail());
    }

    @NotNull
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("createdAt", createdAt);
        return result;
    }
}
