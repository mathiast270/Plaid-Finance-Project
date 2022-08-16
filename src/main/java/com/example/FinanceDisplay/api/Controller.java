package com.example.FinanceDisplay.api;


import com.example.FinanceDisplay.Service.UserService;
import com.example.FinanceDisplay.model.*;
import com.plaid.client.model.LinkTokenCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@CrossOrigin("*")
@RequiredArgsConstructor
public class Controller {
    private final UserService userService;

    @PostMapping(value = "/createuser", consumes = {"application/xml","application/json"})

    public void addUser(@RequestBody loginrequest user) throws Exception {
        userService.adduser(user);
    }

    @ResponseBody
    @GetMapping(value = "/getall", produces = "application/json")
    public List<User> getallUsers(){
        return userService.getallusers();
    }
    @PutMapping(value = "/updateusers")
    public void updateusers() throws IOException {
        userService.updateUsers();
        }

        @PostMapping(value = "/create_link_token")
    public LinkTokenCreateResponse create_link_token() throws IOException {
        return userService.createlinktoken();
        }
        @PostMapping(value = "/exchange_public_token")
    public String getAccessToken(@RequestBody getUserInfoRequest request){
        return userService.getaccesstoken(request.getPublictoken());

        }

        @GetMapping("/viewuser")
    public FinancialInfo getuserinfo(@RequestBody getUserInfoRequest request){
        return userService.getuserinformation(request.getAccesstoken());
        }

    }