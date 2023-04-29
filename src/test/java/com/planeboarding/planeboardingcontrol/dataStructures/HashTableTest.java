package com.planeboarding.planeboardingcontrol.dataStructures;

import com.planeboarding.planeboardingcontrol.model.*;
import com.planeboarding.planeboardingcontrol.exception.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HashTableTest {
    HashTable<String, Reservation> reservations;
    Random rnd;

    @BeforeEach
    void setup() {
        // arrange
        reservations = new HashTable<>();
        rnd = new Random();
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

    // INSERT METHOD

    @Test
    void insertElementEmptyTable() {
        // art
        Reservation newReservation = new Reservation(generateRandomId(), "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        try {
            reservations.insert(newReservation.getId(), newReservation);
        } catch(DuplicatedKeyException e) {
            e.getStackTrace();
        }

        // assert
        assertEquals(reservations.getTakenElements(), 1);
    }

    @Test
    void insertMoreElementsThanArrSize() {
        // art
        Reservation newReservation;
        int limit = reservations.getARR_SIZE()*2;
        for(int i=0; i<limit; i++) {
            newReservation = new Reservation(generateRandomId(), "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
            try {
                reservations.insert(newReservation.getId(), newReservation);
            } catch(DuplicatedKeyException e) {
                e.getStackTrace();
            }
        }
        // assert
        assertEquals(reservations.getTakenElements(), limit);
    }

    @Test
    void insertDuplicatedObjects() {
        // art
        Reservation p1 = new Reservation("AAAAAA", "Bob", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        Reservation p2 = new Reservation("AAAAAA", "Mario", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        boolean insertionFailed = false;
        try {
            reservations.insert(p1.getId(), p1);
            reservations.insert(p2.getId(), p2);
        } catch(DuplicatedKeyException e) {
            e.getStackTrace();
            insertionFailed = true;
        }
        // assert
        assertTrue(insertionFailed);
    }

    // SEARCH METHOD

    @Test
    void searchElementEmptyList() {
        // art
        Reservation foundReservation = reservations.search("ADERFS");
        // assert
        assertNull(foundReservation);
    }

    @Test
    void searchElementNonEmptyList() {
        // art
        Reservation newReservation = new Reservation("AAAAAA", "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        try {
            reservations.insert(newReservation.getId(), newReservation);
        } catch (DuplicatedKeyException e) {
            e.getStackTrace();
        }
        // assert
        assertEquals(reservations.search("AAAAAA"), newReservation);
    }

    @Test
    void searchElementNonEmptyListWrongKey() {
        // art
        Reservation newReservation = new Reservation(generateRandomId(), "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        try {
            reservations.insert(newReservation.getId(), newReservation);
        } catch (DuplicatedKeyException e) {
            e.getStackTrace();
        }
        // assert
        assertNotEquals(reservations.search("AABBRD"), newReservation);
    }

    // DELETE METHOD

    @Test
    void deleteElementEmptyList() {
        // art
        reservations.delete("AAAA");
        // assert
        assertEquals(reservations.getTakenElements(), 0);
    }

    @Test
    void deleteElementOneElement() {
        // art
        Reservation newReservation = new Reservation(generateRandomId(), "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        try {
            reservations.insert(newReservation.getId(), newReservation);
        } catch (DuplicatedKeyException e) {
            e.getStackTrace();
        }
        reservations.delete(newReservation.getId());
        // assert
        assertEquals(reservations.getTakenElements(), 0);
    }

    @Test
    void deleteElementMultipleElementsInList() {
        // art
        Reservation newReservation;
        int limit = 5;
        for(int i=0; i<limit; i++) {
            newReservation = new Reservation("A"+i, "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
            try {
                reservations.insert(newReservation.getId(), newReservation);
            } catch (DuplicatedKeyException e) {
                e.getStackTrace();
            }
        }
        newReservation = new Reservation(generateRandomId(), "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        try {
            reservations.insert(newReservation.getId(), newReservation);
        } catch (DuplicatedKeyException e) {
            e.getStackTrace();
        }

        reservations.delete(newReservation.getId());

        // assert
        assertEquals(reservations.getTakenElements(), limit);
    }

    @Test
    void deleteElementMultipleElementsCollision() {
        // art
        Reservation newReservation;
        int limit = reservations.getARR_SIZE()*4;
        for(int i=0; i<limit; i++) {
            newReservation = new Reservation("A"+i, "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
            try {
                reservations.insert(newReservation.getId(), newReservation);
            } catch (DuplicatedKeyException e) {
                e.getStackTrace();
            }
        }

        newReservation = new Reservation("AAAAAA", "Chris", generateRandomCriteria(), rnd.nextInt(), (char)rnd.nextInt() );
        try {
            reservations.insert(newReservation.getId(), newReservation);
        } catch (DuplicatedKeyException e) {
            e.getStackTrace();
        }

        int initialSize = reservations.getTakenElements();
        reservations.delete("AAAAAA");
        int finalSize = reservations.getTakenElements();

        // assert
        assertEquals(finalSize, initialSize - 1);
    }
}
