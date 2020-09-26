package cs203t10.ryver.cms.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private UserRepository userRepo;

    public List<User> findAll() {
        return userRepo.findAll();
    }

    /**
     * Save the user to the repository with a hashed password.
     * The original user object is not mutated.
     * @return The user with a hashed password.
     */
    public User saveAndHashPassword(User user) {
        return userRepo.save(user.toBuilder().password(encoder.encode(user.getPassword())).build());
    }
}
