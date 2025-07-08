INSERT INTO subscription_management_service.external_user (id, external_user_id)
VALUES ('3f46dfe5-1d15-4358-8bfc-a833d082277f', 'admin');

INSERT INTO subscription_management_service.source_application (id, application_name, user_id)
VALUES ('a4ae6032-b43c-43a3-9573-616ecf258f42', 'aniscan.ru', '3f46dfe5-1d15-4358-8bfc-a833d082277f');

INSERT INTO subscription_management_service.payment_system (id, name)
VALUES ('40b2624c-8489-4cbc-87a8-92a56b1d2879', 'Robokassa');

INSERT INTO subscription_management_service.subscription_details (id, name, description, price, price_by_month,
                                                                  duration, active, source_application_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', 'Pro (1 месяц)', null, 2000.00, 2000.00, '1M', true,
        'a4ae6032-b43c-43a3-9573-616ecf258f42');

INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('fa71e1dc-cc40-4c47-a63a-e85623bb1601', 'Таблица роботов на бирже MEXC (спот)', null, 1, 'ROLE_SUBSCRIBER_PRO',
        null, true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('969536e9-d6af-478d-b101-a269d0cdb2ca', 'Таблица роботов на бирже Bybit (спот)', null, 2, 'ROLE_SUBSCRIBER_PRO',
        null, true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('aadfec73-d715-4b83-b46c-d2a4706d878a', 'История найденных роботов с MEXC', null, 3, 'ROLE_SUBSCRIBER_PRO',
        null, true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('5de0c59a-e867-4aca-8a29-7452f83d2524', 'Истории найденных роботов с Bybit', null, 4, 'ROLE_SUBSCRIBER_PRO',
        null, true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('4d4c1348-6f70-497c-bb8b-cb8edc5ca4dd', 'Мониторинг активности MEXC', null, 5, 'ROLE_SUBSCRIBER_PRO', null,
        true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('2bcb9944-a7b0-4756-88d7-ab6c9ae0aa7c', 'Мониторинг активности Bybit', null, 6, 'ROLE_SUBSCRIBER_PRO', null,
        true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('2418e752-ebd7-48a0-a66a-003a31dd858e', 'Выгрузка истории роботов в CSV', null, 7, 'ROLE_SUBSCRIBER_PRO', null,
        true);
INSERT INTO subscription_management_service.subscription_access (id, name, description, jhi_order, role, role_group, active)
VALUES ('b3937142-2aa6-4ec9-a5ce-15f3ea751a86', 'Звуковые уведомления голосом или сигналом', null, 8,
        'ROLE_SUBSCRIBER_PRO', null, true);

INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', 'fa71e1dc-cc40-4c47-a63a-e85623bb1601');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', '969536e9-d6af-478d-b101-a269d0cdb2ca');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', 'aadfec73-d715-4b83-b46c-d2a4706d878a');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', '5de0c59a-e867-4aca-8a29-7452f83d2524');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', '4d4c1348-6f70-497c-bb8b-cb8edc5ca4dd');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', '2bcb9944-a7b0-4756-88d7-ab6c9ae0aa7c');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', '2418e752-ebd7-48a0-a66a-003a31dd858e');
INSERT INTO subscription_management_service.rel_subscription_access__subscription_details (subscription_details_id, subscription_access_id)
VALUES ('0b644408-4621-4342-bf15-b8df0fcfb203', 'b3937142-2aa6-4ec9-a5ce-15f3ea751a86');
