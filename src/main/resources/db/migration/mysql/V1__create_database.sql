CREATE TABLE `payment_status` (
    `id` INTEGER(3) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,

     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_name___payment_status` (`name`)
);

-- ///////////////////////////////////// --

CREATE TABLE `payment_gateway` (
    `id` INTEGER(3) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,

     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_name___payment_gateway` (`name`)
);

-- ///////////////////////////////////// --

CREATE TABLE `personal_address` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,
    
    `version` INTEGER(7) NOT NULL,
    `street` VARCHAR(64) NOT NULL,
    `number` VARCHAR(64) NOT NULL,
    `neighborhood` VARCHAR(64) NOT NULL,
    `complement` VARCHAR(64),
    `city` VARCHAR(64) NOT NULL,
    `county` VARCHAR(64) NOT NULL,
    `postal_code` VARCHAR(64) NOT NULL,
    `country` VARCHAR(4) NOT NULL,
    `latitude` DECIMAL(11,8),
    `longitude` DECIMAL(11,8),

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_version_uuid___personal_address` (`version`, `uuid`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `personal_info` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,
    
    `version` INTEGER(7) NOT NULL,
    `full_name` VARCHAR(64) NOT NULL,
    `email` VARCHAR(64) NOT NULL,
    `document_type` INTEGER(5) NOT NULL,
    `document` VARCHAR(24) NOT NULL,
    `phone` VARCHAR(24) NOT NULL,
    `nationality` VARCHAR(4) NOT NULL,
    `date_of_birth` DATE NOT NULL,
    
    `personal_address_id` BIGINT(20) NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_version_uuid___personal_info` (`version`, `uuid`),
    CONSTRAINT `FK_personal_info___personal_address` FOREIGN KEY (`personal_address_id`) REFERENCES `personal_address` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `card_enrollment` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,
	
    `version` INTEGER(7) NOT NULL,
    `user_id` VARCHAR(64) NOT NULL,
    `card_id` VARCHAR(64) NOT NULL,
    `payment_gateway_id` INTEGER(3) NOT NULL,
    `personal_info_id` BIGINT(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_version_user_id___card_enrollment` (`version`, `user_id`),
    CONSTRAINT `FK_card_enrollment___payment_gateway` FOREIGN KEY (`payment_gateway_id`) REFERENCES `payment_gateway` (`id`),
    CONSTRAINT `FK_card_enrollment___personal_info` FOREIGN KEY (`personal_info_id`) REFERENCES `personal_info` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `invoice` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,
    
    `user_id` VARCHAR(64) NOT NULL,
    `gateway_transaction_id` VARCHAR(64) NULL,
    `value` BIGINT(20) NOT NULL,
    `payment_status_id` INTEGER(3) NOT NULL,
    `payment_gateway_id` INTEGER(3) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_uuid___invoice` (`uuid`),
    CONSTRAINT `FK_invoice___payment_status` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`id`),
    CONSTRAINT `FK_invoice___payment_gateway` FOREIGN KEY (`payment_gateway_id`) REFERENCES `payment_gateway` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `invoice_item` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,

    `invoice_id` BIGINT(20) NOT NULL,
    `external_id` VARCHAR(64) NOT NULL,
    `quantity` BIGINT(20) NOT NULL,
    `unit_value` BIGINT(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_uuid___invoice_item` (`uuid`),
    CONSTRAINT `FK_invoice_item___invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `invoice_payment_status_history` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    
    `invoice_id` BIGINT(20) NOT NULL,
    `previous_status_id` INTEGER(3) NULL,
    `status_id` INTEGER(3) NOT NULL,
    `gateway_status` VARCHAR(256) NULL,
    `timestamp` DATETIME NOT NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `FK_invoice_payment_status_history___invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
    CONSTRAINT `FK_invoice_payment_status_history___payment_status` FOREIGN KEY (`status_id`) REFERENCES `payment_status` (`id`),
    CONSTRAINT `FK_invoice_payment_status_history___previous_payment_status` FOREIGN KEY (`previous_status_id`) REFERENCES `payment_status` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `invoice_recipient_split` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,

    `invoice_id` BIGINT(20) NOT NULL,
    `recipient_id` VARCHAR(64) NOT NULL,
    `value` BIGINT(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_uuid___invoice_recipient_split` (`uuid`),
    CONSTRAINT `FK_invoice_recipient_split___invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `payment_gateway_interaction` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	
	`payment_gateway_id` INTEGER(3) NOT NULL,
	`timestamp` DATETIME NOT NULL,
	`operation` VARCHAR(256) NOT NULL,
	`destination` VARCHAR(256),
	`request_id` VARCHAR(42),
	`invoice` VARCHAR(42),
	`http_code` INTEGER(3),
	`http_message` VARCHAR(256),
	`http_verb` VARCHAR(16),
	`request_parameters` TEXT,
	`payload` MEDIUMTEXT,
	`headers` MEDIUMTEXT,
	
	PRIMARY KEY (`id`)
) DEFAULT CHARSET=utf8;
