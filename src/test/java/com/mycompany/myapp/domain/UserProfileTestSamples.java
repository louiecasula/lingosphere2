package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id(1L)
            .name("name1")
            .email("email1")
            .password("password1")
            .nativeLanguage("nativeLanguage1")
            .targetLanguage("targetLanguage1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id(2L)
            .name("name2")
            .email("email2")
            .password("password2")
            .nativeLanguage("nativeLanguage2")
            .targetLanguage("targetLanguage2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .password(UUID.randomUUID().toString())
            .nativeLanguage(UUID.randomUUID().toString())
            .targetLanguage(UUID.randomUUID().toString());
    }
}
