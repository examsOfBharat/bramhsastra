package com.examsofbharat.bramhsastra.akash.utils;

import com.examsofbharat.bramhsastra.jal.dto.*;
import com.examsofbharat.bramhsastra.jal.dto.request.AdmitCardRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.EnrichedFormDetailsDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.ResultRequestDTO;
import com.examsofbharat.bramhsastra.jal.dto.request.admin.AdminGenericResponseV1;
import com.examsofbharat.bramhsastra.prithvi.entity.UserDetails;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class PdfGeneratorUtil {
    private static float maxWidth = 600f;
    private static float table1Width = 300f; // Adjust as needed based on your content
    private static float table2Width = 300f;
    private static float titleCol = 150f;
    private static float contentCol = 350f;
    private static float contentWidth[] = {titleCol, contentCol};
    private static float fullLength[] = {maxWidth};
    private static float twoCol = 300f;
    private static float twoColWidth[] = {twoCol, twoCol};
    private static float towHalfCol[] = {titleCol, titleCol};
    private static  Paragraph oneSep = new Paragraph("\n");

    public static byte[] generatePdf(AdminGenericResponseV1 object) throws FileNotFoundException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        Paragraph heading = getPageHeader("Admit Card details");
        document.add(heading);

        Table table = new Table(twoColWidth);
        table.addCell(new Cell().add("Admin name: Bibhu Bhushan").setBorder(Border.NO_BORDER).setBold());
        table.addCell(new Cell().add("AppIdRef: " + object.getAppIdRef()).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        document.add(table);

        Border border = new SolidBorder(Color.GRAY, 2);
        Table divider = new Table(fullLength);
        divider.setBorder(border);
        document.add(divider);
        document.add(oneSep);

        document.add(addKeyValue("Admit card Name", object.getTitle()));
        document.add(addKeyValue("Download Url", object.getDownloadUrl()));
        document.add(addKeyValue("Heading", object.getHeading()));
        document.add(addKeyValue("Body", object.getBody()));
        document.add(addKeyValue("Exam Date", object.getShowDate().toString()));

        document.close();

        return outputStream.toByteArray();
    }

    public static byte[] generateResultPdf(AdminGenericResponseV1 object, UserDetails userDetails) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        Paragraph heading = getPageHeader("Result details");
        document.add(heading);

        Table table = new Table(twoColWidth);
        table.addCell(new Cell().add("Admin name: " + userDetails.getFirstName()).setBorder(Border.NO_BORDER).setBold());
        table.addCell(new Cell().add("AppIdRef: " + object.getAppIdRef()).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));
        document.add(table);

        Border border = new SolidBorder(Color.GRAY, 2);
        Table divider = new Table(fullLength);
        divider.setBorder(border);
        document.add(divider);
        document.add(oneSep);

        document.add(addKeyValue("Result Name", object.getTitle()));
        document.add(addKeyValue("Result Url", object.getDownloadUrl()));
        document.add(addKeyValue("Heading", object.getHeading()));
        document.add(addKeyValue("Body", object.getBody()));
        document.add(addKeyValue("Result Date", object.getUpdateDate().toString()));

        document.close();

        return outputStream.toByteArray();
    }


    private static Cell getCell10fLeft(String textValue, boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? myCell.setBold() : myCell;
    }
    private static Cell getCell10fLeftBorder(String textValue, boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setTextAlignment(TextAlignment.LEFT);
        return isBold ? myCell.setBold() : myCell;
    }

    private static Cell getCell10fRight(String textValue, boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
        return isBold ? myCell.setBold() : myCell;
    }

    private static Paragraph getPageHeader(String textvalue){
        Paragraph heading = new Paragraph(textvalue);
        heading.setBold();
        heading.setFontSize(14f);
        heading.setTextAlignment(TextAlignment.CENTER);

        return heading;
    }

    private static Table addHeading(String userName, String appId){
        Table table = new Table(twoColWidth);
        table.addCell(new Cell().add("Admin name: " + userName).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.LEFT));
        table.addCell(new Cell().add("AppIdRef: " + appId).setBorder(Border.NO_BORDER).setBold().setTextAlignment(TextAlignment.RIGHT));

        return table;
    }

    private static Table addKeyValue(String key, String value){
        Table dataTable = new Table(contentWidth);
        dataTable.addCell(getCell10fLeft(key, false));
        dataTable.addCell(getCell10fLeft(value, false));

        return dataTable;
    }

    private static Table addHeadingBody(String key, String value){
        Table dataTable = new Table(fullLength);
        dataTable.addCell(getCell10fLeft(key, false));
        dataTable.addCell(getCell10fLeft(value, false));

        return dataTable;
    }

    private static Table subHeading(String title){
        Table subHeading1 = new Table(fullLength);
        subHeading1.addCell(new Cell().add(title).setFontSize(12f).setBorder(Border.NO_BORDER).setBold().setFontColor(Color.BLUE).setTextAlignment(TextAlignment.CENTER));
        return subHeading1;
    }
    
    public static byte[] generateFormPdfDoc(EnrichedFormDetailsDTO enrichedFormDetailsDTO){
        byte[] formPdf = new byte[0];
        try{
            formPdf = generateFormPdf(enrichedFormDetailsDTO);
        }catch (Exception e){
            log.info("Exception occurred while generating pdf data",e);
        }
        return formPdf;
    }


    public static byte[] generateFormPdf(EnrichedFormDetailsDTO enrichedFormDetailsDTO) throws FileNotFoundException, IllegalAccessException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        String path = "form.pdf";
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.setDefaultPageSize(PageSize.A4);

        Document document = new Document(pdfDocument);

        Table heading = new Table(fullLength);
        heading.addCell(new Cell().add("Application Form").setFontSize(16f).setBorder(Border.NO_BORDER).setBold().setFontColor(Color.BLUE).setTextAlignment(TextAlignment.CENTER));
        document.add(heading);

        document.add(oneSep);
        document.add(addHeading("BIBHU", enrichedFormDetailsDTO.getAdminUserDetailsDTO().getUserId()));

        Border border = new SolidBorder(Color.GRAY, 2);
        Table divider = new Table(fullLength);
        divider.setBorder(border);
        document.add(divider);
        document.add(oneSep);

        //Application form
        document.add(subHeading("Application Form Details"));

        ApplicationFormDTO applicationFormDTO = enrichedFormDetailsDTO.getApplicationFormDTO();
        updateDocumentWithAdjField(ApplicationFormDTO.class, applicationFormDTO, document);
        document.add(oneSep);

        //Application fee
        document.add(subHeading("Application Fee"));
        updateAppFeeDetails(ApplicationFeeDTO.class, enrichedFormDetailsDTO.getApplicationFeeDTO(), document);
        document.add(oneSep);

        //Application vacancy
        document.add(subHeading("Application Vacancy"));
        updateVacancyData(ApplicationVacancyDTO.class, enrichedFormDetailsDTO.getApplicationVacancyDTOS(), document);
        document.add(oneSep);


        //Application Eligibility
        document.add(subHeading("Application Eligibility"));
        updateEligibilityData(ApplicationEligibilityDTO.class, enrichedFormDetailsDTO.getApplicationEligibilityDTOS(), document);
        document.add(oneSep);

        //Application Eligibility
        document.add(subHeading("Application Content"));
        updateContentData(ApplicationContentManagerDTO.class, enrichedFormDetailsDTO.getApplicationContentManagerDTO(), document);
        document.close();

        return outputStream.toByteArray();
    }

    private static void updateDocumentWithAdjField(Class<?> clas, ApplicationFormDTO applicationFormDTO, Document document) throws IllegalAccessException {
        for(Field field : clas.getDeclaredFields()){
            field.setAccessible(true);
            document.add(addKeyValue(field.getName(), field.get(applicationFormDTO)!=null ? field.get(applicationFormDTO).toString(): "NOT FILLED"));
        }
    }

    private static void updateAppFeeDetails(Class<?> clas, ApplicationFeeDTO applicationFeeDTO,
                                            Document document) throws IllegalAccessException {
        for(Field field : clas.getDeclaredFields()){
            field.setAccessible(true);
            document.add(addKeyValue(field.getName(), field.get(applicationFeeDTO)!=null ? field.get(applicationFeeDTO).toString(): "NOT FILLED"));
        }
    }

    private static void updateVacancyData(Class<?> clas,
                                          List<ApplicationVacancyDTO> applicationVacancyDTOS,
                                          Document document) throws IllegalAccessException {

        Table outerTable = new Table(twoColWidth);;

        int i = 1;
        for(ApplicationVacancyDTO vacancyDTO : applicationVacancyDTOS){
            if(i % 2 == 1){
                Table leftTable  = new Table(towHalfCol);
                for(Field field : clas.getDeclaredFields()){
                    field.setAccessible(true);
                    Cell cell1 = getCell10fLeftBorder(field.getName(),false);
                    Cell cell2 = getCell10fLeftBorder(field.get(vacancyDTO)!=null ? field.get(vacancyDTO).toString(): "NOT FILLED", false);
                    leftTable.addCell(cell1);
                    leftTable.addCell(cell2);
                }
                Cell cell1 = new Cell();
                cell1.add(leftTable);
                outerTable.addCell(cell1);
            }else{
                Table rightTable  = new Table(towHalfCol);
                for(Field field : clas.getDeclaredFields()){
                    field.setAccessible(true);
                    Cell cell1 = getCell10fLeftBorder(field.getName(),false);
                    Cell cell2 = getCell10fLeftBorder(field.get(vacancyDTO)!=null ? field.get(vacancyDTO).toString(): "NOT FILLED", false);
                    rightTable.addCell(cell1);
                    rightTable.addCell(cell2);
                }
                Cell cell2 = new Cell();
                cell2.add(rightTable);
                outerTable.addCell(cell2);
            }
            i++;
        }
        document.add(outerTable);
    }


    private static void updateEligibilityData(Class<?> clas,
                                          List<ApplicationEligibilityDTO> eligibilityDataDTOS,
                                          Document document) throws IllegalAccessException {

        Table outerTable = new Table(twoColWidth);;

        int i = 1;
        for(ApplicationEligibilityDTO eligibilityDataDTO : eligibilityDataDTOS){
            if(i % 2 == 1){
                Table leftTable  = new Table(towHalfCol);
                for(Field field : clas.getDeclaredFields()){
                    field.setAccessible(true);
                    Cell cell1 = getCell10fLeftBorder(field.getName(),false);
                    Cell cell2 = getCell10fLeftBorder(field.get(eligibilityDataDTO)!=null ? field.get(eligibilityDataDTO).toString(): "NOT FILLED", false);
                    leftTable.addCell(cell1);
                    leftTable.addCell(cell2);
                }
                Cell cell1 = new Cell();
                cell1.add(leftTable);
                outerTable.addCell(cell1);
            }else{
                Table rightTable  = new Table(towHalfCol);
                for(Field field : clas.getDeclaredFields()){
                    field.setAccessible(true);
                    Cell cell1 = getCell10fLeftBorder(field.getName(),false);
                    Cell cell2 = getCell10fLeftBorder(field.get(eligibilityDataDTO)!=null ? field.get(eligibilityDataDTO).toString(): "NOT FILLED", false);
                    rightTable.addCell(cell1);
                    rightTable.addCell(cell2);
                }
                Cell cell2 = new Cell();
                cell2.add(rightTable);
                outerTable.addCell(cell2);
            }
            i++;
        }
        document.add(outerTable);
    }

    private static void updateContentData(Class<?> clas,
                                              List<ApplicationContentManagerDTO> contentManagerDTOS,
                                              Document document) throws IllegalAccessException {

        for(ApplicationContentManagerDTO contentManagerDTO : contentManagerDTOS){
            Table outerTable = new Table(fullLength);
                for(Field field : clas.getDeclaredFields()){
                    field.setAccessible(true);
                    Cell cell1 = getCell10fLeftBorder(field.getName(),false);
                    Cell cell2 = getCell10fLeftBorder(field.get(contentManagerDTO)!=null ? field.get(contentManagerDTO).toString(): "NOT FILLED", false);
                    outerTable.addCell(cell1);
                    outerTable.addCell(cell2);
                }
            document.add(oneSep);
            document.add(outerTable);
        }
    }



}
