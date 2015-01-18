package cat.udl.eps.softarch.hello.controller;

import cat.udl.eps.softarch.hello.model.Event;
import cat.udl.eps.softarch.hello.model.Favorite;
import cat.udl.eps.softarch.hello.model.User;
import cat.udl.eps.softarch.hello.repository.FavoriteRepository;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/favorite")
public class FavoriteController {
    final Logger logger = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    FavoriteRepository favoriteRepository;
    JAXBContext  jaxbContext;
    Unmarshaller jaxbUnmarshaller;


    // LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Favorite> list(@RequestParam(required=false, defaultValue="0") int page,
                               @RequestParam(required=false, defaultValue="10") int size,
                               @RequestParam(required=false, defaultValue="10") User user) {


        return favoriteRepository.findFavoritesByUser(user);
    }
    @RequestMapping(method=RequestMethod.GET, produces="text/html")
    public ModelAndView listHTML(@RequestParam(required=false, defaultValue="0") int page,
                                 @RequestParam(required=false, defaultValue="10") int size,
                                 @RequestParam(required=false, defaultValue="10") User user) {
        return new ModelAndView("actes", "actes", list(page, size,user));
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

    // CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Favorite create(@Valid @RequestBody Favorite favorite, HttpServletResponse response) {
        logger.info("Creating acte with nom'{}'", favorite.toString());
        response.setHeader("Location", "/favorite/" + favoriteRepository.save(favorite).toString());
        return favorite;
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("acte") Favorite favorite, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/actes/"+create(favorite, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for favorite creation");
        Event emptyEvent = new Event();
        emptyEvent.setInit_date(new Date().toString());
        return new ModelAndView("form", "favorite", emptyEvent);
    }

    // UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Favorite update(@PathVariable("id") Long id, @Valid @RequestBody Favorite favorite) {
        logger.info("Updating acte {}, new name is '{}'", id, favorite.toString());
        Preconditions.checkNotNull(favoriteRepository.findOne(id), "Acte with id %s not found", id);
        Favorite updateFavorite = favoriteRepository.findOne(id);
        updateFavorite.setEvent(favorite.getEvent());
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
}
