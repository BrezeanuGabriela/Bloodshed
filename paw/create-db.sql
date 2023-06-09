CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_UN` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4;

-- `paw-db`.`role` definition

CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_UN` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- `paw-db`.info_user definition

CREATE TABLE `info_user` (
  `id` int(11) NOT NULL,
  `nume` varchar(100) NOT NULL,
  `prenume` varchar(100) NOT NULL,
  `data_nasterii` date NOT NULL,
  `grupa_sange` varchar(10) NOT NULL,
  `telefon` varchar(10) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `inaltime` int(11) DEFAULT NULL,
  `greutate` int(11) DEFAULT NULL,
  `puncte` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `info_user_UN` (`nume`),
  CONSTRAINT `info_user_FK` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- `paw-db`.user_roles definition

CREATE TABLE `user_roles` (
  `id_user` int(11) NOT NULL,
  `id_role` int(11) NOT NULL,
  PRIMARY KEY (`id_user`,`id_role`),
  KEY `user_roles_FK_1` (`id_role`),
  CONSTRAINT `user_roles_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`),
  CONSTRAINT `user_roles_FK_1` FOREIGN KEY (`id_role`) REFERENCES `role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- `paw-db`.donatii definition

CREATE TABLE `donatii` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) NOT NULL,
  `observatii` varchar(200) DEFAULT NULL,
  `status` varchar(100) DEFAULT NULL,
  `data` date DEFAULT NULL,
  `ora_programarii` time DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `donatii_FK` (`id_user`),
  CONSTRAINT `donatii_FK` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `paw-db`.logs (
	id INT auto_increment NOT NULL,
	description varchar(256) NOT NULL,
	`timestamp` varchar(50) NOT NULL,
	CONSTRAINT logs_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;

CREATE TABLE `paw-db`.rewards (
	id INT auto_increment NOT NULL,
	name VARCHAR(50) NOT NULL,
	price INT NOT NULL,
	CONSTRAINT rewards_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;

