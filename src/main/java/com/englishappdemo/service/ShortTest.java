package com.englishappdemo.service;

import com.englishappdemo.Test;
import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.ml.BeamSearch;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.*;
import opennlp.tools.tokenize.SimpleTokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

public class ShortTest {
    public static void main(String[] args) throws IOException {
//        //Loading parser model
//        InputStream inputStream = ShortTest.class.getResourceAsStream("/models/en-parser-chunking.bin");
//        ParserModel model = new ParserModel(inputStream);
//
//        Parser parser = ParserFactory.create(model);
//        //Parsing the sentence
//        String sentence = "I leave my car at the car park.";
//        Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);
//        for (Parse p : topParses)
//            p.show();
//        System.out.println();
        tagging("");
        System.out.println();


    }


    public static void tagging(String paragraph) throws IOException {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize("John has a sister named Penny. The car park");

        InputStream inputStreamPOSTagger = Test.class
                .getResourceAsStream("/models/en-pos-maxent.bin");
        POSModel posModel = new POSModel(inputStreamPOSTagger);
        POSTaggerFactory factory = posModel.getFactory();

        TagDictionary tagDictionary = factory.getTagDictionary();


        POSTaggerME posTagger = new POSTaggerME(posModel);
        POSDictionary posDictionary = (POSDictionary)tagDictionary;

        Iterator<String> postTags = posDictionary.iterator();
        while(postTags.hasNext()) {

            String postTag = postTags.next();

            System.out.println(postTag + "| type: " + Arrays.toString(posDictionary.getTags(postTag)));
        }

        String tags[] = posTagger.tag(tokens);

        // assertThat(tags).contains("NNP", "VBZ", "DT", "NN", "VBN", "NNP", ".");
        System.out.println(Arrays.toString(tags));
    }
}
