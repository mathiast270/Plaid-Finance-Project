package com.example.FinanceDisplay.BackgroundWorker;

import com.example.FinanceDisplay.model.FinancialInfo;
import com.example.FinanceDisplay.model.User;
import com.example.FinanceDisplay.repository;
import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import lombok.RequiredArgsConstructor;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class PlaidConnection {

    private static final String CLIENTID = "Fill in with your own keys";
    private static final String SecretID = "Fill in with your own keys";
    private final repository res;
    public List<User> newlist() throws IOException {
        List<User> users = res.findAll();

        List<User> updatelist = new ArrayList<>();
        PlaidApi plaidClient = createAPI();
        for(User user:users) {
            List<String> access_tokens = user.getAccess_tokens();
            double savings = 0;
            double investments = 0;
            double debt = 0;
            if (access_tokens != null) {
                for(String access_token: access_tokens) {
                    AccountsGetRequest accountsGetRequest = new AccountsGetRequest();
                    accountsGetRequest.setAccessToken(access_token);
                    accountsGetRequest.setClientId(CLIENTID);
                    accountsGetRequest.setSecret(SecretID);
                    Response<AccountsGetResponse> accountsResponse = plaidClient.accountsGet(accountsGetRequest).execute();
                    List<AccountBase> accounts = accountsResponse.body().getAccounts();

                    for (AccountBase account : accounts) {
                        if (account.getType().getValue().equals("depository")) {
                            savings = savings + account.getBalances().getCurrent();
                        } else if (account.getType().getValue().equals("investment")) {
                            investments += account.getBalances().getCurrent();
                        } else if (account.getType().getValue().equals("credit")) {
                            debt += account.getBalances().getCurrent();

                        }
                    }
                }
            }

                    FinancialInfo info = user.getInfo();
                    info.setDebt(debt);
                    info.setSavingAccount(savings);
                    info.setInvestments(investments);
                    user.setInfo(info);
                    updatelist.add(user);
        }
        return updatelist;
    }


    public static PlaidApi createAPI(){
        HashMap<String, String> apiKeys = new HashMap<String, String>();
        apiKeys.put("clientId", CLIENTID);
        apiKeys.put("secret", SecretID);
        apiKeys.put("plaidVersion", "2020-09-14");
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox);
        PlaidApi plaidClient = apiClient.createService(PlaidApi.class);
        return plaidClient;
    }
}

