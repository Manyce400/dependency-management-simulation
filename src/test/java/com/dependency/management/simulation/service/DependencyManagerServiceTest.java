package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;
import com.google.common.collect.ImmutableSet;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author manyce400
 */
@RunWith(MockitoJUnitRunner.class)
public class DependencyManagerServiceTest {


    @Spy
    private DependencyAssociationService dependencyAssociationService;

    @Spy
    private DependencyManagerService dependencyManagerService;

    //@Test
    public void testInstallDependency() {
        Dependency mainPackage = Dependency.newInstance("TCPIP");
        Dependency socket = Dependency.newInstance("SOCKET");
        Dependency netCard = Dependency.newInstance("NETCARD");
        dependencyAssociationService.buildDependencyHierarchy(mainPackage, ImmutableSet.of(socket, netCard));

        // Install dependency and verify that dependencies also get installed
        dependencyManagerService.installDependency(mainPackage);
        Assert.assertTrue(dependencyManagerService.isInstalled(socket));
        Assert.assertTrue(dependencyManagerService.isInstalled(netCard));
        Assert.assertTrue(dependencyManagerService.isInstalled(mainPackage));
    }

    //@Test
    public void testRemoveDependencyNotInstalled() {
        Dependency mainPackage = Dependency.newInstance("TCPIP");

        // MainPackage is not installed so we dont expect remove operation to be successful
        Assert.assertFalse(dependencyManagerService.isInstalled(mainPackage));
        dependencyManagerService.removeDependency(mainPackage);
    }

    //@Test
    public void testRemoveDependencyWithParentDependency() {
        Dependency mainPackage = Dependency.newInstance("TCPIP");
        Dependency socket = Dependency.newInstance("SOCKET");
        Dependency netCard = Dependency.newInstance("NETCARD");

        dependencyAssociationService.buildDependencyHierarchy(mainPackage, ImmutableSet.of(socket, netCard));
        dependencyManagerService.installDependency(mainPackage);

        // Socket and NetCard cannot be removed since mainPackage currently points to both
        dependencyManagerService.removeDependency(socket);
        dependencyManagerService.removeDependency(netCard);
        Assert.assertTrue(dependencyManagerService.isInstalled(socket));
        Assert.assertTrue(dependencyManagerService.isInstalled(netCard));
    }


    @Test
    public void testRemoveDependencyWithNoParentDependency() {
        Dependency mainPackage = Dependency.newInstance("TCPIP");
        Dependency socket = Dependency.newInstance("SOCKET");
        Dependency netCard = Dependency.newInstance("NETCARD");

        dependencyAssociationService.buildDependencyHierarchy(mainPackage, ImmutableSet.of(socket, netCard));
        dependencyManagerService.installDependency(mainPackage);

        // Socket and NetCard cannot be removed since mainPackage currently points to both
        dependencyManagerService.removeDependency(mainPackage);
        Assert.assertFalse(dependencyManagerService.isInstalled(socket));
        Assert.assertFalse(dependencyManagerService.isInstalled(netCard));
        Assert.assertFalse(dependencyManagerService.isInstalled(mainPackage));
    }


    //@Test
    public void testListAllInstalledPackages() {
        Dependency mainPackage = Dependency.newInstance("TCPIP");
        Dependency socket = Dependency.newInstance("SOCKET");
        Dependency netCard = Dependency.newInstance("NETCARD");

        dependencyAssociationService.buildDependencyHierarchy(mainPackage, ImmutableSet.of(socket, netCard));
        dependencyManagerService.installDependency(mainPackage);

        // List all dependencies
        try {
            dependencyManagerService.listAllInstalledPackages();
        } catch (Exception e) {
            Assert.fail("No failures expected");
        }
    }


}
