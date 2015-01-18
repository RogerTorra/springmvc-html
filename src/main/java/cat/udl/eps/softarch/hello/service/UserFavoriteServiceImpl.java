package cat.udl.eps.softarch.hello.service;
import java.util.Objects;

import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;

/**
 * Created by roger on 17/01/2015.
 */
@Service
public class UserFavoriteServiceImpl implements UserFavoriteService {
    final Logger logger = LoggerFactory.getLogger(UserFavoriteServiceImpl.class);

    @Autowired
    EventRepository eventRepository;
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
        eventRepository.save(g);
        u.addActe(g);
        userRepository.save(u);
        return g;
    }

    @Transactional
    @Override
    public void removeActeFromUser(Long acteId) {
        Acte g = eventRepository.findOne(acteId);
        User u = userRepository.findUserById(g.getId());
        if (u != null) {
            u.removeActe(g);
            userRepository.save(u);
        }
        eventRepository.delete(g);
    }
}
