package com.example.FinanceDisplay.model;



import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum Roles {
    USER("User"),
    ADMIN("Admin");

    private String key;

    public String getKey(){
        return this.key;
    }
}
