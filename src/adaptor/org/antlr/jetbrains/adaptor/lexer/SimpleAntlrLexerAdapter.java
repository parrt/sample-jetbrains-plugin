package org.antlr.jetbrains.adaptor.lexer;

import com.intellij.lang.Language;
import org.antlr.v4.runtime.Lexer;

/**
 * This default implementation of {@link ANTLRLexerAdapter} supports
 * any ANTLR 4 lexer that does not store extra information for use in
 * custom actions. The state is maintained in {@link ANTLRLexerState}
 * which supports single- as well as multi-mode lexers.
 */
public class SimpleANTLRLexerAdapter extends ANTLRLexerAdapter<ANTLRLexerState> {

	public SimpleANTLRLexerAdapter(Language language, Lexer lexer) {
		super(language, lexer);
	}

	@Override
	protected ANTLRLexerState getInitialState() {
		return new ANTLRLexerState(Lexer.DEFAULT_MODE, null);
	}

	@Override
	protected ANTLRLexerState getLexerState(Lexer lexer) {
		if (lexer._modeStack.isEmpty()) {
			return new ANTLRLexerState(lexer._mode, null);
		}

		return new ANTLRLexerState(lexer._mode, lexer._modeStack);
	}

}
