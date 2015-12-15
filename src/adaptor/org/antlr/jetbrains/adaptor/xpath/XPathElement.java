package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;

import java.util.Collection;

public abstract class XPathElement {
	protected String nodeName;
	protected boolean invert;

	/** Construct element like {@code /ID} or {@code ID} or {@code /*} etc...
	 *  op is null if just node
	 */
	public XPathElement(String nodeName) {
		this.nodeName = nodeName;
	}

	/**
	 * Given subtree rooted at {@code t} return all children of t if
	 * t is matched by this path element. I.e., return all nodes that
	 * could potentially match any next element in the xpath.
	 */
	public abstract Collection<PsiElement> evaluate(PsiElement t);

	@Override
	public String toString() {
		String inv = invert ? "!" : "";
		return getClass().getSimpleName()+"["+inv+nodeName+"]";
	}
}
