package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(DependencyManagerService.SPRING_BEAN)
public class DependencyManagerService implements IDependencyManagerService {



    private final Set<Dependency> INSTALLED_DEPENDENCIES = new HashSet<>();

    public static final String SPRING_BEAN = "com.dependency.management.simulation.service.DependencyManagerService";


    @Override
    public void installDependency(Dependency mainPackage) {
        Assert.notNull(mainPackage, "mainPackage cannot be null");

        // First install explicitly all dependencies of mainPackage
        Set<Dependency> dependencies = mainPackage.getDependencies();

        // Step is technically not necessary since Set will eliminate duplicates
        dependencies.forEach(dependency -> {
            if(!INSTALLED_DEPENDENCIES.contains(dependency)) {
                INSTALLED_DEPENDENCIES.add(dependency);
            }
        });

        INSTALLED_DEPENDENCIES.add(mainPackage);

    }

    @Override
    public void removeDependency(Dependency mainPackage) {
        Assert.notNull(mainPackage, "mainPackage cannot be null");
        System.out.println("Executing call to remove dependency: "+mainPackage.getArtifact());

        // First install explicitly all dependencies of mainPackage
        Set<Dependency> dependencies = mainPackage.getDependencies();

        if(isInstalled(mainPackage)) {
            // Before removing check to see if it has a parent dependencies, if not safe to remove
            if(!mainPackage.hasParentDependencies()) {
                INSTALLED_DEPENDENCIES.remove(mainPackage);

                // because we have successfully removed mainPackage, its now safe to remove its dependencies which don't have any explicit parent dependencies
                dependencies.forEach(dependency -> {
                    // Execute recursive call to remove dependencies down all children dependencies
                    dependency.removeParentDependency(mainPackage);
                    removeDependency(dependency);
                });

                System.out.println("Package: {" + mainPackage.getArtifact() + "} has been removed");
            } else {
                String parentDependenciesAsString = getParentDependenciesAsString(mainPackage.getParentDependencies());
                System.out.println("Cannot remove Package: {" + mainPackage.getArtifact() + "}. It's currently a dependency for packages " + parentDependenciesAsString);
            }
        } else {
            System.out.println("Package: {" + mainPackage.getArtifact() + "} is not currently installed, cannot remove");
        }
    }

    private String getParentDependenciesAsString(Set<Dependency> parentDependencies) {
        StringBuffer sb = new StringBuffer("[");
        parentDependencies.forEach(dependency -> {
            sb.append(dependency.getArtifact()).append(", ");
        });
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void listAllInstalledPackages() {
        System.out.println("Listing all installed packages ====> ");
        INSTALLED_DEPENDENCIES.forEach(dependency -> {
            System.out.println("Package: {" + dependency.getArtifact() + "} is installed");
        });

        System.out.println("END");
    }

    @Override
    public boolean isInstalled(Dependency mainPackage) {
        return INSTALLED_DEPENDENCIES.contains(mainPackage);
    }
}
