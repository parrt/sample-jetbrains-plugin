package org.antlr.jetbrains.sample;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.jetbrains.sample.adaptor.lexer.ANTLRLexerAdaptor;
import org.antlr.jetbrains.sample.adaptor.parser.ANTLRParserAdaptor;
import org.antlr.jetbrains.sample.sample.parser.SampleLanguageLexer;
import org.antlr.jetbrains.sample.sample.parser.SampleLanguageParser;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
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
		final SampleLanguageParser parser = new SampleLanguageParser(null);
		return new ANTLRParserAdaptor(SampleLanguage.INSTANCE, parser) {
			@Override
			protected ParseTree parse(Parser parser, IElementType root) {
				return ((SampleLanguageParser)parser).script();
			}
		};
	}

	/** "Tokens of those types are automatically skipped by PsiBuilder." */
	@NotNull
	public TokenSet getWhitespaceTokens() {
		return TokenSet.EMPTY;
	}

	@NotNull
	public TokenSet getCommentTokens() {
		return TokenSet.EMPTY;
	}

	@NotNull
	public TokenSet getStringLiteralElements() {
		return TokenSet.EMPTY;
	}

	@Override
	public IFileElementType getFileNodeType() {
		return FILE;
	}

	/** The root of your PSI tree is a PsiFile.
	 *
	 *  From IntelliJ IDEA Architectural Overview:
	 *  "A PSI (Program Structure Interface) file is the root of a structure
	 *  representing the contents of a file as a hierarchy of elements
	 *  in a particular programming language."
	 *
	 *  PsiFile is to be distinguished from a FileASTNode, which is a parse
	 *  tree node that eventually becomes a PsiFile. From PsiFile, we can get
	 *  it back via: {@link PsiFile#getNode}.
	 */
	@Override
	public PsiFile createFile(FileViewProvider viewProvider) {
		return new SamplePSIFileRoot(viewProvider);
	}

	public SpaceRequirements spaceExistanceTypeBetweenTokens(ASTNode left, ASTNode right) {
		return SpaceRequirements.MAY;
	}

	/** Convert from internal parse node (AST they call it) to final PSI node. This
	 *  converts only internal rule nodes apparently, not leaf nodes. Leaves
	 *  are just tokens I guess.
	 *
	 *  If you don't care to distinguish PSI nodes by type, it is sufficient
	 *  to create a {@link ASTWrapperPsiElement} around the parse tree node
	 *  (ASTNode in jetbrains speak).
	 */
	@NotNull
	public PsiElement createElement(ASTNode node) {
		return new ASTWrapperPsiElement(node);
	}
}