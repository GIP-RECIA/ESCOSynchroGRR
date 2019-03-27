delete from grr_utilisateurs;
delete from grr_etablissement;
delete from grr_log;
delete from grr_entry;
delete from grr_site;

commit ;
INSERT INTO grr_utilisateurs (login, nom, prenom, password, email, statut, etat, default_site, default_area, default_etablissement, default_room, default_style, default_list_type, default_language, source) VALUES('F08001j5', 'Juliana', 'FLORENCE', '', 'FLORENCE.Juliana@domaine.fr', 'utilisateur', 'actif', 0, 0, 2, 0, '', '', '', 'ext');
INSERT INTO grr_etablissement (`id`,`code`, `shortname`, `name`, `codepostal`, `adresse`, `ville`) VALUES ('1','0450822X', '0450822X', 'LGT-JACQUES COEUR-ac-ORL._TOURS', '18026', 'Test adresse', 'BOURGES CEDEX');
INSERT INTO grr_etablissement (`id`,`code`, `shortname`, `name`, `codepostal`, `adresse`, `ville`) VALUES ('2','0180007A', 'LGT-JACQUES COEUR-ac-ORL._TOUR', 'LGT-JACQUES COEUR-ac-ORL._TOURS', '18026', 'Test adresse', 'BOURGES CEDEX');
INSERT INTO grr_utilisateurs (`login`, `nom`, `prenom`, `password`, `email`, `statut`, `etat`, `default_site`, `default_area`, `default_etablissement`, `default_room`, `default_style`, `default_list_type`, `default_language`, `source`) VALUES ('F080d4pe', 'Administrateur4t', 'grrtest', '', 'non_rdenseigne@netocentre.fr', 'administrateur', 'actif', -1, -1, -1, -1, '', 'select', 'fr', 'local');
INSERT INTO grr_utilisateurs (`login`, `nom`, `prenom`, `password`, `email`, `statut`, `etat`, `default_site`, `default_area`, `default_etablissement`, `default_room`, `default_style`, `default_list_type`, `default_language`, `source`) VALUES ('F080D5PE', 'Administrateurtest', 'grrtest2', '', 'non_rdenseigne@netocentre.fr', 'utilisateur', 'actif', -1, -1, -1, -1, '', 'select', 'fr', 'ext');
INSERT INTO grr_log (`LOGIN`, `START`, `SESSION_ID`, `REMOTE_ADDR`, `USER_AGENT`, `REFERER`, `AUTOCLOSE`, `END`) VALUES ('F080D5PE',  '2010-11-09 16:54:31', '', '192.168.1.92', 'Windows 7 Firefox 63.0', 'https://lycees.netocentre.fr/', null, '2010-11-09 16:54:31');
INSERT INTO grr_entry (`id`,`start_time`, `end_time`, `entry_type`, `repeat_id`, `room_id`, `timestamp`, `create_by`, `beneficiaire_ext`, `beneficiaire`, `name`, `type`, `description`, `statut_entry`, `option_reservation`, `overload_desc`, `moderate`, `jours`, `clef`, `courrier`) VALUES ('1',1249897200, 1049899000, 0, 0, 1, '2010-02-11 15:45:21', 'TEST', '', 'TEST', 'test1', 'A', '', '-', -1, '', 0, 0, 0, 0);
INSERT INTO grr_entry (`id`,`start_time`, `end_time`, `entry_type`, `repeat_id`, `room_id`, `timestamp`, `create_by`, `beneficiaire_ext`, `beneficiaire`, `name`, `type`, `description`, `statut_entry`, `option_reservation`, `overload_desc`, `moderate`, `jours`, `clef`, `courrier`) VALUES ('2',1839824092, 1039824992, 0, 0, 1, '2010-02-11 15:45:21', 'TEST', '', 'TEST', 'test1', 'A', '', '-', -1, '', 0, 0, 0, 0);
INSERT INTO grr_site (`id`,`sitecode`, `sitename`, `adresse_ligne1`, `adresse_ligne2`, `adresse_ligne3`, `cp`, `ville`, `pays`, `tel`, `fax`) VALUES ('1','0370101A', 'Lycée Léonard de Vinci (Amboise) ', 'multi', '', '', '', '', '', '', '');
INSERT INTO grr_site (`id`,`sitecode`, `sitename`, `adresse_ligne1`, `adresse_ligne2`, `adresse_ligne3`, `cp`, `ville`, `pays`, `tel`, `fax`) VALUES ('2','0370102A', 'Lycée Léonard de Vinci (Amboise) ', 'multi', '', '', '', '', '', '', '');
commit ;