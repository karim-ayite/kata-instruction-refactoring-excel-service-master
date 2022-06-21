package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.CampaignService;
import com.newlight77.kata.survey.service.ExportCampaignService;
import com.newlight77.kata.survey.service.SurveyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/api")
/***
 * Should be in it own artifact !!!!!!!!!! Only exportCampaign method should be here
 */
public class CampaignControllerV2 {

    private final ExportCampaignService exportCampaignService;

    private final SurveyService surveyService;

    private final CampaignService campaignService;

    public CampaignControllerV2(final ExportCampaignService exportCampaignService, SurveyService surveyService, CampaignService campaignService) {
        this.exportCampaignService = exportCampaignService;
        this.surveyService = surveyService;
        this.campaignService = campaignService;
    }

    @PostMapping("/campaign")
    public void createCampaign(@RequestBody Campaign campaign) {
        campaignService.createCampaign(campaign);
    }

    @GetMapping("/campaign/{id}")
    public Campaign getCampaign(@PathVariable String id) {
        return campaignService.getCampaign(id);
    }

    @PostMapping(value = "/campaign/export")
    public void exportCampaign(@RequestBody String campaignId) {
        Campaign campaign = campaignService.getCampaign(campaignId);
        Survey survey = surveyService.getSurvey(campaign.getSurveyId());
        exportCampaignService.sendResults(campaign, survey);
    }
}
