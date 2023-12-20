package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ArchiveTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Archive getArchiveSample1() {
        return new Archive().id(1L).proficiencyLvl(1);
    }

    public static Archive getArchiveSample2() {
        return new Archive().id(2L).proficiencyLvl(2);
    }

    public static Archive getArchiveRandomSampleGenerator() {
        return new Archive().id(longCount.incrementAndGet()).proficiencyLvl(intCount.incrementAndGet());
    }
}
