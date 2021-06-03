package log;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class ConsumerQueue {

    @SuppressWarnings("resourse")
    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory)context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();

        connection.start();

        // --- It needs to be true to works in transaction way
        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        /*
        ------ It works with client confirmation
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

        ------ Client works with duplications
        Session session = connection.createSession(true, Session.DUPS_OK_ACKNOWLEDGE);
        */

        Destination queue = (Destination) context.lookup("log");
        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener(new MessageListener(){

            @Override
            public void onMessage(Message message){
                TextMessage textMessage  = (TextMessage)message;
                try{
                    //message.acknowledge();
                    System.out.println(textMessage.getText());
                    session.commit();
                } catch(JMSException e){
                    e.printStackTrace();
                }
            }

        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();

    }
}
