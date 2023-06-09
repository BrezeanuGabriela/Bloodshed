package com.paw.Model.LogService.Services;

import com.paw.Model.LogService.Entities.Log;
import com.paw.Model.LogService.Repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LogService {
    @Autowired
    LogRepository logRepository;
    public Log addLog(String description)
    {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy.HH:mm:ss");
        String timeStamp = df.format(new Date());
        Log newLog = new Log(description, timeStamp);
        logRepository.save(newLog);
        return newLog;
    }
}
