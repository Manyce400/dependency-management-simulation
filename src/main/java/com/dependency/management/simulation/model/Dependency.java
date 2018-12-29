package com.dependency.management.simulation.model;

import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * Domain object that represents a dependency.
 *
 * @author manyce400
 */
public class Dependency {


    // Core artifact/name of this dependency
    private String artifact;

    // Dependencies that this parent Dependency object needs.
    public Set<Dependency> dependencies = new HashSet<>();

    // Contains other packages/components which need this dependency
    private Set<Dependency> parentDependencies = new HashSet<>();

    public Dependency(String artifact) {
        this.artifact = artifact;
    }

    public String getArtifact() {
        return artifact;
    }

    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    public void addDependency(Dependency dependency) {
        Assert.notNull(dependency, "dependency cannot be null");
        // Add to the collection of dependencies of this object
        dependencies.add(dependency);
        // Finally record a link to this object on dependency passed as argument
        dependency.parentDependencies.add(this);
    }

    public void addDependencies(Set<Dependency> dependencies) {
        Assert.notNull(dependencies, "dependencies cannot be null");
        for(Dependency dependency : dependencies) {
            addDependency(dependency);
        }
    }

    public Set<Dependency> getDependencies() {
        // Returning defensive copy of dependencies to prevent modification externally
        return ImmutableSet.copyOf(dependencies);
    }

    public Set<Dependency> getParentDependencies() {
        // Returning defensive copy of dependencies to prevent modification externally
        return ImmutableSet.copyOf(parentDependencies);
    }

    public void removeParentDependency(Dependency parentDependency) {
        Assert.notNull(parentDependency, "parentDependency");
        parentDependencies.remove(parentDependency);
    }

    public boolean hasDependencies() {
        return dependencies.size() > 0;
    }

    public boolean hasParentDependencies() {
        return parentDependencies.size() > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Dependency that = (Dependency) o;

        return new EqualsBuilder()
                .append(artifact, that.artifact)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(artifact)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("artifact", artifact)
                .append("dependencies", dependencies)
                .append("parentDependencies", parentDependencies)
                .toString();
    }

    public static Dependency newInstance(String artifact) {
        Assert.notNull(artifact, "artifact cannot be null");
        return new Dependency(artifact);
    }

}
