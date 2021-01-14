package Converter;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Remote
@Stateful(name = "ConverterEjbEJB")
public class ConverterEjbBean implements IConverter{
    public ConverterEjbBean() {
    }
    //Map<String,Double> codeToRate = new HashMap<>();
    Map<Monnaie,Double> allData = new HashMap<>();

    @Override
    public Double euroToOtherCurrency(double amount,Double rate) {
        try{
            return  amount *  rate;
        }catch (Exception e ){
            return null;
        }
    }

    public Map<String,Double> getAllCodeCurrencyRate(){
        Map<String,Double> codeToRate = new HashMap<>();
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

    @Override
    public Map<Monnaie, Double> getDataMonnaie(Double monnaie)throws IOException, JDOMException{
        Map<String,Double> codeToRate = getAllCodeCurrencyRate();
        SAXBuilder sxb = new SAXBuilder();
        URL url = new URL("https://www.currency-iso.org/dam/downloads/lists/list_one.xml");
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        Document document = sxb . build (con.getInputStream());
        Element racine = document.getRootElement ( ) ;
        List<Element> elem = racine.getChild ( "CcyTbl" ).getChildren();
        elem.forEach(e -> {
            if(e.getChild("Ccy") != null){
                String currency = e.getChild("Ccy").getValue();
                if(codeToRate.containsKey(currency)){
                    List<String> namePays = new ArrayList<>();
                    namePays.add(e.getChild("CtryNm").getValue());
                    Monnaie m = new Monnaie(namePays,e.getChild("CcyNm").getValue(),currency,codeToRate.get(currency));
                    allData.put(m, euroToOtherCurrency(monnaie,m.getTauxChange()));
                }
            }
        });
        return allData;
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
