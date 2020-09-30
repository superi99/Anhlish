package com.englishappdemo;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.*;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws IOException {

        //findName("");
        //tagging("");
        chucking("");

//        WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
//        String[] tokens = tokenizer.tokenize("It’s been a while — like");
//        System.out.println();
    }

    public static void splitSentence(String para) throws IOException {
        String paragraph = "It’s been a while — like, forever — since anyone claimed to have discovered life on Venus.";

        InputStream is = Test.class.getResourceAsStream("/models/en-sent.bin");
        SentenceModel model = new SentenceModel(is);

        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String[] sentences = sdetector.sentDetect(paragraph);
        System.out.println(Arrays.toString(sentences));
    }

    public static void getTokenizer(String sentence) throws IOException {
        InputStream inputStream = Test.class
                .getResourceAsStream("/models/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize("Baeldung is a Spring Resource. the car park");

//        assertThat(tokens).contains(
//                "Baeldung", "is", "a", "Spring", "Resource", ".");
        System.out.println(Arrays.toString(tokens));
    }

    public static void findName(String paragraph) throws IOException {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer
                .tokenize("John is 26 years old. His best friend's "
                        + "name is Leonard. He has a sister named Wick. The company Google");

        InputStream inputStreamNameFinder = Test.class
                .getResourceAsStream("/models/en-ner-person.bin");
        TokenNameFinderModel model = new TokenNameFinderModel(
                inputStreamNameFinder);
        NameFinderME nameFinderME = new NameFinderME(model);
        List<Span> spans = Arrays.asList(nameFinderME.find(tokens));
        System.out.println(spans);

//        assertThat(spans.toString())
//                .isEqualTo("[[0..1) person, [13..14) person, [20..21) person]");
    }

    public static void tagging(String paragraph) throws IOException {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize("John has a sister named Penny. The car park");

        InputStream inputStreamPOSTagger = Test.class
                .getResourceAsStream("/models/en-pos-maxent.bin");
        POSModel posModel = new POSModel(inputStreamPOSTagger);
        POSTaggerME posTagger = new POSTaggerME(posModel);
        String tags[] = posTagger.tag(tokens);

        // assertThat(tags).contains("NNP", "VBZ", "DT", "NN", "VBN", "NNP", ".");
        System.out.println(Arrays.toString(tags));
    }

    public static void chucking(String str) throws IOException {

        InputStream inputStream = Test.class
                .getResourceAsStream("/models/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize("The car park. I don’t know. It’s been a while — like the car park, forever — since anyone claimed to have discovered life on Venus. \n" +
                "\n" +
                "And truth be told, the scientists who announced on Sept. 14 the discovery of phosphine, a gas, in Venus’s atmosphere did not claim to have discovered life, either — only that they could not think of anything that might have produced it other than microbes in the clouds. “ We’re not saying we discovered life on Venus, ”Sara Seager of the Massachusetts Institute of Technology said in an interview a few days before the announcement.\n" +
                "\n" +
                "On Earth, anyway, the only natural source of phosphine is microbes; the gas is often associated with feces. But it would hardly rank as a surprise to find out that scientists don’t know everything there is to know yet about the geochemistry of Venus, our nearest but rarely visited neighbor in the solar system.");



        InputStream inputStreamPOSTagger = Test.class
                .getResourceAsStream("/models/en-pos-maxent.bin");

        POSModel posModel = new POSModel(inputStreamPOSTagger);
        POSTaggerME posTagger = new POSTaggerME(posModel);
        String tags[] = posTagger.tag(tokens);

        InputStream inputStreamChunker = Test.class
                .getResourceAsStream("/models/en-chunker.bin");
        ChunkerModel chunkerModel
                = new ChunkerModel(inputStreamChunker);
        ChunkerME chunker = new ChunkerME(chunkerModel);
        String[] chunks = chunker.chunk(tokens, tags);

        Span[] span = chunker.chunkAsSpans(tokens, tags);
        List<String> words = new ArrayList<>();
        for (Span s : span) {
            String word = "";
            for (int i = s.getStart(); i < s.getEnd(); i++) {
                word += " " + tokens[i];
            }
//            String word = "He reckons the current account deficit will narrow to only 8 billion. The car park".substring(s.getStart(), s.getEnd());
            words.add(word);
        }
        System.out.println();
        //As we can see, we get an output for each token from the chunker.
        // “B” represents the start of a chunk,
        // “I” represents the continuation of the chunk and
        // “O” represents no chunk.
        System.out.println(Arrays.toString(chunks));


        System.out.println(words);
    }

}




