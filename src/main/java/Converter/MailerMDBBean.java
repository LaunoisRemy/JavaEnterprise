package Converter;

import org.jdom2.JDOMException;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.*;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(
                propertyName = "destination",
                propertyValue = "queue/MailContent"),
        @ActivationConfigProperty(
                propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
public class MailerMDBBean implements MessageListener {
    @EJB
    IConverter converter;
    public MailerMDBBean() {
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage mesg = (TextMessage) message;
                String content = mesg.getText();
                // Recuperer le montant a convertir :
                String s = content.substring(0,content.indexOf("#"));
                double amount = Double.parseDouble(s);
                // Demander au bean session de faire toutes les conversions ...
                Map<Monnaie,Double> map = converter.getDataMonnaie(amount);
                Properties p = new Properties();
                p.put("mail.smtp.host", "smtp.gmail.com");
                p.put("mail.smtp.auth", "true");
                p.put("mail.smtp.starttls.enable","true");
                javax.mail.Session session = javax.mail.Session.getInstance(p);
                javax.mail.Message msg = new MimeMessage(session);
                try {
                    // Preparation du mail
                    msg.setFrom(new InternetAddress("<user>@gmail.com"));
                    String dest = content.substring(content.indexOf("#")+1);
                    msg.setRecipient(javax.mail.Message.RecipientType.TO,
                            new InternetAddress(dest));
                    String sujet = "Conversions de monnaie";
                    msg.setSubject(sujet);
                    // Mettre en forme les resultats retournes par le bean session (Map)
                    // dans une chaine de caracteres contenant les balises HTML
                    // necessaires pour construire le tableau HTML (variable content)
                    // Voir la capture d'ecran de la Figure 1
                    StringBuilder builder = new StringBuilder();
                    builder.append("<table>")
                            .append("<thead>")
                            .append("<tr><th>Currency</th><th>Actual rate</th><th>Converted amount</th>")
                            .append("</thead>")
                            .append("<tbody>");
                    map.forEach((money, convertedAmount) ->
                    {
                        builder.append("<tr><td>")
                                .append(money.getCodeMonnaie()).append(" (").append(money.getNameMonnaie()).append(")")
                                .append("</td><td>")
                                .append(money.getTauxChange())
                                .append("</td><td>")
                                .append(convertedAmount)
                                .append("</td></tr>");
                    });
                    content+= "/n"+builder;
                    builder.append("</table>");
                    msg.setContent(content,"text/html;charset=utf8");
                    msg.setSentDate(Calendar.getInstance().getTime());
                    // Preparation de l'envoi du mail
                    Transport transport = session.getTransport("smtp");
                    Properties mails = PropertiesResources.getMailProperties();
                    transport.connect("smtp.gmail.com",587,mails.getProperty("ADDRESS"),mails.getProperty("PASSWORD"));
                    // Envoi du mail
                    transport.sendMessage(msg,msg.getAllRecipients());
                    transport.close();
                    System.out.println("Email envoye a "+dest);
                }
                catch(MessagingException e){e.printStackTrace();}
            }
        } catch (JMSException | JDOMException | IOException ex) {ex.printStackTrace();}

    }
}
