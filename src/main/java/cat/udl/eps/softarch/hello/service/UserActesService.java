package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Event;
import cat.udl.eps.softarch.hello.model.User;

/**
 * Created by roger on 17/01/2015.
 */
public interface UserActesService {
    User getUserAndActes(Long userId);

    Event addActeToUser(Event event);

    void removeActeFromUser(Long acteId);
}
