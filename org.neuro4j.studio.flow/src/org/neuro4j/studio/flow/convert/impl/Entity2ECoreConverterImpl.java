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
package org.neuro4j.studio.flow.convert.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.neuro4j.studio.core.ActionNode;
import org.neuro4j.studio.core.Network;
import org.neuro4j.studio.core.Neuro4jFactory;
import org.neuro4j.studio.core.Neuro4jPackage;
import org.neuro4j.studio.core.impl.CallNodeImpl;
import org.neuro4j.studio.core.impl.DecisionNodeImpl;
import org.neuro4j.studio.core.impl.EndNodeImpl;
import org.neuro4j.studio.core.impl.FollowByRelationNodeImpl;
import org.neuro4j.studio.core.impl.JoinNodeImpl;
import org.neuro4j.studio.core.impl.LogicNodeImpl;
import org.neuro4j.studio.core.impl.LoopNodeImpl;
import org.neuro4j.studio.core.impl.MapperNodeImpl;
import org.neuro4j.studio.core.impl.OperatorOutputImpl;
import org.neuro4j.studio.core.impl.StartNodeImpl;
import org.neuro4j.studio.core.impl.ViewNodeImpl;
import org.neuro4j.studio.core.util.PropetiesConstants;
import org.neuro4j.studio.flow.convert.Entity2ECoreConverter;
import org.neuro4j.workflow.xml.Transition;
import org.neuro4j.workflow.xml.WorkflowNode;

public class Entity2ECoreConverterImpl implements Entity2ECoreConverter {
    Neuro4jFactory neuro4jFactory = null;
    Neuro4jPackage neuro4jPackage = null;
    Network network = null;

    public Entity2ECoreConverterImpl(Network network) {
        this.network = network;
        neuro4jFactory = Neuro4jFactory.eINSTANCE;
        neuro4jPackage = Neuro4jPackage.eINSTANCE;
    }

    /**
     * Converts Entity to ActionNode
     */
    @Override
    public EObject convert(WorkflowNode entity) {

        EClass eClass = getEClassByEntity(entity);
        EObject eObject = null;
        if (eClass != null)
        {
            eObject = neuro4jFactory.create(eClass);

            EcoreUtil.setID(eObject, entity.getUuid());

            processAttributes(eObject, entity);

        }

        return eObject;
    }

    private EClass getEClassByEntity(WorkflowNode entity)
    {
        String blockClass = entity.getParameter("SWF_BLOCK_CLASS");
        EClass eClass = getClass(blockClass);
        return eClass;
    }

    private EClass getClass(String className) {
        if (JoinNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getJoinNode();
        } else if (StartNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getStartNode();
        } else if (EndNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getEndNode();
        } else if (CallNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getCallNode();
        } else if (DecisionNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getDecisionNode();
        } else if (LoopNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getLoopNode();
        } else if (LogicNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getLogicNode();
        } else if (MapperNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getMapperNode();
        } else if (FollowByRelationNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getFollowByRelationNode();
        } else if (ViewNodeImpl.IMPL_CLASS.equals(className)) {
            return neuro4jPackage.getViewNode();
        } else if (Network.CONFIG_NODE_CLASS_NAME.equals(className)) {
            return null;
        } else if (Network.NOTE_NODE_CLASS_NAME.equals(className)) {
            return neuro4jPackage.getNoteNode();
            // return null;
        } else {
            // if not it is custom implementation of logic node
            // return neuro4jPackage.getS
            return neuro4jPackage.getLogicNode();
        }

    }

    private void processAttributes(EObject eObject, WorkflowNode entity) {
        ActionNode node = (ActionNode) eObject;
        node.setNetwork(this.network);
        processNodeName(node, entity);
        processCoordinates(node, entity);
        // set specific properties from entity to Node
        node.getNodeSpecificProperties(entity);
    }

    /**
     * Reads node name.
     * 
     * @param node
     * @param entity
     */
    private void processNodeName(ActionNode node, WorkflowNode entity) {
        if (entity.getName() != null)
        {
            node.setName(entity.getName());
        }
    }

    /**
     * Reads X,Y coordinates from entity
     * 
     * @param node
     * @param entity
     */
    private void processCoordinates(ActionNode node, WorkflowNode entity) {

        String locationX = entity.getParameter(PropetiesConstants.LOCATION_X);
        if (locationX != null)
        {
            int x = Integer.parseInt(locationX);
            node.setX(x);
        }

        String locationY = entity.getParameter(PropetiesConstants.LOCATION_Y);
        if (locationY != null)
        {
            int y = Integer.parseInt(locationY);
            node.setY(y);
        }
    }

    /**
     * getting all out relations
     * TODO:
     * 
     **/
    @Override
    public List<OperatorOutputImpl> getOutRelations(WorkflowNode entity, Map<String, EObject> map) {
        Collection<Transition> relations = entity.getExits();
        List<OperatorOutputImpl> re = new ArrayList<OperatorOutputImpl>();
        for (Transition rel : relations) {

            OperatorOutputImpl relation = (OperatorOutputImpl) map.get(rel.getUuid());

            relation.setId(rel.getUuid());
            relation.setName(rel.getName());
            re.add(relation);
        }

        return re;
    }

}
