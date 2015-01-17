package cat.udl.eps.softarch.hello.service;
import java.util.List;
import java.util.Objects;

import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.repository.ActesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;

/**
 * Created by roger on 17/01/2015.
 */
@Service
public class UserActesServiceImpl implements UserActesService{
    final Logger logger = LoggerFactory.getLogger(UserActesServiceImpl.class);

    @Autowired
    ActesRepository actesRepository;
    @Autowired UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public User getUserAndActes(Long userId) {
        User u = userRepository.findOne(userId);
        logger.info("User {} has {} actes", u.getUsername(), u.getActes().size());
        return u;
    }

    @Transactional
    @Override
    public Acte addActeToUser(Acte g) {
        User u = userRepository.findUserById(g.getId());
        if (u == null) {
            String email = Objects.toString(g.getId(), null);
            String username = email.substring(0, email.indexOf('@'));
            u = new User(username, email);
        }
        actesRepository.save(g);
        u.addActe(g);
        userRepository.save(u);
        return g;
    }

    @Transactional
    @Override
    public void removeActeFromUser(Long acteId) {
        Acte g = actesRepository.findOne(acteId);
        User u = userRepository.findUserById(g.getId());
        if (u != null) {
            u.removeActe(g);
            userRepository.save(u);
        }
        actesRepository.delete(g);
    }
}
