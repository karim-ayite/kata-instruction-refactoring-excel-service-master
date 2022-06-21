package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.CampaignService;
import com.newlight77.kata.survey.service.ExportCampaignService;
import com.newlight77.kata.survey.service.SurveyService;
import org.springframework.web.bind.annotation.*;

@RestController
/**
 * @deprecated Use CampaignController and SurveyControllerV2
 */
@Deprecated
public class SurveyController {

    private final SurveyService surveyService;

    private final CampaignService campaignService;

    private final ExportCampaignService exportCampaignService;

    public SurveyController(SurveyService surveyService, CampaignService campaignService, final ExportCampaignService exportCampaignService) {
        this.surveyService = surveyService;
        this.campaignService = campaignService;
        this.exportCampaignService = exportCampaignService;
    }

    @RequestMapping(value = "/api/survey/create", method = RequestMethod.POST)
    public void createSurvey(@RequestBody Survey survey) {
        surveyService.creerSurvey(survey);
    }

    @RequestMapping(value = "/api/survey/get", method = RequestMethod.GET)
    public Survey getSurvey(@RequestParam String id) {
        return surveyService.getSurvey(id);
    }

    @RequestMapping(value = "/api/survey/campaign/create", method = RequestMethod.POST)
    public void createCampaign(@RequestBody Campaign campaign) {
        campaignService.createCampaign(campaign);
    }

    @RequestMapping(value = "/api/survey/campaign/get", method = RequestMethod.GET)
    public Campaign getCampaign(@RequestParam String id) {
        return campaignService.getCampaign(id);
    }

    @RequestMapping(value = "/api/survey/campaign/export", method = RequestMethod.POST)
    public void exportCampaign(@RequestParam String campaignId) {

        Campaign campaign = campaignService.getCampaign(campaignId);
        Survey survey = surveyService.getSurvey(campaign.getSurveyId());
        exportCampaignService.sendResults(campaign, survey);

    }
}

