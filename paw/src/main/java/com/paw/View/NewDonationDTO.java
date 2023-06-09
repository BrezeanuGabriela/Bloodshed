package com.paw.View;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

public class NewDonationDTO {

    @Setter
    @Getter
    private Integer user_id;

    @Setter
    @Getter
    private String status;

    @Setter
    @Getter
    private String observations;

    @Setter
    @Getter
    private String date;

    @Setter
    @Getter
    private String time;

    public NewDonationDTO() {}
}
