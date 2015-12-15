package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import org.antlr.jetbrains.adaptor.lexer.RuleIElementType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class XPathRuleElement extends XPathElement {
	protected int ruleIndex;
	public XPathRuleElement(String ruleName, int ruleIndex) {
		super(ruleName);
		this.ruleIndex = ruleIndex;
	}

	@Override
	public Collection<PsiElement> evaluate(PsiElement t) {
		// return all children of t that match ANTLR rule index
		List<PsiElement> nodes = new ArrayList<>();
		for (PsiElement c : t.getChildren()) {
			IElementType elementType = c.getNode().getElementType();
			if ( elementType instanceof RuleIElementType ) {
				RuleIElementType ruleEl = (RuleIElementType)elementType;
				if ( (ruleEl.getRuleIndex() == ruleIndex && !invert) ||
					 (ruleEl.getRuleIndex() != ruleIndex && invert) )
				{
					nodes.add(c);
				}
			}
		}
		return nodes;
	}
}
