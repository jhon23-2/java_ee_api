package org.example.service;

import org.example.model.UserModel;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Stateless
public class UserEventProducer {

    private static final Logger LOGGER = Logger.getLogger(UserEventProducer.class.getName());

    @Resource(lookup = "java:jboss/DefaultJMSConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/topic/UserCreatedTopic")
    private Topic userCreatedTopic;

    public void sendMessageUserCreated(UserModel userModel) {

        Connection connection = null;
        Session session = null;

       try {
           connection = connectionFactory.createConnection();
           session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

           MessageProducer producer = session.createProducer(userCreatedTopic);

           TextMessage textMessage = session.createTextMessage();
           textMessage.setText(buildUserCreatedMessage(userModel));
           textMessage.setStringProperty("eventType", "USER_CREATED");
           textMessage.setLongProperty("userId", userModel.getId());
           textMessage.setLongProperty("timestamp", System.currentTimeMillis());

           producer.send(textMessage);  
           LOGGER.info("Message sent to the topic: " + userCreatedTopic.getTopicName());

       }catch (Exception e) {
           LOGGER.info("Error sending message to topic: " + e.getMessage());
           e.printStackTrace();
       }finally {
           try {
               if (session != null) session.close();
               if (connection != null) connection.close();
           } catch (JMSException e) {
               LOGGER.info("Error Close session and connection: " + e.getMessage());
               e.printStackTrace();
           }
       }
    }


    private String buildUserCreatedMessage(UserModel userModel) {
        return String.format(
                "{\"eventType\":\"USER_CREATED\"," +
                        "\"userId\":\"%s\"," +
                        "\"username\":\"%s\"," +
                        "\"email\":\"%s\"," +
                        "\"createdAt\":\"%s\"," +
                        "\"timestamp\":\"%s\"}",
                userModel.getId().toString(),
                userModel.getName(),
                userModel.getEmail(),
                LocalDateTime.now().toString(),
                LocalDateTime.now().toString()
        );
    }

}
