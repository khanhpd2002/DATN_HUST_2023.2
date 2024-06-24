package vn.com.cardoctor.garage_service.utils.excel;

import com.google.common.collect.ImmutableMap;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.BaseFormulaEvaluator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.formula.FastFormulaProcessor;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiContext;
import org.jxls.transform.poi.PoiTransformer;
import org.springframework.core.io.ClassPathResource;
import vn.com.cardoctor.garage_service.utils.excel.commands.AutoRowHeightCommand;
import vn.com.cardoctor.garage_service.utils.excel.commands.DataValidationDto;
import vn.com.cardoctor.garage_service.utils.excel.commands.GroupRowCommand;

public class Jxls2xTransformerUtils {

  private Jxls2xTransformerUtils() {
  }

  private static final Logger log = LogManager.getLogger(Jxls2xTransformerUtils.class);

  public static String poiTransformer(String relativeTemplatePath, Map<String, Object> mapBeanParams, String outPath,
      String fileName) {
    return Jxls2xTransformerUtils.transformer(relativeTemplatePath, mapBeanParams, outPath, fileName, null, new HashMap<>());
  }

  public static String poiTransformer(String relativeTemplatePath, Map<String, Object> mapBeanParams, String outPath,
      String fileName, Map<String, String> template2ResultSheets, Map<String, String> hideSheets) {
    return Jxls2xTransformerUtils.transformer(relativeTemplatePath, mapBeanParams, outPath, fileName,
        template2ResultSheets, hideSheets);
  }

  public static String poiTransformer(String relativeTemplatePath, Map<String, Object> mapBeanParams, String outPath,
      String fileName, Map<String, String> template2ResultSheets) {
    return Jxls2xTransformerUtils.transformer(relativeTemplatePath, mapBeanParams, outPath, fileName,
        template2ResultSheets, new HashMap<>());
  }

