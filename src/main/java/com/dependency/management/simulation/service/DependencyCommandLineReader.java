package com.dependency.management.simulation.service;

import com.dependency.management.simulation.model.Dependency;
import com.dependency.management.simulation.model.DependencyCommandE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * @author manyce400
 */
@Service(DependencyCommandLineReader.SPRING_BEAN)
public class DependencyCommandLineReader {


    @Autowired
    @Qualifier(DependencyAssociationService.SPRING_BEAN)
    private IDependencyAssociationService iDependencyAssociationService;

    @Autowired
    @Qualifier(DependencyManagerService.SPRING_BEAN)
    private IDependencyManagerService iDependencyManagerService;


    public static final String SPRING_BEAN = "com.dependency.management.simulation.service.DependencyCommandLineReader";


    @PostConstruct
    public void execCommandLinePrompt() {
        printExpectedCommands();
        Scanner scanner = new Scanner(System.in);


        while (scanner.hasNext()) {
            String command = scanner.nextLine();

            if(StringUtils.hasLength(command)) {
                String [] commandParts = command.split(" ");
                DependencyCommandE dependencyCommandE = DependencyCommandE.valueOf(commandParts[0]);
                System.out.println("Actual commandParts[0] = " + commandParts[0] + " dependencyCommandE: " + dependencyCommandE);
                processCommand(dependencyCommandE, commandParts);
            } else {
                System.out.println("Please enter a valid command");
            }

            iDependencyManagerService.listAllInstalledPackages();
        }
    }


    public void printExpectedCommands() {
        System.out.println("::::::::   Enter Dependency Management Commands    :::::::::");
        System.out.println("DEPEND <package> <dependency1> [<additional dependencies>]");
        System.out.println("INSTALL <package>");
        System.out.println("REMOVE <package>");
        System.out.println("LIST");
    }

    public void processCommand(DependencyCommandE dependencyCommandE, String [] commandParts) {
        Set<Dependency> dependencies = new HashSet<>();
        switch (dependencyCommandE) {
            case DEPEND:
                Dependency mainPackage = Dependency.newInstance(commandParts[1]);
                for(int i = 2; i< commandParts.length; i++) {
                    Dependency dependency = Dependency.newInstance(commandParts[i]);
                    dependencies.add(dependency);
                }

                iDependencyAssociationService.buildDependencyHierarchy(mainPackage, dependencies);
                iDependencyManagerService.installDependency(mainPackage);
                break;
            default:
                break;
        }
    }

}
