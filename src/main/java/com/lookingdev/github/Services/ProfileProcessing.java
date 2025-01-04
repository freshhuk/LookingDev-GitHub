package com.lookingdev.github.Services;

import com.lookingdev.github.Domain.Entities.DeveloperProfile;
import com.lookingdev.github.Domain.Models.DeveloperDTOModel;
import com.lookingdev.github.Repositories.DeveloperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileProcessing {

    private final DeveloperRepository repository;
    private final GitHubService gitHubService;

    /* CONSTANTS */
    private static final int USER_COUNT_IN_DB = 100;
    private static final int LIMIT_USERS = 10;
    /* Logger */
    private static final Logger logger = LoggerFactory.getLogger(ProfileProcessing.class);


    @Autowired
    public ProfileProcessing(DeveloperRepository repository, GitHubService gitHubService) {
        this.repository = repository;
        this.gitHubService = gitHubService;
    }


    /**
     * Method gets users from gitHub and sets they in database
     */
    public void initUsers() {
        List<DeveloperDTOModel> users = gitHubService.fetchMultipleUsers(USER_COUNT_IN_DB);
        for (DeveloperDTOModel user : users) {
            repository.add(user);
        }
        logger.info("Users was added in db");
    }

    public List<DeveloperDTOModel> getDevelopersDTO(int lastIndex) {
        List<DeveloperProfile> profiles = repository.getDevelopers(LIMIT_USERS, lastIndex);
        if (profiles == null || profiles.isEmpty()) {
            return List.of(); // if data null or empty
        }
        return convertToDTO(profiles);
    }


    private List<DeveloperDTOModel> convertToDTO(List<DeveloperProfile> developerProfiles) {
        return developerProfiles.stream()
                .map(profile -> new DeveloperDTOModel(
                        profile.getPlatform(),
                        profile.getUsername(),
                        profile.getProfileUrl(),
                        profile.getReputation(),
                        List.of(profile.getSkills()), // Convert array in List
                        profile.getLocation(),
                        profile.getLastActivityDate()
                ))
                .collect(Collectors.toList());
    }

}
