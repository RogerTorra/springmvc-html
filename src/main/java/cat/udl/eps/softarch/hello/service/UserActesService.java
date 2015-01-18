package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.model.User;

/**
 * Created by roger on 17/01/2015.
 */
public interface UserActesService {
    User getUserAndActes(Long userId);

    Acte addActeToUser(Acte acte);

    void removeActeFromUser(Long acteId);
}
