package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WordTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Word getWordSample1() {
        return new Word()
            .id(1L)
            .language("language1")
            .wordText("wordText1")
            .partOfSpeech("partOfSpeech1")
            .pronunciation("pronunciation1")
            .audio("audio1")
            .definition("definition1")
            .exampleSentence("exampleSentence1")
            .etymology("etymology1");
    }

    public static Word getWordSample2() {
        return new Word()
            .id(2L)
            .language("language2")
            .wordText("wordText2")
            .partOfSpeech("partOfSpeech2")
            .pronunciation("pronunciation2")
            .audio("audio2")
            .definition("definition2")
            .exampleSentence("exampleSentence2")
            .etymology("etymology2");
    }

    public static Word getWordRandomSampleGenerator() {
        return new Word()
            .id(longCount.incrementAndGet())
            .language(UUID.randomUUID().toString())
            .wordText(UUID.randomUUID().toString())
            .partOfSpeech(UUID.randomUUID().toString())
            .pronunciation(UUID.randomUUID().toString())
            .audio(UUID.randomUUID().toString())
            .definition(UUID.randomUUID().toString())
            .exampleSentence(UUID.randomUUID().toString())
            .etymology(UUID.randomUUID().toString());
    }
}
