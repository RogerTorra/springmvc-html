package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hellfish90 on 18/01/15.
 */
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Transactional
    Iterable<User> getAll(){

        return userRepository.findAll();

    }

    @Transactional
    User getById(long id){

        return userRepository.findUserById(id);
    }

    @Transactional
    void removeUser(long id){
        userRepository.delete(id);
    }

}
