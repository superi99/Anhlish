package com.englishappdemo;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() throws IOException {
//        String paragraph = "This is a statement. This is another statement."
//                + "Now is an abstract word for time, "
//                + "that is always flying. And my email address is google@gmail.com.";
//
//        InputStream is = getClass().getResourceAsStream("/models/en-sent.bin");
//        SentenceModel model = new SentenceModel(is);
//
//        SentenceDetectorME sdetector = new SentenceDetectorME(model);
//
//        String sentences[] = sdetector.sentDetect(paragraph);
//        assert (sentences).contains(
//                "This is a statement.",
//                "This is another statement.",
//                "Now is an abstract word for time, that is always flying.",
//                "And my email address is google@gmail.com.");
    }

}
