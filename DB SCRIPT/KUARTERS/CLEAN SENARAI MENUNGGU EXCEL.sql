## RUN THIS FIRST BEFORE IMPORT DATA FROM EXCEL
truncate table kua_menunggu_backup;
insert into kua_menunggu_backup select * from kua_menunggu;
truncate table kua_menunggu;


## RUN THIS AFTER IMPORT DATA FROM EXCEL TO KUA_MENUNGGU
update kua_menunggu set NOKP = trim(UPPER(REPLACE(NOKP,'-','')));
update kua_menunggu set NOKP = trim(UPPER(REPLACE(NOKP,'\'','')));
update kua_menunggu set ID_LOKASI_PERMOHONAN = '01' WHERE ID_LOKASI_PERMOHONAN = 'PUTRAJAYA';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '02' WHERE ID_LOKASI_PERMOHONAN = 'KUALA LUMPUR / PETALING JAYA';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '03' WHERE ID_LOKASI_PERMOHONAN = 'LABUAN';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '04' WHERE ID_LOKASI_PERMOHONAN = 'JOHOR';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '05' WHERE ID_LOKASI_PERMOHONAN = 'PETALING JAYA';
update kua_menunggu set ID_LOKASI_PERMOHONAN = '06' WHERE ID_LOKASI_PERMOHONAN = 'KLIA';
update kua_menunggu set FLAG_MANUAL = 'Y';
update kua_menunggu set TARIKH_KEMASKINI = '2020-08-31';

## RUN THIS QUERY TO GET MISSING Q NUMBER
INSERT INTO kua_menunggu
(NOKP, NOGILIRAN, KELAS_KUARTERS, ID_LOKASI_PERMOHONAN, NODAFTAR, JENIS_KELAS_KUARTERS, FLAG_MANUAL, ID_MASUK, TARIKH_MASUK, ID_KEMASKINI, TARIKH_KEMASKINI) 
SELECT NOKP, NOGILIRAN, KELAS_KUARTERS, ID_LOKASI_PERMOHONAN, NODAFTAR, JENIS_KELAS_KUARTERS, FLAG_MANUAL, ID_MASUK, TARIKH_MASUK, ID_KEMASKINI, TARIKH_KEMASKINI 
FROM kua_menunggu_backup
WHERE ID IN (SELECT ID
FROM kua_menunggu_backup
WHERE     FLAG_MANUAL = 'T' AND NOKP NOT IN
             (SELECT NOKP
              FROM kua_menunggu
              WHERE     kua_menunggu_backup.NOKP = kua_menunggu.NOKP
                    AND kua_menunggu_backup.KELAS_KUARTERS =
                        kua_menunggu.KELAS_KUARTERS));