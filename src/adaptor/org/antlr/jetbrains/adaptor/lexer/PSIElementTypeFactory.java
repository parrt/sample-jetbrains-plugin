package org.antlr.jetbrains.adaptor.lexer;

import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.TokenSet;
import org.antlr.v4.runtime.Token;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PSIElementTypeFactory {
	private static final Map<Language, List<TokenElementType>> tokenElementTypesCache =
		new HashMap<Language, List<TokenElementType>>();
	private static final Map<Language, List<RuleElementType>> ruleElementTypesCache =
		new HashMap<Language, List<RuleElementType>>();
	private static final Map<Language, TokenElementType> eofElementTypesCache =
		new HashMap<Language, TokenElementType>();

	private PSIElementTypeFactory() {
	}

	public static TokenElementType getEofElementType(Language language) {
		TokenElementType result = eofElementTypesCache.get(language);
		if (result == null) {
			result = new TokenElementType(Token.EOF, "EOF", language);
			eofElementTypesCache.put(language, result);
		}

		return result;
	}

	public static List<TokenElementType> getTokenIElementTypes(Language language) {
		return tokenElementTypesCache.get(language);
	}

	public static List<TokenElementType> getTokenIElementTypes(Language language,
	                                                           List<String> tokenNames)
	{
		List<TokenElementType> types = tokenElementTypesCache.get(language);
		if (types == null) {
			types = createTokenIElementTypes(language, tokenNames);
			tokenElementTypesCache.put(language, types);
		}

		return types;
	}

	@NotNull
	private static List<TokenElementType> createTokenIElementTypes(Language language, List<String> tokenNames) {
		List<TokenElementType> result;
		TokenElementType[] elementTypes = new TokenElementType[tokenNames.size()];
		for (int i = 0; i < tokenNames.size(); i++) {
			if ( tokenNames.get(i)!=null ) {
				elementTypes[i] = new TokenElementType(i, tokenNames.get(i), language);
			}
		}

		result = Collections.unmodifiableList(Arrays.asList(elementTypes));
		return result;
	}

	public static List<RuleElementType> getRuleIElementTypes(Language language,
	                                                         List<String> ruleNames)
	{
		List<RuleElementType> result = ruleElementTypesCache.get(language);
		if (result == null) {
			result = createRuleIElementTypes(language, ruleNames);
			ruleElementTypesCache.put(language, result);
		}

		return result;
	}

	@NotNull
	private static List<RuleElementType> createRuleIElementTypes(Language language, List<String> ruleNames) {
		List<RuleElementType> result;
		RuleElementType[] elementTypes = new RuleElementType[ruleNames.size()];
		for (int i = 0; i < ruleNames.size(); i++) {
			elementTypes[i] = new RuleElementType(i, ruleNames.get(i), language);
		}

		result = Collections.unmodifiableList(Arrays.asList(elementTypes));
		return result;
	}

	public static TokenSet createTokenSet(Language language,
	                                      List<String> tokenNames,
	                                      int... types)
	{
		List<TokenElementType> tokenElementTypes =
			getTokenIElementTypes(language, tokenNames);

		IElementType[] elementTypes = new IElementType[types.length];
		for (int i = 0; i < types.length; i++) {
			if (types[i] == Token.EOF) {
				elementTypes[i] = getEofElementType(language);
			}
			else {
				elementTypes[i] = tokenElementTypes.get(types[i]);
			}
		}

		return TokenSet.create(elementTypes);
	}

	public static TokenSet createRuleSet(Language language,
	                                     List<String> ruleNames,
	                                     int... rules)
	{
		List<RuleElementType> tokenElementTypes =
			getRuleIElementTypes(language, ruleNames);

		IElementType[] elementTypes = new IElementType[rules.length];
		for (int i = 0; i < rules.length; i++) {
			if (rules[i] == Token.EOF) {
				elementTypes[i] = getEofElementType(language);
			}
			else {
				elementTypes[i] = tokenElementTypes.get(rules[i]);
			}
		}

		return TokenSet.create(elementTypes);
	}
}
