package org.antlr.jetbrains.adaptor.psi;

import com.intellij.lang.Language;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.impl.PsiFileFactoryImpl;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import org.antlr.jetbrains.adaptor.lexer.RuleIElementType;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class Trees {
	private Trees() { }

	/** Return a list of all ancestors of this node.  The first node of
	 *  list is the root and the last is the parent of this node.
	 */
	public static List<? extends PsiElement> getAncestors(PsiElement t) {
		if ( t.getParent()==null ) return Collections.emptyList();
		List<PsiElement> ancestors = new ArrayList<>();
		t = t.getParent();
		while ( t!=null ) {
			ancestors.add(0, t); // insert at start
			t = t.getParent();
		}
		return ancestors;
	}

	/** Return true if t is u's parent or a node on path to root from u.
	 *  Use == not equals().
	 *
	 *  @since 4.5.1
	 */
	public static boolean isAncestorOf(PsiElement t, PsiElement u) {
		if ( t==null || u==null || t.getParent()==null ) return false;
		PsiElement p = u.getParent();
		while ( p!=null ) {
			if ( t==p ) return true;
			p = p.getParent();
		}
		return false;
	}

	public static Collection<PsiElement> findAllTokenNodes(PsiElement t, int ttype) {
		return findAllNodes(t, ttype, true);
	}

	public static Collection<PsiElement> findAllRuleNodes(PsiElement t, int ruleIndex) {
		return findAllNodes(t, ruleIndex, false);
	}

	public static List<PsiElement> findAllNodes(PsiElement t, int index, boolean findTokens) {
		List<PsiElement> nodes = new ArrayList<>();
		_findAllNodes(t, index, findTokens, nodes);
		return nodes;
	}

	public static void _findAllNodes(PsiElement t, int index, boolean findTokens,
									 List<? super PsiElement> nodes)
	{
		// check this node (the root) first
		if ( findTokens && t instanceof TerminalNode ) {
			TerminalNode tnode = (TerminalNode)t;
			if ( tnode.getSymbol().getType()==index ) nodes.add(t);
		}
		else if ( !findTokens && t instanceof ParserRuleContext ) {
			ParserRuleContext ctx = (ParserRuleContext)t;
			if ( ctx.getRuleIndex() == index ) nodes.add(t);
		}
		// check children
		for (PsiElement c : t.getChildren()) {
			_findAllNodes(c, index, findTokens, nodes);
		}
	}

	/** Get all descendents; includes t itself. */
	public static List<PsiElement> getDescendants(PsiElement t) {
		List<PsiElement> nodes = new ArrayList<>();
		nodes.add(t);

		for (PsiElement c : t.getChildren()) {
			nodes.addAll(getDescendants(c));
		}
		return nodes;
	}

	/** Find smallest subtree of t enclosing range startCharIndex..stopCharIndex
	 *  inclusively using postorder traversal.  Recursive depth-first-search.
	 */
	public static PsiElement getRootOfSubtreeEnclosingRegion(PsiElement t,
	                                                         int startCharIndex, // inclusive
	                                                         int stopCharIndex)  // inclusive
	{
		for (PsiElement c : t.getChildren()) {
			PsiElement sub = getRootOfSubtreeEnclosingRegion(c, startCharIndex, stopCharIndex);
			if ( sub!=null ) return sub;
		}
		IElementType elementType = t.getNode().getElementType();
		if ( elementType instanceof RuleIElementType ) {
			TextRange r = t.getNode().getTextRange();
			// is range fully contained in t?  Note: jetbrains uses exclusive right end (use < not <=)
			if ( startCharIndex>=r.getStartOffset() && stopCharIndex<r.getEndOffset() ) {
				return t;
			}
		}
		return null;
	}

	/** Return first node satisfying the pred among descendants. Depth-first order. Test includes t itself. */
	public static PsiElement findNodeSuchThat(PsiElement t, Predicate<PsiElement> pred) {
		if ( pred.test(t) ) return t;

		for (PsiElement c : t.getChildren()) {
			PsiElement u = findNodeSuchThat(c, pred);
			if ( u!=null ) return u;
		}
		return null;
	}

	public static PsiElement createLeafFromText(Project project, Language language, PsiElement context,
												String text, IElementType type)
	{
		PsiFileFactoryImpl factory = (PsiFileFactoryImpl) PsiFileFactory.getInstance(project);
		PsiElement el = factory.createElementFromText(text, language, type, context);
		if ( el==null ) return null;
		return PsiTreeUtil.getDeepestFirst(el); // forces parsing of file!!
		// start rule depends on root passed in
	}

	public static void replacePsiFileFromText(final Project project, Language language, final PsiFile psiFile, String text) {
		final PsiFile newPsiFile = createFile(project, language, text);
		if ( newPsiFile==null ) return;
		WriteCommandAction setTextAction = new WriteCommandAction(project) {
			@Override
			protected void run(@NotNull Result result) throws Throwable {
				psiFile.deleteChildRange(psiFile.getFirstChild(), psiFile.getLastChild());
				psiFile.addRange(newPsiFile.getFirstChild(), newPsiFile.getLastChild());
			}
		};
		setTextAction.execute();
	}

	public static PsiFile createFile(Project project, Language language, String text) {
		LanguageFileType ftype = language.getAssociatedFileType();
		if ( ftype==null ) return null;
		String ext = ftype.getDefaultExtension();
		String fileName = "___fubar___."+ext; // random name but must have correct extension
		PsiFileFactoryImpl factory = (PsiFileFactoryImpl)PsiFileFactory.getInstance(project);
		return factory.createFileFromText(fileName, language, text, false, false);
	}
}
