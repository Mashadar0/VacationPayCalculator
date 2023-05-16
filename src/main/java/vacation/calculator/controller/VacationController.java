package vacation.calculator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import vacation.calculator.exception.IncorrectDataVacationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import vacation.calculator.service.VacationService;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/vacation")
@Api(value = "Калькулятор отпускных")
public class VacationController {

    private final VacationService service;

    public VacationController(VacationService service) {
        this.service = service;
    }

    @GetMapping("/calculate")
    @ApiOperation(value = "Рассчет отпускных")
    public ResponseEntity<?> getVacation(@RequestParam(required = false) Integer vacationDays,
                                         @RequestParam Double salary,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startVacation,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endVacation) {
        try {
            return new ResponseEntity<>(service.getVacation(vacationDays, salary, startVacation, endVacation), HttpStatus.OK);
        } catch (IncorrectDataVacationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}