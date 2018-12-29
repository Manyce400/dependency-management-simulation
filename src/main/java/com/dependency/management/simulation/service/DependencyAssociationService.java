package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * @author manyce400
 */
@Service(DependencyAssociationService.SPRING_BEAN)
public class DependencyAssociationService implements IDependencyAssociationService {


    public static final String SPRING_BEAN = "com.dependency.management.simulation.service.DependencyAssociationService";


    @Override
    public boolean isReferencedDependency(Dependency dependency) {
        Assert.notNull(dependency, "dependency cannot be null");
        return dependency.getParentDependencies().size() > 0;
    }

    @Override
    public void buildDependencyHierarchy(Dependency mainPackage, Set<Dependency> dependencies) {
        Assert.notNull(mainPackage, "mainPackage cannot be null");
        Assert.notNull(dependencies, "dependencies cannot be null");
        Assert.isTrue(dependencies.size() > 0, "dependencies to create associations for cannot be empty");
        mainPackage.addDependencies(dependencies);
    }
}
