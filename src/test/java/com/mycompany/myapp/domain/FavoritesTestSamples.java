package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FavoritesTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Favorites getFavoritesSample1() {
        return new Favorites().id(1L).proficiencyLvl(1);
    }

    public static Favorites getFavoritesSample2() {
        return new Favorites().id(2L).proficiencyLvl(2);
    }

    public static Favorites getFavoritesRandomSampleGenerator() {
        return new Favorites().id(longCount.incrementAndGet()).proficiencyLvl(intCount.incrementAndGet());
    }
}
