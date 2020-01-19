SELECT rk_fail.id,
       rk_fail.no_fail,
       rk_fail.nilai_tunggakan,
       rk_perjanjian.id_status_perjanjian,
       rk_perjanjian.tarikh_mula,
       rk_perjanjian.tarikh_tamat
FROM rk_fail, rk_perjanjian
WHERE     rk_fail.id = rk_perjanjian.id_fail
      AND rk_perjanjian.flag_perjanjian_semasa = 'Y'
      ##and rk_perjanjian.id_status_perjanjian = 1
      AND rk_fail.id NOT IN (SELECT rk_invois.id_fail
                             FROM rk_invois
                             WHERE rk_invois.no_invois LIKE '%102018');