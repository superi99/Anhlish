package com.englishappdemo.utils;

import com.englishappdemo.Test;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class NLPUtils {

//    public static List<Span> chucking(String sentence) throws IOException {
//
//        TokenizerME tokenizer = Global.get.getTokenizerME();
//        String[] tokens = tokenizer.tokenize(sentence);
//
//        POSTaggerME posTagger = Global.getPosTaggerME();
//        String tags[] = posTagger.tag(tokens);
//
//        InputStream inputStreamChunker = Test.class
//                .getResourceAsStream("/models/en-chunker.bin");
//        ChunkerModel chunkerModel
//                = new ChunkerModel(inputStreamChunker);
//        ChunkerME chunker = Global.getChunkerME();
//
//        Span[] span = chunker.chunkAsSpans(tokens, tags);
//
//        //As we can see, we get an output for each token from the chunker.
//        // “B” represents the start of a chunk,
//        // “I” represents the continuation of the chunk and
//        // “O” represents no chunk.
//        return Arrays.asList(span);
//    }
}
