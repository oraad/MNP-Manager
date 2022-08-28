package com.gtss.mnp_manager.utils;

import static java.lang.Math.abs;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

/**
 * Temporal Comparator compares time instances
 */
public class TemporalComparator {

    /**
     * Check if two time instance are within the specified offset
     * 
     * @param offsetInSeconds allowed time difference in seconds
     * @return 0-instances are within the offset, 1-instances are outside the offset
     */
    public static ComparatorWithEquals<Temporal> isCloseTo(
            long offsetInSeconds) {

        TemporalUnit unit = ChronoUnit.SECONDS;
        return isCloseTo(offsetInSeconds, unit);
    }

    /**
     * /** Check if two time instance are within the specified offset
     * 
     * @param offsetInUnit allowed time difference in specified unit
     * @param unit offset unit
     * @return 0-instances are within the offset, 1-instances are outside the offset
     */
    public static ComparatorWithEquals<Temporal> isCloseTo(long offsetInUnit,
            TemporalUnit unit) {

        return new ComparatorWithEquals<Temporal>() {

            public int compare(Temporal temporal1, Temporal temporal2) {
                return equals(temporal1, temporal2) ? 0 : 1;
            }

            public boolean equals(Temporal temporal1, Temporal temporal2) {
                long difference = getDifference(unit, temporal1, temporal2);
                return difference <= offsetInUnit;
            }

            private long getDifference(TemporalUnit unit, Temporal temporal1,
                    Temporal temporal2) {
                return abs(unit.between(temporal1, temporal2));
            }

        };

    }
}
