package com.example.demo.controller;

import com.example.demo.model.UniversalRequest;
import com.example.demo.service.MyCalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private MyCalendarService myCalendarService;

    @Autowired
    public CalendarController(MyCalendarService myCalendarService){
        this.myCalendarService = myCalendarService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCalendars(@RequestParam() String function,
                               @RequestParam(required = false) String param,
                               @RequestParam(required = false) Integer value,
                               @RequestParam(required = false) String[] params,
                               @RequestParam(required = false) Integer[] values){
        UniversalRequest request = new UniversalRequest();
        request.setFunction(function);
        request.setParam(param);
        request.setValue(value);
        request.setParameters(params);
        request.setValues(values);
        String result = myCalendarService.getCalendar(request);
         return result;
    }
}
