package net.usermanager.controller;

import net.usermanager.model.User;
import net.usermanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by x555l on 15.08.2016.
 */
@Controller
public class UserController {
    private UserService userService;
    private int currentPageNumber = 1;
    private int lastPageNumber = 1;
    private List<User> userList = new ArrayList<User>();
    private List<User> usersByPageNumber = new ArrayList<User>();
    private User currentUser = new User();
    private final int usersOnOnePage = 5;

    @Autowired(required = true)
    @Qualifier(value = "userService")
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<User> getUsersByPageNumber(List<User> userList, Integer pageNumber){
        int usersSize = userList.size();
        List<User> users = new ArrayList<User>();
        for (int i = 0; i < usersOnOnePage; i++) {
            int index = (pageNumber-1)*usersOnOnePage+i;
            if (index>=usersSize) break;
            users.add(userList.get(index));
        }
        return users;
    }

    public void setUserListAndLastPage(){
        userList = this.userService.listUsers();
        int usersSize = userList.size();
        int countTotalPages =usersSize/usersOnOnePage;
        if (usersSize%usersOnOnePage>0) countTotalPages++;
        if (countTotalPages<1) countTotalPages++;
        lastPageNumber = countTotalPages;
    }

    @RequestMapping(value = "/pages/{pageNumber}", method = RequestMethod.GET)
    public String getUserPage(@PathVariable Integer pageNumber, Model model) {
        setUserListAndLastPage();
        while (pageNumber>lastPageNumber){
            pageNumber--;
        }
        if (pageNumber<1) pageNumber = 1;
        usersByPageNumber = getUsersByPageNumber(userList, pageNumber);
        //Page<User> page = userService.getUsers(pageNumber);

//        int current = page.getNumber() + 1;
/*
        int current = pageNumber;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, countTotalPages);
*/
        currentPageNumber = pageNumber;
        currentUser = new User();

/*
        model.addAttribute("usersByPageNumber", usersByPageNumber);
        model.addAttribute("countTotalPages", countTotalPages);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);
        model.addAttribute("user", new User());
*/
        setModelAttributes(model);
        return "users";
    }

    public void setModelAttributes(Model model){
        int begin = Math.max(1, currentPageNumber - 5);
        int end = Math.min(begin + 10, lastPageNumber);
        model.addAttribute("usersByPageNumber", usersByPageNumber);
        model.addAttribute("countTotalPages", lastPageNumber);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", currentPageNumber);
        model.addAttribute("user", currentUser);
    }

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public String listUsers(Model model){
/*
        List<User> userList = this.userService.listUsers();
        //model.addAttribute("user", new User());
        model.addAttribute("listUsers", userList);

        return "/pages/"+currentPageNumber;
*/
        setModelAttributes(model);
        return "users";
    }

    @RequestMapping(value = "/findusers", method = RequestMethod.GET)
    public String listFoundUsers(Model model, @RequestParam String searchName){
//        model.addAttribute("user", new User());
        model.addAttribute("listFoundUsers", this.userService.findUsersByName(searchName));

        return "foundusers";
    }

    @RequestMapping(value = "/users/add", method = RequestMethod.POST)
    public String addUser(@ModelAttribute("user") User user){
        if (user.getId()==0){
            this.userService.addUser(user);
            setUserListAndLastPage();
            return "redirect:/pages/"+lastPageNumber;
        } else {
//            Date date1 = new Date(date);
//            user.setCreatedDate(date1);
            this.userService.updateUser(user);
            currentUser = new User();
        }

        return "redirect:/pages/"+currentPageNumber;
    }

    @RequestMapping("/remove/{id}")
    public String removeUser(@PathVariable("id") int id){
        this.userService.removeUser(id);

        return "redirect:/pages/"+currentPageNumber;
    }

    @RequestMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model){
        currentUser = this.userService.getUserById(id);
        setModelAttributes(model);
//        model.addAttribute("user", user);
        //model.addAttribute("listUsers", this.userService.listUsers());

        return "users";
    }

    @RequestMapping("userdata/{id}")
    public String userData(@PathVariable("id") int id, Model model){
        model.addAttribute("chosenuser", this.userService.getUserById(id));

        return "userdata";
    }

}
