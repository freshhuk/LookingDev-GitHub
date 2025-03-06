package com.lookingdev.github.Services;

import com.lookingdev.github.Domain.Entities.DeveloperProfile;
import com.lookingdev.github.Domain.Models.DeveloperDTOModel;
import com.lookingdev.github.Repositories.DeveloperRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    @Async
    public CompletableFuture<Void> initUsers() {
        return gitHubService.fetchMultipleUsers(USER_COUNT_IN_DB)
                .thenAccept(users -> {
                    for (DeveloperDTOModel user : users) {
                        repository.add(user);
                        logger.info("User was added in db");
                    }
                    logger.info("ALL Users were added in db");
                })
                .exceptionally(ex -> {
                    logger.error("Error fetching users: {}", ex.getMessage());
                    return null;
                });
    }

    /**
     * Method for getting users with default users limit
     * @param lastIndex last index entity which will be return
     * @return list with users
     */
    public List<DeveloperDTOModel> getDevelopersDTO(int lastIndex) {
        return getDevelopersDTO(LIMIT_USERS, lastIndex);
    }

    /**
     * Method for getting users with custom users limit
     * @param lastIndex last index entity which will be return
     * @return list with users
     */
    public List<DeveloperDTOModel> getDevelopersDTO(int limitUsers, int lastIndex) {
        List<DeveloperProfile> profiles = repository.getDevelopers(limitUsers, lastIndex);
        if (profiles == null || profiles.isEmpty()) {
            return List.of();
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
