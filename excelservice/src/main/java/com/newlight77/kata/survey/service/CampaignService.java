package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.client.CampaignClient;
import com.newlight77.kata.survey.model.Campaign;
import org.springframework.stereotype.Service;

@Service
public class CampaignService {
    private final CampaignClient campaignWebService;

    public CampaignService(CampaignClient campaignWebService) {
        this.campaignWebService = campaignWebService;
    }

    public void createCampaign(Campaign campaign) {
        campaignWebService.createCampaign(campaign);
    }

    public Campaign getCampaign(String id) {
        return campaignWebService.getCampaign(id);
    }
}
