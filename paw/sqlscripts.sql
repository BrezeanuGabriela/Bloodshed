CREATE TABLE `paw-db`.logs (
	id INT auto_increment NOT NULL,
	description varchar(256) NOT NULL,
	`timestamp` varchar(50) NOT NULL,
	CONSTRAINT logs_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;
