package org.antlr.jetbrains.sample;

import com.intellij.lang.PsiBuilder;
import com.intellij.psi.tree.IElementType;
import org.antlr.jetbrains.sample.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.jetbrains.sample.adaptor.parser.SyntaxErrorListener;
import org.antlr.jetbrains.sample.sample.parser.SampleLanguageParser;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class AdaptedSampleLanguageParser extends ANTLRParserAdaptor {
	public AdaptedSampleLanguageParser() {
		super(SampleLanguage.INSTANCE);
	}

	@Override
	protected SampleLanguageParser createParserImpl(TokenStream tokenStream, IElementType root, PsiBuilder builder) {
		SampleLanguageParser parser = new SampleLanguageParser(tokenStream);
		parser.removeErrorListeners();
		parser.addErrorListener(new SyntaxErrorListener());
		return parser;
	}

	@Override
	protected ParseTree parseImpl(SampleLanguageParser parser,
	                              IElementType root,
	                              PsiBuilder builder)
	{
		return parser.script();
	}
}
