CREATE TABLE `int_janm` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `file_name` varchar(100) DEFAULT NULL,
  `file_dir` varchar(100) DEFAULT NULL,
  `type` varchar(2) DEFAULT NULL,
  `date` varchar(6) DEFAULT NULL,
  `ag_branch_code` varchar(4) DEFAULT NULL,
  `total_record` int(5) DEFAULT NULL,
  `total_amount` decimal(11,2) DEFAULT NULL,
  `agency_name` varchar(20) DEFAULT NULL,
  `id_masuk` varchar(20) DEFAULT NULL,
  `tarikh_masuk` datetime DEFAULT NULL,
  `id_kemaskini` varchar(20) DEFAULT NULL,
  `tarikh_kemaskini` datetime DEFAULT NULL,
  `flag_migrate` varchar(1) DEFAULT 'T',
  PRIMARY KEY (`id`),
  KEY `int_janm_idx1` (`ag_branch_code`),
  KEY `int_janm_idx2` (`tarikh_masuk`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `int_janm_arkib` (
  `id` varchar(50) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `file_name` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `file_dir` varchar(100) CHARACTER SET latin1 DEFAULT NULL,
  `type` varchar(2) CHARACTER SET latin1 DEFAULT NULL,
  `date` varchar(6) CHARACTER SET latin1 DEFAULT NULL,
  `ag_branch_code` varchar(4) CHARACTER SET latin1 DEFAULT NULL,
  `ag_branch` varchar(1000) DEFAULT NULL,
  `total_record` int(5) DEFAULT NULL,
  `total_amount` decimal(11,2) DEFAULT NULL,
  `agency_name` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `id_masuk` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `tarikh_masuk` datetime DEFAULT NULL,
  `id_kemaskini` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `tarikh_kemaskini` datetime DEFAULT NULL,
  `flag_migrate` varchar(1) CHARACTER SET latin1 DEFAULT 'T',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `int_janm_rekod` (
  `id` varchar(50) NOT NULL DEFAULT '',
  `id_int_janm` varchar(20) DEFAULT NULL,
  `type` varchar(2) DEFAULT NULL,
  `department_code` varchar(8) DEFAULT NULL,
  `department` varchar(8) DEFAULT NULL,
  `pay_center` varchar(4) DEFAULT NULL,
  `region` varchar(2) DEFAULT NULL,
  `personnel_no` varchar(8) DEFAULT NULL,
  `ic` varchar(30) DEFAULT NULL,
  `account_no` varchar(20) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `deduction_amount` decimal(9,2) DEFAULT NULL,
  `deduction_code` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `int_janm_rekod_idx1` (`id_int_janm`),
  KEY `int_janm_rekod_idx2` (`ic`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `int_janm_rekod_arkib` (
  `id` varchar(50) CHARACTER SET latin1 NOT NULL DEFAULT '',
  `id_int_janm` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `type` varchar(2) CHARACTER SET latin1 DEFAULT NULL,
  `department_code` varchar(8) DEFAULT NULL,
  `department` varchar(500) DEFAULT NULL,
  `pay_center_code` varchar(4) DEFAULT NULL,
  `pay_center` varchar(500) DEFAULT NULL,
  `region_code` varchar(2) DEFAULT NULL,
  `region` varchar(500) DEFAULT NULL,
  `personnel_no` varchar(8) CHARACTER SET latin1 DEFAULT NULL,
  `ic` varchar(30) CHARACTER SET latin1 DEFAULT NULL,
  `account_no` varchar(20) CHARACTER SET latin1 DEFAULT NULL,
  `name` varchar(40) CHARACTER SET latin1 DEFAULT NULL,
  `deduction_amount` decimal(9,2) DEFAULT NULL,
  `deduction_code` varchar(4) CHARACTER SET latin1 DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

