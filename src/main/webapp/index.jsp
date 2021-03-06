<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Collections" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.jms.*" %>
<%@ page import="javax.naming.NamingException" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>JSP - Converter - Remy</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
</head>
<body>

<div class="container">
    <h1> Converter Euro </h1>
    <jsp:useBean id="convert" scope="session" class="Converter.ConverterEjbBean" />
    <form method="get" action="index.jsp">
        <div class="form-group">
            <label for="amount">Enter your amount :</label>
            <input type="text" class="form-control" id="amount" name="amount" aria-describedby="Enter amount" placeholder="Enter amount">
        </div>
        <div>
            <%
                List<String> listCurrency = new ArrayList<>(convert.getAllCodeCurrencyRate().keySet());
                Collections.sort(listCurrency);
            %>
            <select id="currency" name="currency" class="form-select form-select-lg mb-3" aria-label=".form-select-lg example">
                <option value = null selected>Chose your currency</option>
                <%
                    for (String currency: listCurrency
                    ) {
                %>
                <option id=<%=currency%> value=<%=currency%>><%=currency%></option>
                <%
                    }
                %>
            </select>

        </div>
        <input type="text" class="form-control" id="email" name="email" aria-describedby="Enter your email" placeholder="Enter your email">
        <button type="submit" class="btn btn-primary">Let's go</button>
    </form>
    <%
        String amount = request.getParameter("amount");
        String currency = request.getParameter("currency");
        String email = request.getParameter("email");
        if(amount != null && currency != null &&amount.length()>0 && !amount.equals("null") && !currency.equals("null")){
        Double d = new Double(amount);
        Double converti = convert.euroToOtherCurrency(d,convert.getAllCodeCurrencyRate().get(currency));
        if(converti != null){
    %>
    <p>
        <%= amount%> euros =
        <%= converti %> of <%=currency%>
    </p>
    <%
            }
        if(email !=null && email.length() != 0){
            try {
                Context jndiContext = null;
                jndiContext = new InitialContext();

                javax.jms.ConnectionFactory connectionFactory =
                    (QueueConnectionFactory)jndiContext.lookup("/ConnectionFactory");


            Connection connection = connectionFactory.createConnection();

            Session sessionQ =
                    connection.createSession(false,Session.AUTO_ACKNOWLEDGE);


            TextMessage message = sessionQ.createTextMessage();

            message.setText(amount+"#"+email);

            javax.jms.Queue queue =
                    (javax.jms.Queue) jndiContext.lookup("queue/MailContent");

            MessageProducer messageProducer=sessionQ.createProducer(queue);


                messageProducer.send(message);
            } catch (JMSException | NamingException e) {
                e.printStackTrace();
            }
    %>
    <p>
        <%= email%>
    </p>
    <%
        }
        }
    %>

</div>
</body>
</html>