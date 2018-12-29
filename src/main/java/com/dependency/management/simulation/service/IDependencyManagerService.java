package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;

/**
 * @author manyce400
 */
public interface IDependencyManagerService {


    public void installDependency(Dependency mainPackage);

    public void removeDependency(Dependency mainPackage);

    public void listAllInstalledPackages();

    public boolean isInstalled(Dependency mainPackage);

}
