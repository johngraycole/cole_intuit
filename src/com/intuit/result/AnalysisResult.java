package com.intuit.result;

public class AnalysisResult {
	public enum ColumnJustify {
		LEFT_JUSTIFY,
		CENTER_JUSTIFY,
		RIGHT_JUSTIFY
	}
	
	protected int _rows, _cols;
	protected int[] _colWidths;
	protected String[] _colTitles;
	protected ColumnJustify[] _colJustify;
	protected String[][] _vals;
	protected String _errorMsg;
	
	public AnalysisResult( int rows, int cols ) {
		_rows = rows;
		_cols = cols;
		_colWidths = new int[_cols];
		_colTitles = new String[_cols];
		_colJustify = new ColumnJustify[_cols];
		_vals = new String[_rows][_cols];
		
		for (int i=0; i<_cols; i++) {
			_colWidths[i] = 0;
			_colTitles[i] = "";
			_colJustify[i] = ColumnJustify.LEFT_JUSTIFY;
			for (int j=0; j<_rows; j++)
				_vals[j][i] = "";
		}
		
		_errorMsg = "Result Not Populated";
	}

	public int getNumRows() {
		return _rows;
	}
	public int getNumCols() {
		return _cols;
	}
	
	public void setColWidth(int idx, int width) throws IllegalArgumentException {
		if (idx<0 || idx>=_cols)
			throw new IllegalArgumentException();
		_colWidths[idx] = width;
	}
	public int getColWidth(int idx) throws IllegalArgumentException {
		if (idx<0 || idx>=_cols)
			throw new IllegalArgumentException();
		return _colWidths[idx];
	}

	public void setColTitle(int idx, String title) throws IllegalArgumentException {
		if (idx<0 || idx>=_cols)
			throw new IllegalArgumentException();
		_colTitles[idx] = title;
	}
	public String getColTitle(int idx) throws IllegalArgumentException {
		if (idx<0 || idx>=_cols)
			throw new IllegalArgumentException();
		return _colTitles[idx];
	}

	public void setColJustify(int idx, ColumnJustify justify) throws IllegalArgumentException {
		if (idx<0 || idx>=_cols)
			throw new IllegalArgumentException();
		_colJustify[idx] = justify;
	}
	public ColumnJustify getColJustify(int idx) throws IllegalArgumentException {
		if (idx<0 || idx>=_cols)
			throw new IllegalArgumentException();
		return _colJustify[idx];
	}

	public String getErrorMsg() {
		return _errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		_errorMsg = errorMsg;
	}

	public void setVal(int r, int c, String val) throws IllegalArgumentException {
		if (r<0 || r>=_rows || c<0 || c>=_cols)
			throw new IllegalArgumentException();
		_vals[r][c] = val;
	}
	public String getVal(int r, int c) throws IllegalArgumentException {
		if (r<0 || r>=_rows || c<0 || c>=_cols)
			throw new IllegalArgumentException();
		return _vals[r][c];
	}

}
