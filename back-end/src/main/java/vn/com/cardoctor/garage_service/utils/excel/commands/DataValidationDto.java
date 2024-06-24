package vn.com.cardoctor.garage_service.utils.excel.commands;


import lombok.Data;

@Data
public class DataValidationDto {
	private int firstRow;
	private int lastRow;
	private int firstCol;
	private int lastCol;
	private String formular;

	public DataValidationDto(int firstRow, int lastRow, int firstCol, int lastCol, String formular) {
		super();
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.firstCol = firstCol;
		this.lastCol = lastCol;
		this.formular = formular;
	}

}
