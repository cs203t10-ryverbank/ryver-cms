package cs203t10.ryver.cms.user;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @RolesAllowed("MANAGER")
    public List<User> getUsers() {
        return userService.findAll();
    }

    /**
     * Using BCrypt encoder to encrypt the password for storage
     * @param user
     * @return
     */
    @PostMapping("/users")
    @RolesAllowed("MANAGER")
    public User addUser(@Valid @RequestBody User user){
        return userService.saveAndHashPassword(user);
    }

}

