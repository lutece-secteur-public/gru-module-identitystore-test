
--
-- Data for table core_admin_right
--
DELETE FROM core_admin_right WHERE id_right = 'IDENTITYSTORE_TEST_MANAGEMENT';
INSERT INTO core_admin_right (id_right,name,level_right,admin_url,description,is_updatable,plugin_name,id_feature_group,icon_url,documentation_url, id_order ) VALUES 
('IDENTITYSTORE_TEST_MANAGEMENT','identitystore-test.adminFeature.ManageTester.name',1,'jsp/admin/plugins/identitystore-test/ManageTestDefinitions.jsp','identitystore-test.adminFeature.ManageTester.description',0,'identitystore-test',NULL,NULL,NULL,4);


--
-- Data for table core_user_right
--
DELETE FROM core_user_right WHERE id_right = 'IDENTITYSTORE_TEST_MANAGEMENT';
INSERT INTO core_user_right (id_right,id_user) VALUES ('IDENTITYSTORE_TEST_MANAGEMENT',1);

