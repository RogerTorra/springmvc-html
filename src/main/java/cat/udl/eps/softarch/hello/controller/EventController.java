package cat.udl.eps.softarch.hello.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import cat.udl.eps.softarch.hello.model.Event;
import cat.udl.eps.softarch.hello.repository.EventRepository;
import cat.udl.eps.softarch.hello.repository.XMLConnection;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xquery.*;

/**
 * Created by http://rhizomik.net/~roberto/
 */

@Controller
@RequestMapping(value = "/actes")
public class EventController {
    final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    EventRepository eventRepository;
    JAXBContext  jaxbContext;
    Unmarshaller jaxbUnmarshaller;
    private int retriveXMLEvent(){
        int count=0;
        try {

            URL url;
            XQPreparedExpression expr;
            XQConnection conn;
            //TODO Change local path file
            //InputStream testFile = new FileInputStream("/home/hellfish90/IdeaProjects/springmvc-html/src/test/java/cat/udl/eps/softarch/hello/testXML.xml");
            String xqueryString =
                    "declare variable $doc  external;\n"
                            + "for $r in $doc /response/body/resultat/actes/* \n"
                            +"order by $r\n"
                            + "return\n"
                            + "<acte>\n"
                            + "  <id>{$r/id/text()}</id>\n"
                            + "  <name>{$r/nom/text()}</name>\n"
                            + "  <init_date>{$r/data/data_inici/text()}</init_date>\n"
                            + "  <start_time>{$r/data/hora_inici/text()}</start_time>\n"
                            + "  <type>{$r/classificacions/nivell/text()}</type>\n"
                            + "  <localization>{$r/lloc_simple/nom/text()}</localization>\n"
                            + "  <street>{$r/lloc_simple/adreca_simple/carrer/text()}</street>\n"
                            + "  <street_num>{$r/lloc_simple/adreca_simple/numero/text()}</street_num>\n"
                            + "  <district>{$r/lloc_simple/adreca_simple/districte/text()}</district>\n"
                            + "  <CP>{$r/lloc_simple/adreca_simple/codi_postal/text()}</CP>\n"
                            + "  <x>{$r/lloc_simple/adreca_simple/coordenades/geocodificacio/data(@x)}</x>\n"
                            + "  <y>{$r/lloc_simple/adreca_simple/coordenades/geocodificacio/data(@y)}</y>\n"
                            + "</acte>";


            url = new URL("http://w10.bcn.es/APPS/asiasiacache/peticioXmlAsia?id=203");

            URLConnection urlconn = url.openConnection();
            urlconn.setReadTimeout(50000);

            XQDataSource xqds = (XQDataSource)Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
            conn = xqds.getConnection();
            expr = conn.prepareExpression(xqueryString);
            expr.bindDocument(new javax.xml.namespace.QName("doc"), urlconn.getInputStream(), null, null);

            XQResultSequence rs = expr.executeQuery();
            XMLConnection xmlPars = new XMLConnection();
            jaxbContext = JAXBContext.newInstance(Event.class);
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            for (Event event : xmlPars.getActes(jaxbUnmarshaller,expr,conn)) {
                eventRepository.save(event).getId();
                count++;

            }
            return count;
        } catch (XQException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return 0;
    }
    // LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Event> list(@RequestParam(required=false, defaultValue="0") int page,
                               @RequestParam(required=false, defaultValue="10") int size) {
        size = retriveXMLEvent();
        PageRequest request = new PageRequest(page, size);
        return eventRepository.findAll(request).getContent();
    }
    @RequestMapping(method=RequestMethod.GET, produces="text/html")
    public ModelAndView listHTML(@RequestParam(required=false, defaultValue="0") int page,
                                 @RequestParam(required=false, defaultValue="10") int size) {
        return new ModelAndView("actes", "actes", list(page, size));
    }

    // RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    @ResponseBody
    public Event retrieve(@PathVariable( "id" ) Long id) {
        logger.info("Retrieving acte number {}", id);
        Preconditions.checkNotNull(eventRepository.findOne(id), "Acte with id %s not found", id);
        return eventRepository.findOne(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("acte", "acte", retrieve(id));
    }

    // CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Event create(@Valid @RequestBody Event event, HttpServletResponse response) {
        logger.info("Creating acte with nom'{}'", event.getName());
        response.setHeader("Location", "/actes/" + eventRepository.save(event).getId());
        return event;
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("acte") Event event, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/actes/"+create(event, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for actes creation");
        Event emptyEvent = new Event();
        emptyEvent.setInit_date(new Date().toString());
        return new ModelAndView("form", "acte", emptyEvent);
    }

    // UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Event update(@PathVariable("id") Long id, @Valid @RequestBody Event event) {
        logger.info("Updating acte {}, new name is '{}'", id, event.getName());
        Preconditions.checkNotNull(eventRepository.findOne(id), "Acte with id %s not found", id);
        Event updateEvent = eventRepository.findOne(id);
        updateEvent.setName(event.getName());
        updateEvent.setInit_date(event.getInit_date());
        return eventRepository.save(updateEvent);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.OK)
    public String updateHTML(@PathVariable("id") Long id, @Valid @ModelAttribute("acte") Event event,
                             BindingResult binding) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/actes/"+update(id, event).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Generating form for updating acte number {}", id);
        Preconditions.checkNotNull(eventRepository.findOne(id), "Acte with id %s not found", id);
        return new ModelAndView("form", "acte", eventRepository.findOne(id));
    }

    // DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting acte number {}", id);
        Preconditions.checkNotNull(eventRepository.findOne(id), "Acte with id %s not found", id);
        eventRepository.delete(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        delete(id);
        return "redirect:/actes";
    }
}
