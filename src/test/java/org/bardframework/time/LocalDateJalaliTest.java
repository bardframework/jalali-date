package org.bardframework.time;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Random;

/**
 * Created by Vahid Zafari on 4/29/2016.
 */
@Slf4j
public class LocalDateJalaliTest {

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
            log.info("test converting LocalDateJalali --> LocalDate  --> LocalDateJalali : {} --> {}  --> {}", generatedDate, localDate, returnedDate);
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
            log.info("test converting LocalDate --> LocalDateJalali  --> LocalDate : {} --> {}  --> {}", generatedDate, localDateJalali, returnedDate);
            Assertions.assertEquals(generatedDate, returnedDate);
        }
    }

    @Test
    public void testEpochDay() {
        LocalDateJalali localDateJalali = LocalDateJalali.now();
        long epoch = localDateJalali.toEpochDay();
        Assertions.assertEquals(localDateJalali, LocalDateJalali.ofEpochDay(epoch));
    }

    @Test
    public void testPlusMonth1() {
        Assertions.assertEquals(LocalDateJalali.of(1401, 6, 31).plusMonths(1).getDayOfMonth(), 30);
    }

    @Test
    public void testPlusMonth2() {
        Assertions.assertEquals(LocalDateJalali.of(1401, 12, 29).plusMonths(1).getDayOfMonth(), 29);
    }

    @Test
    public void testPlusDay1() {
        Assertions.assertEquals(LocalDateJalali.of(1401, 12, 29).plusDays(1).getDayOfMonth(), 1);
    }
}
