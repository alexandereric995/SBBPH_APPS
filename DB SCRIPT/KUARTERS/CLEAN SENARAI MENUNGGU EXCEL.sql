update kua_menunggu set NOKP = trim(UPPER(REPLACE(NOKP,'-','')));
update kua_menunggu set NOKP = trim(UPPER(REPLACE(NOKP,'\'','')));
update kua_menunggu set ID_LOKASI_PERMOHONAN = '01' WHERE ID_LOKASI_PERMOHONAN = 'PUTRAJAYA';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '02' WHERE ID_LOKASI_PERMOHONAN = 'KUALA LUMPUR / PETALING JAYA';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '03' WHERE ID_LOKASI_PERMOHONAN = 'LABUAN';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '04' WHERE ID_LOKASI_PERMOHONAN = 'JOHOR';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '05' WHERE ID_LOKASI_PERMOHONAN = 'PETALING JAYA';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '06' WHERE ID_LOKASI_PERMOHONAN = 'KLIA';
update kua_menunggu set FLAG_MANUAL = 'Y';
update kua_menunggu set TARIKH_KEMASKINI = '2018-10-31';




truncate table kua_menunggu_backup;
insert into kua_menunggu_backup select * from kua_menunggu;
truncate table kua_menunggu;