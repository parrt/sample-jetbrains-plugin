package org.antlr.jetbrains.sample;

import com.intellij.lang.ASTFactory;
import com.intellij.lang.ParserDefinition;
import com.intellij.psi.impl.source.tree.CompositeElement;
import com.intellij.psi.impl.source.tree.FileElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;

public class SampleASTFactory extends ASTFactory {
	/** Create a FileElement for root or a parse tree CompositeElement (not
	 *  PSI) for the token. This impl is more or less the default.
	 *  The FileElement is a parse tree node, which is converted to a PsiFile
	 *  by {@link ParserDefinition#createFile}.
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
}
