package com.example.demo.service;

import com.example.demo.model.UniversalRequest;
import com.example.demo.repository.MyCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyCalendarService {

    @Autowired
    private MyCalendarRepository myCalendarRepository;

    @Transactional
    public String getCalendar(UniversalRequest request){
        return myCalendarRepository.findMyCalendars(request);
    }
}
