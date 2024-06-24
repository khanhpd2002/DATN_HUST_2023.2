package vn.com.cardoctor.garage_service.utils.excel;

public class DrawBase64Dto {
	private String base64;
	private int beginColIndex;
	private int beginRowIndex;
	private int endColIndex;
	private int endRowIndex;

	public DrawBase64Dto() {
	}

	public DrawBase64Dto(String base64, int beginColIndex, int beginRowIndex, int endColIndex, int endRowIndex) {
		this.base64 = base64;
		this.beginColIndex = beginColIndex;
		this.beginRowIndex = beginRowIndex;
		this.endColIndex = endColIndex;
		this.endRowIndex = endRowIndex;
	}

	public String getBase64() {
		return this.base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public int getBeginColIndex() {
		return this.beginColIndex;
	}

	public void setBeginColIndex(int beginColIndex) {
		this.beginColIndex = beginColIndex;
	}

	public int getBeginRowIndex() {
		return this.beginRowIndex;
	}

	public void setBeginRowIndex(int beginRowIndex) {
		this.beginRowIndex = beginRowIndex;
	}

	public int getEndColIndex() {
		return this.endColIndex;
	}

	public void setEndColIndex(int endColIndex) {
		this.endColIndex = endColIndex;
	}

	public int getEndRowIndex() {
		return this.endRowIndex;
	}

	public void setEndRowIndex(int endRowIndex) {
		this.endRowIndex = endRowIndex;
	}

}
