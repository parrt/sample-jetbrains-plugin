package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;
import org.antlr.jetbrains.adaptor.psi.Trees;

import java.util.ArrayList;
import java.util.Collection;

public class XPathWildcardAnywhereElement extends XPathElement {
	public XPathWildcardAnywhereElement() {
		super(XPath.WILDCARD);
	}

	@Override
	public Collection<PsiElement> evaluate(PsiElement t) {
		if ( invert ) return new ArrayList<PsiElement>(); // !* is weird but valid (empty)
		return Trees.getDescendants(t);
	}
}
