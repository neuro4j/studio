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
package org.neuro4j.studio.core;

import java.util.List;

import org.neuro4j.workflow.xml.WorkflowNode;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Standard Node</b></em>'.
 * <!-- end-user-doc -->
 * 
 * 
 * @see org.neuro4j.studio.core.Neuro4jPackage#getStandardNode()
 * @model
 * @generated
 */
public interface StandardNode extends ActionNode {

    public static final int X_OFFSET = 15;

    public static final int Y_OFFSET = 17;

    public int getType();

    public void setType(int type);

    public List<KeyValuePair> getProperties();

    public void addProperty(String key, String value);

    public void addConnection(WorkflowNode node);

    public List getRepresentations();

} // StandardNode
