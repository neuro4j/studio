/*
 * Copyright (c) 2013-2014, Neuro4j.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neuro4j.studio.core.diagram.navigator;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.workspace.util.WorkspaceSynchronizer;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionConstants;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.part.FileEditorInput;
import org.neuro4j.studio.core.diagram.edit.parts.NetworkEditPart;
import org.neuro4j.studio.core.diagram.part.Messages;
import org.neuro4j.studio.core.diagram.part.Neuro4jDiagramEditor;
import org.neuro4j.studio.core.diagram.part.Neuro4jDiagramEditorPlugin;
import org.neuro4j.studio.core.diagram.part.Neuro4jVisualIDRegistry;

/**
 * @generated
 */
public class Neuro4jNavigatorActionProvider extends CommonActionProvider {

    /**
     * @generated
     */
    private boolean myContribute;

    /**
     * @generated
     */
    private OpenDiagramAction myOpenDiagramAction;

    /**
     * @generated
     */
    public void init(ICommonActionExtensionSite aSite) {
        super.init(aSite);
        if (aSite.getViewSite() instanceof ICommonViewerWorkbenchSite) {
            myContribute = true;
            makeActions((ICommonViewerWorkbenchSite) aSite.getViewSite());
        } else {
            myContribute = false;
        }
    }

    /**
     * @generated
     */
    private void makeActions(ICommonViewerWorkbenchSite viewerSite) {
        myOpenDiagramAction = new OpenDiagramAction(viewerSite);
    }

    /**
     * @generated
     */
    public void fillActionBars(IActionBars actionBars) {
        if (!myContribute) {
            return;
        }
        IStructuredSelection selection = (IStructuredSelection) getContext()
                .getSelection();
        myOpenDiagramAction.selectionChanged(selection);
        if (myOpenDiagramAction.isEnabled()) {
            actionBars.setGlobalActionHandler(ICommonActionConstants.OPEN,
                    myOpenDiagramAction);
        }
    }

    /**
     * @generated
     */
    public void fillContextMenu(IMenuManager menu) {
    }

    /**
     * @generated
     */
    private static class OpenDiagramAction extends Action {

        /**
         * @generated
         */
        private Diagram myDiagram;

        /**
         * @generated
         */
        private ICommonViewerWorkbenchSite myViewerSite;

        /**
         * @generated
         */
        public OpenDiagramAction(ICommonViewerWorkbenchSite viewerSite) {
            super(Messages.NavigatorActionProvider_OpenDiagramActionName);
            myViewerSite = viewerSite;
        }

        /**
         * @generated
         */
        public final void selectionChanged(IStructuredSelection selection) {
            myDiagram = null;
            if (selection.size() == 1) {
                Object selectedElement = selection.getFirstElement();
                if (selectedElement instanceof Neuro4jNavigatorItem) {
                    selectedElement = ((Neuro4jNavigatorItem) selectedElement)
                            .getView();
                } else if (selectedElement instanceof IAdaptable) {
                    selectedElement = ((IAdaptable) selectedElement)
                            .getAdapter(View.class);
                }
                if (selectedElement instanceof Diagram) {
                    Diagram diagram = (Diagram) selectedElement;
                    if (NetworkEditPart.MODEL_ID.equals(Neuro4jVisualIDRegistry
                            .getModelID(diagram))) {
                        myDiagram = diagram;
                    }
                }
            }
            setEnabled(myDiagram != null);
        }

        /**
         * @generated
         */
        public void run() {
            if (myDiagram == null || myDiagram.eResource() == null) {
                return;
            }

            IEditorInput editorInput = getEditorInput(myDiagram);
            IWorkbenchPage page = myViewerSite.getPage();
            try {
                page.openEditor(editorInput, Neuro4jDiagramEditor.ID);
            } catch (PartInitException e) {
                Neuro4jDiagramEditorPlugin.getInstance().logError("Exception while openning diagram", e); //$NON-NLS-1$
            }
        }

        /**
         * @generated
         */
        private static IEditorInput getEditorInput(Diagram diagram) {
            Resource diagramResource = diagram.eResource();
            for (EObject nextEObject : diagramResource.getContents()) {
                if (nextEObject == diagram) {
                    return new FileEditorInput(
                            WorkspaceSynchronizer.getFile(diagramResource));
                }
                if (nextEObject instanceof Diagram) {
                    break;
                }
            }
            URI uri = EcoreUtil.getURI(diagram);
            String editorName = uri.lastSegment() + '#'
                    + diagram.eResource().getContents().indexOf(diagram);
            IEditorInput editorInput = new URIEditorInput(uri, editorName);
            return editorInput;
        }

    }

}
