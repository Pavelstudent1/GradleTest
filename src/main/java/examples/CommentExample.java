package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.xssf.model.CommentsTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTComment;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTxbxContent;

import com.microsoft.schemas.vml.CTShape;
import com.microsoft.schemas.vml.CTTextbox;

import packtest.Utils;

public class CommentExample {
	
	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws IOException, NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {

		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("Sheet #1");
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell1 = row.createCell(0);
		cell1.setCellValue("0/1");
		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 3, 2);
		Comment comment = drawing.createCellComment(anchor);
		

		Field f = comment.getClass().getDeclaredField("_vmlShape");
		f.setAccessible(true);
		CTShape vmlShape = (CTShape) f.get(comment);
		CTTextbox textBoxArgs = vmlShape.getTextboxList().get(0);
		String cStyle = textBoxArgs.getStyle();
		cStyle += ";mso-fit-shape-to-text:t";
		cStyle += "mso-rotate:90";
		textBoxArgs.setStyle(cStyle);

		StringBuilder commentText = new StringBuilder("Custom comment line 1 Custom comment line 2 Custom comment line 3");
		int textLen = commentText.length();
		double q = (double)textLen / 3;
		
		for(int i = (int) q; i < textLen; i += q){
			if (commentText.charAt(i) == ' '){
				commentText.replace(i, i + 1, "\n");
			}else continue;
		}
		
		comment.setString(new XSSFRichTextString(commentText.toString()));

		FileOutputStream out = new FileOutputStream(new File(Utils.getPath("comments1.xlsx")));
		wb.write(out);
		out.close();

		System.out.println("Complete");

	}

}
