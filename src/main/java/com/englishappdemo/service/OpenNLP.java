package com.englishappdemo.service;

import com.englishappdemo.Test;
import com.englishappdemo.model.Answer;
import com.englishappdemo.model.Question;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OpenNLP {

    private static TokenizerME tokenizerME;

    private static POSTaggerME posTaggerME;

    private static ChunkerME chunkerME;

    private static SentenceDetectorME sentenceDetectorME;

    private static final String tagChosen = "NP|VBN|VBP|VBD";

    // load needed library
    static {
        try (InputStream inputStream = OpenNLP.class
                .getResourceAsStream("/models/en-token.bin");
             InputStream inputStreamPOSTagger = OpenNLP.class
                     .getResourceAsStream("/models/en-pos-maxent.bin");
             InputStream inputStreamChunker = OpenNLP.class
                     .getResourceAsStream("/models/en-chunker.bin");
             InputStream is = Test.class.getResourceAsStream("/models/en-sent.bin")) {

            TokenizerModel model = new TokenizerModel(inputStream);
            tokenizerME = new TokenizerME(model);

            POSModel posModel = new POSModel(inputStreamPOSTagger);
            posTaggerME = new POSTaggerME(posModel);

            ChunkerModel chunkerModel
                    = new ChunkerModel(inputStreamChunker);
            chunkerME = new ChunkerME(chunkerModel);


            SentenceModel sentenceModel = new SentenceModel(is);

            sentenceDetectorME = new SentenceDetectorME(sentenceModel);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Question> generateQuestion(String paragraph) {
        System.out.println("generate question with content: " + paragraph);
        List<Question> questions = new ArrayList<>();
        List<String> answerBank = new ArrayList<>(
                new HashSet<>(spanToString(paragraph, filterWords(paragraph))));

        List<String> senteces = splitSentence(paragraph);
        for (String sentence : senteces) {
            List<Span> spans = filterWords(sentence);
            Question question = generateQuestion(sentence, spans, answerBank, 4, 1);
            questions.add(question);
        }
        System.out.println("return question: " + questions.toString());
        return questions;
    }

    public List<Span> chucking(String sentence) {

        String[] tokens = tokenizerME.tokenize(sentence);

        POSTaggerME posTagger = posTaggerME;
        String[] tags = posTagger.tag(tokens);

        Span[] span = chunkerME.chunkAsSpans(tokens, tags);

        //As we can see, we get an output for each token from the chunker.
        // “B” represents the start of a chunk,
        // “I” represents the continuation of the chunk and
        // “O” represents no chunk.
        return Arrays.asList(span);
    }

    public Question generateQuestion(String sentence, List<Span> allAnswer, List<String> answerBank, int numberAnswer, int numberBlank) {

        if (allAnswer.size() > 0) {
            //random choose answer
            List<Span> trueAnswers = new ArrayList<>(allAnswer);
            Collections.shuffle(trueAnswers);
            trueAnswers = trueAnswers.subList(0, Math.min(trueAnswers.size(), numberBlank));
            Collections.sort(trueAnswers);

            //remove true answer in wrongAnswers
            List<String> wrongAnswers = new ArrayList<>(answerBank);
            wrongAnswers.removeAll(spanToString(sentence, trueAnswers));
            // random wrong answer
            Collections.shuffle(wrongAnswers);
            // thêm tìm đúng loại từ để tăng độ khó cho bài đọc. VD: đáp án là từ NP thì đáp án sai cũng là NP

            if (trueAnswers.size() > 0) {
                String question = generateBlank(sentence, trueAnswers);
                List<Answer> answers = new ArrayList<>();
                StringBuilder trueAnswer = new StringBuilder();
                for (Span trueAns : trueAnswers) {
                    trueAnswer.append(spanToString(sentence, trueAns)).append(" - ");
                }
                trueAnswer = new StringBuilder(trueAnswer.substring(0, trueAnswer.lastIndexOf(" - ")));
                answers.add(new Answer(trueAnswer.toString(), true));

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

    public String generateBlank(String sentence, List<Span> blanks) {

        String blSentece = sentence;
        if (!blanks.isEmpty()) {
            int lastBlank = 0;
            for (Span blank : blanks) {
                blSentece = blSentece.replace(spanToString(sentence, blank), " ... ");
//                blSentece += sentence.substring(lastBlank, blank.getStart()) + "...";
//                lastBlank = blank.getEnd();
            }
        }

        return blSentece;
    }

    public String spanToString(String text, Span span) {
        if (!"".equals(text) && span != null) {
            String[] tokens = tokenizerME.tokenize(text);

            StringBuilder word = new StringBuilder();
            for (int i = span.getStart(); i < span.getEnd(); i++) {
                word.append(tokens[i]).append(" ");
            }
            return word.toString().trim();

        }
        return "";
    }

    public List<String> spanToString(String text, List<Span> spans) {
        List<String> words = new ArrayList<>();
        if (!"".equals(text) && spans.size() > 0) {
            for (Span s : spans) {
                words.add(spanToString(text, s));
            }
        }

        return words;
    }

    public List<Span> filterWords(String text) {
        return chucking(text).stream().filter(span -> span.getType().matches(tagChosen)).collect(Collectors.toList());
    }

    public List<String> splitSentence(String paragraph) {
        String[] sentences = sentenceDetectorME.sentDetect(paragraph);
        return Arrays.asList(sentences);
    }


}
