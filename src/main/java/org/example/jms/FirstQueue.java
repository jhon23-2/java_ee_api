package org.example.jms;


import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

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
    public void createQueue(String message) throws NamingException {
            InitialContext initialContext = null;
            Queue queue = null;

            try (
                    ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                    JMSContext context = cf.createContext()
            ){

                initialContext = new InitialContext();
                queue = (Queue) initialContext.lookup("queue/myQueue");

                JMSProducer producer = context.createProducer();

                String messages[]  = new String[3];

                messages[0] = "Jhonattan";
                messages[1] = "Jesus";
                messages[2] = "Sultan";

                LOGGER.info("Set priority and sending messages...");

                producer.setPriority(1);
                producer.send(queue, messages[0]);

                producer.setPriority(9);
                producer.send(queue, messages[1]);

                producer.setPriority(2);
                producer.send(queue, messages[2]);

                LOGGER.info("All messages sent successfully!");

                JMSConsumer consumer = context.createConsumer(queue);

                for (int i =0 ; i<messages.length; i++){
                    Message messageReceived = consumer.receive();
                    LOGGER.info("Message received is: " + messageReceived.getBody(String.class));
                    LOGGER.info("Priority message: " + messageReceived.getJMSPriority());
                }

                LOGGER.info("Messages received !!");


            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }
    public void createAnDropQueue(String message) {
        InitialContext initialContext = null;
        Queue queue = null;
        Queue expiredQueue =  null;

        try (
                ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
                JMSContext jmsContext = cf.createContext();
                ){
            initialContext = new InitialContext();
            queue = (Queue) initialContext.lookup("queue/myQueue");
            expiredQueue = (Queue) initialContext.lookup("queue/ExpiryQueue");

            JMSProducer jmsProducer = jmsContext.createProducer();
            jmsProducer.setTimeToLive(2000);
            jmsProducer.send(queue, message);

            Thread.sleep(5000);
            Message messageReceived = jmsContext.createConsumer(queue).receive(5000);
            LOGGER.info("Receiving Message:  " + messageReceived);

            JMSConsumer jmsConsumer = jmsContext.createConsumer(expiredQueue);
            LOGGER.info("Message come back: " + jmsConsumer.receiveBody(String.class));


        } catch (NamingException  | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
