package com.atkison.login.seed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.atkison.login.seed.model.JwtUserDetails;
import com.atkison.login.seed.model.User;
import com.atkison.login.seed.repository.UserRepository;
import com.atkison.login.seed.security.PasswordUtils;
import com.atkison.login.seed.security.ExceptionHandling.AuthenticationFailedError;
import com.atkison.login.seed.security.ExceptionHandling.EmailNotFoundException;
import com.atkison.login.seed.security.ExceptionHandling.RegistrationError;
import com.atkison.login.seed.security.ExceptionHandling.UserException;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordUtils passwordUtils;

    @Autowired
    public UserService(UserRepository userRepository, PasswordUtils passwordUtils)
    {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
    }

    /**
     * This is called when the application is registering a user
     * @Param email - the new users email address
     * @Param password - this is the users password passed in
     * @Return User - this is the user object*/
    public User registerUser(String email, String password)
    {
        //check to see if email already exists
        Optional<User> u = userRepository.findByEmail(email);
        if(u.isPresent())
        {
            throw new RegistrationError("Email already exists. Please enter a new email");
        }

        String salt = this.passwordUtils.getSalt(30);
        String securePassword = this.passwordUtils.generateSecurePassword(password, salt);
        User user = new User(email, securePassword, salt);
        userRepository.save(user);

        Optional<User> newUser = userRepository.findByEmailAndPassword(email, securePassword);
        newUser.orElseThrow(() -> new RegistrationError("Email not found!"));

        return newUser.map(JwtUserDetails::new).get();
    }

    /**
     * Load the user by email and password
     * @Param Email - the email of the user
     * @Param Password - this is the password of the user
     * Returns User - user object that is returned*/
    public User loadByEmailAndPassword(String email, String password) throws EmailNotFoundException
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        boolean passwordMatch = this.passwordUtils.verifyUserPassword(password, user.get().getPassword(), user.get().getSalt());

        if(!passwordMatch)
            throw new AuthenticationFailedError("Email and Password not authenticated");

        return user.map(JwtUserDetails::new).get();
    }

    /**
     * Load the user by his email
     * @Param Email - this is the email of the user that we want load
     * @Returns User - the user object from the database*/
    public User loadByEmail(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        return user.map(JwtUserDetails::new).get();
    }

    public User getUser(String email)
    {
        Optional<User> user = userRepository.findByEmail(email);
        user.orElseThrow(() -> new EmailNotFoundException("The email address given was not found"));

        return user.get();
    }

    /**
     * Updates the user information
     * @Param User - this is the user that we want to update
     * @Returns User - this is the user we wanted to update*/
    public User updateUserInformation(User user)
    {
        Optional<User> u = this.userRepository.findByEmail(user.getEmail());
        u.orElseThrow(() -> new UserException("User not found"));

        u.get().setFirstname(user.getFirstname());
        u.get().setLastname(user.getLastname());
        u.get().setUsername(user.getUsername());
        u.get().setEmail(user.getEmail());

        User newUser = this.userRepository.save(u.get());
        User resultU = new User(newUser.getId(), newUser.getFirstname(), newUser.getLastname(), newUser.getUsername(), newUser.getEmail());

        return resultU;
    }

    public class Success
    {
        private String success;
        public Success(String success)
        {
            this.success = success;
        }
    }

}




























