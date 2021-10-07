package by.leonovich.hibernatecrm.sandbox;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created : 30/09/2021 10:36
 * Project : hibernate-crm
 * IDE : IntelliJ IDEA
 *
 * @author alexanderleonovich
 * @version 1.0
 */
public class StreamJava9 {
    private static final Logger log = LogManager.getLogger(StreamJava9.class);

    public static void main(String[] args) {
        takeWhileDemo();
        dropWhileDemo();
        iterateDemo();
    }

    private static void takeWhileDemo() {
        log.info("takeWhileDemo >>> ");
        Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .takeWhile(n -> n < 5)
            .forEach(element -> log.info("{}", element)); // 0 1 2 3 4
    }

    private static void dropWhileDemo() {
        log.info("dropWhileDemo >>> ");
        Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
            .dropWhile(n -> n < 6)
            .forEach(element -> log.info("{}", element)); // 6 7 8 9 10
    }

    private static void iterateDemo() {
        log.info("iterateDemo >>> ");

//        infinite stream
//        IntStream.iterate(0, n -> n + 1).forEach(element -> log.info("{}", element));

        // Option 1 - using takeWhile
        IntStream.iterate(0, n -> n + 1)
            .takeWhile(element -> element < 50)
            .forEach(element -> log.info("{}", element));

        // Option 2 - using 3 arguments iterate
        IntStream.iterate(0, element -> element < 50, n -> n + 1)
            .forEach(element -> log.info("{}", element));
    }
}
