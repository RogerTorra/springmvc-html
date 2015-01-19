package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.repository.FavoriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.google.common.base.Preconditions;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Created by roger on 17/01/2015.
 */
@Controller
@RequestMapping(value = "/users")
public class UserController {

    final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired UserRepository userRepository;
    @Autowired FavoriteRepository favoriteRepository;


    // LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<User> list(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "10") int size) {
        PageRequest request = new PageRequest(page, size);
        return userRepository.findAll(request).getContent();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "text/html")
    public ModelAndView listHTML(@RequestParam(required = false, defaultValue = "0") int page,
                                 @RequestParam(required = false, defaultValue = "10") int size) {
        return new ModelAndView("users", "users", list(page, size));
    }

    // RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User retrieve(@PathVariable("id") Long id) {
        logger.info("Retrieving user number {}", id);
        Preconditions.checkNotNull(userRepository.findOne(id), "User with id %s not found", id);
        return userRepository.getOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("user", "user", retrieve(id));
    }


    //Create
    @RequestMapping(method = RequestMethod.POST)
    public String createUser(HttpServletRequest request, Model model){
        // this way you get value of the input you want
        String email = request.getParameter("email");

        User u = new User(email);


        if(null==userRepository.findUserByEmail(u.getEmail())){
            userRepository.save(u);
        }


        return u.toString();
    }

    //Delete
    @RequestMapping(method = RequestMethod.DELETE)
    public String DeleteUser(HttpServletRequest request, Model model){
        // this way you get value of the input you want
        String email = request.getParameter("email");

        FavoriteRepository favRep;

        User u = new User(email);

        favoriteRepository.delete(favoriteRepository.findFavoritesByUser(u));

        userRepository.delete(u);

        return u.toString();
    }




}
