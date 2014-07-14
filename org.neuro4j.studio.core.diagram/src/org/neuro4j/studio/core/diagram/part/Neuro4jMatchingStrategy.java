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
package org.neuro4j.studio.core.diagram.part;

import org.eclipse.emf.common.ui.URIEditorInput;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorMatchingStrategy;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PartInitException;

/**
 * @generated
 */
public class Neuro4jMatchingStrategy implements IEditorMatchingStrategy {

    /**
     * @generated NOT
     */
    public boolean matches(IEditorReference editorRef, IEditorInput input) {
        IEditorInput editorInput;
        try {
            editorInput = editorRef.getEditorInput();
        } catch (PartInitException e) {
            return false;
        }
        if (editorInput == null || input == null)
        {
            return false;
        }
        if (editorInput.equals(input)) {
            return true;
        }
        if (editorInput instanceof URIEditorInput
                && input instanceof URIEditorInput) {
            return ((URIEditorInput) editorInput).getURI().equals(
                    ((URIEditorInput) input).getURI());
        }
        return false;
    }

}
