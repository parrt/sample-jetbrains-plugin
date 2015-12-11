package org.antlr.jetbrains.plugin;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.jetbrains.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.jetbrains.sample.parser.SampleLanguageLexer;
import org.antlr.jetbrains.sample.parser.SampleLanguageParser;
import org.jetbrains.annotations.NotNull;

public class SampleParserDefinition implements ParserDefinition {
	public static final IFileElementType FILE =
		new IFileElementType(SampleLanguage.INSTANCE);

	@NotNull
	@Override
	public Lexer createLexer(Project project) {
		SampleLanguageLexer lexer = new SampleLanguageLexer(null);
		return new ANTLRLexerAdaptor(SampleLanguage.INSTANCE, lexer);
	}

	@NotNull
	public PsiParser createParser(final Project project) {
		return new SampleLanguageParser();
	}

	@NotNull
	public TokenSet getWhitespaceTokens() {
		return ANTLRv4TokenTypes.WHITESPACES;
	}

	@NotNull
	public TokenSet getCommentTokens() {
		return ANTLRv4TokenTypes.COMMENTS;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return TokenSet.EMPTY;
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new ANTLRv4FileRoot(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

	/** Convert from internal parse node (AST they call it) to final PSI node. This
	 *  converts only internal rule nodes apparently, not leaf nodes. Leaves
	 *  are just tokens I guess.
	 */
	@NotNull
	public PsiElement createElement(ASTNode node) {
		return ANTLRv4ASTFactory.createInternalParseTreeNode(node);
	}
}
