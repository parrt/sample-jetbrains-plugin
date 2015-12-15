package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.antlr.jetbrains.adaptor.lexer.TokenIElementType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPathTokenElement extends XPathElement {
	protected int tokenType;

	public XPathTokenElement(String tokenName, int tokenType) {
		super(tokenName);
		this.tokenType = tokenType;
	}

	@Override
	public Collection<PsiElement> evaluate(PsiElement t) {
		// return all children of t that match nodeName
		List<PsiElement> nodes = new ArrayList<>();
		for (PsiElement c : t.getChildren()) {
			IElementType elementType = c.getNode().getElementType();
			if ( elementType instanceof TokenIElementType ) {
				TokenIElementType tokEl = (TokenIElementType) elementType;
				if ( (tokEl.getANTLRTokenType()==tokenType && !invert) ||
					 (tokEl.getANTLRTokenType()!=tokenType && invert) )
				{
					nodes.add(c);
				}
			}
		}
		return nodes;
	}
}