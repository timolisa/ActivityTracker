package com.timolisa.activitytracker.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDTO {
    private long id;

    @NotBlank(message = "Enter a title")
    @Length(min = 5, max = 255, message = "Title must contain 5 or more characters")
    private String title;

    @NotBlank(message = "Enter a description")
    private String description;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;
}
