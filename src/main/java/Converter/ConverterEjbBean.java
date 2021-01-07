package Converter;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Stateless(name = "ConverterEjbEJB")
public class ConverterEjbBean implements IConverter{
    public ConverterEjbBean() {
    }
    Map<String,Double> codeToRate = new HashMap<>();

    @Override
    public Double euroToOtherCurrency(double amount, String currencyCode) {
        try{
            return  amount *  codeToRate.get(currencyCode);
        }catch (Exception e ){
            return null;
        }
    }

    public Map<String,Double> getAllCodeCurrencyRate(){
        SAXBuilder sxb = new SAXBuilder();

        try{
            connecToSite(sxb);
            List<Element> elem = connecToSite(sxb);
            elem.forEach(e -> codeToRate.put(e.getAttributeValue("currency"), Double.parseDouble(e.getAttributeValue("rate"))));

        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return codeToRate;
    }

    public List<String> getAllCodeCurrency(){
        SAXBuilder sxb = new SAXBuilder();
        List<String> res = new ArrayList<>();
        try{
            ;
            List<Element> elem = connecToSite(sxb);
            res = elem.stream().map( e ->e.getAttributeValue("currency")).collect(Collectors.toList());
        } catch (IOException | JDOMException e) {
            e.printStackTrace();
        }
        return res;
    }

    private List<Element> connecToSite(SAXBuilder sxb) throws IOException, JDOMException {
        URL url = new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        Document document = sxb . build (con.getInputStream());
        Element racine = document.getRootElement ( ) ;
        Namespace ns = Namespace.getNamespace (
                "http://www.ecb.int/vocabulary/2002-08-01/eurofxref") ;
        return racine.getChild ( "Cube" , ns ).getChild("Cube",ns).getChildren();
    }
}
