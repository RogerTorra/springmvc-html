package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Acte;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xquery.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by roger on 29/12/2014.
 */
public class XMLConnection {
    private static final Logger log = Logger.getLogger(XMLConnection.class.getName());

    private XQPreparedExpression expr;
    private XQConnection conn;

    private JAXBContext jaxbContext;
    private Unmarshaller jaxbUnmarshaller;

    public XMLConnection(String xquery, URL url)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, XQException, IOException, JAXBException {
        URLConnection urlconn = url.openConnection();
        urlconn.setReadTimeout(50000);

        XQDataSource xqds = (XQDataSource) Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
        this.conn = xqds.getConnection();
        this.expr = conn.prepareExpression(xquery);
        this.expr.bindDocument(new javax.xml.namespace.QName("doc"), urlconn.getInputStream(), null, null);

        this.jaxbContext = JAXBContext.newInstance(Acte.class);
        this.jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    }

    public ArrayList<Acte> getEvents() {
        ArrayList<Acte> actes = new ArrayList<Acte>();
        try {
            XQResultSequence rs = this.expr.executeQuery();
            while (rs.next()) {
                XQItem item = rs.getItem();
                Acte acte = (Acte) jaxbUnmarshaller.unmarshal(item.getNode());
                actes.add(acte);
            }
        }
        catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
        }
        finally { close(); }
        return actes;
    }

    private void close() {
        try {
            this.expr.close();
            this.conn.close();
        } catch (XQException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }
}
