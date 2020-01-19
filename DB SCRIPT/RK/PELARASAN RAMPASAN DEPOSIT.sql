select * from rk_fail where no_fail = 'BPH.UT.200-5/5/225';

## RAMPASAN DEPOSIT
insert into `rk_akaun`(`id_fail`,`tarikh_invois`,`tarikh_transaksi`,`id_kod_hasil`,`id_jenis_transaksi`,`debit`,`kredit`,`keterangan`,`id_resit`,`no_rujukan_pelarasan`,`flag_aktif`,`id_invois`,`id_masuk`,`tarikh_masuk`,`id_kemaskini`,`tarikh_kemaskini`) values 
('878490074237',null,'2019-04-03 00:00:00','79503','4',0,2700,'PELARASAN MEMINDAHKAN DEPOSIT CAGARAN (L1131103) KE HASIL SEWA (H274202)',null,'11000259','Y',null,null,now(),null,null);
insert into `rk_akaun`(`id_fail`,`tarikh_invois`,`tarikh_transaksi`,`id_kod_hasil`,`id_jenis_transaksi`,`debit`,`kredit`,`keterangan`,`id_resit`,`no_rujukan_pelarasan`,`flag_aktif`,`id_invois`,`id_masuk`,`tarikh_masuk`,`id_kemaskini`,`tarikh_kemaskini`) values 
('878490074237',null,'2019-04-03 00:00:00','74202','4',0,2700,'PELARASAN MEMINDAHKAN DEPOSIT CAGARAN (L1131103) KE HASIL SEWA (H274202)',null,'11000259','Y',null,null,now(),null,null);

## PEMULANGAN DEPOSIT
insert into `rk_akaun`(`id_fail`,`tarikh_invois`,`tarikh_transaksi`,`id_kod_hasil`,`id_jenis_transaksi`,`debit`,`kredit`,`keterangan`,`id_resit`,`no_rujukan_pelarasan`,`flag_aktif`,`id_invois`,`id_masuk`,`tarikh_masuk`,`id_kemaskini`,`tarikh_kemaskini`) values 
('1194591628044327',null,'2019-01-18 00:00:00','79503','4',0,8000,'BAYARAN BALIK DEPOSIT CAGARAN',null,'12000195','Y',null,null,now(),null,null);
