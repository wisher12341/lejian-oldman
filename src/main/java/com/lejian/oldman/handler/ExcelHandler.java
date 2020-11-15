package com.lejian.oldman.handler;

import com.google.common.collect.Lists;
import com.lejian.oldman.bo.ExportOldmanBo;
import com.lejian.oldman.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.PARSE_DATA_ERROR;
import static com.lejian.oldman.common.ComponentRespCode.UN_KNOWN;

@Slf4j
@Component
public class ExcelHandler {

    /**
     * left 为表头
     * right 为具体内容
     * left 跟righr里list的值顺序保持一致
     * @param file
     * @param start 表头的行数
     * @return
     */
    public Pair<List<String>, List<List<String>>> parse(MultipartFile file,int start) {
        try {
            Workbook workbook=getWorkbook(file);
            List<String> titleList= Lists.newArrayList();
            List<List<String>> valueList=Lists.newArrayList();
            int i=1;
            for (Row row : workbook.getSheetAt(0)) {
                List<String> list=Lists.newArrayList();
                if (start>i){
                    i++;
                    continue;
                }
                if(start==i){
                    //表头
                    Iterator<Cell> iterable = row.cellIterator();
                    while (iterable.hasNext()) {
                        Cell cell = iterable.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        //结束
                        if(StringUtils.isBlank(cell.getStringCellValue())){
                            break;
                        }
                        titleList.add(cell.getStringCellValue().trim());
                    }
                }else {
                    //用cellIterator，如果前面几个是空值 是无法遍历的
                    for(int j=0;j<titleList.size();j++){
                        Cell cell = row.getCell(j);
                        if(cell == null){
                            list.add(StringUtils.EMPTY);
                            continue;
                        }
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        list.add(cell.getStringCellValue().trim());
                    }
                    if (CollectionUtils.isNotEmpty(list.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList()))) {
                        valueList.add(list);
                    }
                }
                i++;
            }
            return Pair.of(titleList,valueList);
        }catch (Exception e){
            log.warn("fail to parse",e);
            PARSE_DATA_ERROR.doThrowException();
        }
        return null;
    }

    private Workbook getWorkbook(MultipartFile file) throws IOException {
        String  fix=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        Workbook wb0;
        if(fix.equals("xls"))
            wb0= new HSSFWorkbook(file.getInputStream());
        else
            wb0=new XSSFWorkbook(file.getInputStream());
        return wb0;
    }

    /**
     * 导出
     * @param fileName 不能有中文
     * @param title
     * @param content
     */
    public void export(String fileName, String[] title, String[][] content) {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = servletRequestAttributes.getResponse();
        //创建HSSFWorkbook
        HSSFWorkbook wb = getHSSFWorkbook("list", title, content);
        //响应到客户端
        try {
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename="+fileName+".xls");
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            UN_KNOWN.doThrowException("fail to export",e);
        }
    }


    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @return
     */
    private HSSFWorkbook getHSSFWorkbook(String sheetName,String[] title,String[][] values){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);

        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<values.length;i++){
            row = sheet.createRow(i + 1);
            for(int j=0;j<values[i].length;j++){
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }
}
