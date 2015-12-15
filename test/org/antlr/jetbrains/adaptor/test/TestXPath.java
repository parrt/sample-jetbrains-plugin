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

	public void testSingleVarDef() throws Exception {
		String code = "var x = 1";
		String output = code;
		String xpath = "/script/statement";
		checkXPathResults(code, xpath, output);
	}

	public void testMultiVarDef() throws Exception {
		String code =
			"var x = 1\n" +
			"var y = [1,2,3]\n";
		String output = code;
		String xpath = "/script/statement";
		checkXPathResults(code, xpath, output);
	}

	public void testFuncNames() throws Exception {
		String code = loadFile("test/org/antlr/jetbrains/adaptor/test/test.sample");
		String output =
			"f\n"+
			"bubbleSort\n"+
			"z\n"+
			"q";
		String xpath = "/script/function/ID";
		checkXPathResults(code, xpath, output);
	}

	protected void checkXPathResults(String code, String xpath, String allNodesText) throws IOException {
		checkXPathResults(SampleLanguage.INSTANCE, code, xpath, allNodesText);
	}

	protected void checkXPathResults(Language language, String code, String xpath, String allNodesText) throws IOException {
		myFile = createPsiFile("a", code);
		ensureParsed(myFile);
		assertEquals(code, myFile.getText());
		Collection<? extends PsiElement> nodes = XPath.findAll(language, myFile, xpath);
		StringBuilder buf = new StringBuilder();
		for (PsiElement t : nodes) {
			buf.append(t.getText());
			buf.append("\n");
		}
		assertEquals(allNodesText.trim(), buf.toString().trim());
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
