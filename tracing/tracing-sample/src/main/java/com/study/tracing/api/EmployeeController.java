package com.study.tracing.api;

import com.study.tracing.config.GlobalStore;
import com.study.tracing.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CreateEmployeeController {

    @Autowired
    GlobalStore globalStore;

    @RequestMapping(value = "/api/1.0/employees", method = RequestMethod.POST)
    public ResponseEntity createEmployee(@RequestBody Employee employee) {

        globalStore.insertEmployee(employee);
        return new ResponseEntity(null, HttpStatus.CREATED);
    }

}
