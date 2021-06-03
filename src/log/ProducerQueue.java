package log;

import javax.jms.*;
import javax.naming.InitialContext;

public class ProducerQueue {

    @SuppressWarnings("resourse")
    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory)context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();

        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination queue = (Destination) context.lookup("log");
        MessageProducer producer = session.createProducer(queue);


        Message message = session.createTextMessage("<pessoa><id>12</id></pessoa>");
        //producer.send(message, Delivery mode, priority, time to live)
        //To confirm the priority we have to change the ActiveMQ configuration
        // with <policyEntry queue=">" prioritizedMessages="true"/>
        producer.send(message, DeliveryMode.NON_PERSISTENT, 3, 5000);


        session.close();
        connection.close();
        context.close();

    }
}
