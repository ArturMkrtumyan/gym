package com.example.gym.utility;

import com.example.gym.model.User;
import com.example.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserUtilService {

    @Autowired
    private UserRepository userRepository;

    public User createUserWithGeneratedUsernameAndPassword(String firstName, String lastName) {
        String username = generateUsername(firstName, lastName);
        String password = generateRandomPassword(10);

        int serialNumber = 1;
        String uniqueUsername = username;
        while (userRepository.existsByUsername(uniqueUsername)) {
            serialNumber++;
            uniqueUsername = username + "." + serialNumber;
        }
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(uniqueUsername);
        user.setPassword(password);
        user.setIsActive(true);


        return userRepository.save(user);
    }

    private String generateUsername(String firstName, String lastName) {
        return firstName + "." + lastName;
    }

    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";

       return  new Random().ints(length, 0, characters.length())
                .mapToObj(characters::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

}
