package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.client.CampaignClient;
import com.newlight77.kata.survey.model.Survey;
import org.springframework.stereotype.Service;

@Service
public class SurveyService {

    private final CampaignClient campaignWebService;

    public SurveyService(final CampaignClient campaignWebService) {
        this.campaignWebService = campaignWebService;
    }

    public void creerSurvey(Survey survey) {
        campaignWebService.createSurvey(survey);
    }

    public Survey getSurvey(String id) {
        return campaignWebService.getSurvey(id);
    }


}
