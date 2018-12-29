package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;

import java.util.Set;

/**
 * @author manyce400
 */
public interface IDependencyAssociationService {


    /**
     * @param dependency
     * @return <code>true</code> IF this dependency passed in as argument is being referenced as a dependency of another
     *         package/module
     */
    public boolean isReferencedDependency(Dependency dependency);

    /**
     * Creates and establishes dependency relationship between main package {@Link #mainPackage} and collection of
     * all #dependencies that main packageRequires
     *
     * @param mainPackage
     * @param dependencies
     */
    public void buildDependencyHierarchy(Dependency mainPackage, Set<Dependency> dependencies);

}
