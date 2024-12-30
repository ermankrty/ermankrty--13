package com.coderscampus.assignment13.web;

import com.coderscampus.assignment13.domain.Account;
import com.coderscampus.assignment13.domain.User;
import com.coderscampus.assignment13.service.AccountService;
import com.coderscampus.assignment13.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AccountController {

    private UserService userService;
    private AccountService accountService;

    public AccountController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }
    
    @GetMapping("/users/{userId}/accounts/new")
    public String getCreateAccount(ModelMap model, @PathVariable Long userId) {
        User user = userService.findById(userId);
        Account account = new Account();
        account.getUsers().add(user);
        user.getAccounts().add(account);
        account.setAccountName(user.getName() + "'s Account " + user.getAccounts().size());
        model.put("user", user);
        model.put("account", account);
        return "create";
    }

    @PostMapping("/users/{userId}/accounts/new")
    public String postCreateAccount(Account account, @PathVariable Long userId) {
        User user = userService.findById(userId);
        account.setAccountName(account.getAccountName());
        account.getUsers().add(user);
        user.getAccounts().add(account);
        accountService.saveAccount(account);
        userService.saveUser(user);
        return "redirect:/users/" + userId;
    }


    @GetMapping("/users/{userId}/accounts/{accountId}")
    public String getUpdateAccount(ModelMap model, @PathVariable Long userId, @PathVariable Long accountId) {
        model.put("user", userService.findById(userId));
        Account account = accountService.findById(accountId);
        model.put("account", account);
        return "update";
    }

    @PostMapping("/users/{userId}/accounts/{accountId}")
    public String postUpdateAccount(@ModelAttribute("account") Account updatedAccount, @PathVariable Long userId, @PathVariable Long accountId) {
        Account account = accountService.findById(accountId);
        account.setAccountName(updatedAccount.getAccountName());
        accountService.saveAccount(account);
        return "redirect:/users/" + userId;
    }

    @PostMapping("users/{userId}/accounts/{accountId}/delete")
    public String deleteOneUser(@PathVariable Long accountId, @PathVariable Long userId) {
        Account account = accountService.findById(accountId);
        User user = userService.findById(userId);
        user.getAccounts().remove(account);
        userService.saveUser(user);
        accountService.delete(accountId);
        return "redirect:/users/" + userId;
    }

}
