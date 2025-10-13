//package com.arturfrimu.lifemanager.archunit;
//
//import com.arturfrimu.lifemanager.common.storage.MinioStorageService;
//import com.tngtech.archunit.core.domain.JavaClasses;
//import com.tngtech.archunit.core.importer.ClassFileImporter;
//import com.tngtech.archunit.lang.ArchRule;
//import io.minio.MinioClient;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import static com.arturfrimu.lifemanager.comon.Constants.BASE_PACKAGE;
//import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
//
//@DisplayName("MinioClient Usage Architecture Tests")
//class MinioClientUsageArchitectureTest {
//
//    private static JavaClasses importedClasses;
//
//    @BeforeAll
//    static void setUp() {
//        importedClasses = new ClassFileImporter().importPackages(BASE_PACKAGE);
//    }
//
//    @Test
//    @DisplayName("MinioClient poate fi injectat DOAR în clasele care implementează MinioStorageService")
//    void onlyMinioStorageServiceImplementations_canHaveMinioClient_asField() {
//        ArchRule rule = fields()
//                .that().haveRawType(MinioClient.class)
//                .should().beDeclaredInClassesThat().implement(MinioStorageService.class)
//                .because("Doar clasele care implementează MinioStorageService pot injecta MinioClient. " +
//                         "Alte clase trebuie să folosească interfața MinioStorageService pentru operații cu MinIO.");
//
//        rule.check(importedClasses);
//    }
//
//    @Test
//    @DisplayName("Clasele care au MinioClient ca field TREBUIE să implementeze MinioStorageService")
//    void classesThatHaveMinioClientField_mustImplementMinioStorageService() {
//        ArchRule rule = fields()
//                .that().haveRawType(MinioClient.class)
//                .should().beDeclaredInClassesThat().implement(MinioStorageService.class)
//                .andShould().beDeclaredInClassesThat().resideInAPackage("..storage..")
//                .because("Doar implementările MinioStorageService pot deține o referință către MinioClient");
//
//        rule.check(importedClasses);
//    }
//
//    @Test
//    @DisplayName("MinioClient fields trebuie să fie private și final")
//    void minioClientFields_shouldBePrivateAndFinal() {
//        ArchRule rule = fields()
//                .that().haveRawType(MinioClient.class)
//                .should().bePrivate()
//                .andShould().beFinal()
//                .because("MinioClient trebuie injectat prin constructor și să fie private final");
//
//        rule.check(importedClasses);
//    }
//}
//
