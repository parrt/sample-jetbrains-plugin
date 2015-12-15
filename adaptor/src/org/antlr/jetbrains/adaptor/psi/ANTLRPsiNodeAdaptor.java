package org.antlr.jetbrains.adaptor.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import org.antlr.jetbrains.adaptor.lexer.RuleIElementType;
import org.antlr.jetbrains.adaptor.lexer.TokenIElementType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/** This class adapts ANTLR parse subtree roots to be PSI "composite" nodes.
 *  So for, the only reason I needed is to make sure that I get all children
 *  from {@link #getChildren()}. The IElementType of the ASTNodes will
 *  be {@link RuleIElementType}.
 *
 *  So far, there is no need for an adapter for LeafElement so I
 *  won't create one in order to reduce the requirements of plugins
 *  using this library. Note that the ASTNode's associated with
 *  LeafElement's are {@link TokenIElementType}.
 */
public class ANTLRPsiNodeAdaptor extends ASTWrapperPsiElement {
	public ANTLRPsiNodeAdaptor(@NotNull ASTNode node) {
		super(node);
	}

	/** For some reason, default impl of this only returns rule refs
	 *  (composite nodes in jetbrains speak) but we want ALL children.
	 */
	@Override
	@NotNull
	public PsiElement[] getChildren() {
		PsiElement psiChild = getFirstChild();
		if (psiChild == null) return PsiElement.EMPTY_ARRAY;

		List<PsiElement> result = new ArrayList<>();
		while (psiChild != null) {
			result.add(psiChild);
			psiChild = psiChild.getNextSibling();
		}
		return PsiUtilCore.toPsiElementArray(result);
	}
}
