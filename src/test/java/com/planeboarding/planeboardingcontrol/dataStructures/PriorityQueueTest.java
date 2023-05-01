package com.planeboarding.planeboardingcontrol.dataStructures;

import com.planeboarding.planeboardingcontrol.exception.DuplicatedKeyException;
import com.planeboarding.planeboardingcontrol.exception.HeapUnderFlowException;
import com.planeboarding.planeboardingcontrol.exception.KeyIsSmallerException;
import com.planeboarding.planeboardingcontrol.model.PriorityCriteria;
import com.planeboarding.planeboardingcontrol.model.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityQueueTest {
    PriorityQueue<Integer, Reservation> queue;
    Random rnd;

    @BeforeEach
    void setup() {
        queue = new PriorityQueue<>();
        rnd = new Random();
    }

    void setUp2() throws KeyIsSmallerException {
        queue.maxHeapInsert(1, new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(2, new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(3, new Reservation(generateRandomId(), "3", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
    }

    void setUp3() throws KeyIsSmallerException {
        queue.maxHeapInsert(1, new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(2, new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(2, new Reservation(generateRandomId(), "2.2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(3, new Reservation(generateRandomId(), "3", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
    }

    boolean[] generateRandomCriteria() {
        boolean[] priority = new boolean[PriorityCriteria.values().length];
        for(int i=0; i<priority.length; i++) {
            priority[i] = rnd.nextBoolean();
        }
        return priority;
    }

    String generateRandomId() {
        char[] charId = new char[6];
        for(int i=0; i<charId.length; i++) {
            charId[i] = (char)rnd.nextInt(65,91);
        }
        return new String(charId);
    }

    //Heapify
    @Test
    void insertThreeElements() {
        // art
        Reservation newReservation2 = new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation newReservation1 = new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation newReservation = new Reservation(generateRandomId(), "0", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );

        queue.getArray().add(new PQNode<>(0, newReservation));
        queue.getArray().add(new PQNode<>(1, newReservation1));
        queue.getArray().add(new PQNode<>(2, newReservation2));

        // assert
        queue.maxHeapify(0);
        for (int i = 2; i >= 0; i--) {
            assertEquals(i, queue.getArray().get(i).getKey());
        }
    }

    @Test
    void insertThreeElementsSameKey() {
        // art
        Reservation newReservation2 = new Reservation(generateRandomId(), "2.1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation newReservation1 = new Reservation(generateRandomId(), "2.2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation newReservation = new Reservation(generateRandomId(), "4", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );

        queue.getArray().add(new PQNode<>(4, newReservation));
        queue.getArray().add(new PQNode<>(2, newReservation2));
        queue.getArray().add(new PQNode<>(2, newReservation1));

        // assert
        queue.maxHeapify(0);
        assertAll("Should return 4, 2.1, 2.2",
                () -> assertEquals("4",  queue.getArray().get(0).getValue().getPassengerName()),
                () -> assertEquals("2.1",  queue.getArray().get(1).getValue().getPassengerName()),
                () -> assertEquals("2.2",  queue.getArray().get(2).getValue().getPassengerName())
        );
    }

    @Test
    void insertThreeElementsAllSameKey() {
        // art
        Reservation newReservation2 = new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation newReservation1 = new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation newReservation = new Reservation(generateRandomId(), "0", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );

        queue.getArray().add(new PQNode<>(2, newReservation2));
        queue.getArray().add(new PQNode<>(2, newReservation1));
        queue.getArray().add(new PQNode<>(2, newReservation));

        // assert
        assertAll("Should return 2, 1, 0",
                () -> assertEquals(queue.getArray().get(0).getValue().getPassengerName(), "2"),
                () -> assertEquals(queue.getArray().get(1).getValue().getPassengerName(), "1"),
                () -> assertEquals(queue.getArray().get(2).getValue().getPassengerName(), "0")
        );
    }

    // Heap extract
    @Test
    void extractThreeElements() throws KeyIsSmallerException {
        // art
        setUp3();

        // assert
        assertAll("Should return 3, 2.2, 2",
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "3"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "2.2"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "2")
        );
    }

    @Test
    void extractThreeNormalElements() throws KeyIsSmallerException {
        // art
        setUp2();
        // assert
        assertAll("Should return 3, 2, 1",
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "3"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "2"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "1")
        );
    }

    @Test
    void extractFiveElementsException() {
        // art
        try {
            setUp2();
            queue.heapExtractMax().getPassengerName();
            queue.heapExtractMax().getPassengerName();
            queue.heapExtractMax().getPassengerName();
            queue.heapExtractMax().getPassengerName();
            queue.heapExtractMax().getPassengerName();
        } catch (KeyIsSmallerException e) {
            throw new RuntimeException(e);
        } catch (HeapUnderFlowException e) {
            // assert
            assertNotNull(e);
        }
    }

    //Heap Increase key
    @Test
    void increaseKeyNormal() throws KeyIsSmallerException {
        // art
        setUp3();
        queue.heapIncreaseKey(3, 4);
        //assert
        assertEquals("2.2", queue.heapMaximun().getPassengerName());
    }

    @Test
    void increaseKeyException() {
        // art
        try {
            setUp3();
            queue.heapIncreaseKey(0, 3);
        } catch (KeyIsSmallerException e) {
            assertNotNull(e);
        }
    }

    @Test
    void increaseKeyNegative() {
        // art
        try {
            setUp3();
            queue.heapIncreaseKey(1, -1);
        } catch (KeyIsSmallerException e) {
            //assert
            assertNotNull(e);
        }
    }

    //Max Heap Insert
    @Test
    void insertRepeatedKeys() throws KeyIsSmallerException {
        // art
        queue.maxHeapInsert(1, new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(0, new Reservation(generateRandomId(), "0", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(1, new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));

        //assert
        assertAll("Should return 1, 2, 0",
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "1"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "2"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "0")
        );
    }

    @Test
    void insertNormalKeys() throws KeyIsSmallerException {
        // art
        queue.maxHeapInsert(1, new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(2, new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(3, new Reservation(generateRandomId(), "3", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));

        //assert
        assertAll("Should return 3, 2, 1",
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "3"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "2"),
                () -> assertEquals(queue.heapExtractMax().getPassengerName(), "1")
        );
    }

    @Test
    void insertNegativeKeys() throws KeyIsSmallerException, HeapUnderFlowException {
        // art
        queue.maxHeapInsert(-1, new Reservation(generateRandomId(), "2", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(0, new Reservation(generateRandomId(), "0", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));
        queue.maxHeapInsert(-1, new Reservation(generateRandomId(), "1", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() ));

        //assert
        String [] result = {"0", "1", "2"};


        for (int i = 0; i < result.length; i++) {
            assertEquals(result[i], queue.heapExtractMax().getPassengerName());
        }
    }
}
