package ch.hevs.aislab.demo.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class AccountEntity {
    private String id;
    private String name;
    private Double balance;
    private String owner;

    public AccountEntity() {
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Exclude
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof AccountEntity)) return false;
        AccountEntity o = (AccountEntity) obj;
        return o.getId().equals(this.getId());
    }

    @Override
    public String toString() {
        return name;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("balance", balance);

        return result;
    }
}
