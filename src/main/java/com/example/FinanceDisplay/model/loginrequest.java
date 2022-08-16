package com.example.FinanceDisplay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class loginrequest {
    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
}
