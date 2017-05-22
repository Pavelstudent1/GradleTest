package packtest;

import java.util.Arrays;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Library {
    

	public static void main(String[] args) {
		
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet();
		System.out.println(
				"Time to sleep. Args=" + Arrays.asList(args) + 
				"  WB=" + wb);
		long start = System.nanoTime();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		long stop = System.nanoTime();
		System.out.println("Awaken! Sleeping=" + (double)(stop - start)/1000000 + "ms");
		
	}
}
