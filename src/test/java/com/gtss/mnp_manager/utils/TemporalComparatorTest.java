package com.gtss.mnp_manager.utils;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TemporalComparatorTest {

    @ParameterizedTest
    @CsvSource({"1,0", "2,0", "3,1", "60,1", "3600,1", "-1,0", "-2,0", "-60,1",
            "-3600,1"})
    void itShouldValidateCloseToTemporalsByOffsetInSeconds(long offset,
            int expected) {

        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = dateTime1.plus(offset, ChronoUnit.SECONDS);

        assertThat(
                TemporalComparator.isCloseTo(2).compare(dateTime1, dateTime2))
                .isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"1,0", "2,0", "3,1", "60,1", "3600,1", "-1,0", "-2,0", "-60,1",
            "-3600,1"})
    void itShouldValidateCloseToTemporalsByOffsetUnit(long offset,
            int expected) {

        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = dateTime1.plus(offset, ChronoUnit.SECONDS);

        assertThat(TemporalComparator
                .isCloseTo(2, ChronoUnit.SECONDS).compare(dateTime1, dateTime2))
                .isEqualTo(expected);
    }

}
