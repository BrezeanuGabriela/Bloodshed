package com.paw.Model.DonationsService.Services;

import com.paw.Exceptions.*;
import com.paw.Model.DonationsService.Entities.Donation;
import com.paw.Model.DonationsService.Repositories.DonationRepository;
import com.paw.Model.LogService.Entities.Log;
import com.paw.Model.UserManagementService.Entities.Role;
import com.paw.Model.UserManagementService.Repositories.RoleRepository;
import com.paw.Model.UserManagementService.Repositories.UserInfoRepository;
import com.paw.Model.UserManagementService.Repositories.UserRepository;
import com.paw.Model.UserManagementService.Entities.InfoUser;
import com.paw.Model.UserManagementService.Entities.User;
import com.paw.Model.UserManagementService.Repositories.UserRolesRepository;
import com.paw.View.NewDonationDTO;
import com.paw.View.NewUserDTO;
import com.paw.View.RoleInputDTO;
import com.paw.View.UpdateUserDTO;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DonationService {

    @Autowired
    DonationRepository donationRepository;

    public Donation addDonation(NewDonationDTO donation) {

        if (!Arrays.asList(Donation.statusValues).contains(donation.getStatus())) {
            throw new InvalidDonationData("[ERROR] Invalid Donation Data: Status Value");
        }

        Donation newDonation;
        try
        {
            newDonation = new Donation(donation);

        }
        catch (InvalidDonationData e)
        {
            throw new InvalidDonationData(e.getMessage());
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException(e.getMessage());
        }

        try
        {
            donationRepository.save(newDonation);
        }
        catch (Exception e)
        {
            throw new InvalidDonationData("[ERROR] Invalid Donation Data: Database Constraint Violation");
        }

        return newDonation;
    }

    public List<Donation> getDonationForUser(Integer id_user) {
        List<Donation> results = donationRepository.findDonationsByUserId(id_user);;
//        if (donationRepository.findByUserId(idUser).isEmpty())
        return results;
    }

    public List<Donation> getAllDonations() {
        List<Donation> results = donationRepository.findAll();
        return results;
    }

    public Donation updateDonation(Integer idDonatie, NewDonationDTO donation) {
        if (!Arrays.asList(Donation.statusValues).contains(donation.getStatus())) {
            throw new InvalidDonationData("[ERROR] Invalid Donation Data: Status Value");
        }

        Donation newDonation;
        try
        {
            newDonation = new Donation(donation);
        }
        catch (InvalidDonationData e)
        {
            throw new InvalidDonationData(e.getMessage());
        }
        catch (RuntimeException e)
        {
            throw new RuntimeException(e.getMessage());
        }

        Donation existingDonation;
        try
        {
            existingDonation = donationRepository.findById(idDonatie).get();
        }
        catch (Exception e)
        {
            throw new DonationNotFound("[ERROR] Invalid Donation: Donation Not Found");
        }

        try
        {
            System.out.println(existingDonation.getId());

            existingDonation.setUserId(newDonation.getUserId());
            existingDonation.setObservations(newDonation.getObservations());
            existingDonation.setTime(newDonation.getTime());
            existingDonation.setData(newDonation.getData());
            existingDonation.setStatus(newDonation.getStatus());

            donationRepository.saveAndFlush(existingDonation);
        }
        catch (Exception e)
        {
//            throw new RuntimeException("[ERROR] Database Eroor");
            throw new RuntimeException(e.getMessage());
        }

        return existingDonation;
    }
}
