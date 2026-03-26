package com.periodtracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.periodtracker.model.User;
import com.periodtracker.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    UserRepository repo;

    // REGISTER PAGE
    @GetMapping("/")
    public String registerPage(){
        return "register";
    }

    // REGISTER LOGIC
    @PostMapping("/register")
    public String register(User user, Model model){

        if(repo.findByEmail(user.getEmail()) != null){
            model.addAttribute("msg","User already exists");
            return "register";
        }

        repo.save(user);
        model.addAttribute("msg","Registration Successful. Please Login.");
        return "login";
    }

    // LOGIN PAGE
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    // LOGIN LOGIC
    @PostMapping("/login")
    public String login(String email, String password, Model model, HttpSession session){

        User u = repo.findByEmail(email);

        if(u != null && u.getPassword().equals(password)){
            
            // ✅ STORE SESSION
            session.setAttribute("userId", u.getId());

            // ✅ REDIRECT TO DASHBOARD (URL WILL CHANGE)
            return "redirect:/dashboard";
        }

        model.addAttribute("msg","Invalid Login");
        return "login";
    }

    // LOGOUT
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login";
    }
}