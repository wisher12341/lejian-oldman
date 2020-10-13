package com.lejian.oldman.handler;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.lejian.oldman.common.ComponentRespCode.PARSE_DATA_ERROR;

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
                if (start<=i) {
                    Iterator<Cell> iterable = row.cellIterator();
                    while (iterable.hasNext()) {
                        Cell cell = iterable.next();
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        if(StringUtils.isBlank(cell.getStringCellValue().trim())){
                            continue;
                        }
                        if (start == i) {
                            //表名 结束
                            if(StringUtils.isBlank(cell.getStringCellValue())){
                                break;
                            }
                            titleList.add(cell.getStringCellValue().trim());
                        } else {
                            list.add(cell.getStringCellValue().trim());
                        }
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
}
