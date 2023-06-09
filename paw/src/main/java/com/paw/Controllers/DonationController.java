package com.paw.Controllers;

import com.paw.Model.DonationsService.Entities.Donation;
import com.paw.Model.DonationsService.Services.DonationService;
import com.paw.View.NewDonationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class DonationController {
    @Autowired
    DonationService donationService;

    @GetMapping("/api/donations/user/{idUser}")
    public ResponseEntity<?> getDonationForUser(@PathVariable Integer idUser)
    {
        List<Donation> donationsForUser = donationService.getDonationForUser(idUser);
        return new ResponseEntity<>(donationsForUser, HttpStatus.OK);
    }

    @GetMapping("/api/donations/")
    public ResponseEntity<?> getAllDonations()
    {
        List<Donation> allDonations = donationService.getAllDonations();
        return new ResponseEntity<>(allDonations, HttpStatus.OK);
    }

    @PostMapping("/api/donations/")
    public ResponseEntity<?> addDonation(@RequestBody NewDonationDTO newDonation) {
        try {
            Donation donation = donationService.addDonation(newDonation);
            return new ResponseEntity<>(donation, HttpStatus.CREATED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping("/api/donations/{idDonatie}")
    public ResponseEntity<?> addDonation(@RequestBody NewDonationDTO newDonation,
                                         @PathVariable Integer idDonatie) {
        try {
            Donation donation = donationService.updateDonation(idDonatie, newDonation);
            return new ResponseEntity<>(donation, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }
}