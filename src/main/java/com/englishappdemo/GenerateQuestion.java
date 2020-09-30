package com.englishappdemo;

import com.englishappdemo.model.Answer;
import com.englishappdemo.model.Question;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class GenerateQuestion {

    public static String spanToString(String text, Span span) {
        if (!"".equals(text) && span != null) {
            try {
                InputStream inputStream = Test.class
                        .getResourceAsStream("/models/en-token.bin");
                TokenizerModel model = new TokenizerModel(inputStream);
                TokenizerME tokenizer = new TokenizerME(model);
                String[] tokens = tokenizer.tokenize(text);

                StringBuilder word = new StringBuilder();
                for (int i = span.getStart(); i < span.getEnd(); i++) {
                    word.append(tokens[i]).append(" ");
                }
                return word.toString().trim();
            }catch (IOException e) {
                e.printStackTrace();
            }

        }
        return "";
    }

    public static List<String> spanToString(String text, List<Span> spans) {
        List<String> words = new ArrayList<>();
        if (!"".equals(text) && spans.size() > 0) {
            for (Span s : spans) {
                words.add(spanToString(text, s));
            }
        }

        return words;
    }

    public static List<Span> filterWords(String text) throws IOException {
        return chucking(text).stream().filter(span -> span.getType().matches("NP")).collect(Collectors.toList());
    }

    public static void main(String[] args) throws IOException {
        List<Question> questions = new ArrayList<>();
        String paragraph = "It’s been a while — like, forever — since anyone claimed to have discovered life on Venus. \n" +
                "\n" +
                "And truth be told, the scientists who announced on Sept. 14 the discovery of phosphine, a gas, in Venus’s atmosphere did not claim to have discovered life, either — only that they could not think of anything that might have produced it other than microbes in the clouds. “We’re not saying we discovered life on Venus, ”Sara Seager of the Massachusetts Institute of Technology said in an interview a few days before the announcement.\n" +
                "\n" +
                "On Earth, anyway, the only natural source of phosphine is microbes; the gas is often associated with feces. But it would hardly rank as a surprise to find out that scientists don’t know everything there is to know yet about the geochemistry of Venus, our nearest but rarely visited neighbor in the solar system.";
        List<Span> filter =  filterWords(paragraph);
        List<String> answerBank = new ArrayList<>(
                new HashSet<>(spanToString(paragraph, filterWords(paragraph))));

        List<String> senteces = splitSentence(paragraph);
        for (String sentence : senteces) {
            List<Span> spans = filterWords(sentence);
            Question question = generateQuestion(sentence, spans, answerBank, 4, 1);
            System.out.println();
        }
        System.out.println();

    }

    public static List<String> splitSentence(String paragraph) throws IOException {

        InputStream is = Test.class.getResourceAsStream("/models/en-sent.bin");
        SentenceModel model = new SentenceModel(is);

        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String[] sentences = sdetector.sentDetect(paragraph);
        return Arrays.asList(sentences);
    }

    public static List<String> getTokenizer(String sentence) throws IOException {
        InputStream inputStream = Test.class
                .getResourceAsStream("/models/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize(sentence);

//        assertThat(tokens).contains(
//                "Baeldung", "is", "a", "Spring", "Resource", ".");
        return Arrays.asList(tokens);
    }

    public static List<Span> chucking(String sentence) throws IOException {
        InputStream inputStream = Test.class
                .getResourceAsStream("/models/en-token.bin");
        TokenizerModel model = new TokenizerModel(inputStream);
        TokenizerME tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize(sentence);

//        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
////        Span[] spans = tokenizer.tokenizePos("the car park");
//        String[] tokens = tokenizer.tokenize(sentence);

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
//        String[] chunks = chunker.chunk(tokens, tags);

        Span[] span = chunker.chunkAsSpans(tokens, tags);

        //As we can see, we get an output for each token from the chunker.
        // “B” represents the start of a chunk,
        // “I” represents the continuation of the chunk and
        // “O” represents no chunk.
        return Arrays.asList(span);
    }

//    public static List<Answer> generateWrongAnswers(List<String> bankAnswer, int numbAnswer) {
//
//    }

    public static Question generateQuestion(String sentence, List<Span> allAnswer, List<String> answerBank, int numberAnswer, int numberBlank) {

        if (allAnswer.size() > 0) {
            //filter span is noun, verb, adj
//            List<Span> trueAnswers = allAnswer.stream().filter(span -> span.getType().matches("NP|ADJP|ADVP|CONJP ")).collect(Collectors.toList());

            //random choose answer
            List<Span> trueAnswers = new ArrayList<>(allAnswer);
            Collections.shuffle(trueAnswers);
            trueAnswers = trueAnswers.subList(0, Math.min(trueAnswers.size(), numberBlank));
            Collections.sort(trueAnswers);

            //remove true answer in wrongAnswers
            List<String> wrongAnswers = new ArrayList<>(answerBank);
            wrongAnswers.removeAll(spanToString(sentence,trueAnswers));
            // random wrong answer
            Collections.shuffle(wrongAnswers);
            // thêm tìm đúng loại từ để tăng độ khó cho bài đọc. VD: đáp án là từ NP thì đáp án sai cũng là NP

            if (trueAnswers.size() > 0) {
                String question = generateBlank(sentence, trueAnswers);
                List<Answer> answers = new ArrayList<>();
                String trueAnswer = "";
                for (Span trueAns : trueAnswers) {
                    trueAnswer += spanToString(sentence, trueAns) + " - ";
                }
                trueAnswer = trueAnswer.substring(0, trueAnswer.lastIndexOf(" - "));
                answers.add(new Answer(trueAnswer, true));

                for (int indexAns = 0; numberAnswer < wrongAnswers.size() && numberAnswer > 1 && indexAns < numberAnswer - 1; indexAns++) {
                    answers.add(new Answer(wrongAnswers.get(indexAns), false));
                }
                // shuffle answer
                Collections.shuffle(answers);
                return new Question(question, answers);
            }


        }

        return null;
    }

    public static String generateBlank(String sentence, List<Span> blanks) {

        String blSentece = sentence;
        if (!blanks.isEmpty()) {
            int lastBlank = 0;
            for (Span blank : blanks) {
                blSentece = blSentece.replace(spanToString(sentence, blank), "...");
//                blSentece += sentence.substring(lastBlank, blank.getStart()) + "...";
//                lastBlank = blank.getEnd();
            }
        }

        return blSentece;
    }


}
