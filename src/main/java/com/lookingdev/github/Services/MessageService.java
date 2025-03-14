package com.lookingdev.github.Services;

import com.lookingdev.github.Domain.Enums.QueueAction;
import com.lookingdev.github.Domain.Models.DeveloperDTOModel;
import com.lookingdev.github.Domain.Models.MessageModel;
import com.lookingdev.github.Domain.Models.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class MessageService {

    @Value("${queueGitStatus.name}")
    private String queueGitModel;

    @Value("${queueGitStatus.init.name}")
    private String queueGitInitStatus;

    @Value("${queueGitSagaChain.name}")
    private String queueGitSagaChain;


    private String initStatus = "Wait...";

    private final ProfileProcessing profileService;
    private final RabbitTemplate rabbitTemplate;

    private CountDownLatch latch;
    private List<DeveloperDTOModel> devModels;

    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);
    private static final int LIMIT_USERS = 5;

    @Autowired
    public MessageService(ProfileProcessing profileService, RabbitTemplate rabbitTemplate) {
        this.profileService = profileService;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Method returns initialization status of GitHub microservice
     */
    public void getInitStatus() {
        MessageStatus messageWithStatus = new MessageStatus();
        messageWithStatus.setAction(QueueAction.GET_INIT_STATUS_GIT);
        messageWithStatus.setStatus(initStatus);

        sendStatusInQueue(queueGitInitStatus, messageWithStatus);
    }

    /**
     * Method for init database with data
     */
    public void initDatabase() {
        initStatus = "In Progress"; // Set status before start
        profileService.initUsers().thenRun(() -> {
            initStatus = "Done"; // Change status after finish
            logger.info("Users have been initiated");
        }).exceptionally(ex -> {
            initStatus = "Failed"; // If we get error, then set failed status
            logger.error("Error with init users {}", ex.getMessage());
            return null;
        });
    }

    //TODO
    public void getAllUsers(MessageStatus messageStatus) {
        try {
            if (messageStatus.getAction().equals(QueueAction.GET_ALL)) {

                latch = new CountDownLatch(1);

                requestStackUsers(messageStatus);

                boolean received = latch.await(5, TimeUnit.SECONDS); // wait 5 seconds until we get the status

                if (!received) {
                    logger.warn("Status not received in time");
                }
                //TODO доделать: сделать получение 5 юзеров с гит хаба, обьеденить их с получеными юзерами и отправить 10 юзиров в апи

                logger.info("User was sent in queue");
            }
        } catch (Exception ex) {
            logger.error("Error with get gitHub users {}", String.valueOf(ex));
        }
    }

    /**
     * Method get gitHub users from DB and return them to API microservice
     * @param messageStatus received status from API
     */
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
     * Method send request to Stack Overflow microservice for get those users
     *
     * @param messageStatus received status from API
     */
    private void requestStackUsers(MessageStatus messageStatus) {
        //Parsing message for get last entity index
        int lastIndex = (Integer.parseInt(messageStatus.getStatus())) * 5;

        MessageStatus message = new MessageStatus(QueueAction.GET_STACK_USER, lastIndex + "");

        sendStatusInQueue(queueGitSagaChain, message);

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

    /* Listeners */
    /* for Stack Overflow */
    @RabbitListener(queues = "StackOverflowStatusQueue")
    public void receiveStackUsers(MessageModel messageWithData){

        if (messageWithData != null && messageWithData.getAction().equals(QueueAction.GET_ALL) && !messageWithData.getDeveloperProfiles().isEmpty()) {
            devModels = messageWithData.getDeveloperProfiles();
            latch.countDown();
            logger.info("Developers was got: {}", messageWithData.getDeveloperProfiles());
        }
    }
}
