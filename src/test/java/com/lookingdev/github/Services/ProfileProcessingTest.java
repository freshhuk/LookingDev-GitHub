package com.lookingdev.github.Services;

import com.lookingdev.github.Domain.Entities.DeveloperProfile;
import com.lookingdev.github.Domain.Models.DeveloperDTOModel;
import com.lookingdev.github.Repositories.DeveloperRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProfileProcessingTest {

    @InjectMocks
    private ProfileProcessing service;

    @Mock
    private DeveloperRepository repository;

    @Mock
    private GitHubService gitHubService;
    /*
    @Test
    void testInitUsers() {
        // Arrange: Prepare mock data
        List<DeveloperDTOModel> mockUsers = List.of(
                new DeveloperDTOModel("GitHub", "user1", "https://github.com/user1", 120, List.of("Java", "Spring"), "USA", LocalDate.now()),
                new DeveloperDTOModel("GitHub", "user2", "https://github.com/user2", 100, List.of("Python", "Django"), "Canada", LocalDate.now())
        );

        Mockito.when(gitHubService.fetchMultipleUsers(100)).thenReturn(mockUsers);

        // Act: Call the method
        service.initUsers();

        // Assert: Verify repository interactions
        verify(gitHubService, times(1)).fetchMultipleUsers(100);
        verify(repository, times(mockUsers.size())).add(any(DeveloperDTOModel.class));
    }

    @Test
    void testGetDevelopersDTO_WhenDataExists() {
        // Arrange: Prepare mock data
        List<DeveloperProfile> mockProfiles = List.of(
                new DeveloperProfile(1, "GitHub", "user1", "https://github.com/user1", 120, new String[]{"Java", "Spring"}, "USA", LocalDate.now()),
                new DeveloperProfile(2, "GitHub", "user2", "https://github.com/user2", 100, new String[]{"Python", "Django"}, "Canada", LocalDate.now())
        );

        when(repository.getDevelopers(10, 0)).thenReturn(mockProfiles);

        // Act: Call the method
        List<DeveloperDTOModel> result = service.getDevelopersDTO(0);

        // Assert: Verify conversion and result
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getUsername());
        assertEquals("user2", result.get(1).getUsername());
    }

    @Test
    void testGetDevelopersDTO_WhenNoDataExists() {
        // Arrange: Empty data
        when(repository.getDevelopers(10, 0)).thenReturn(List.of());

        // Act: Call the method
        List<DeveloperDTOModel> result = service.getDevelopersDTO(0);

        // Assert: Verify result is empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }*/
}
