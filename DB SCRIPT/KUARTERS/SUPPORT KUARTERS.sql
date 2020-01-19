SELECT * FROM ruj_status WHERE id_seksyen = '02' order by keterangan asc;

SELECT * FROM kua_permohonan WHERE id_pemohon = 801221085547 ;
SELECT * FROM kua_permohonan WHERE no_permohonan = 2013096701;

SELECT * FROM kua_kuarters WHERE no_rujukan = 'BPH/QTR/P8/PTJ/27338';
SELECT * FROM kua_penghuni WHERE id_pemohon = 641118107138;
UPDATE kua_penghuni SET tarikh_keluar_kuarters = null WHERE id = 1432041436;

INSERT INTO `kua_penghuni`(`id_permohonan`,`id_pemohon`,`id_kuarters`,
                           `tarikh_masuk_kuarters`,`no_fail_lama`,
                           `no_rujukan_kuarters_syspintar`)
VALUES ('63894', '801221085547', '1432043269',
        '2017-12-17 00:00:00', NULL,
        'BPH/QTR/P8/PTJ/24623');
        

#SENGGARA
INSERT INTO `mtn_kuarters`(`id_kuarters`,
                           `jenis_laporan`,
                           `keterangan_laporan`,
                           `status`)
VALUES ('1432018219',
        'D',
        'KUARTERS KOSONG',
        '1426130691699');
        

SELECT * FROM kua_menunggu WHERE NOKP = 890914085336;
SELECT * FROM kua_agihan WHERE id_pemohon = 841015016168;
SELECT * FROM kua_agihan WHERE id_permohonan = 87634;


