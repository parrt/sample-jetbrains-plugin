package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPathWildcardElement extends XPathElement {
	public XPathWildcardElement() {
		super(XPath.WILDCARD);
	}

	@Override
	public Collection<PsiElement> evaluate(final PsiElement t) {
		if ( invert ) return new ArrayList<>(); // !* is weird but valid (empty)
		List<PsiElement> kids = new ArrayList<PsiElement>();
		for (PsiElement c : t.getChildren()) {
			kids.add(c);
		}
		return kids;
	}
}
