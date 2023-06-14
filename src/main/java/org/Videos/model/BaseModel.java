package org.Videos.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
public abstract class BaseModel {
    private UUID id;
    private LocalDateTime created_time;
    private LocalDateTime updated_time;
    {
        id = UUID.randomUUID();
        created_time = LocalDateTime.now();
        updated_time = LocalDateTime.now();
    }
}
