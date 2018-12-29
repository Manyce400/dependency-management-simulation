package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Set;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class DependencyAssociationServiceTest {

    @Spy
    private DependencyAssociationService dependencyAssociationService;


    @Test
    public void testBuildDependencyHierarchy() {
        // Create mainPackage, it should have no dependencies on creation
        Dependency mainPackage = Dependency.newInstance("TCPIP");
        Assert.assertFalse(mainPackage.hasDependencies());

        // create dependencies for mainPackage
        Dependency socket = Dependency.newInstance("SOCKET");
        Dependency netCard = Dependency.newInstance("NETCARD");

        // Use service bean to define and establish the dependency relationships.  Verify results
        dependencyAssociationService.buildDependencyHierarchy(mainPackage, ImmutableSet.of(socket, netCard));
        Set<Dependency> dependencies = mainPackage.getDependencies();
        Assert.assertTrue(mainPackage.hasDependencies());
        Assert.assertTrue(dependencies.size() == 2);
        Assert.assertTrue(dependencies.contains(socket));
        Assert.assertTrue(dependencies.contains(netCard));

        // Validate SOCKET and NETCARD point to mainPackage as their parent dependency
        Set<Dependency> socketParentDependencies = socket.getParentDependencies();
        Set<Dependency> netCardParentDependencies = netCard.getParentDependencies();
        Assert.assertTrue(socketParentDependencies.size() == 1);
        Assert.assertTrue(socketParentDependencies.size() == 1);
        Assert.assertTrue(socketParentDependencies.iterator().next().equals(mainPackage));
        Assert.assertTrue(socketParentDependencies.iterator().next().equals(netCardParentDependencies.iterator().next()));

    }

    @Test
    public void testIsReferencedDependency() {
        Dependency mainPackage = Dependency.newInstance("TCPIP");
        Dependency socket = Dependency.newInstance("SOCKET");
        Dependency netCard = Dependency.newInstance("NETCARD");
        dependencyAssociationService.buildDependencyHierarchy(mainPackage, ImmutableSet.of(socket, netCard));

        boolean isReferencedSocket = dependencyAssociationService.isReferencedDependency(socket);
        boolean isReferencedNetCard = dependencyAssociationService.isReferencedDependency(netCard);
        Assert.assertTrue(isReferencedSocket);
        Assert.assertTrue(isReferencedNetCard);
    }
}
