package org.example.jms;


import javax.ejb.Singleton;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.logging.Logger;


@Singleton
public class FirstQueue {


    private static final Logger LOGGER = Logger.getLogger(FirstQueue.class.getName());


    public void createInitialQueue(String message){

        InitialContext initialContext = null;
        Connection connection = null;
        Session session = null;

        try {
            LOGGER.info("Creating Context Queue configuration ...");

            initialContext = new InitialContext();
            ConnectionFactory cf = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            Queue queue = (Queue) initialContext.lookup("queue/myQueue");

            connection = cf.createConnection();
            session = connection.createSession();
            LOGGER.info("Creating Context Queue configuration ...");


            MessageProducer messageProducer = session.createProducer(queue);
            TextMessage textMessage = session.createTextMessage(message);

            LOGGER.info("Sending Message ...");
            messageProducer.send(textMessage);
            LOGGER.info("Message sent!!");


            MessageConsumer messageConsumer = session.createConsumer(queue);
            connection.start();

            TextMessage messageReceived = (TextMessage) messageConsumer.receive(5000);
            LOGGER.info("Message received ->  " + messageReceived.getText());

        }catch (NamingException | JMSException e) {
            throw new RuntimeException(e.getMessage());
        }finally {


            try {
                if(initialContext !=null) {
                    try {
                        initialContext.close();
                    }catch (NamingException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
                connection.close();
                session.close();
            }catch (JMSException e) {
                throw new RuntimeException(e.getMessage());
            }

        }
    }

}
