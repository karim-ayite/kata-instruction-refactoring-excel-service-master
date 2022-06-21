package com.newlight77.kata.survey.controler;

import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import com.newlight77.kata.survey.service.ExportCampaignService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/api")
public class CampaignControllerV2 {

    private final ExportCampaignService exportCampaignService;

    public CampaignControllerV2(final ExportCampaignService exportCampaignService) {
        this.exportCampaignService = exportCampaignService;
    }

    @PostMapping("/campaign")
    public void createCampaign(@RequestBody Campaign campaign) {
        exportCampaignService.createCampaign(campaign);
    }

    @GetMapping("/campaign/{id}")
    public Campaign getCampaign(@PathVariable String id) {
        return exportCampaignService.getCampaign(id);
    }

    @PostMapping(value = "/campaign/export")
    public void exportCampaign(@RequestBody String campaignId) {
        Campaign campaign = exportCampaignService.getCampaign(campaignId);
        Survey survey = exportCampaignService.getSurvey(campaign.getSurveyId());
        exportCampaignService.sendResults(campaign, survey);
    }
}
