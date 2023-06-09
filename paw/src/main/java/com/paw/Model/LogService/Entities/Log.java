package com.paw.Model.LogService.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String description;

    @Setter
    @Getter
    private String timestamp;

    public Log(String description, String timestamp)
    {
        this.description = description;
        this.timestamp = timestamp;
    }
}
