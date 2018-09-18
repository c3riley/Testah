package org.testah.framework.report.mgmt;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testah.TS;
import org.testah.client.dto.TestCaseDto;
import org.testah.client.dto.TestPlanDto;
import org.testah.framework.annotations.KnownProblem;
import org.testah.framework.annotations.TestCase;
import org.testah.framework.annotations.TestPlan;
import org.testah.framework.cli.Params;
import org.testah.framework.cli.TestFilter;
import org.testah.framework.dto.TestDtoHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class AuditReport {

    private final CellStyle headerCellStyle;
    private final CellStyle bodyCellStyle;
    private final CellStyle xbodyCellStyle;
    private final XSSFWorkbook workbook;
    private String filePathAndName;

    /**
     * Constructor.
     *
     * @throws IOException if writing to file fails
     */
    public AuditReport() throws IOException {
        this.filePathAndName = Params.addUserDir("AutomationAudit.xlsx");
        File report = new File(filePathAndName);
        if (report.exists()) {
            TS.log().info("Delete old report: " + report.delete());
        }
        this.workbook = new XSSFWorkbook();
        this.headerCellStyle = createHeaderCellStyle();
        this.bodyCellStyle = createBodyCellStyle();
        this.xbodyCellStyle = createXBodyCellStyle();
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        AuditReport auditReport = new AuditReport();
        auditReport.buildReport();
    }

    private CellStyle createHeaderCellStyle() {
        final CellStyle cellStyle = workbook.createCellStyle();
        final XSSFFont cellFont = workbook.createFont();
        cellStyle.setFillBackgroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellStyle.setShrinkToFit(false);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(BorderStyle.MEDIUM);
        cellStyle.setBorderTop(BorderStyle.MEDIUM);
        cellStyle.setBorderRight(BorderStyle.MEDIUM);
        cellStyle.setBorderLeft(BorderStyle.MEDIUM);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellFont.setBold(true);
        cellFont.setFontName("Arial");
        cellFont.setColor(IndexedColors.BLACK.getIndex());
        cellFont.setFontHeightInPoints((short) 14);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    private CellStyle createBodyCellStyle() {
        final CellStyle cellStyle = workbook.createCellStyle();
        final XSSFFont cellFont = workbook.createFont();
        cellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
        cellStyle.setShrinkToFit(false);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellFont.setFontName("Arial");
        cellFont.setColor(IndexedColors.BLACK.getIndex());
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    private CellStyle createXBodyCellStyle() {
        final CellStyle cellStyle = workbook.createCellStyle();
        final XSSFFont cellFont = workbook.createFont();
        cellStyle.setFillBackgroundColor(HSSFColor.WHITE.index);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setShrinkToFit(false);
        cellStyle.setVerticalAlignment(VerticalAlignment.TOP);
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellFont.setFontName("Arial");
        cellFont.setBold(true);
        cellFont.setColor(IndexedColors.BLACK.getIndex());
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        return cellStyle;
    }

    /**
     * Create results report.
     */
    public void buildReport() {

        final HashMap<String, TestPlanDto> testPlans = getTestMetadata();

        XSSFSheet testCaseSheet = workbook.createSheet("TestCase Info");
        XSSFSheet testPlanSheet = workbook.createSheet("TestPlan Info");

        int tpRowNum = 0;
        int tcRowNum = 0;
        int tpColNum = 0;
        int tcColNum = 0;
        System.out.println("Creating excel");

        Row tcRow = testCaseSheet.createRow(tcRowNum++);
        Row tpRow = testPlanSheet.createRow(tpRowNum++);

        addHeader(tcRow, "TestPlan", "TestCase", "RunTypes", "ACCEPTANCE", "SMOKE", "REG", "RelatedIds", "TestType",
                "HasKnownProblem", "Platforms",
                "Components", "Comments", "Description", "KnownProblem Ids", "KnownProblem Desc", "KnownProblem Type");

        addHeader(tpRow, "TestPlan", "Description", "RunTypes", "ACCEPTANCE", "SMOKE", "REG", "RelatedIds", "TestType",
                "HasKnownProblem", "Platforms",
                "Components", "Comments", "KnownProblem Ids", "KnownProblem Desc", "KnownProblem Type");



        for (final Entry<String, TestPlanDto> testPlan : testPlans.entrySet()) {
            List<String> tpRelatedIds = testPlan.getValue().getRelatedIds();
            if(tpRelatedIds==null) {
                tpRelatedIds = new ArrayList<>();
            }
            tpRow = testPlanSheet.createRow(tpRowNum++);
            tpColNum = 0;
            addToCell(tpRow.createCell(tpColNum++), testPlan.getKey());
            addToCell(tpRow.createCell(tpColNum++), testPlan.getValue().getDescription());
            addToCell(tpRow.createCell(tpColNum++), String.join("\n", testPlan.getValue().getRunTypes()));
            addToXCell(tpRow.createCell(tpColNum++), testPlan.getValue().getRunTypes().contains("ACCEPTANCE"));
            addToXCell(tpRow.createCell(tpColNum++), testPlan.getValue().getRunTypes().contains("SMOKE"));
            addToXCell(tpRow.createCell(tpColNum++), testPlan.getValue().getRunTypes().contains("REG"));
            addToCell(tpRow.createCell(tpColNum++), String.join(", ", tpRelatedIds));
            addToCell(tpRow.createCell(tpColNum++), testPlan.getValue().getTestType().name());
            addToXCell(tpRow.createCell(tpColNum++), (null != testPlan.getValue().getKnownProblem()));
            addToCell(tpRow.createCell(tpColNum++), String.join(", ", testPlan.getValue().getPlatforms()));
            addToCell(tpRow.createCell(tpColNum++), String.join(", ", testPlan.getValue().getComponents()));
            addToCell(tpRow.createCell(tpColNum++), "");
            if (null != testPlan.getValue().getKnownProblem()) {
                if (null != testPlan.getValue().getKnownProblem().getLinkedIds()) {
                    addToCell(tcRow.createCell(tpColNum++), String.join(", ", testPlan.getValue()
                            .getKnownProblem()
                            .getLinkedIds()));
                } else {
                    addToCell(tcRow.createCell(tpColNum++), "");
                }
                addToCell(tcRow.createCell(tpColNum++), testPlan.getValue().getKnownProblem()
                        .getDescription());
                addToCell(tcRow.createCell(tpColNum++), testPlan.getValue().getKnownProblem()
                        .getTypeOfKnown()
                        .name());
            }

            for (final TestCaseDto testCase : testPlan.getValue().getTestCases()) {
                List<String> tcRelatedIds = testCase.getRelatedIds();
                if(tcRelatedIds==null) {
                    tcRelatedIds = tpRelatedIds;
                }
                tcRow = testCaseSheet.createRow(tcRowNum++);
                tcColNum = 0;
                addToCell(tcRow.createCell(tcColNum++), testPlan.getKey());
                addToCell(tcRow.createCell(tcColNum++), testCase.getName());
                addToCell(tcRow.createCell(tcColNum++), String.join("\n", testCase.getRunTypes()));
                addToXCell(tcRow.createCell(tcColNum++), testCase.getRunTypes().contains("ACCEPTANCE"));
                addToXCell(tcRow.createCell(tcColNum++), testCase.getRunTypes().contains("SMOKE"));
                addToXCell(tcRow.createCell(tcColNum++), testCase.getRunTypes().contains("REG"));
                addToCell(tcRow.createCell(tcColNum++), String.join(", ", tcRelatedIds));
                addToCell(tcRow.createCell(tcColNum++), testCase.getTestType().name());
                addToXCell(tcRow.createCell(tcColNum++), (null != testCase.getKnownProblem()));
                addToCell(tcRow.createCell(tcColNum++), String.join(", ", testCase.getPlatforms()));
                addToCell(tcRow.createCell(tcColNum++), String.join(", ", testCase.getComponents()));
                addToCell(tcRow.createCell(tcColNum++), "");
                addToCell(tcRow.createCell(tcColNum++), String.join(", ", testCase.getDescription()));
                if (null != testCase.getKnownProblem()) {
                    if (null != testCase.getKnownProblem().getLinkedIds()) {
                        addToCell(tcRow.createCell(tcColNum++), String.join(", ", testCase.getKnownProblem()
                                .getLinkedIds()));
                    } else {
                        addToCell(tcRow.createCell(tcColNum++), "");
                    }
                    addToCell(tcRow.createCell(tcColNum++), testCase.getKnownProblem().getDescription());
                    addToCell(tcRow.createCell(tcColNum++), testCase.getKnownProblem().getTypeOfKnown()
                            .name());
                }
                tcRow.setHeightInPoints(-1);
                // cell.setCellValue((Integer) field);
            }
            tpRow.setHeightInPoints(-1);

        }
        for (int column = tpColNum; column >= 0; column--) {
            testPlanSheet.autoSizeColumn(column);
        }
        for (int column = tcColNum; column >= 0; column--) {
            testCaseSheet.autoSizeColumn(column);
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePathAndName)) {
            workbook.write(outputStream);
            workbook.close();
        } catch (Exception e) {
            TS.log().error("Issue Creating Report: " + filePathAndName, e);
        }

        System.out.println("Done");
    }

    private HashMap<String, TestPlanDto> getTestMetadata() {
        final TestFilter testPlanFilter = new TestFilter();

        testPlanFilter.filterTestPlansToRun();
        final HashMap<String, TestPlanDto> testPlans = new HashMap<>();
        for (final Class<?> test : testPlanFilter.getTestClassesMetFilters()) {
            testPlans
                    .put(test.getCanonicalName(),
                            TestDtoHelper
                                    .createTestPlanDto(test, test.getAnnotation(TestPlan.class),
                                            test.getAnnotation(KnownProblem.class))
                                    .setRunTime(null).setRunInfo(null));

            for (final Method method : test.getDeclaredMethods()) {
                if (null != method.getAnnotation(TestCase.class)) {
                    testPlans.get(test.getCanonicalName())
                            .addTestCase(TestDtoHelper.createTestCaseDto(test.getCanonicalName(), method.getName(),
                                    method.getAnnotation(TestCase.class), method.getAnnotation(KnownProblem.class),
                                    test.getAnnotation(TestPlan.class)).setRunTime(null));
                }
            }
        }
        return testPlans;
    }

    private void addHeader(final Row row, final String... headerValues) {
        int colNum = 0;
        for (final String headerValue : headerValues) {
            Cell cell = row.createCell(colNum++);
            cell.setCellValue(headerValue);
            cell.setCellStyle(headerCellStyle);
        }
    }

    private void addToCell(final Cell cell, final String value) {
        cell.setCellValue(value);
        cell.setCellStyle(bodyCellStyle);
    }

    private void addToXCell(final Cell cell, final boolean value) {
        if (value) {
            cell.setCellValue("X");
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(xbodyCellStyle);
    }

}
