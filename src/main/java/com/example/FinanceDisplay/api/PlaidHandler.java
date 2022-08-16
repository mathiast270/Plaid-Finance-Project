package com.example.FinanceDisplay.api;

import com.example.FinanceDisplay.model.User;
import com.plaid.client.ApiClient;
import com.plaid.client.model.*;
import com.plaid.client.request.PlaidApi;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


public class PlaidHandler {
    private final String CLIENTID = "Fill in with our own keys";
    private final String SecretID = "Fill in with your own Keys";
    private final PlaidApi plaidClient = InitlizeAPI();
    public PlaidApi InitlizeAPI(){
        HashMap<String, String> apiKeys = new HashMap<String, String>();
        apiKeys.put("clientId", CLIENTID);
        apiKeys.put("secret", SecretID);
        ApiClient apiClient = new ApiClient(apiKeys);
        apiClient.setPlaidAdapter(ApiClient.Sandbox);
        return apiClient.createService(PlaidApi.class);
    }
    public LinkTokenCreateResponse createLinkToken(User user) throws IOException {
        //code from https://plaid.com/docs/quickstart/
        LinkTokenCreateRequestUser linkuser = new LinkTokenCreateRequestUser().clientUserId(user.getId().toString());
        LinkTokenCreateRequest request = new LinkTokenCreateRequest().user(linkuser).
                language("en").
                countryCodes(Arrays.asList(CountryCode.US)).clientName("Finance project").
                products(Arrays.asList(Products.AUTH));
        Response<LinkTokenCreateResponse> response = plaidClient
                .linkTokenCreate(request)
                .execute();
        return response.body();
    }

    public String getAccessToken(String public_token) throws IOException {
        ItemPublicTokenExchangeRequest request = new ItemPublicTokenExchangeRequest()
                .publicToken(public_token);
        Response<ItemPublicTokenExchangeResponse> response = plaidClient
                .itemPublicTokenExchange(request)
                .execute();
        return response.body().getAccessToken();
    }
}

