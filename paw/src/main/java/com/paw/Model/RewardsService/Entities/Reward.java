package com.paw.Model.RewardsService.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rewards")
public class Reward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private Integer price;
    public Reward(String name, Integer price)
    {
        this.name = name;
        this.price = price;
    }

}
