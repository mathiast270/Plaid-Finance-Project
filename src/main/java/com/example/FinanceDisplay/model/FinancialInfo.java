package com.example.FinanceDisplay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class FinancialInfo implements Serializable {
     private double SavingAccount;
     private double Investments;
     private double debt;

    public FinancialInfo(@JsonProperty("SavingAccount") double SavingAccount, @JsonProperty("Investments") double Investments,
                         @JsonProperty("debt") double debt) {
        this.SavingAccount = SavingAccount;
        this.Investments = Investments;
        this.debt = debt;
    }
}