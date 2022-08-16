package com.example.FinanceDisplay.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter

public class getUserInfoRequest {
    @JsonProperty("accesstoken")
    private String accesstoken;

    @JsonProperty("publictoken")
    private String publictoken;
}
