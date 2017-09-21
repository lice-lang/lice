package org.lice.runtime;

/**
 * Created by Glavo on 17-9-21.
 *
 * @author Glavo
 * @since 4.0.0
 */
@SuppressWarnings("WeakerAccess")
public class MetaData {
	public final int startLine;
	public final int startColumn;
	public final int endLine;
	public final int endColumn;

	public MetaData(int startLine, int startColumn, int endLine, int endColumn) {
		this.startLine = startLine;
		this.endLine = endLine;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}
}
