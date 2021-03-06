package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.model.Favorite;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.EventRepository;
import cat.udl.eps.softarch.hello.repository.FavoriteRepository;
import cat.udl.eps.softarch.hello.repository.UserRepository;
import cat.udl.eps.softarch.hello.service.FavoriteService;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/favorite")
public class FavoriteController {
    final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    JAXBContext  jaxbContext;
    Unmarshaller jaxbUnmarshaller;

    FavoriteService favoriteService;


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ModelAndView listFavorite(HttpServletRequest request, Model model){
        // this way you get value of the input you want
        String idUser =request.getParameter("email");

        User u=userRepository.findUserByEmail(idUser);

        return new ModelAndView("favorites","favorites",favoriteRepository.findFavoritesByUser(u));
    }

    // LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Favorite> list(@RequestParam(required=false, defaultValue="0") int page,
                               @RequestParam(required=false, defaultValue="10") int size,
                               @RequestParam(required=false, defaultValue="") String user) {



        return favoriteRepository.findAll();
    }
    @RequestMapping(method=RequestMethod.GET, produces="text/html")
    public ModelAndView listHTML(@RequestParam(required=false, defaultValue="0") int page,
                                 @RequestParam(required=false, defaultValue="10") int size,
                                 @RequestParam(required=false, defaultValue="") String user) {
        return new ModelAndView("favorites", "favorites", list(page, size,user));
    }

    // RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    @ResponseBody
    public Favorite retrieve(@PathVariable( "id" ) Long id) {
        logger.info("Retrieving acte number {}", id);
        Preconditions.checkNotNull(favoriteRepository.findOne(id), "Acte with id %s not found", id);
        return favoriteRepository.findOne(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("favorite", "favorite", retrieve(id));
    }




    @RequestMapping(method = RequestMethod.POST)
    public String createFavorite(HttpServletRequest request, Model model){
        // this way you get value of the input you want
        String idUser = request.getParameter("email");
        Long idActe = Long.valueOf(request.getParameter("idacte"));
        String data = request.getParameter("data");

        User u =userRepository.findUserByEmail(idUser);
        Acte act= eventRepository.findActeById(idActe);

        logger.info(u+" "+act);

        favoriteRepository.save(new Favorite(act,u,new Date()));

        return "redirect:/search/result";
    }



/*
    // CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Favorite create(@Valid @RequestBody User user, HttpServletResponse response) {
        logger.info("Creating acte with nom'{}'", favorite.toString());
        response.setHeader("Location", "/favorite/form" + favoriteRepository.save(favorite).toString());
        return favorite;
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("acte") Favorite favorite, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form_favorite";
        }
        return "redirect:/favorite/"+create(favorite, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for favorite creation");




        Favorite emptyFavorite = GenerateTestFavorite();

        return new ModelAndView("form_favorite","favorite", emptyFavorite);
    }*/

    // UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Favorite update(@PathVariable("id") Long id, @Valid @RequestBody Favorite favorite) {
        logger.info("Updating acte {}, new name is '{}'", id, favorite.toString());
        Preconditions.checkNotNull(favoriteRepository.findOne(id), "Acte with id %s not found", id);
        Favorite updateFavorite = favoriteRepository.findOne(id);
        updateFavorite.setActe(favorite.getActe());
        updateFavorite.setUser(favorite.getUser());
        return favoriteRepository.save(updateFavorite);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.OK)
    public String updateHTML(@PathVariable("id") Long id, @Valid @ModelAttribute("acte") Favorite favorite,
                             BindingResult binding) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/favorite/"+update(id, favorite).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Generating form for updating favorite number {}", id);
        Preconditions.checkNotNull(favoriteRepository.findOne(id), "Favorite with id %s not found", id);
        return new ModelAndView("form", "favorite", favoriteRepository.findOne(id));
    }

    // DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting favorite number {}", id);
        Preconditions.checkNotNull(favoriteRepository.findOne(id), "Favorite with id %s not found", id);
        favoriteRepository.delete(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        delete(id);
        return "redirect:/favorite";
    }


    private void lookOverAllReminders(){// execute this in subroutine

        Iterable<Favorite> favorites =favoriteRepository.findAll();

        for(Favorite fav: favorites){
            fav.reminderEvent(Calendar.getInstance().getTime());
        }
    }

/*
    private Favorite GenerateTestFavorite(){

        Acte act = event.findAll().iterator().next();

        User user = new User("marc@lala.com");
        User user1 = new User("roger@lala.com");
        User user2 = new User("roberto@lala.com");

        users.save(user);
        users.save(user1);
        users.save(user2);

        Random rand = new Random();



        Favorite fav = new Favorite(act,user,Calendar.getInstance().getTime());


        return fav;
    }
*/

    public class JsonResponse {

        private String status = "";
        private String errorMessage = "";

        public JsonResponse(String status, String errorMessage) {
            this.status = status;
            this.errorMessage = errorMessage;
        }
    }
}
