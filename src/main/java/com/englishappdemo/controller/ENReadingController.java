package com.englishappdemo.controller;

import com.englishappdemo.model.Question;
import com.englishappdemo.service.OpenNLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ENReadingController {

    @Autowired
    private OpenNLP openNLP;

    @CrossOrigin
    @PostMapping("/make-question")
    public List<Question> makeQuestion(@RequestBody String paragraph){
        return openNLP.generateQuestion(paragraph);

    }

}
