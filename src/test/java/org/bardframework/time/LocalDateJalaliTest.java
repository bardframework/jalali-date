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
public class LocalDateJalaliTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalDateJalaliTest.class);

    private int getRandomNumberInRange(int min, int max) {
        return new Random().ints(min, (max + 1)).findFirst().orElse(min);
    }

    @Test
    public void jalaliToLocalBatch() {
        LocalDateJalali generatedDate;
        for (int i = 0; i < 10000; i++) {
            generatedDate = null;
            while (null == generatedDate) {
                try {
                    generatedDate = LocalDateJalali.of(getRandomNumberInRange(1200, 1500), getRandomNumberInRange(1, 12), getRandomNumberInRange(1, 31));
                } catch (Exception e) {
                    /*
                      maybe invalid date generated.
                     */
                }
            }
            LocalDate localDate = generatedDate.toLocalDate();
            LocalDateJalali returnedDate = LocalDateJalali.of(localDate);
            LOGGER.info("test converting JalaliDate --> JalaliDate  --> JalaliDate : {} --> {}  --> {}", generatedDate, localDate, returnedDate);
            Assertions.assertEquals(generatedDate, returnedDate);
        }
    }

    @Test
    public void localToJalaliBatch() {
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
            LocalDateJalali localDateJalali = LocalDateJalali.of(generatedDate);
            LocalDate returnedDate = localDateJalali.toLocalDate();
            LOGGER.info("test converting LocalDate --> JalaliDate  --> LocalDate : {} --> {}  --> {}", generatedDate, localDateJalali, returnedDate);
            Assertions.assertEquals(generatedDate, returnedDate);
        }
    }

    @Test
    public void testEpochDay() {
        LocalDateJalali localDateJalali = LocalDateJalali.now();
        long epoch = localDateJalali.toEpochDay();
        Assertions.assertEquals(localDateJalali, LocalDateJalali.ofEpochDay(epoch));
    }
}
