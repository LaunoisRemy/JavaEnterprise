package Converter;

import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import javax.ejb.Remote;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Remote
public interface IConverter {
    Double euroToOtherCurrency(double amount,Double rate);
    Map<String,Double> getAllCodeCurrencyRate();
    Map<Monnaie, Double> getDataMonnaie(Double monnaie)throws IOException, JDOMException;
}
