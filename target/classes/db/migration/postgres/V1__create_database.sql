CREATE TABLE `payment_status` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    `description` VARCHAR(64) NOT NULL,

     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_name___payment_status` (`name`)
);

-- ///////////////////////////////////// --

CREATE TABLE `item_type` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    `description` VARCHAR(64) NOT NULL,

     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_name___item_type` (`name`)
);

-- ///////////////////////////////////// --

CREATE TABLE `payment_gateway` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) NOT NULL,
    `description` VARCHAR(64) NOT NULL,

     PRIMARY KEY (`id`),
     UNIQUE KEY `UK_key___payment_gateway` (`name`)
);

-- ///////////////////////////////////// --

CREATE TABLE `personal_address` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,
    
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
    UNIQUE KEY `UK_id_uuid___personal_address` (`id`, `uuid`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `personal_info` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` BIT NOT NULL DEFAULT 0,
    
    `full_name` VARCHAR(64) NOT NULL,
    `email` VARCHAR(64) NOT NULL,
    `document_type` INTEGER(5) NOT NULL,
    `document` VARCHAR(24) NOT NULL,
    `phone` VARCHAR(24) NOT NULL,
    `nationality` VARCHAR(4) NOT NULL,
    `date_of_birth` DATE NOT NULL,
    
    `personal_address_id` BIGINT(20) NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_id_uuid___personal_info` (`id`, `uuid`),
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
    `payment_gateway_id` BIGINT(20) NOT NULL,
    `personal_info_id` BIGINT(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_id_uuid___card_enrollment` (`id`, `uuid`),
    UNIQUE KEY `UK_version_per_user___card_enrollment` (`version`, `user_id`),
    CONSTRAINT `FK_card_enrollment___payment_gateway` FOREIGN KEY (`payment_gateway_id`) REFERENCES `payment_gateway` (`id`),
    CONSTRAINT `FK_card_enrollment___personal_info` FOREIGN KEY (`personal_info_id`) REFERENCES `personal_info` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `bill` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` bit NOT NULL DEFAULT 0,

    `card_enrollment_id` BIGINT(20) NOT NULL,
    `gateway_transaction_id` VARCHAR(64) NULL,
    `value` BIGINT(20) NOT NULL,
    `payment_status_id` BIGINT(20) NOT NULL,
    `payment_gateway_id` BIGINT(20) NOT NULL,
    `reference_month` INTEGER(6) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_uuid___bill` (`uuid`),
    UNIQUE KEY `UK_user_id___reference_month` (`card_enrollment_id`, `reference_month`),
    CONSTRAINT `FK_bill___card_enrollment` FOREIGN KEY (`card_enrollment_id`) REFERENCES `card_enrollment` (`id`),
    CONSTRAINT `FK_bill___payment_status` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`id`),
    CONSTRAINT `FK_bill___payment_gateway` FOREIGN KEY (`payment_gateway_id`) REFERENCES `payment_gateway` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `bill_status_history` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `bill_id` BIGINT(20) NOT NULL,
    `previous_status_id` BIGINT(20) NULL,
    `payment_status_id` BIGINT(20) NOT NULL,
    `gateway_status` varchar(256) NULL,
    `timestamp` DATETIME NOT NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT `FK_status_history___bill` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`),
    CONSTRAINT `FK_status_history___payment_status` FOREIGN KEY (`payment_status_id`) REFERENCES `payment_status` (`id`),
    CONSTRAINT `FK_status_history___previous_payment_status` FOREIGN KEY (`previous_status_id`) REFERENCES `payment_status` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `bill_detail` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` bit NOT NULL DEFAULT 0,

    `item_type_id` BIGINT(20) NOT NULL,
    `bill_id` BIGINT(20) NOT NULL,
    `item_id` VARCHAR(64) NOT NULL,
    `quantity` BIGINT(20) NOT NULL,
    `unit_value` BIGINT(20) NOT NULL,
    `value` BIGINT(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_uuid___bill_detail` (`uuid`),
    CONSTRAINT `FK_bill_detail___bill` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`),
    CONSTRAINT `FK_bill_detail___item_type` FOREIGN KEY (`item_type_id`) REFERENCES `item_type` (`id`)
) DEFAULT CHARSET=utf8;

-- ///////////////////////////////////// --

CREATE TABLE `bill_split` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uuid` VARCHAR(42) NOT NULL,
    `created_at` DATETIME NOT NULL,
    `last_update_at` DATETIME,
    `active` bit NOT NULL DEFAULT 0,

    `bill_id` BIGINT(20) NOT NULL,
    `recipient_id` VARCHAR(64) NOT NULL,
    `value` BIGINT(20) NOT NULL,

    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_uuid___bill_split` (`uuid`),
    CONSTRAINT `FK_bill_split___bill` FOREIGN KEY (`bill_id`) REFERENCES `bill` (`id`)
) DEFAULT CHARSET=utf8;