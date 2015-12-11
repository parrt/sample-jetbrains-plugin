package org.antlr.jetbrains.plugin;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementFactory;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;

public class SampleASTFactory extends ASTFactory {
	/** Create a FileElement for root or a parse tree CompositeElement (not
	 *  PSI) for the token. This impl is more or less the default.
	 */
    @Override
    public CompositeElement createComposite(IElementType type) {
        if (type instanceof IFileElementType ) {
            return new FileElement(type, null);
		}
        return new CompositeElement(type);
    }

	/** Create PSI leaf node from a token.
	 *  Does not see whitespace tokens.
	 */
    @Override
    public LeafElement createLeaf(IElementType type, CharSequence text) {
	    return new LeafPsiElement(type, text);
    }

	public static PsiElement createInternalParseTreeNode(ASTNode node) {
		PsiElement t;
		IElementType tokenType = node.getElementType();
		PsiElementFactory factory = ruleElementTypeToPsiFactory.get(tokenType);
		if (factory != null) {
			t = factory.createElement(node);
		}
		else {
			t = new ASTWrapperPsiElement(node);
		}

		return t;
	}
}
