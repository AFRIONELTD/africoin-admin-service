package com.afrione.africoinservice.infrastructure.serviceimpl;


import com.afrione.africoinservice.domain.services.ApplicationProperty;
import com.afrione.africoinservice.domain.services.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;


@Slf4j
@Service
public class SequenceGeneratorImpl implements SequenceGenerator {

    private static final AtomicLong SEQ = new AtomicLong(123456);


    private static final int NODE_ID_BITS = 10;
    private static final int SEQUENCE_BITS = 12;

    private static final int maxNodeId = (int)(Math.pow(2, NODE_ID_BITS) - 1);
    private static final int maxSequence = (int)(Math.pow(2, SEQUENCE_BITS) - 1);

    // Custom Epoch (January 1, 2020 Midnight UTC = 2019-01-01T00:00:00Z)
    private static final long CUSTOM_EPOCH = 1577836800000L;

    private final int nodeId;
    private volatile long lastTimestamp = -1L;
    private volatile long sequence = 0L;

    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final ApplicationProperty applicationProperty;

    public SequenceGeneratorImpl(@Value("${machine.id:1}") int machineId, ApplicationProperty applicationProperty) {
        this.nodeId = machineId;
        this.applicationProperty = applicationProperty;
       // log.info("Sequence Node Id: - {}.", nodeId);
    }

    @Override
    public String nextSequenceId() {
        return String.valueOf(generateSequenceId());
    }



    @Override
    public String generateCode(int size) {
        return "123456";
    }

    @Override
    public String generateID(int alphaLength, int numericLength) {

        if (alphaLength < 1 || numericLength < 1) {
            throw new IllegalArgumentException("Alpha and numeric lengths must be at least 1");
        }

        StringBuilder sb = new StringBuilder(alphaLength + numericLength);

        // Generate alphabetic characters
        for (int i = 0; i < alphaLength; i++) {
            sb.append(ALPHABETS.charAt(RANDOM.nextInt(ALPHABETS.length())));
        }

        // Generate numeric characters
        int maxNumber = (int) Math.pow(10, numericLength);
        int number = RANDOM.nextInt(maxNumber);
        sb.append(String.format("%0" + numericLength + "d", number));

        return sb.toString();
    }


    private Long generateSequenceId() {
        //long nowT = System.currentTimeMillis();
        long currentTimestamp = timestamp();

        if(currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("Invalid System Clock!");
        }
        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & maxSequence;
            if(sequence == 0) {
                // Sequence Exhausted, wait till next millisecond.
                currentTimestamp = waitNextMillis(currentTimestamp);
            }
        } else {
            // reset sequence to start with zero for the next millisecond
            sequence = 0;
        }
        lastTimestamp = currentTimestamp;
        long id = currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS);
        id |= (nodeId << SEQUENCE_BITS);
        id |= sequence;

        //long endTime = System.currentTimeMillis(); // Record end time
        // long elapsedTime = endTime - nowT;
        // System.out.println("Time taken for generateSequenceId - "+elapsedTime+"ms");
        return id;
    }

    // Block and wait till next millisecond
    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            System.out.println("waiting...");
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }

    // Get current timestamp in milliseconds, adjust for the custom epoch.
    private static long timestamp() {
        return Instant.now().toEpochMilli() - CUSTOM_EPOCH;
    }
}
