package com.arturfrimu.lifemanager.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.arturfrimu.lifemanager.comon.Constants.BASE_PACKAGE;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@DisplayName("Common Package Architecture Tests")
class CommonPackageArchitectureTest {

    private static JavaClasses importedClasses;

    @BeforeAll
    static void setUp() {
        importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);
    }

    @Test
    @DisplayName("Pachetul common nu poate importa nimic din pachetul sport")
    void commonPackage_shouldNotDependOn_sportPackage() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..common..")
                .should().dependOnClassesThat().resideInAPackage("..sport..")
                .because("Pachetul common și sub-pachetele lui nu pot avea importuri din pachetul sport și sub-pachetele lui");

        rule.check(importedClasses);
    }
}