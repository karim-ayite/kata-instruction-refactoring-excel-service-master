package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.SurveyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/api")
/***
 * Should be in it own artifact !!!!!!!!!!
 */
public class SurveyControllerV2 {

    private final SurveyService surveyService;

    public SurveyControllerV2(final SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/survey")
    public void createSurvey(@RequestBody Survey survey) {
        surveyService.creerSurvey(survey);
    }


    @GetMapping("/survey/{id}")
    public Survey getSurvey(@PathVariable String id) {
        return surveyService.getSurvey(id);
    }


}

