package com.lookingdev.github.Services;

import com.lookingdev.github.Domain.Enums.QueueAction;
import com.lookingdev.github.Domain.Models.MessageModel;
import com.lookingdev.github.Domain.Models.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Value("${queueGitStatus.name}")
    private String queueGitModel;

    @Value("${queueGitStatus.init.name}")
    private String queueGitInitStatus;

    private String initStatus = "Wait...";

    private final ProfileProcessing profileService;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);


    @Autowired
    public MessageService(ProfileProcessing profileService, RabbitTemplate rabbitTemplate) {
        this.profileService = profileService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Method returns initialization status of GitHub microservice
     *
     */
    public void getInitStatus(){
        MessageStatus messageWithStatus = new MessageStatus();
        messageWithStatus.setAction(QueueAction.GET_INIT_STATUS_GIT);
        messageWithStatus.setStatus(initStatus);

        sendStatusInQueue(queueGitInitStatus, messageWithStatus);
    }

    /**
     * Method for init database with data
     */
    public void initDatabase() {
        try {
            profileService.initUsers();
            initStatus = "Done";
            logger.info("Users have been initiated");
        } catch (Exception ex) {
            logger.error("Error with init users {}", String.valueOf(ex));
        }
    }

    public void getGitHubUsers(MessageStatus messageStatus) {

        try {
            if (messageStatus.getAction().equals(QueueAction.GET_GIT_DEV)) {
                //Parsing message for get last entity index
                int lastIndex = (Integer.parseInt(messageStatus.getStatus())) * 10;

                MessageModel messageWithData = new MessageModel();
                messageWithData.setAction(QueueAction.GET_GIT_DEV);
                messageWithData.setDeveloperProfiles(profileService.getDevelopersDTO(lastIndex));

                sendDataInQueue(queueGitModel, messageWithData);
                logger.info("User was sent in queue");
            }
        } catch (Exception ex) {
            logger.error("Error with get gitHub users {}", String.valueOf(ex));
        }
    }

    /**
     * Method for sending status message in queue
     *
     * @param queueName queue name
     * @param message   message which be sent in the queue
     */
    private void sendDataInQueue(String queueName, MessageModel message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }

    /**
     * Method for sending status message in queue
     *
     * @param queueName queue name
     * @param message   message which be sent in the queue
     */
    private void sendStatusInQueue(String queueName, MessageStatus message) {
        rabbitTemplate.convertAndSend(queueName, message);
    }
}
