package com.paw.Controllers;

import com.paw.Exceptions.NotAcceptableReq;
import com.paw.Exceptions.UnprocessableReq;
import com.paw.Exceptions.UserAlreadyExists;
import com.paw.Model.LogService.Entities.Log;
import com.paw.Model.LogService.Services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class LogController {
    @Autowired
    LogService logService;

    @PostMapping("/api/logs/")
    public ResponseEntity<?> addLog(@RequestBody String description)
    {
        try {
            Log newLog = logService.addLog(description);
            return new ResponseEntity<>(newLog, HttpStatus.CREATED);
        }
        catch (UserAlreadyExists userAlreadyExists)
        {
            return new ResponseEntity<>(userAlreadyExists.getMessage(), HttpStatus.CONFLICT);
        }
        catch (DataAccessException dataAccessException)
        {
            return new ResponseEntity<>(dataAccessException.getLocalizedMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        catch (UnprocessableReq | NotAcceptableReq unprocessableReq)
        {
            return new ResponseEntity<>(unprocessableReq.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        catch (RuntimeException runtimeException)
        {
            return new ResponseEntity<>(runtimeException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
