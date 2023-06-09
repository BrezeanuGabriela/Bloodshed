package com.paw.Model.LogService.Repositories;

import com.paw.Model.LogService.Entities.Log;
import org.springframework.data.repository.CrudRepository;

    public interface LogRepository extends CrudRepository<Log, Integer>{
}
