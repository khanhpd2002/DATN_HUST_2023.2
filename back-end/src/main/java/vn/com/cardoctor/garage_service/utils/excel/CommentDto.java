package vn.com.cardoctor.garage_service.utils.excel;

public class CommentDto {
	private int rowIndex;
	private int rowPlus;
	private int colIndex;
	private int colPlus;
	private String author;
	private String comment;

	public CommentDto(int rowIndex, int rowPlus, int colIndex, int colPlus, String author, String comment) {
		super();
		this.rowIndex = rowIndex;
		this.rowPlus = rowPlus;
		this.colIndex = colIndex;
		this.colPlus = colPlus;
		this.author = author;
		this.comment = comment;
	}

	public int getRowIndex() {
		return this.rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getRowPlus() {
		return this.rowPlus;
	}

	public void setRowPlus(int rowPlus) {
		this.rowPlus = rowPlus;
	}

	public int getColIndex() {
		return this.colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}

	public int getColPlus() {
		return this.colPlus;
	}

	public void setColPlus(int colPlus) {
		this.colPlus = colPlus;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
