# Règles de matching des prénoms

## Règles générales

### Égalité stricte
La liste de prénoms A est une égalité stricte des prénoms de la liste B si :

- même nombre de prénoms
- tous les prénoms sont strictement égaux
- les prénoms sont dans le même ordre

### Approximation
La liste de prénoms A est une approximation de la liste B si :

- il y a au moins un prénom de A dans B (même approximé)
- il n'y a pas de prénoms différents
- il n'y a qu'un seul prénom approximé

### Différence

La liste de prénom de A est différente de B dans tous les autres cas

## Matrice de tests
Le jeu de données utilisé est le même pour tous les cas de test. Il y a plusieurs cas de test pour chaque règle (égalité, approximation, et différence).

Le jeu de données comporte n identités différentes nommées Identité 1 à Identité n. Le tableau ci-dessous décrit les différents cas de test avec un critère de recherche et la liste des identités qu'ils doivent renvoyer. 

Ce tableau est implémenté sous forme de tests json disponibles dans [le dossier suivant](./definition) et qui seront exécutés par la classe de test [IdentitySearchRuleTest.java](../../../java/fr/paris/lutece/plugins/identitystore/modules/test/identitystore/service/search/IdentitySearchRuleTest.java).

### Identités

| Nom de l'identité | Prénom(s)           |
|-------------------|---------------------|
| Identité 1        | Jean                |
| Identité 2        | Pascal Jean         |
| Identité 3        | Jean Pascal         |
| Identité 4        | Jean Pascale        |
| Identité 5        | Jan                 |
| Identité 6        | Pascal Jan          |
| Identité 7        | Jean-Pascal         |
| Identité 8        | Jeanne              |
| Identité 9        | Pascal              |
| Identité 10       | Jean Pascal Thierry |
| Identité 11       | Jean Pascal Jérémie |
| Identité 12       | Jean Jérémie Pascal |

### Tests

| Code                                                               | Critère de recherche | Identités devant matcher        | Résultat comparaison                                                                                                                               |
|--------------------------------------------------------------------|----------------------|---------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------|
| [egalite-01](./definition/testFirstNameEgalite01.json)             | Jean                 | 1                               | Égalité                                                                                                                                            |
| [egalite-02](./definition/testFirstNameEgalite02.json)             | Jean-Pascal          | 7                               | Égalité                                                                                                                                            |
| [egalite-03](./definition/testFirstNameEgalite03.json)             | Pascal Jean          | 2                               | Égalité                                                                                                                                            |
| [egalite-04](./definition/testFirstNameEgalite04.json)             | Jean Pascal Jérémie  | 11                              | Égalité                                                                                                                                            |
| [egalite-05](./definition/testFirstNameEgalite05.json)             | Jean Jérémie         | aucune                          | pas de match car il manque un prénom                                                                                                               |
| [egalite-06](./definition/testFirstNameEgalite06.json)             | Thierry Jean Pascal  | aucune                          | pas de match car les prénoms sont dans le désordre                                                                                                 |
| [egalite-07](./definition/testFirstNameEgalite07.json)             | Jean Pascal Thierri  | aucune                          | pas de match car un prénom est différent                                                                                                           |
| [approximation-01](./definition/testFirstNameApproximation01.json) | Jean                 | 1, 2, 3, 4, 5, 6, 10, 11, 12    | Approximation                                                                                                                                      |
| [approximation-02](./definition/testFirstNameApproximation02.json) | Pascal Jean          | 1, 2, 3, 4, 5, 6, 9, 10, 11, 12 | Approximation                                                                                                                                      |
| [approximation-03](./definition/testFirstNameApproximation03.json) | Jean Pascal          | 1, 2, 3, 4, 5, 6, 9, 10, 11, 12 | Approximation                                                                                                                                      |
| [approximation-04](./definition/testFirstNameApproximation04.json) | Pascale Jean         | 1, 2, 3, 4, 5, 9, 10, 11, 12    | Approximation                                                                                                                                      |
| [approximation-05](./definition/testFirstNameApproximation05.json) | Jérémy Joan          | 1, 5                            | Approximation (ne remonte pas 11 et 12 car il y a deux approximations, ni 6 et 10 car il y a un prénom différent )                                 |
| [approximation-06](./definition/testFirstNameApproximation06.json) | Jean-Pascal          | 7                               | Approximation qui équivaut à une égalité, mais on s'assure de ne pas remonter Jean Pascal (approximation de type "-" vs " " )                      |
| [difference-01](./definition/testFirstNameDifference01.json)       | Jean                 | 7,8,9                           | Différence                                                                                                                                         |
| [difference-02](./definition/testFirstNameDifference02.json)       | Pierre Pascal        | 1,2,3,4,5,6,7,8,10,11,12        | Différence car il y a un paquet de prénoms qui diffèrent ou plusieurs approximations sauf pour 9 qui contient seulement Pascal                     |
| [difference-03](./definition/testFirstNameDifference03.json)       | Jean-Pascal          | 1,2,3,4,5,6,8,9,10,11,12        | Différence car il y a un paquet de prénoms qui diffèrent ou plusieurs approximations sauf pour 7 qui contient seulement Jean-Pascal                |
| [difference-04](./definition/testFirstNameDifference04.json)       | Pascal Pierre        | 1,2,3,4,5,6,7,8,10,11,12        | Différence car il y a un paquet de prénoms qui diffèrent ou plusieurs approximations sauf pour 9 qui contient seulement Pascal                     |
| [difference-05](./definition/testFirstNameDifference05.json)       | Pierre Pascale       | 1,2,3,4,5,6,7,8,10,11,12        | Différence car il y a un paquet de prénoms qui diffèrent ou plusieurs approximations sauf pour 9 qui contient seulement Pascal (Pascale approximé) |
| [difference-06](./definition/testFirstNameDifference06.json)       | Pascale Jean         | 6,7,8                           | Différent (une seule approximation possible)                                                                                                       |