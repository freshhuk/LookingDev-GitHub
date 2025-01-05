package com.lookingdev.github.Domain.Models;

import com.lookingdev.github.Domain.Enums.QueueAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {
    private QueueAction action;
    private List<DeveloperDTOModel> developerProfiles;
}
