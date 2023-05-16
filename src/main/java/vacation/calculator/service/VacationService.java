package vacation.calculator.service;

import vacation.calculator.exception.IncorrectDataVacationException;
import vacation.calculator.feing.DayOffFeignClient;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class VacationService {

    private static final Double AVG_NUMBER_DAYS_IN_MONTH = 29.3;
    private final DayOffFeignClient dayOffFeignClient;

    public VacationService(DayOffFeignClient dayOffFeignClient) {
        this.dayOffFeignClient = dayOffFeignClient;
    }

    public String getVacation(Integer vacationDays, Double salary,
                              LocalDate startVacation, LocalDate endVacation) {
        if (salary == null || salary <= 0) {
            throw new IncorrectDataVacationException("The salary is missing or it is not correct");
        }
        long countDays;
        if (startVacation != null && endVacation != null) {
            countDays = Stream.iterate(startVacation, date -> date.plusDays(0))
                    .limit(endVacation.getDayOfMonth() - startVacation.getDayOfMonth())
                    .filter((day) -> Objects.equals(dayOffFeignClient.getDayOff(day), "0"))
                    .count();
        } else {
            if (vacationDays == null || vacationDays <= 0) {
                throw new IncorrectDataVacationException("The number of vacation days is missing or it is not correct");
            } else {
                countDays = vacationDays;
            }
        }
        return String.format("%.2f", salary / AVG_NUMBER_DAYS_IN_MONTH * countDays);
    }
}