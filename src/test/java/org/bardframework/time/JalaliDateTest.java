package org.bardframework.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Random;

/**
 * Created by Vahid Zafari on 4/29/2016.
 */
class JalaliDateTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JalaliDateTest.class);

    @Test
    void jalaliToLocalBatch() {
        JalaliDate generatedDate;
        for (int i = 0; i < 10000; i++) {
            generatedDate = null;
            while (null == generatedDate) {
                try {
                    generatedDate = JalaliDate.of(getRandomNumberInRange(1200, 1500), getRandomNumberInRange(1, 12), getRandomNumberInRange(1, 31));
                } catch (Exception e) {
                    /*
                      maybe invalid date generated.
                     */
                }
            }
            LocalDate localDate = generatedDate.toLocalDate();
            JalaliDate returnedDate = JalaliDate.of(localDate);
            LOGGER.info("test converting JalaliDate --> JalaliDate  --> JalaliDate : {} --> {}  --> {}", generatedDate, localDate, returnedDate);
            Assertions.assertEquals(generatedDate, returnedDate);
        }
    }

    @Test
    void localToJalaliBatch() {
        LocalDate generatedDate;
        for (int i = 0; i < 10000; i++) {
            generatedDate = null;
            while (null == generatedDate) {
                try {
                    generatedDate = LocalDate.of(getRandomNumberInRange(1800, 2100), getRandomNumberInRange(1, 12), getRandomNumberInRange(1, 31));
                } catch (Exception e) {
                    /*
                      maybe invalid date generated.
                     */
                }
            }
            JalaliDate jalaliDate = JalaliDate.of(generatedDate);
            LocalDate returnedDate = jalaliDate.toLocalDate();
            LOGGER.info("test converting LocalDate --> JalaliDate  --> LocalDate : {} --> {}  --> {}", generatedDate, jalaliDate, returnedDate);
            Assertions.assertEquals(generatedDate, returnedDate);
        }
    }

    private int getRandomNumberInRange(int min, int max) {
        return new Random().ints(min, (max + 1)).findFirst().orElse(min);
    }

    @Test
    void testEpochDay() {
        JalaliDate jalaliDate = JalaliDate.now();
        long epoch = jalaliDate.toEpochDay();
        Assertions.assertEquals(jalaliDate, JalaliDate.ofEpochDay(epoch));
    }
}
