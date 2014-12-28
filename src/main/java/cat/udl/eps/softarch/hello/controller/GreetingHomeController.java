package cat.udl.eps.softarch.hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by roger on 08/10/2014.
 */

@Controller
@RequestMapping(value = "/")
public class GreetingHomeController {
    final Logger logger = LoggerFactory.getLogger(GreetingHomeController.class);
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        logger.info("Redirect to home page");
        return "redirect:/actes";
    }

}
