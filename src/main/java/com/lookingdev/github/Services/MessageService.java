package com.lookingdev.github.Services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final ProfileProcessing profileService;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);


    @Autowired
    public MessageService(ProfileProcessing profileService){
        this.profileService =profileService;
    }

    /**
     * Method for init database with data
     */
    public void initDatabase(){
        try{
            profileService.initUsers();
            logger.info("Users have been initiated");
        } catch (Exception ex){
            logger.error("Error with init users " + ex);
        }
    }
    public void getGitHubUsers( int lastIndex){
            
    }
}
