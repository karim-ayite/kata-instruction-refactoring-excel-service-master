package com.newlight77.kata.survey.service;

import com.newlight77.kata.survey.exception.ExcelServiceException;
import com.newlight77.kata.survey.model.AddressStatus;
import com.newlight77.kata.survey.model.Campaign;
import com.newlight77.kata.survey.model.Survey;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ExportCampaignService {


    private final MailService mailService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Logger logger = LoggerFactory.getLogger(ExportCampaignService.class);


    public ExportCampaignService(MailService mailService) {
        this.mailService = mailService;
    }


    public void sendResults(Campaign campaign, Survey survey) {

        XSSFWorkbook workbook = WorkBookGeneratorHelper.generateFrom(campaign, survey);

        writeFileAndSend(survey, workbook);

    }

    protected void writeFileAndSend(Survey survey, Workbook workbook) {
        String filename = "survey-" + survey.getId() + "-" + dateTimeFormatter.format(LocalDate.now()) + ".xlsx";
        String systemTempDir = System.getProperty("java.io.tmpdir");

        File resultFile = new File(systemTempDir, filename);

        try (FileOutputStream outputStream = new FileOutputStream(resultFile)) {
            workbook.write(outputStream);
            mailService.send(resultFile);
            resultFile.deleteOnExit();
        } catch (Exception ex) {
            throw new ExcelServiceException("Error while trying to send email", ex);
        } finally {
            try {
                workbook.close();
            } catch (Exception e) {
                logger.info("Well, it should not happen, but it happened -> workbook close() exception");
            }
        }
    }

}
