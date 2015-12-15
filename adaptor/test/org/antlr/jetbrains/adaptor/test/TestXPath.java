package org.antlr.jetbrains.adaptor.test;

import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.ParsingTestCase;
import org.antlr.jetbrains.adaptor.xpath.XPath;
import org.antlr.jetbrains.sample.SampleLanguage;
import org.antlr.jetbrains.sample.SampleParserDefinition;

import java.io.IOException;
import java.util.Collection;

public class TestXPath extends ParsingTestCase {
	public TestXPath() {
		super("", "Sample", new SampleParserDefinition());
	}

	public void testVarDef() throws Exception {
		checkXPathResults(SampleLanguage.INSTANCE, "var x = 1", "/script/statement", "var x = 1");

	}

	protected void checkXPathResults(Language language, String code, String xpath, String allNodesText) throws IOException {
		myFile = createPsiFile("a", code);
		ensureParsed(myFile);
		assertEquals(code, myFile.getText());
		Collection<? extends PsiElement> nodes = XPath.findAll(language, myFile, xpath);
		StringBuilder buf = new StringBuilder();
		for (PsiElement t : nodes) {
			buf.append(t.getText());
		}
		assertEquals(allNodesText, buf.toString());
	}

	@Override
	protected String getTestDataPath() {
		return ".";
	}

	@Override
	protected boolean skipSpaces() {
		return false;
	}

	@Override
	protected boolean includeRanges() {
		return true;
	}
}
