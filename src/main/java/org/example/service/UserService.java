package org.example.service;

import org.example.model.UserModel;
import org.example.repository.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
public class UserService {

    @Inject
    private UserRepository userRepository;

    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    public UserModel create(UserModel user){
        LOGGER.info("Saving user " + user.getName());
        this.userRepository.save(user);
        LOGGER.info("User saved!!");
        return user;
    }

    public UserModel updateUser(UserModel user, Long id) {
        Optional<UserModel>  userModelOptional = this.userRepository.findById(id);

        if (!userModelOptional.isPresent()) {
            throw new RuntimeException("User with id " + id + " not found ");
        }


        LOGGER.info("Updating user with id " + id);
        UserModel userFound = userModelOptional.get();
        userFound.setName(user.getName());
        userFound.setEmail(user.getEmail());

        this.userRepository.update(userFound);
        LOGGER.info("User updated!!");
        return userFound;
    }


    public List<UserModel> findAll() {
        return this.userRepository.findAll();
    }

    public UserModel findById(Long id) {
        Optional<UserModel>  userModelOptional = this.userRepository.findById(id);

        if (!userModelOptional.isPresent()) {
            throw new RuntimeException("User with id " + id + " not found ");
        }

        return userModelOptional.get();
    }

    public UserModel findByEmail(String email) {
        Optional<UserModel> userModelOptional = this.userRepository.findByEmail(email);

        if(!userModelOptional.isPresent()){
            throw new RuntimeException("User with email " + email + " not found ");
        }

        return userModelOptional.get();
    }


    public void delete(Long id){
        this.userRepository.delete(id);
    }



}
