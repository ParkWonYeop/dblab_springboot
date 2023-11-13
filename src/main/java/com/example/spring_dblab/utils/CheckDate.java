package com.example.spring_dblab.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Slf4j
public class CheckDate {
    public static LocalDate parseDate(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException err) {
            log.error("Is not date");
            throw err;
        }
    }
}
