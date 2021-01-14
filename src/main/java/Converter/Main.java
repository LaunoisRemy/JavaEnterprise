package Converter;

import org.jdom2.JDOMException;

import javax.naming.InitialContext;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, JDOMException {
        ConverterEjbBean converterEjbBean = new ConverterEjbBean();
        System.out.println(converterEjbBean.getDataMonnaie(15.0));
//        IConverter converter = (IConverter) InitialContext.doLookup(
//                "ejb:/Converter-1.0-SNAPSHOT/ConverterEjbEJB!Converter.IConverter");

//        ConverterEjbBean converterEjbBean = new ConverterEjbBean();
//        Map<String,Double> currencyRate = converterEjbBean.getAllCodeCurrencyRate();
//        System.out.println("WHat is the ammount in euro you want to convert ?");
//        Scanner sc = new Scanner(System.in);  // Create a Scanner object
//        double amount = 0;
//        String a = sc.nextLine();
//        try {
//            amount = Double.parseDouble(a);
//            System.out.println("In which currency ? You can chose between :  ");
//            List<String> listCurrency = new ArrayList<>(currencyRate.keySet());
//            int i = 0;
//            for (String s: listCurrency
//            ) {
//                System.out.print("\t● "+s);
//                if(i == 10){
//                    System.out.println();
//                    i=0;
//                }else{
//                    i+=1;
//                }
//            }
//            System.out.println("");
//            String curren = sc.nextLine().toUpperCase(Locale.ROOT);
//            if(!listCurrency.contains(curren)){
//                throw new Exception("Not in the lsit you have");
//            }
//            Double d = converterEjbBean.euroToOtherCurrency(amount,curren);
//            System.out.println("Your amount in "+curren +" is :"+d);
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }


    }
}
