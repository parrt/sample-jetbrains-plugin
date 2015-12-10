package org.antlr.jetbrains.adaptor.lexer;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Represents a token in the language of the plug-in. The "token type" of
 *  leaf nodes in PSI tree.
 *
 *  Intellij Lexer token types are instances of IElementType.
 *  We differentiate between parse tree subtree roots and tokens with
 *  {@link RuleElementType} and {@link TokenElementType}.
 */
public class TokenElementType extends IElementType {
	private final int type;

	public TokenElementType(int type,
	                        @NotNull @NonNls String debugName,
	                        @Nullable Language language)
	{
		super(debugName, language);
		this.type = type;
	}

	public int getType() {
		return type;
	}
}
