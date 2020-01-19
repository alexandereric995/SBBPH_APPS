update rk_akaun_migrate set tarikh_bayaran = replace(tarikh_bayaran, '00:00:00', '');
update rk_akaun_migrate set no_fail = TRIM(UPPER(no_fail)), no_resit = TRIM(UPPER(no_resit)), tarikh_bayaran = TRIM(UPPER(tarikh_bayaran));

update rk_akaun_migrate set tarikh_bayaran = CONCAT(0, SUBSTRING(tarikh_bayaran, 1, LOCATE('.', tarikh_bayaran) -1), SUBSTRING(tarikh_bayaran, 2)) where LENGTH(SUBSTRING(tarikh_bayaran, 1, LOCATE('.', tarikh_bayaran) -1)) < 2;
update rk_akaun_migrate set tarikh_bayaran = CONCAT(SUBSTRING(tarikh_bayaran, 1, 3), 0, replace(SUBSTRING(tarikh_bayaran, 4, LOCATE('.', tarikh_bayaran) -1),'.',''), '.', replace(tarikh_bayaran, SUBSTRING(tarikh_bayaran, 1, 5),'')) where LENGTH(replace(SUBSTRING(tarikh_bayaran, 4, LOCATE('.', tarikh_bayaran) -1),'.','')) < 2;
update rk_akaun_migrate set tarikh_bayaran = concat(SUBSTRING(tarikh_bayaran, 1, 6), '20', replace(tarikh_bayaran, SUBSTRING(tarikh_bayaran, 1, 6),'')) where LENGTH(replace(tarikh_bayaran, SUBSTRING(tarikh_bayaran, 1, 6),'')) < 4;

update rk_akaun_migrate set tarikh_bayaran = CONCAT(SUBSTRING(tarikh_bayaran, 9, 2),'.', SUBSTRING(tarikh_bayaran, 6, 2),'.', SUBSTRING(tarikh_bayaran, 1, 4)) where tarikh_bayaran like '%-%';

SELECT no_fail, no_resit, COUNT(no_resit) 
FROM rk_akaun_migrate
GROUP BY no_resit, no_fail, id_kod_hasil
HAVING ( COUNT(no_resit) > 1 );

select * from rk_akaun_migrate where no_fail not in (select no_fail from rk_fail);
select * from rk_akaun_migrate where no_resit not in (select no_resit from kew_bayaran_resit);

SELECT DISTINCT(NO_FAIL) FROM rk_akaun_migrate;
update rk_fail set no_fail = 'BPH.UT.200-5/5/22' where no_fail = '200-5/5/22';

delete from rk_akaun where id_jenis_transaksi != 1 and id_fail in
(select id from rk_fail where no_fail = 'BPH.UT.200-5/13/2');
delete from rk_akaun_migrate where no_fail = 'BPH.UT.200-5/13/2';

select * from rk_akaun where id_fail in (select id from rk_fail where no_fail = 'BPH.BGS.200-3/1/193')
and id_resit = (select id from kew_bayaran_resit where no_resit = '04082017HQZ00001');