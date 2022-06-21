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


    public static final String LABEL_NUMBER_OF_SURVEYS = "Number of surveys";
    public static final String LABEL_N_STREET = "N° street";
    public static final String LABEL_STREET = "streee"; // Report spelling error to business
    public static final String LABEL_POSTAL_CODE = "Postal code";
    public static final String LABEL_CITY = "City";
    public static final String LABEL_STATUS = "Status";
    public static final String FONT_NAME = "Arial";
    public static final String HEADER_CELL_NAME = "Survey";
    public static final String LABEL_CLIENT = "Client";
    public static final int FIRST_COLUMN_WIDE = 10500;
    public static final int OTHERS_COLUMNS_WIDTH = 6000;
    public static final int NB_COLUMNS = 18;
    private final MailService mailService;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final Logger logger = LoggerFactory.getLogger(ExportCampaignService.class);


    public ExportCampaignService(MailService mailService) {
        this.mailService = mailService;
    }


    public void sendResults(Campaign campaign, Survey survey) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(HEADER_CELL_NAME);
        sheet.setColumnWidth(0, FIRST_COLUMN_WIDE);
        for (int i = 1; i <= NB_COLUMNS; i++) {
            sheet.setColumnWidth(i, OTHERS_COLUMNS_WIDTH);
        }

        // 1ere ligne =  l'entête
        Row header = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        XSSFFont font = workbook.createFont();
        font.setFontName(FONT_NAME);
        font.setFontHeightInPoints((short) 14);
        font.setBold(true);
        headerStyle.setFont(font);
        headerStyle.setWrapText(false);

        Cell headerCell = header.createCell(0);
        headerCell.setCellValue(HEADER_CELL_NAME);
        headerCell.setCellStyle(headerStyle);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontName(FONT_NAME);
        titleFont.setFontHeightInPoints((short) 12);
        titleFont.setUnderline(FontUnderline.SINGLE);
        titleStyle.setFont(titleFont);

        CellStyle style = workbook.createCellStyle();
        style.setWrapText(true);

        // section client
        createSectionClientRow(sheet, titleStyle);

        createClientRow(survey, sheet, style);

        createClientAddressLabelRow(survey, sheet, style);

        createNumberOfSurveyRow(campaign, sheet);

        createSurveyRow(sheet, style);

        int startIndex = 9;
        int currentIndex = 0;

        for (AddressStatus addressStatus : campaign.getAddressStatuses()) {

            currentIndex = createAddressStatusRow(sheet, style, startIndex, currentIndex, addressStatus);

        }

        writeFileAndSend(survey, workbook);

    }

    private void createNumberOfSurveyRow(Campaign campaign, Sheet sheet) {
        Cell cell;
        Row row;
        row = sheet.createRow(6);
        cell = row.createCell(0);
        cell.setCellValue(LABEL_NUMBER_OF_SURVEYS);
        cell = row.createCell(1);
        cell.setCellValue(campaign.getAddressStatuses().size());
    }

    private void createSectionClientRow(Sheet sheet, CellStyle titleStyle) {
        Row row = sheet.createRow(2);
        Cell cell = row.createCell(0);
        cell.setCellValue(LABEL_CLIENT);
        cell.setCellStyle(titleStyle);
    }

    private void createClientRow(Survey survey, Sheet sheet, CellStyle style) {
        Row clientRow = sheet.createRow(3);
        Cell nomClientRowLabel = clientRow.createCell(0);
        nomClientRowLabel.setCellValue(survey.getClient());
        nomClientRowLabel.setCellStyle(style);
    }

    private void createClientAddressLabelRow(Survey survey, Sheet sheet, CellStyle style) {
        Row clientAddressLabelRow = sheet.createRow(4);
        Cell clientAddressCell = clientAddressLabelRow.createCell(0);

        String clientAddress = survey.getClientAddress().getStreetNumber() + " "
                + survey.getClientAddress().getStreetName() + survey.getClientAddress().getPostalCode() + " "
                + survey.getClientAddress().getCity();

        clientAddressCell.setCellValue(clientAddress);
        clientAddressCell.setCellStyle(style);
    }

    private void createSurveyRow(Sheet sheet, CellStyle style) {
        Row surveyLabelRow = sheet.createRow(8);
        Cell surveyQuestionLabelCell = surveyLabelRow.createCell(0);
        surveyQuestionLabelCell.setCellValue(LABEL_N_STREET);
        surveyQuestionLabelCell.setCellStyle(style);

        surveyQuestionLabelCell = surveyLabelRow.createCell(1);
        surveyQuestionLabelCell.setCellValue(LABEL_STREET);
        surveyQuestionLabelCell.setCellStyle(style);

        surveyQuestionLabelCell = surveyLabelRow.createCell(2);
        surveyQuestionLabelCell.setCellValue(LABEL_POSTAL_CODE);
        surveyQuestionLabelCell.setCellStyle(style);

        surveyQuestionLabelCell = surveyLabelRow.createCell(3);
        surveyQuestionLabelCell.setCellValue(LABEL_CITY);
        surveyQuestionLabelCell.setCellStyle(style);

        surveyQuestionLabelCell = surveyLabelRow.createCell(4);
        surveyQuestionLabelCell.setCellValue(LABEL_STATUS);
        surveyQuestionLabelCell.setCellStyle(style);
    }

    private int createAddressStatusRow(Sheet sheet, CellStyle style, int startIndex, int currentIndex, AddressStatus addressStatus) {
        Row surveyRow = sheet.createRow(startIndex + currentIndex);
        Cell surveyRowCell = surveyRow.createCell(0);
        surveyRowCell.setCellValue(addressStatus.getAddress().getStreetNumber());
        surveyRowCell.setCellStyle(style);

        surveyRowCell = surveyRow.createCell(1);
        surveyRowCell.setCellValue(addressStatus.getAddress().getStreetName());
        surveyRowCell.setCellStyle(style);

        surveyRowCell = surveyRow.createCell(2);
        surveyRowCell.setCellValue(addressStatus.getAddress().getPostalCode());
        surveyRowCell.setCellStyle(style);

        surveyRowCell = surveyRow.createCell(3);
        surveyRowCell.setCellValue(addressStatus.getAddress().getCity());
        surveyRowCell.setCellStyle(style);

        surveyRowCell = surveyRow.createCell(4);
        surveyRowCell.setCellValue(addressStatus.getStatus().toString());
        surveyRowCell.setCellStyle(style);

        currentIndex++;
        return currentIndex;
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
