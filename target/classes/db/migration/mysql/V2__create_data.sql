-- ENUM		Payment Status --
INSERT INTO `payment_status` (`id`, `name`) VALUES (1, 'CREATED');
INSERT INTO `payment_status` (`id`, `name`) VALUES (2, 'PROCESSING');
INSERT INTO `payment_status` (`id`, `name`) VALUES (3, 'PROCESSING_RETRY');
INSERT INTO `payment_status` (`id`, `name`) VALUES (4, 'REFUNDING');
INSERT INTO `payment_status` (`id`, `name`) VALUES (5, 'REFUNDED');
INSERT INTO `payment_status` (`id`, `name`) VALUES (6, 'FAILED');
INSERT INTO `payment_status` (`id`, `name`) VALUES (7, 'SUCCESS');

-- ENUM		Payment Gateway --
INSERT INTO `payment_gateway` (`id`, `name`) VALUES (1, 'GR1D');
INSERT INTO `payment_gateway` (`id`, `name`) VALUES (2, 'PAGARME');

-- DEFAULT	Personal Address (gr1d) --
INSERT INTO
	`personal_address` (`id`, `uuid`, `created_at`, `last_update_at`, `active`, `version`, `street`, `number`, `neighborhood`, `complement`, `city`, `county`, `postal_code`, `country`, `latitude`, `longitude`)
VALUES
	(1, 'PA-bf7bbacb-755e-11e8-8765-8cec4b1a4fcf', '2018-06-21 11:24:03', null, 1, 0, 'Avenida Brigadeiro Faria Lima', '2391', 'Jardim Paulistano', null, 'São Paulo', 'SP', '01452905', 'br', -23.5799195, -46.6856682);
