package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;
import org.antlr.jetbrains.adaptor.psi.Trees;

import java.util.Collection;

/**
 * Either {@code ID} at start of path or {@code ...//ID} in middle of path.
 */
public class XPathTokenAnywhereElement extends XPathElement {
	protected int tokenType;
	public XPathTokenAnywhereElement(String tokenName, int tokenType) {
		super(tokenName);
		this.tokenType = tokenType;
	}

	@Override
	public Collection<PsiElement> evaluate(PsiElement t) {
		return Trees.findAllTokenNodes(t, tokenType);
	}
}
