package cat.udl.eps.softarch.hello.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.model.Greeting;
import cat.udl.eps.softarch.hello.repository.ActesRepository;
import cat.udl.eps.softarch.hello.repository.GreetingRepository;
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
public class ActesController {
    final Logger logger = LoggerFactory.getLogger(ActesController.class);

    @Autowired
    ActesRepository actesRepository;
    private JAXBContext jaxbContext;
    private Unmarshaller jaxbUnmarshaller;
    private void retriveXMLEvent(){
        try {

            XQPreparedExpression expr;
            XQConnection conn;
            //TODO Change local path file
            InputStream testFile = new FileInputStream("C:\\Users\\roger\\IdeaProjects\\springmvc-html\\src\\test\\java\\cat\\udl\\eps\\softarch\\hello\\testXML.xml");
            String xqueryString =
                    " declare variable $doc external;\n" +
                            "for $x in $doc return $x//acte/nom/text()";

            XQDataSource xqds = (XQDataSource)Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
            conn = xqds.getConnection();
            expr = conn.prepareExpression(xqueryString);
            expr.bindDocument(new javax.xml.namespace.QName("doc"), testFile, null, null);

            XQResultSequence rs = expr.executeQuery();

            while (rs.next()) {
                XQItem item = rs.getItem();
                Acte acte = null;
                    acte = (Acte) jaxbUnmarshaller.unmarshal(item.getNode());
                    actesRepository.save(acte).getId();

            }
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

    }
// LIST
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public Iterable<Acte> list(@RequestParam(required=false, defaultValue="0") int page,
                                   @RequestParam(required=false, defaultValue="10") int size) {
        retriveXMLEvent();
        PageRequest request = new PageRequest(page, size);
        return actesRepository.findAll(request).getContent();
    }
    @RequestMapping(method=RequestMethod.GET, produces="text/html")
    public ModelAndView listHTML(@RequestParam(required=false, defaultValue="0") int page,
                                 @RequestParam(required=false, defaultValue="10") int size) {
        return new ModelAndView("actes", "actes", list(page, size));
    }

// RETRIEVE
    @RequestMapping(value = "/{id}", method = RequestMethod.GET )
    @ResponseBody
    public Acte retrieve(@PathVariable( "id" ) Long id) {
        logger.info("Retrieving acte number {}", id);
        Preconditions.checkNotNull(actesRepository.findOne(id), "Acte with id %s not found", id);
        return actesRepository.findOne(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView retrieveHTML(@PathVariable( "id" ) Long id) {
        return new ModelAndView("acte", "acte", retrieve(id));
    }

// CREATE
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Acte create(@Valid @RequestBody Acte acte, HttpServletResponse response) {
        logger.info("Creating acte with nom'{}'", acte.getNom());
        response.setHeader("Location", "/actes/" + actesRepository.save(acte).getId());
        return acte;
    }
    @RequestMapping(method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded", produces="text/html")
    public String createHTML(@Valid @ModelAttribute("acte") Acte acte, BindingResult binding, HttpServletResponse response) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/actes/"+create(acte, response).getId();
    }
    // Create form
    @RequestMapping(value = "/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView createForm() {
        logger.info("Generating form for actes creation");
        Acte emptyActe = new Acte();
        emptyActe.setDate(new Date().toString());
        return new ModelAndView("form", "acte", emptyActe);
    }

// UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Acte update(@PathVariable("id") Long id, @Valid @RequestBody Acte acte) {
        logger.info("Updating acte {}, new name is '{}'", id, acte.getNom());
        Preconditions.checkNotNull(actesRepository.findOne(id), "Acte with id %s not found", id);
        Acte updateActe = actesRepository.findOne(id);
        updateActe.setNom(acte.getNom());
        updateActe.setDate(acte.getDate());
        return actesRepository.save(updateActe);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/x-www-form-urlencoded")
    @ResponseStatus(HttpStatus.OK)
    public String updateHTML(@PathVariable("id") Long id, @Valid @ModelAttribute("acte") Acte acte,
                         BindingResult binding) {
        if (binding.hasErrors()) {
            logger.info("Validation error: {}", binding);
            return "form";
        }
        return "redirect:/actes/"+update(id, acte).getId();
    }
    // Update form
    @RequestMapping(value = "/{id}/form", method = RequestMethod.GET, produces = "text/html")
    public ModelAndView updateForm(@PathVariable("id") Long id) {
        logger.info("Generating form for updating acte number {}", id);
        Preconditions.checkNotNull(actesRepository.findOne(id), "Acte with id %s not found", id);
        return new ModelAndView("form", "acte", actesRepository.findOne(id));
    }

// DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Long id) {
        logger.info("Deleting acte number {}", id);
        Preconditions.checkNotNull(actesRepository.findOne(id), "Acte with id %s not found", id);
        actesRepository.delete(id);
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    @ResponseStatus(HttpStatus.OK)
    public String deleteHTML(@PathVariable("id") Long id) {
        delete(id);
        return "redirect:/actes";
    }
}
