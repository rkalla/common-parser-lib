package com.thebuzzmedia.common.parser;

import java.io.InputStream;
import java.io.Reader;

import com.thebuzzmedia.common.io.IInput;

/**
 * 
 * @author Riyad Kalla (software@thebuzzmedia.com)
 * 
 * @param <IT>
 *            The type of the input that this parser will process (e.g.
 *            {@link InputStream}, <code>byte[]</code>, {@link Reader}, etc).
 *            <p/>
 *            This is also the IT (input type) for the {@link IInput} the parser
 *            will process.
 * @param <DT>
 *            The type of the <code>delimiters</code> argument this parser uses
 *            to parse delimited data.
 * @param <TT>
 *            The type of the token, if necessary. If not needed, simply use a
 *            type of {@link Void}.
 * @param <VT>
 *            The type of the values returned by {@link IToken#getValue()}.
 *            <p/>
 *            This may be different from the &lt;ST&gt; type based on any
 *            processing the token does to the <code>source</code> while pulling
 *            the marked data from it. For example, the source might be a
 *            {@link StringBuilder} and the value returned by the token might be
 *            <code>char[]</code>.
 * @param <ST>
 *            The type of the <code>source</code> that the tokens created by
 *            this parser reference and pull {@link IToken#getValue()} values
 *            from.
 *            <p/>
 *            This is also the BT (buffer type) for the {@link IInput} the
 *            parser will process.
 *            <p/>
 *            It is not uncommon for this type to be different from the
 *            <code>IT</code> type. For example, a parser that processes data
 *            from an {@link InputStream} would have an <code>IT</code> type
 *            (input type) of {@link InputStream}, but a <code>ST</code> type
 *            (source type) of <code>byte[]</code> most likely.
 */
public interface IDelimitedTokenizer<IT, DT, TT, VT, ST> extends
		ITokenizer<IT, TT, VT, ST> {
	public enum DelimiterMode {
		MATCH_ANY, MATCH_EXACT;
	}

	public DT getDelimiters();

	public DelimiterMode getDelimiterMode();

	public void setInput(IInput<IT, ST> input, DT delimiters, DelimiterMode mode)
			throws IllegalArgumentException;
}