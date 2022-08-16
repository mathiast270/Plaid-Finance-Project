package com.example.FinanceDisplay.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.FinanceDisplay.BackgroundWorker.PlaidConnection;
import com.example.FinanceDisplay.api.PlaidHandler;
import com.example.FinanceDisplay.model.FinancialInfo;
import com.example.FinanceDisplay.model.Roles;
import com.example.FinanceDisplay.model.User;
import com.example.FinanceDisplay.model.loginrequest;
import com.example.FinanceDisplay.repository;
import com.plaid.client.model.LinkTokenCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {
    private final repository res;
    private final PasswordEncoder encoder;
    @Override
    public void adduser(loginrequest request) throws Exception {
        request.setPassword(encoder.encode(request.getPassword()));
        if(res.finduserByusername(request.getUsername()) != null){
            return;
        }
        User user = new User(null,new FinancialInfo(0,0,0),null,request.getUsername(), request.getPassword(), Roles.USER);
        res.save(user);
    }

    @Override
    public List<User> getallusers() {
        return res.findAll();
    }

    @Override
    public void updateUsers() throws IOException {
        PlaidConnection connection = new PlaidConnection(res);
        List<User> users = connection.newlist();
        res.saveAll(users);
    }

    @Override
    public LinkTokenCreateResponse createlinktoken() throws IOException {
        PlaidHandler handler = new PlaidHandler();
        User user = res.finduserByToken("token");
        LinkTokenCreateResponse response = handler.createLinkToken(user);
        return response;
    }

    @Override
    public String getaccesstoken(String public_token) {
        PlaidHandler handler = new PlaidHandler();
        try{
            String access_token = handler.getAccessToken(public_token);
            User user = res.finduserByToken("token");
            List<String> access_tokens = user.getAccess_tokens();
            if(access_tokens == null) {
                access_tokens = new ArrayList<>();
            }

            access_tokens.add(access_token);
            user.setAccess_tokens(access_tokens);
            res.save(user);
            return "Sucusessful Go";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FinancialInfo getuserinformation(String access_token) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decoded = verifier.verify(access_token);
        String Username = decoded.getSubject();
        User user = res.finduserByusername(Username);
        if(user == null){
            return null;
        }
        return user.getInfo();

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = res.finduserByusername(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().getKey()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
    }

}
