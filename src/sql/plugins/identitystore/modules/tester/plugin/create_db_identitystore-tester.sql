
--
-- Structure for table identitystore_test_test_definition
--

DROP TABLE IF EXISTS identitystore_test_test_definition;
CREATE TABLE identitystore_test_test_definition (
id_test_definition int AUTO_INCREMENT,
name long varchar NOT NULL,
PRIMARY KEY (id_test_definition)
);

--
-- Structure for table identitystore_test_test_identity
--

DROP TABLE IF EXISTS identitystore_test_test_identity;
CREATE TABLE identitystore_test_test_identity (
id_test_identity int AUTO_INCREMENT,
name long varchar,
customer_id long varchar,
connection_id long varchar,
PRIMARY KEY (id_test_identity)
);

--
-- Structure for table identitystore_test_test_identity_attribute
--

DROP TABLE IF EXISTS identitystore_test_test_identity_attribute;
CREATE TABLE identitystore_test_test_identity_attribute (
id_test_identity_attribute int AUTO_INCREMENT,
key long varchar NOT NULL,
value long varchar NOT NULL,
certifier long varchar,
certification_date date,
certification_level int default '0',
PRIMARY KEY (id_test_identity_attribute)
);
