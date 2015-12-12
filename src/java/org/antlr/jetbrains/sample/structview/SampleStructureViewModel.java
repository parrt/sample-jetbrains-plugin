package org.antlr.jetbrains.sample.structview;

import com.intellij.ide.structureView.StructureViewModel;
import com.intellij.ide.structureView.StructureViewModelBase;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.psi.PsiFile;
import org.antlr.jetbrains.sample.SamplePSIFileRoot;

public class SampleStructureViewModel
	extends StructureViewModelBase
	implements StructureViewModel.ElementInfoProvider
{
	public SampleStructureViewModel(SamplePSIFileRoot root) {
		super(root, new ANTLRv4StructureViewElement(rootElement));
	}

	@Override
	public boolean isAlwaysLeaf(StructureViewTreeElement element) {
		return false;
	}

	@Override
	public boolean isAlwaysShowsPlus(StructureViewTreeElement element) {
		return false;
	}
}
