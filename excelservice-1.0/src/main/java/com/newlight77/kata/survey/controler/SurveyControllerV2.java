package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/api")
public class SurveyControllerV2 {

    private final ExportCampaignService exportCampaignService;


    public SurveyControllerV2(final ExportCampaignService exportCampaignService) {
        this.exportCampaignService = exportCampaignService;
    }

    @PostMapping("/survey")
    public void createSurvey(@RequestBody Survey survey) {
        exportCampaignService.creerSurvey(survey);
    }


    @GetMapping("/campaign/{id}")
    public Survey getSurvey(@PathVariable String id) {
        return exportCampaignService.getSurvey(id);
    }


}

