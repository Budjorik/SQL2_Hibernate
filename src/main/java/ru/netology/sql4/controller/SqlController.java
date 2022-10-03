package ru.netology.sql4.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.sql4.entity.Person;
import ru.netology.sql4.service.SqlService;

import java.util.List;

@RestController
public class SqlController {
    private final SqlService sqlService;

    public SqlController(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @GetMapping("${endpoint}")
    public List<Person> getPersonsByCity(@RequestParam("city") String city) {
        if (sqlService.checkCity(city) == true) {
            return sqlService.getPersonsByCity(city);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found into DataBase!");
    }

}
