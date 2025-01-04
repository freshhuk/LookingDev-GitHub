package com.lookingdev.github.Services;

import com.lookingdev.github.Domain.Models.DeveloperDTOModel;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.PagedIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class GitHubService {

    private static final Logger logger = LoggerFactory.getLogger(GitHubService.class);
    private static final int USERS_PER_PAGE = 100; // GitHub limit 100 users per one page

    @Value("${gitapi.key}")
    private String gitHubToken;

    public List<DeveloperDTOModel> fetchMultipleUsers(int userCount){
        try{
            GitHub github = GitHub.connectUsingOAuth(gitHubToken); // Connect with gitHub with token
            List<DeveloperDTOModel> userProfiles = new ArrayList<>();

            PagedIterable<GHUser> users = github.listUsers();
            PagedIterator<GHUser> iterator = users.withPageSize(USERS_PER_PAGE).iterator();

            int fetchedUsers = 0;

            while (iterator.hasNext() && fetchedUsers < userCount) {
                GHUser ghUser = iterator.next();

                DeveloperDTOModel userProfile = convertToUserProfile(ghUser);
                userProfiles.add(userProfile);

                fetchedUsers++;
            }

            return userProfiles;
        } catch (Exception ex){
            logger.error("Something was wrong, error: " + ex);
            return null;
        }

    }

    private DeveloperDTOModel convertToUserProfile(GHUser ghUser) throws IOException {

        DeveloperDTOModel convertedModel = new DeveloperDTOModel();{
            convertedModel.setPlatform("GitHub");
            convertedModel.setUsername(ghUser.getLogin());
            convertedModel.setProfileUrl(ghUser.getHtmlUrl().toString());
            convertedModel.setReputation(ghUser.getFollowersCount());

            convertedModel.setLastActivityDate(
                    ghUser.getUpdatedAt() != null
                            ? ghUser.getUpdatedAt().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                            : null
            );

            convertedModel.setLocation(
                    ghUser.getLocation() != null ? ghUser.getLocation() : "Unknown"
            );

            convertedModel.setSkills(List.of("Java", "Spring", "Git")); // TODO
        }

        return convertedModel;
    }

}
