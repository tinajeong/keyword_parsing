package parser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class XlsxParser implements Parser, Runnable{
    private String fileName;

    private List<String> keywordList = new ArrayList<>();

    private List<String> containedKeywordList = new ArrayList<>();

    private XSSFWorkbook xlsxWorkbook;

    private XSSFSheet questionSheet;

    public XlsxParser( String fileName) {
        this.fileName = fileName;
        System.out.println(this.fileName);
    }

    @Override
    public void run() {
        readKeywords();
        readQuestion();
        write();
    }

    @Override
    public void readKeywords() {
        try {
            try (FileInputStream file = new FileInputStream((new File(ClassLoader.getSystemResource(fileName).toURI())))) {
                xlsxWorkbook = new XSSFWorkbook(file);

                XSSFSheet keywordSheet = xlsxWorkbook.getSheet("키워드");

                for (Row row : keywordSheet) {
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        switch (cell.getCellType()) {
                            case STRING:
                                System.out.print(cell.getStringCellValue() + "\t");
                                keywordList.add(cell.getStringCellValue());
                                break;
                            default:
                                System.out.println("유효한 키워드가 아닙니다. :" + cell.getStringCellValue());
                        }
                    }
                }
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void readQuestion() {
        questionSheet = xlsxWorkbook.getSheet("문의글");
        String[] splitQuestion = {};
        for (Row row : questionSheet) {
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                    case STRING:
                        System.out.println(cell.getStringCellValue());
                        splitQuestion = cell.getStringCellValue().split(" ");
                        break;
                    default:
                        System.out.println("유효한 키워드가 아닙니다. :" + cell.getStringCellValue());
                }

                for(String word : splitQuestion)
                    for(String keyword : keywordList) {
                        if(word.contains(keyword)) {
                            containedKeywordList.add(keyword);
                        }
                    }
//                    containedKeywordList.add(keywordList.stream().filter(word::contains).findFirst().get());
            }
        }
    }

    @Override
    public void write() {
        int index = 1;
        Row row = questionSheet.getRow(0);

        for(String keyword : containedKeywordList) {
            Cell cell = row.getCell(index);
            if(cell == null) {
                cell = row.createCell(index++);
                cell.setCellValue(keyword);
            }
        }

        try {
            FileOutputStream fileOutputStream = new FileOutputStream("new"+fileName);
            xlsxWorkbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
