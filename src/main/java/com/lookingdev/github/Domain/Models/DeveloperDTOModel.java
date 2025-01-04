package com.lookingdev.github.Domain.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeveloperDTOModel {

    private String platform;
    private String username;
    private String profileUrl;
    private Integer reputation;
    private List<String> skills;
    private String location;
    private LocalDate lastActivityDate;
}
