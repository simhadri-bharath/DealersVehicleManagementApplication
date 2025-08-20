package com.bharath.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DealerDto {
    private Long id;
    private String name;
    private String email;
    // Note we are NOT including the password
}