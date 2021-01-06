package Converter;

public class Main {
    public static void main(String[] args) {
        ConverterEjbBean converterEjbBean = new ConverterEjbBean();
        Double d = converterEjbBean.euroToOtherCurrency(15,"PLN");
        converterEjbBean.getAllCodeCurrencyRate();
    }
}
