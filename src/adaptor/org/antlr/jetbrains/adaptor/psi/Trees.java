package org.antlr.jetbrains.adaptor.psi;

import com.intellij.psi.PsiElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Trees {
	/** Return a list of all ancestors of this node.  The first node of
	 *  list is the root and the last is the parent of this node.
	 *
	 *  @since 4.5.1
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

	/** Get all descendents; includes t itself.
	 *
	 * @since 4.5.1
 	 */
	public static List<PsiElement> getDescendants(PsiElement t) {
		List<PsiElement> nodes = new ArrayList<>();
		nodes.add(t);

		for (PsiElement c : t.getChildren()) {
			nodes.addAll(getDescendants(c));
		}
		return nodes;
	}
}
