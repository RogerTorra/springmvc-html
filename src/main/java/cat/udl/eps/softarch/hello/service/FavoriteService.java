package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Favorite;

import cat.udl.eps.softarch.hello.repository.EventRepository;
import cat.udl.eps.softarch.hello.repository.FavoriteRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by hellfish90 on 18/01/15.
 */
public class FavoriteService {

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Transactional
    Iterable<Favorite> getAll(){
        return favoriteRepository.findAll();
    }

    @Transactional
    Iterable<Favorite> getFavoritesByUserId(long id){

        return favoriteRepository.findFavoritesByUser(id);
    }

    @Transactional
    void removeFavorite(long id){

        if(favoriteRepository.exists(id)){
            favoriteRepository.delete(id);
        }

    }

    @Transactional
    void save(Favorite favorite){

        if(favorite.getUser()!=null &&
                favorite.getActe()!= null &&
                userRepository.exists(favorite.getUser().getId()) &&
                eventRepository.exists((favorite.getUser().getId()))) {

            favoriteRepository.save(favorite);

        }

    }

}
