# gru-module-identitystore-test
Identitystore test tools

## Lancement des tests

### Lancement de tous les tests
```shell
mvn test
```

### Lancement d'une classe de test
```shell
mvn test -Dtest=SearchDuplicatesServiceTest
```

### Lancement d'une classe de test avec filtrage des inputs 
```shell
mvn test -Dtest=SearchDuplicatesServiceTest -Dinputs=RG_GEN_StrictDoublon_01.json,RG_GEN_SuspectDoublon_03.json
```

