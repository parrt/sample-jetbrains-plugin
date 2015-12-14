package org.antlr.jetbrains.adaptor.xpath;

import com.intellij.psi.PsiElement;
import org.antlr.jetbrains.adaptor.psi.Trees;

import java.util.Collection;

/**
 * Either {@code ID} at start of path or {@code ...//ID} in middle of path.
 */
public class XPathRuleAnywhereElement extends XPathElement {
	protected int ruleIndex;
	public XPathRuleAnywhereElement(String ruleName, int ruleIndex) {
		super(ruleName);
		this.ruleIndex = ruleIndex;
	}

	@Override
	public Collection<PsiElement> evaluate(PsiElement t) {
		return Trees.findAllRuleNodes(t, ruleIndex);
	}
}
