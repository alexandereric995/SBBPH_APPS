update kew_subsidiari_rpp_migrate set no_kp = TRIM(replace(no_kp, '-', ''));

update kew_subsidiari_rpp_migrate set tarikh_terima_permohonan = CONCAT(0, SUBSTRING(tarikh_terima_permohonan, 1, LOCATE('.', tarikh_terima_permohonan) -1), SUBSTRING(tarikh_terima_permohonan, 2)) where LENGTH(SUBSTRING(tarikh_terima_permohonan, 1, LOCATE('.', tarikh_terima_permohonan) -1)) < 2;
update kew_subsidiari_rpp_migrate set tarikh_terima_permohonan = CONCAT(SUBSTRING(tarikh_terima_permohonan, 1, 3), 0, replace(SUBSTRING(tarikh_terima_permohonan, 4, LOCATE('.', tarikh_terima_permohonan) -1),'.',''), '.', replace(tarikh_terima_permohonan, SUBSTRING(tarikh_terima_permohonan, 1, 5),'')) where LENGTH(replace(SUBSTRING(tarikh_terima_permohonan, 4, LOCATE('.', tarikh_terima_permohonan) -1),'.','')) < 2;
update kew_subsidiari_rpp_migrate set tarikh_terima_permohonan = concat(SUBSTRING(tarikh_terima_permohonan, 1, 6), '20', replace(tarikh_terima_permohonan, SUBSTRING(tarikh_terima_permohonan, 1, 6),'')) where LENGTH(replace(tarikh_terima_permohonan, SUBSTRING(tarikh_terima_permohonan, 1, 6),'')) < 4;
update kew_subsidiari_rpp_migrate set tarikh_terima_permohonan = CONCAT(SUBSTRING(tarikh_terima_permohonan, 9, 2),'.', SUBSTRING(tarikh_terima_permohonan, 6, 2),'.', SUBSTRING(tarikh_terima_permohonan, 1, 4)) where tarikh_terima_permohonan like '%-%';
update kew_subsidiari_rpp_migrate set tarikh_terima_permohonan = TRIM(replace(tarikh_terima_permohonan, '.', '-'));

update kew_subsidiari_rpp_migrate set tarikh_eft = CONCAT(0, SUBSTRING(tarikh_eft, 1, LOCATE('.', tarikh_eft) -1), SUBSTRING(tarikh_eft, 2)) where LENGTH(SUBSTRING(tarikh_eft, 1, LOCATE('.', tarikh_eft) -1)) < 2;
update kew_subsidiari_rpp_migrate set tarikh_eft = CONCAT(SUBSTRING(tarikh_eft, 1, 3), 0, replace(SUBSTRING(tarikh_eft, 4, LOCATE('.', tarikh_eft) -1),'.',''), '.', replace(tarikh_eft, SUBSTRING(tarikh_eft, 1, 5),'')) where LENGTH(replace(SUBSTRING(tarikh_eft, 4, LOCATE('.', tarikh_eft) -1),'.','')) < 2;
update kew_subsidiari_rpp_migrate set tarikh_eft = concat(SUBSTRING(tarikh_eft, 1, 6), '20', replace(tarikh_eft, SUBSTRING(tarikh_eft, 1, 6),'')) where LENGTH(replace(tarikh_eft, SUBSTRING(tarikh_eft, 1, 6),'')) < 4;
update kew_subsidiari_rpp_migrate set tarikh_eft = CONCAT(SUBSTRING(tarikh_eft, 9, 2),'.', SUBSTRING(tarikh_eft, 6, 2),'.', SUBSTRING(tarikh_eft, 1, 4)) where tarikh_eft like '%-%';
update kew_subsidiari_rpp_migrate set tarikh_eft = TRIM(replace(tarikh_eft, '.', '-'));

update kew_subsidiari_rpp_migrate set tarikh_baucer = CONCAT(0, SUBSTRING(tarikh_baucer, 1, LOCATE('.', tarikh_baucer) -1), SUBSTRING(tarikh_baucer, 2)) where LENGTH(SUBSTRING(tarikh_baucer, 1, LOCATE('.', tarikh_baucer) -1)) < 2;
update kew_subsidiari_rpp_migrate set tarikh_baucer = CONCAT(SUBSTRING(tarikh_baucer, 1, 3), 0, replace(SUBSTRING(tarikh_baucer, 4, LOCATE('.', tarikh_baucer) -1),'.',''), '.', replace(tarikh_baucer, SUBSTRING(tarikh_baucer, 1, 5),'')) where LENGTH(replace(SUBSTRING(tarikh_baucer, 4, LOCATE('.', tarikh_baucer) -1),'.','')) < 2;
update kew_subsidiari_rpp_migrate set tarikh_baucer = concat(SUBSTRING(tarikh_baucer, 1, 6), '20', replace(tarikh_baucer, SUBSTRING(tarikh_baucer, 1, 6),'')) where LENGTH(replace(tarikh_baucer, SUBSTRING(tarikh_baucer, 1, 6),'')) < 4;
update kew_subsidiari_rpp_migrate set tarikh_baucer = CONCAT(SUBSTRING(tarikh_baucer, 9, 2),'.', SUBSTRING(tarikh_baucer, 6, 2),'.', SUBSTRING(tarikh_baucer, 1, 4)) where tarikh_baucer like '%-%';
update kew_subsidiari_rpp_migrate set tarikh_baucer = TRIM(replace(tarikh_baucer, '.', '-'));

UPDATE kew_subsidiari_rpp_migrate SET no_resit = concat('0', no_resit) WHERE LENGTH(no_resit) = 14;