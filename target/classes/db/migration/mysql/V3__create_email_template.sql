CREATE TABLE `email_template_config` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(64) NOT NULL,
    `template_id` VARCHAR(64) NOT NULL,

     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_code___email_template_config` (`code`)
);
