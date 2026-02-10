package org.example.service;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Topic;
import java.util.logging.Logger;

@Stateless
public class UserProducer {

    @Resource(lookup = "java:/JmsXA")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/topic/UserCreatedTopic")
    private Topic topic;

    private static final Logger LOGGER = Logger.getLogger(UserProducer.class.getName());

    public void sendMessageWhenUserCreated(String message) {
        try (JMSContext jmsContext = connectionFactory.createContext()){
                LOGGER.info("Sending message to the Topic: " + topic.getTopicName());
                jmsContext.createProducer().send(topic, message);
                LOGGER.info("Awesome!! message sent to the Topic: " + topic.getTopicName());
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
