package com.paw.Model.DonationsService.Repositories;

import com.paw.Model.DonationsService.Entities.Donation;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DonationRepository extends CrudRepository<Donation, Integer>{

    @Query("SELECT * FROM donatii d WHERE d.id_user = :userid")
    List<Donation> findDonationsByUserId(@Param("userid") Integer userid);

    List<Donation> findAll();

    void saveAndFlush(Donation newDonation);
}
