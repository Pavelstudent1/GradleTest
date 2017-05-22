package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import javax.swing.GroupLayout.Alignment;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCellAlignment;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientFill;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTGradientStop;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTStylesheet;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STGradientType;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STHorizontalAlignment;

import packtest.Utils;

public class Examples {
	
	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {

		XSSFWorkbook wb = new XSSFWorkbook();  
        XSSFSheet sheet = wb.createSheet("Sheet #1"); 
        Row row = sheet.createRow(0);
        XSSFCell cell1 = (XSSFCell) row.createCell(0);
        cell1.setCellValue("0/1");
        
        /**
         * Настраиваем компонент для заливки градиентом
         * Градиент: Синий -> Белый -> Красный
         */
        XSSFCellFill cFill = new XSSFCellFill();
        CTGradientFill cGrudient = cFill.getCTFill().addNewGradientFill();
        cGrudient.setDegree(0);
        
        CTGradientStop stop_0 = cGrudient.addNewStop();
        stop_0.setPosition(0);
        stop_0.addNewColor().setRgb(javax.xml.bind.DatatypeConverter.parseHexBinary("0033cc"));
        
        CTGradientStop stop_1 = cGrudient.addNewStop();
        stop_1.setPosition(0.5);
        stop_1.addNewColor().setRgb(javax.xml.bind.DatatypeConverter.parseHexBinary("ffffff"));
        
        CTGradientStop stop_2 = cGrudient.addNewStop();
        stop_2.setPosition(1.0);
        stop_2.addNewColor().setRgb(javax.xml.bind.DatatypeConverter.parseHexBinary("ff3300"));
        
        /**
         * Магия рефлексии: вытягиваем private метод addFill(..) данного объекта стиля и передаём ему 
         * ранее созданный объект заливки. И всё =)
         */
        XSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        Method m = XSSFCellStyle.class.getDeclaredMethod("addFill", CTFill.class);
        m.setAccessible(true);	//т.к. вызываемый метод private (true также нехило увеличивает производительность, 
        						//т.к. отменяет многие security проверки, проверки видимости, доступности и т.п.)
        m.invoke(style, cFill.getCTFill());
        cell1.setCellStyle(style);
        
        /** Вот это всё нужно было бы писать, если рефлексию не использовать 
         * (ну, и если б XSSFCellStyle не имел нужного метода =))
         */
/*        int fillIndex = wb.getStylesSource().putFill(cFill);
        
        CTXf addCTX=CTXf.Factory.newInstance(); 
        addCTX.setNumFmtId(0L);
        addCTX.setFontId(0L);
        addCTX.setFillId(fillIndex);
        addCTX.setBorderId(0L);
        addCTX.setXfId(0L);
        addCTX.setApplyFill(true);
        addCTX.setApplyAlignment(true);
        
        CTCellAlignment align = CTCellAlignment.Factory.newInstance();
        align.setHorizontal(STHorizontalAlignment.CENTER);
        addCTX.setAlignment(align);
        
        int cellXfIndex = wb.getStylesSource().putCellXf(addCTX);
        
        cell1.getCTCell().setS(cellXfIndex - 1);
*/
        
        
        FileOutputStream out = new FileOutputStream(new File(Utils.getPath("c222.xlsx")));
        wb.write(out);
        out.close();
        
        System.out.println("Complete");
	}
}
