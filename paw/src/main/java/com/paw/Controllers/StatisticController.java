package com.paw.Controllers;

import com.paw.Model.StatisticsService.Entities.StatisticModelView;
import com.paw.Model.StatisticsService.Services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticController {
    @Autowired
    StatisticService statisticService;

    @GetMapping("/api/statistics/age")
    public ResponseEntity<?> getAgeStatistics()
    {
        List<StatisticModelView> statistics = statisticService.getAgeStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/api/statistics/donationstatus")
    public ResponseEntity<?> getDonationStatusStatistics()
    {
        List<StatisticModelView> statistics = statisticService.getDonationStatusStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/api/statistics/bloodtype")
    public ResponseEntity<?> getDonorBloodTypeStatistics()
    {
        List<StatisticModelView> statistics = statisticService.getDonorBloodTypeStatistics();
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
