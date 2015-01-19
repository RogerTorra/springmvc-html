package cat.udl.eps.softarch.hello.service;

import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.model.Favorite;

import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.EventRepository;
import cat.udl.eps.softarch.hello.repository.FavoriteRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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
    public void removeFavorite(long id){

        if(favoriteRepository.exists(id)){
            favoriteRepository.delete(id);
        }

    }


    public int save(long user, long event, String Date){

        User userO = userRepository.findUserById(user);
        Acte eventO = eventRepository.findActeById(event);

        if (userO != null && eventO!=null){
            Favorite fav= new Favorite(eventO,userO,new java.util.Date());

            favoriteRepository.save(fav);
            return 1;
        }

        return -1;
    }

}
