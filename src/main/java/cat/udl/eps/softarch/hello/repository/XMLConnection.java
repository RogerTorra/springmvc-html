package cat.udl.eps.softarch.hello.repository;

import cat.udl.eps.softarch.hello.model.Acte;

import org.w3c.dom.Node;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xquery.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.StringWriter;
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

    public XMLConnection() {

    }

    public ArrayList<Acte> getActes(Unmarshaller jaxbUnmarshaller,XQPreparedExpression expr, XQConnection conn) {
        ArrayList<Acte> songs = new ArrayList<Acte>();
        try {
            XQResultSequence rs = expr.executeQuery();


            while (rs.next()) {
                XQItem item = rs.getItem();
                Acte acte = (Acte) jaxbUnmarshaller.unmarshal(item.getNode());
                songs.add(acte);
                System.out.print(acte + "\n");
            }
        }
        catch (Exception e) {
            //log.log(Level.SEVERE, e.getMessage());
        }
        finally { close(expr, conn); }
        return songs;
    }

    private void close(XQPreparedExpression expr, XQConnection conn) {
        try {
            expr.close();
            conn.close();
        } catch (XQException e) {
            // log.log(Level.SEVERE, e.getMessage());
        }
    }

    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
            System.out.println("nodeToString Transformer Exception");
        }
        return sw.toString();
    }
}
