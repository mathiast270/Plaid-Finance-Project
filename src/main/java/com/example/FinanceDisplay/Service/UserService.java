package com.example.FinanceDisplay.Service;

import com.example.FinanceDisplay.model.FinancialInfo;
import com.example.FinanceDisplay.model.User;
import com.example.FinanceDisplay.model.loginrequest;
import com.plaid.client.model.LinkTokenCreateResponse;

import java.io.IOException;
import java.util.List;

public interface UserService {
     void adduser(loginrequest user) throws Exception;
     List<User> getallusers();
     void updateUsers() throws IOException;
    LinkTokenCreateResponse createlinktoken() throws IOException;
    String getaccesstoken(String public_token);
    FinancialInfo getuserinformation(String access_token);
}
