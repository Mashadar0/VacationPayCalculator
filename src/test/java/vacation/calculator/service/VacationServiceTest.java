package service;

import vacation.calculator.exception.IncorrectDataVacationException;
import vacation.calculator.feing.DayOffFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import vacation.calculator.service.VacationService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

class VacationServiceTest {

    @Mock
    private DayOffFeignClient dayOffFeignClient;

    private VacationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new VacationService(dayOffFeignClient);
    }

    @Test
    void canGetVacationWhenExactDaysAreUnknown() {
        // Given
        Integer vacationDays = 10;
        Double salary = 30000.0;
        // When
        String actual = underTest.getVacation(vacationDays, salary, null, null);
        // Then
        assertThat(actual).isEqualTo("10238,91");
    }

    @Test
    void canGetVacationWhenExactDaysAreKnown() {
        // Given
        Double salary = 30000.0;
        LocalDate startVacation = LocalDate.of(2023, 10, 5);
        LocalDate endVacation = LocalDate.of(2023, 10, 15);

        LocalDate day = startVacation;
        for (int i = 1; i < 6; i++) {
            given(dayOffFeignClient.getDayOff(day)).willReturn("0");
            day = startVacation.plusDays(i);
        }
        // When
        String actual = underTest.getVacation(null, salary, startVacation, endVacation);
        // Then
        assertThat(actual).isEqualTo("10238,91");
    }

    @Test
    void cannotGetVacationWhenVacationEmpty() {
        // Given

        // When
        Throwable thrown = assertThrows(IncorrectDataVacationException.class, () -> {
            underTest.getVacation(0, 0.0, null, null);
        });
        // Then
        assertNotNull(thrown.getMessage());
    }
}