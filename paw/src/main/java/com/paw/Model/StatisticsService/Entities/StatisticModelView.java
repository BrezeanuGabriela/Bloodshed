package com.paw.Model.StatisticsService.Entities;

import lombok.Getter;
import lombok.Setter;

public class StatisticModelView {

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private Integer number;

    @Setter
    @Getter
    private Double percentage;
    public StatisticModelView(String _name, Integer _number, Double _percentage)
    {
        this.name = _name;
        this.number = _number;
        this.percentage = _percentage;
    }

}
