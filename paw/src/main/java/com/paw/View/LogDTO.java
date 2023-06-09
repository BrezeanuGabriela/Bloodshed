package com.paw.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;

public class LogDTO {
    @Id
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String timestamp;

}
