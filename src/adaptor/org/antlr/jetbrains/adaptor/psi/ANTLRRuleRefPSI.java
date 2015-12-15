package org.antlr.jetbrains.adaptor.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ANTLRRuleRefPSI extends ASTWrapperPsiElement {
	public ANTLRRuleRefPSI(@NotNull ASTNode node) {
		super(node);
	}

	/** For some reason, default impl of this only returns rule refs
	 *  (composite nodes in jetbrains speak) but we want ALL children. duh.
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
