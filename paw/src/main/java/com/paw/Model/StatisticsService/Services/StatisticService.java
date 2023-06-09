package com.paw.Model.StatisticsService.Services;

import com.paw.Model.DonationsService.Entities.Donation;
import com.paw.Model.DonationsService.Repositories.DonationRepository;
import com.paw.Model.StatisticsService.Entities.StatisticModelView;
import com.paw.Model.UserManagementService.Entities.InfoUser;
import com.paw.Model.UserManagementService.Entities.User;
import com.paw.Model.UserManagementService.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Calendar.*;

@Service
public class StatisticService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    DonationRepository donationRepository;


    public List<StatisticModelView> getAgeStatistics()
    {
        List<User> users = userRepository.findAll();
        int userCount = users.size();

        int youngerUsersCount = 0;
        int middleAgeUsersCount = 0;
        int olderUsersCount = 0;

        for (User user: users) {
            InfoUser userInfo = user.getInfoUser();
            if (userInfo != null) {
                Date birthday = userInfo.getData_nasterii();
                int age = getDiffYears(birthday, new Date());
                if (age < 32)
                    youngerUsersCount++;
                else if (age > 46)
                    olderUsersCount++;
                else
                    middleAgeUsersCount++;
            }
            else
                userCount--;
        }

        if (userCount <= 0)
            userCount = 1;
        StatisticModelView youngerUsers = new StatisticModelView("18-32", youngerUsersCount, youngerUsersCount * 100.0 / userCount);
        StatisticModelView middleAgeUsers = new StatisticModelView("32-46", middleAgeUsersCount, middleAgeUsersCount * 100.0 / userCount);
        StatisticModelView olderUsers = new StatisticModelView("46-60", olderUsersCount, olderUsersCount * 100.0 / userCount);

        StatisticModelView[] statistics = {youngerUsers, middleAgeUsers, olderUsers};
        return Arrays.asList(statistics);
    }

    private static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.UK);
        cal.setTime(date);
        return cal;
    }

    public List<StatisticModelView> getDonationStatusStatistics()
    {
        List<Donation> donations = donationRepository.findAll();

        int donationCount = donations.size();
        if(donationCount == 0)
            donationCount = 1;

        int reservedDonationsCount = 0;
        int canceledDonationsCount = 0;
        int successfulDonationsCount = 0;

        for (Donation donation : donations) {
            if (Objects.equals(donation.getStatus(), "rezervat"))
                reservedDonationsCount++;
            else if (Objects.equals(donation.getStatus(), "anulat"))
                canceledDonationsCount++;
            else
                successfulDonationsCount++;
            }

        StatisticModelView reservedDonations = new StatisticModelView("Rezervari", reservedDonationsCount, reservedDonationsCount * 100.0 / donationCount);
        StatisticModelView canceledDonations = new StatisticModelView("Donatii anulate", canceledDonationsCount, canceledDonationsCount * 100.0 / donationCount);
        StatisticModelView successfulDonations = new StatisticModelView("Donatii cu succes", successfulDonationsCount, successfulDonationsCount * 100.0 / donationCount);

        StatisticModelView[] statistics = {reservedDonations, canceledDonations, successfulDonations};
        return Arrays.asList(statistics);

    }

    public List<StatisticModelView> getDonorBloodTypeStatistics()
    {
        List<Donation> donations = donationRepository.findAll();

        int donationCount = donations.size();
        if (donationCount == 0)
            donationCount = 1;

        int bloodType0UsersCount = 0;
        int bloodTypeAUsersCount = 0;
        int bloodTypeBUsersCount = 0;
        int bloodTypeABUsersCount = 0;

        for (Donation donation : donations) {
           User user = userRepository.findUserById(donation.getUserId());
           String bloodType = user.getInfoUser().getGrupaSange();
           if (bloodType.startsWith("AB"))
               bloodTypeABUsersCount++;
           else if (bloodType.startsWith("A"))
               bloodTypeAUsersCount++;
           else if (bloodType.startsWith("B"))
               bloodTypeBUsersCount++;
           else
               bloodType0UsersCount++;

        }

        StatisticModelView bloodType0Users = new StatisticModelView("Donatori cu grupa 0", bloodType0UsersCount, bloodType0UsersCount * 100.0 / donationCount);
        StatisticModelView bloodTypeAUsers = new StatisticModelView("Donatori cu grupa A", bloodTypeAUsersCount, bloodTypeAUsersCount * 100.0 / donationCount);
        StatisticModelView bloodTypeBUsers = new StatisticModelView("Donatori cu grupa B", bloodTypeBUsersCount, bloodTypeBUsersCount * 100.0 / donationCount);
        StatisticModelView bloodTypeABUsers = new StatisticModelView("Donatori cu grupa AB", bloodTypeABUsersCount, bloodTypeABUsersCount * 100.0 / donationCount);

        StatisticModelView[] statistics = {bloodType0Users, bloodTypeAUsers, bloodTypeBUsers, bloodTypeABUsers};
        return Arrays.asList(statistics);
    }

}
