package cat.udl.eps.softarch.hello;
import cat.udl.eps.softarch.hello.repository.XMLConnection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import javax.validation.constraints.AssertTrue;
import javax.xml.xquery.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.io.*;
/**
 * Created by roger on 17/12/2014.
 */
public class XMLConnectionTest {

    public URL url;
    private static final int BUFFER_SIZE = 8192;

    public static long copy(InputStream is, OutputStream os) {
        byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        int len = 0;
        try {
            while (-1 != (len = is.read(buf))) {
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
        OutputStream testFile = new FileOutputStream("C:\\Users\\roger\\IdeaProjects\\springmvc-html\\src\\test\\java\\cat\\udl\\eps\\softarch\\hello\\testXML.xml");
        copy(xml,testFile);
    }

    @Test
    public void stepOneTest() throws IOException, XQException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String eventXQ =
                " declare variable $doc external;\n" +
                "for $x in $doc return $x//acte/nom/text()";

        url = new URL("http://w10.bcn.es/APPS/asiasiacache/peticioXmlAsia?id=203");

        URLConnection urlconn = url.openConnection();
        urlconn.setReadTimeout(50000);
        InputStream xml = urlconn.getInputStream();
        //TODO Change local path file
        OutputStream testFile = new FileOutputStream("C:\\Users\\roger\\IdeaProjects\\springmvc-html\\src\\test\\java\\cat\\udl\\eps\\softarch\\hello\\testXML.xml");
        copy(xml,testFile);

        XMLConnection testcon = new XMLConnection(eventXQ, url);
        ArrayList<String> result = testcon.getEvents();
        assertEquals(54,result.size());
        System.out.println("test:");
        System.out.println(result.size());
    }

    @Test
    public void staticFileTest() throws ClassNotFoundException, IllegalAccessException, InstantiationException, XQException, FileNotFoundException {

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
        System.out.println("test:");
        while(rs.next())
            System.out.println(rs.getItemAsString(null));
        assertNotNull(rs);
        conn.close();
    }
}
