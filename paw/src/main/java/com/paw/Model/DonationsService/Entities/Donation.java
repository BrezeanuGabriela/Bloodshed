package com.paw.Model.DonationsService.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.paw.Exceptions.InvalidDonationData;
import com.paw.View.NewDonationDTO;
import com.paw.View.NewUserInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@AllArgsConstructor
@Entity
@Table(name="donatii")
public class Donation {
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Getter
    @Setter
    @Column(name="id_user")
    private Integer userId;

    @Getter
    @Setter
    @Column(name="observatii")
    private String observations;

    @Getter
    @Setter
    @Column(name="status")
    private String status;
    public static String[] statusValues = {
            "rezervat",
            "anulat",
            "succes"
    };

    @Getter
    @Setter
    @Column(name="data")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date data;

    @Getter
    @Setter
    @Column(name="ora_programarii")
    @JsonFormat(pattern="hh:mm:ss")
    private Time time;


    public Donation(NewDonationDTO newDonation)
    {
        this.userId          = newDonation.getUser_id();
        this.status          = newDonation.getStatus();
        this.observations    = newDonation.getObservations();

        SimpleDateFormat time_formatter = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Time time = new Time(time_formatter.parse(newDonation.getTime()).getTime());
            Date date = date_formatter.parse(newDonation.getDate());
            this.data = date;
            this.time = time;
        }
        catch (ParseException e) {
            throw new InvalidDonationData("[ERROR] Invalid Donation Data: Date and Time Values");
        }
    }
    public Donation(Integer id_user)
    {
        this.userId = id_user;
    }

    public Donation(Donation newDonation)
    {
        this.id              = newDonation.getId();
        this.userId          = newDonation.getUserId();
        this.status          = newDonation.getStatus();
        this.observations    = newDonation.getObservations();
        this.data            = newDonation.getData();
        this.time            = newDonation.getTime();
    }
    public Donation() {

    }
}
