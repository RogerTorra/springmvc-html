package cat.udl.eps.softarch.hello;
import cat.udl.eps.softarch.hello.model.Acte;
import cat.udl.eps.softarch.hello.repository.XMLConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import javax.validation.constraints.AssertTrue;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xquery.*;
import net.sf.saxon.xqj.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.io.*;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by roger on 17/12/2014.
 */
public class XMLConnectionTest {

    public URL url;
    private static final int BUFFER_SIZE = 8192;
    private String testXmlRoute = "/home/hellfish90/IdeaProjects/springmvc-html/src/test/java/cat/udl/eps/softarch/hello/testXML.xml" ;

    public static long copy(InputStream is, OutputStream os) {
        byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        int len = 0;
        try {
            while (-1 != (len = is.read(buf))) {
                //System.out.print(buf);
                os.write(buf, 0, len);
                total += len;
            }
        } catch (IOException ioe) {
            throw new RuntimeException("error reading stream", ioe);
        }

        return total;
    }


    @Test
    public void urlConnectionTest() throws Exception{



        url = new URL("http://w10.bcn.es/APPS/asiasiacache/peticioXmlAsia?id=203");
        URLConnection urlconn = url.openConnection();
        urlconn.setReadTimeout(50000);
        InputStream xml = urlconn.getInputStream();
        //TODO Change local path file
        OutputStream testFile = new FileOutputStream(testXmlRoute);
        //copy(xml,testFile);
    }

    @Test
    public void stepOneTest() throws IOException, XQException, ClassNotFoundException, InstantiationException, IllegalAccessException, JAXBException {
        String eventXQ =
                " declare variable $doc external;\n" +
                        "for $x in $doc return $x//acte/nom/text()";

        url = new URL("http://w10.bcn.es/APPS/asiasiacache/peticioXmlAsia?id=203");

        URLConnection urlconn = url.openConnection();
        urlconn.setReadTimeout(50000);
        InputStream xml = urlconn.getInputStream();
        //TODO Change local path file
        OutputStream testFile = new FileOutputStream(testXmlRoute);
        copy(xml,testFile);

        XMLConnection testcon = new XMLConnection(eventXQ, url);
        ArrayList<Acte> result = testcon.getEvents();
        System.out.println("test:");
        System.out.println(result.size());
        assertEquals(54,result.size());

    }

    //@Test
    public void staticFileTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException, XQException, FileNotFoundException {

        XQPreparedExpression expr;
        XQConnection conn;
        //TODO Change local path file
        InputStream testFile = new FileInputStream(testXmlRoute);
        String xqueryString =
                " declare variable $doc external;\n" +
                        "for $x in $doc return $x//acte/nom/text()";

        XQDataSource xqds = (XQDataSource)Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
        conn = xqds.getConnection();
        expr = conn.prepareExpression(xqueryString);
        expr.bindDocument(new javax.xml.namespace.QName("doc"), testFile, null, null);

        XQResultSequence rs = expr.executeQuery();
        System.out.println("test:");
        while(rs.next())
            System.out.println(rs.getItemAsString(null));
        assertNotNull(rs);
        conn.close();
    }


    @Test
    public void xqueryTest() throws IOException, ClassNotFoundException, XQException, IllegalAccessException, InstantiationException, JAXBException {

        XQPreparedExpression expr;
        XQConnection         conn;

        JAXBContext          jaxbContext;
        Unmarshaller jaxbUnmarshaller;

        String xquery =
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

        XQDataSource xqds = (XQDataSource) Class.forName("net.sf.saxon.xqj.SaxonXQDataSource").newInstance();
        conn = xqds.getConnection();
        expr = conn.prepareExpression(xquery);
        expr.bindDocument(new javax.xml.namespace.QName("doc"), urlconn.getInputStream(), null, null);

        jaxbContext = JAXBContext.newInstance(Acte.class);
        jaxbUnmarshaller = jaxbContext.createUnmarshaller();


        OutputStream testFile = new FileOutputStream(testXmlRoute);

        List<Acte> actes= getActes(jaxbUnmarshaller,expr,conn);

        //System.out.print(getActes(jaxbUnmarshaller,expr,conn));

        assertTrue(!actes.isEmpty());
    }



    ArrayList<Acte> getActes(Unmarshaller jaxbUnmarshaller,XQPreparedExpression expr, XQConnection conn) {
        ArrayList<Acte> songs = new ArrayList<Acte>();
        try {
            XQResultSequence rs = expr.executeQuery();


            while (rs.next()) {
                XQItem item = rs.getItem();
                // System.out.print(item);
                Acte acte = (Acte) jaxbUnmarshaller.unmarshal(item.getNode());
                songs.add(acte);
                System.out.print(acte+"\n");
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
}