package com.lookingdev.github.Domain.Models;

import com.lookingdev.github.Domain.Enums.QueueAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageStatus {
    private QueueAction action;
    private String status;
}