  private static String transformer(String relativeTemplatePath, Map<String, Object> mapBeanParams, String outPath,
      String fileName, Map<String, String> template2ResultSheets, Map<String, String> hideSheets) {
    Jxls2xTransformerUtils.log.info("Start export Excel!");
    long startTime = System.nanoTime();

    if (fileName == null) {
      fileName = new SimpleDateFormat("yyyyMMdd-HHmmssSSS").format(new Date()) + "_"
          + relativeTemplatePath.substring(relativeTemplatePath.lastIndexOf('/') + 1);
    }
    if ((template2ResultSheets == null) || template2ResultSheets.isEmpty()) {
      template2ResultSheets = ImmutableMap.<String, String>builder().put("", "Result").build();
    }
    String absoluteOutPath = Paths.get(Paths.get(outPath).toString(), fileName).toString();
    int resultSheetIndex = 1;
    try (InputStream inputStream = new ClassPathResource(relativeTemplatePath).getInputStream();
        OutputStream outputStream = new FileOutputStream(absoluteOutPath)) {
      if (inputStream.available() < 0) {
        return null;
      }
      Workbook workbook = WorkbookFactory.create(inputStream);
      Transformer transformer = PoiTransformer.createSxssfTransformer(workbook, 100, false);
      transformer.setEvaluateFormulas(true);
      AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
      XlsCommentAreaBuilder.addCommandMapping("groupRow", GroupRowCommand.class);
      // jx:groupRow(collapseIf="detail.orderDetailDtos.size()>1" lastCell="D10")
      XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);
      // jx:autoRowHeight(lastCell="A2")
      List<Area> xlsAreaList = areaBuilder.build();
      for (Map.Entry<String, String> entry : template2ResultSheets.entrySet()) {
        // Apply template
        long startTime1 = System.nanoTime();
        OptionalInt xlsAreaIndex = IntStream.rangeClosed(1 - xlsAreaList.size(), 0).map(i -> -i).filter(
                ix -> xlsAreaList.get(ix).getStartCellRef().getSheetName().equalsIgnoreCase(entry.getKey()))
            .findFirst();
        Area xlsArea = xlsAreaList.get(xlsAreaIndex.orElse(0));
        Context context = new PoiContext(mapBeanParams);
        context.getConfig().setIsFormulaProcessingRequired(true);

        xlsArea.setFormulaProcessor(new FastFormulaProcessor());
        xlsArea.applyAt(new CellRef(entry.getValue() + "!A1"), context);
        xlsArea.processFormulas();
        long endTime1 = System.nanoTime();
        Jxls2xTransformerUtils.log.info("Apply template: {}", ((endTime1 - startTime1) / 1e9));

        // Ẩn hiện sheet theo nhu cầu
        long startTime2 = System.nanoTime();
        if (!entry.getValue().equals(hideSheets.get(entry.getValue()) == null ? 1 : entry.getValue()) || entry.getValue().equals("Result")) {
          OptionalInt activeSheetIndex = IntStream.rangeClosed(1 - workbook.getNumberOfSheets(), 0).map(i -> -i)
              .filter(ix -> workbook.getSheetAt(ix).getSheetName().equalsIgnoreCase(entry.getValue()))
              .findFirst();
          resultSheetIndex = activeSheetIndex.orElse(1);
          workbook.setActiveSheet(resultSheetIndex);
          workbook.setSelectedTab(resultSheetIndex);
        }
        OptionalInt hideSheetIndex = IntStream.rangeClosed(1 - workbook.getNumberOfSheets(), 0).map(i -> -i)
            .filter(ix -> workbook.getSheetAt(ix).getSheetName().equalsIgnoreCase(entry.getKey()))
            .findFirst();
        workbook.setSheetHidden(hideSheetIndex.orElse(0), true);
        long endTime2 = System.nanoTime();
        Jxls2xTransformerUtils.log.info("Ẩn hiện sheet theo nhu cầu: {}", ((endTime2 - startTime2) / 1e9));
      }

      if (!hideSheets.entrySet().isEmpty()) {
        for (Map.Entry<String, String> entry : hideSheets.entrySet()) {
          OptionalInt hideSheetIndex = IntStream.rangeClosed(1 - workbook.getNumberOfSheets(), 0).map(i -> -i)
              .filter(ix -> workbook.getSheetAt(ix).getSheetName().equalsIgnoreCase(entry.getKey()))
              .findFirst();
          workbook.setSheetHidden(hideSheetIndex.orElse(0), true);
        }
      }

      // Tính toán lại các Formula ngay và luôn
      long startTime3 = System.nanoTime();
      // workbook.setForceFormulaRecalculation(true)
      Workbook poiWorkbook = ((PoiTransformer) transformer).getWorkbook();
      BaseFormulaEvaluator.evaluateAllFormulaCells(poiWorkbook);
      // Ghi ra outputstream
      poiWorkbook.write(outputStream);
      long endTime3 = System.nanoTime();
      Jxls2xTransformerUtils.log.info("Tính toán lại công thức cell và ghi lại file: {}",
          ((endTime3 - startTime3) / 1e9));
    } catch (IOException | IllegalArgumentException | IllegalStateException e) {
      Jxls2xTransformerUtils.log.error(e.getMessage(), e);
      return null;
    }
    Jxls2xTransformerUtils.drawSpecifyBase64Cells(absoluteOutPath, mapBeanParams, resultSheetIndex);
    if (mapBeanParams.containsKey("specialImagePosition")) {
      @SuppressWarnings("unchecked")
      List<DrawBase64Dto> drawBase64Dtos = (List<DrawBase64Dto>) mapBeanParams.get("specialImagePosition");
      Jxls2xTransformerUtils.drawSpecialImagePosition(absoluteOutPath, resultSheetIndex, drawBase64Dtos);
    }
    long endTime = System.nanoTime();
    Jxls2xTransformerUtils.log.info("Total time to export Excel: {}", ((endTime - startTime) / 1e9));
    return absoluteOutPath;
  }

  private static void drawSpecialImagePosition(String absoluteOutPath, int resultSheetIndex,
      List<DrawBase64Dto> drawBase64Dtos) {
    XSSFWorkbook workbook = null;
    try (FileInputStream fis = new FileInputStream(absoluteOutPath)) {
      workbook = new XSSFWorkbook(fis);
      XSSFSheet sheet = workbook.getSheetAt(resultSheetIndex);
      for (DrawBase64Dto drawBase64Dto : drawBase64Dtos) {
        XSSFRow row = sheet.getRow(drawBase64Dto.getBeginRowIndex());
        XSSFCell cell = row.getCell(drawBase64Dto.getBeginColIndex());
        if (StringUtils.isEmpty(drawBase64Dto.getBase64())) {
          drawBase64Dto.setBase64(cell.getStringCellValue());
        }
        Jxls2xTransformerUtils.drawQrCodeFromBase64(workbook, sheet, drawBase64Dto);
        cell.setBlank();
      }
    } catch (Exception e) {
      Jxls2xTransformerUtils.log.error(e.getMessage(), e);
    }
    if (workbook != null) {
      try (FileOutputStream fos = new FileOutputStream(absoluteOutPath)) {
        workbook.write(fos);
        workbook.close();
      } catch (IOException e) {
        Jxls2xTransformerUtils.log.error(e.getMessage(), e);
      }
    }
  }

  private static void drawSpecifyBase64Cells(String absoluteOutPath, Map<String, Object> mapBeanParams,
      int resultSheetIndex) {
    XSSFWorkbook workbook = null;
    try (FileInputStream fis = new FileInputStream(absoluteOutPath)) {
      workbook = new XSSFWorkbook(fis);
      XSSFSheet sheet = workbook.getSheet("SpecifyBase64Cells");
      if (sheet == null) {
        return;
      }
      Map<Integer, Map<Integer, List<Integer>>> mapCellJump = new HashMap<>();
      int endRow = sheet.getLastRowNum();
      for (int rowIndex = 0; rowIndex <= endRow; rowIndex++) {
        Row row = sheet.getRow(rowIndex);
        double numberJumping = row.getCell(1).getNumericCellValue();
        if ((int) numberJumping < 1) {
          mapCellJump.put(rowIndex, null);
          continue;
        }
        Map<Integer, List<Integer>> mapJumping = new HashMap<>();
        List<Integer> listJumping = new ArrayList<>();
        for (int i = 0; i < ((int) numberJumping - 1); i++) {
          double jumpingValue = row.getCell(i + 3).getNumericCellValue();
          listJumping.add((int) jumpingValue);
        }
        double startJumping = row.getCell(2).getNumericCellValue();
        mapJumping.put((int) startJumping, listJumping);
        mapCellJump.put(rowIndex, mapJumping);
      }
      @SuppressWarnings("unchecked")
      List<List<Object>> list = (List<List<Object>>) mapBeanParams.get("objData");
      Jxls2xTransformerUtils.resolveBase64(workbook, mapCellJump, resultSheetIndex,
          mapBeanParams.get("objData") == null ? null : list);
    } catch (Exception e) {
      Jxls2xTransformerUtils.log.error(e.getMessage(), e);
    }
    if (workbook != null) {
      try (FileOutputStream fos = new FileOutputStream(absoluteOutPath)) {
        workbook.write(fos);
        workbook.close();
      } catch (IOException e) {
        Jxls2xTransformerUtils.log.error(e.getMessage(), e);
      }
    }
  }

  private static void resolveBase64(XSSFWorkbook workbook, Map<Integer, Map<Integer, List<Integer>>> mapCellJumping,
      int resultSheetIndex, List<List<Object>> qrCodes) throws IllegalAccessException {
    if (qrCodes == null) {
      return;
    }
    XSSFSheet sheet = workbook.getSheetAt(resultSheetIndex);
    int beginRow = sheet.getFirstRowNum();
    int endRow = sheet.getLastRowNum();
    sheet.createRow(endRow + 1);
    for (int rowIndex = beginRow; rowIndex <= endRow; rowIndex++) {
      Row row = sheet.getRow(rowIndex);
      if (row == null) {
        continue;
      }
      int beginCol = row.getFirstCellNum();
      int endCol = row.getLastCellNum();
      if (mapCellJumping.get(1) != null) {
        Map<Integer, List<Integer>> mapJumping = mapCellJumping.get(1);
        beginCol = mapJumping.keySet().iterator().next();
        endCol = beginCol + mapJumping.get(beginCol).parallelStream().mapToInt(Integer::intValue).sum();
      }
      for (int colIndex = beginCol; colIndex <= endCol; colIndex++) {
        Cell cell = row.getCell(colIndex);
        Map<String, Object> mapObj = Jxls2xTransformerUtils.getMapObj(cell, qrCodes);
        if (mapObj == null) {
          continue;
        }
        Jxls2xTransformerUtils.drawQrCodeFromBase64(workbook, sheet, new DrawBase64Dto(
            mapObj.get("qRCode").toString(), colIndex, rowIndex, colIndex + 1, rowIndex + 1));
        cell.setCellValue("");
      }
    }
  }

  private static Map<String, Object> getFieldNamesAndValues(final Object obj, boolean publicOnly)
      throws IllegalAccessException {
    Class<?> c1 = obj.getClass();
    Map<String, Object> map = new HashMap<>();
    Field[] fields = c1.getDeclaredFields();
    for (Field field : fields) {
      String name = field.getName();
      if (publicOnly) {
        if (Modifier.isPublic(field.getModifiers())) {
          Object value = field.get(obj);
          map.put(name, value);
        }
      } else {
        field.setAccessible(true);
        Object value = field.get(obj);
        map.put(name, value);
      }
    }
    return map;
  }

  private static Map<String, Object> getMapObj(Cell cell, List<List<Object>> qrCodes) throws IllegalAccessException {
    if ((cell == null) || !CellType.STRING.equals(cell.getCellType())) {
      return Collections.emptyMap();
    }
    String value = String.valueOf(cell);
    if (!value.startsWith("[") || !value.endsWith("]")) {
      return Collections.emptyMap();
    }
    String[] cellIndex = value.replace("[", "").replace("]", "").split("-");
    if ((cellIndex.length != 2) || !StringUtils.isNumeric(cellIndex[0]) || !StringUtils.isNumeric(cellIndex[1])) {
      return Collections.emptyMap();
    }
    Object obj = qrCodes.get(Integer.parseInt(cellIndex[0])).get(Integer.parseInt(cellIndex[1]));
    Map<String, Object> mapObj = Jxls2xTransformerUtils.getFieldNamesAndValues(obj, false);
    if (!mapObj.containsKey("qRCode")) {
      return Collections.emptyMap();
    }
    return mapObj;
  }

  private static void drawQrCodeFromBase64(XSSFWorkbook workbook, XSSFSheet sheet, DrawBase64Dto drawBase64Dto) {
    final int pictureIndex = workbook.addPicture(Base64.getDecoder().decode(drawBase64Dto.getBase64()),
        Workbook.PICTURE_TYPE_PNG);

    final XSSFDrawing drawing = sheet.createDrawingPatriarch();
    int padding = 5;
    XSSFClientAnchor anchor = new XSSFClientAnchor(padding * Units.EMU_PER_PIXEL, padding * Units.EMU_PER_PIXEL,
        -padding * Units.EMU_PER_PIXEL, -padding * Units.EMU_PER_PIXEL, drawBase64Dto.getBeginColIndex(),
        drawBase64Dto.getBeginRowIndex(), drawBase64Dto.getEndColIndex(), drawBase64Dto.getEndRowIndex());
    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
    drawing.createPicture(anchor, pictureIndex);
  }

  public static void addDataValidation(String absoluteOutPath, String resultSheetName,
      List<DataValidationDto> dataValidationDtos) {
    XSSFWorkbook workbook = null;
    try (FileInputStream fis = new FileInputStream(absoluteOutPath)) {
      workbook = new XSSFWorkbook(fis);
      XSSFSheet sheet = workbook.getSheet(resultSheetName);
      DataValidationHelper helper = sheet.getDataValidationHelper();
      for (DataValidationDto dataValidationDto : dataValidationDtos) {
        DataValidationConstraint constraint = helper
            .createFormulaListConstraint(dataValidationDto.getFormular());
        CellRangeAddressList range = new CellRangeAddressList(dataValidationDto.getFirstRow(),
            dataValidationDto.getLastRow(), dataValidationDto.getFirstCol(),
            dataValidationDto.getLastCol());
        DataValidation validation = helper.createValidation(constraint, range);
        sheet.addValidationData(validation);
      }
    } catch (IOException e) {
      Jxls2xTransformerUtils.log.error(e.getMessage(), e);
    }
    if (workbook != null) {
      try (FileOutputStream fos = new FileOutputStream(absoluteOutPath)) {
        workbook.write(fos);
        workbook.close();
      } catch (IOException e) {
        Jxls2xTransformerUtils.log.error(e.getMessage(), e);
      }
    }
  }

  public static void addComment(String absoluteOutPath, String resultSheetName, List<CommentDto> commentDtos) {
    XSSFWorkbook workbook = null;
    try (FileInputStream fis = new FileInputStream(absoluteOutPath)) {
      workbook = new XSSFWorkbook(fis);
      XSSFSheet sheet = workbook.getSheet(resultSheetName);
      for (CommentDto commentDto : commentDtos) {
        int rowIdx = commentDto.getRowIndex();
        int colIdx = commentDto.getColIndex();
        CreationHelper factory = workbook.getCreationHelper();
        Row row = sheet.getRow(rowIdx);
        if (row == null) {
          row = sheet.createRow(rowIdx);
        }
        Cell cell = row.getCell(colIdx);
        if (cell == null) {
          cell = row.createCell(colIdx);
        }
        ClientAnchor anchor = factory.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex() + 1);
        anchor.setCol2(cell.getColumnIndex() + 1 + commentDto.getColPlus());
        anchor.setRow1(rowIdx + 1);
        anchor.setRow2(rowIdx + 1 + commentDto.getRowPlus());
        XSSFDrawing drawing = sheet.createDrawingPatriarch();
        Comment comment = drawing.createCellComment(anchor);
        comment.setString(factory.createRichTextString(commentDto.getComment()));
        comment.setAuthor(commentDto.getAuthor());
        cell.setCellComment(comment);
      }
    } catch (IOException e) {
      Jxls2xTransformerUtils.log.error(e.getMessage(), e);
    }
    if (workbook != null) {
      try (FileOutputStream fos = new FileOutputStream(absoluteOutPath)) {
        workbook.write(fos);
        workbook.close();
      } catch (IOException e) {
        Jxls2xTransformerUtils.log.error(e.getMessage(), e);
      }
    }
  }

}
