CREATE TABLE `players` (
                           `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
                           `birth_date` DATE NULL DEFAULT NULL,
                           `name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_hungarian_ci',
                           `position` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_hungarian_ci',
                           `team_id` BIGINT(20) NULL DEFAULT NULL,
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `FK5nglidr00c4dyybl171v6kask` (`team_id`) USING BTREE,
                           CONSTRAINT `FK5nglidr00c4dyybl171v6kask` FOREIGN KEY (`team_id`) REFERENCES `exam`.`teams` (`id`) ON UPDATE RESTRICT ON DELETE RESTRICT
)
    COLLATE='utf8mb4_hungarian_ci'
    ENGINE=InnoDB
    AUTO_INCREMENT=10
;
