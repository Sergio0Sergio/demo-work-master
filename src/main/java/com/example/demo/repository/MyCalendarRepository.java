package com.example.demo.repository;

import com.example.demo.model.MyCalendarDto;
import com.example.demo.model.UniversalRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;


public interface MyCalendarRepository {

    String findMyCalendars(UniversalRequest request);

}
