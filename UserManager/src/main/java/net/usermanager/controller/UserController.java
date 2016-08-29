package net.usermanager.controller;

import net.usermanager.model.User;
import net.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by x555l on 15.08.2016.
 */
@Controller
public class UserController {
    private UserService userService;

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

/*
    @RequestMapping(value = "/pages/{pageNumber}", method = RequestMethod.GET)
    public String getUserPage(@PathVariable Integer pageNumber, Model model) {
        Page<User> page = userService.getUsers(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("deploymentLog", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "users";
    }
*/

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String listUsers(Model model){
        List<User> userList = this.userService.listUsers();
        model.addAttribute("user", new User());
        model.addAttribute("listUsers", userList);

        return "users";
    }

    @RequestMapping(value = "/findusers", method = RequestMethod.GET)
    public String listFoundUsers(Model model, @RequestParam String searchName){
        model.addAttribute("user", new User());
        model.addAttribute("listFoundUsers", this.userService.findUsersByName(searchName));

        return "foundusers";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user){
        if (user.getId()==0){
            this.userService.addUser(user);
        } else {
//            Date date1 = new Date(date);
//            user.setCreatedDate(date1);
            this.userService.updateUser(user);
        }

        return "redirect:/users";
    }

    @RequestMapping("/remove/{id}")
    public String removeUser(@PathVariable("id") int id){
        this.userService.removeUser(id);

        return "redirect:/users";
    }

    @RequestMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model){
        User user = this.userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("listUsers", this.userService.listUsers());
        model.addAttribute("createdDate",user.getCreatedDate());

        return "users";
    }

    @RequestMapping("userdata/{id}")
    public String userData(@PathVariable("id") int id, Model model){
        model.addAttribute("user", this.userService.getUserById(id));

        return "userdata";
    }

}
