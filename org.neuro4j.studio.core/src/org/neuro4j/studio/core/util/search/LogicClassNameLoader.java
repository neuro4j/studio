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
package org.neuro4j.studio.core.util.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.neuro4j.studio.core.util.ClassloaderHelper;
import org.neuro4j.studio.core.util.MapWorkspaceUpdater;
import org.neuro4j.studio.core.util.WorkspaceUpdater;

public class LogicClassNameLoader {

    private static LogicClassNameLoader instance = new LogicClassNameLoader();

    private static final IType[] EMPTY_ARRAY = new IType[0];
    private static final String LOGIC_BASE_CLASS = "org.neuro4j.workflow.node.CustomBlock";

    private Map<String, List<String>> classes = new HashMap<String, List<String>>();

    private LogicClassNameLoader()
    {
        super();

    }

    public static LogicClassNameLoader getInstance()
    {
        return instance;
    }

    private synchronized void load(String projectName) {
        loadClassses(projectName);

    }

    public List<String> getClasses(String projectName)
    {
        if (!classes.containsKey(projectName))
        {
            load(projectName);
        }
        return classes.get(projectName);
    }

    /**
     * Returns classes which extend import org.neuro4j.workflow.def.CustomBlock
     * 
     * @return
     * @throws JavaModelException
     */
    private void loadClassses(String projectName) {

        IJavaProject javaProject = ClassloaderHelper.getJavaProject(projectName);
        List<String> classList = new LinkedList<String>();
        // //Set<String> classList = new HashSet<String>();
        // IWorkspace workspace = ResourcesPlugin.getWorkspace();
        // IWorkspaceRoot root = workspace.getRoot();
        // IProject[] projects = root.getProjects();
        // for (IProject project : projects) {
        // IJavaProject javaProject = JavaCore.create(project);

        IType[] types = null;
        try {
            types = getAllSubtypes(javaProject, new NullProgressMonitor());
        } catch (JavaModelException e) {
            e.printStackTrace();

        }
        for (IType t : types) {
            t.getPath();

            classList.add(getClassName(t));
        }

        classes.put(projectName, classList);

    }

    private IType[] getAllSubtypes(IJavaProject project, IProgressMonitor pm) throws JavaModelException {
        IType parentType = project.findType(LOGIC_BASE_CLASS);
        if (parentType == null)
        {
            return EMPTY_ARRAY;
        }
        ITypeHierarchy h = parentType.newTypeHierarchy(project, pm);
        return h.getAllSubtypes(parentType);
    }

    private String getClassName(IType type)
    {
        return type.getFullyQualifiedName();
    }

    public static WorkspaceUpdater getUpdater()
    {
        return new MapWorkspaceUpdater(instance.classes);
    }

}