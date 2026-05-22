-- ============================================================
-- FILE: asop_schema.sql
-- DATABASE: TAVRIDA (PostgreSQL)
-- ОПИСАНИЕ: Схема БД АСОП. Префикс ASOP_, множественное число, UPPER_CASE
--           Бизнес-логика: анонимные карты → регистрация по СНИЛС → льготы Минтруда
--           Тарификация: ступенчатые скидки, политика льгот по маршрутам
-- ============================================================

CREATE
EXTENSION IF NOT EXISTS postgis;

-- ========================
-- 1. СПРАВОЧНИКИ
-- ========================

CREATE TABLE ASOP_ROLES
(
    ROLE_ID   UUID         NOT NULL,
    ROLE_NAME VARCHAR(255) NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (ROLE_ID)
);
COMMENT
ON TABLE ASOP_ROLES IS 'Роли доступа в системе: администраторы, кассиры, водители, контролеры, ревизоры.';
INSERT INTO ASOP_ROLES (ROLE_ID, ROLE_NAME)
VALUES (gen_random_uuid(), 'Администратор'),
       (gen_random_uuid(), 'Администратор перевозчика'),
       (gen_random_uuid(), 'Кассир'),
       (gen_random_uuid(), 'Водитель'),
       (gen_random_uuid(), 'Диспетчер'),
       (gen_random_uuid(), 'Контролер'),
       (gen_random_uuid(), 'Ревизор');

CREATE TABLE ASOP_CARD_TYPES
(
    CARD_TYPE_ID   UUID         NOT NULL,
    CARD_TYPE_NAME VARCHAR(255) NOT NULL,
    CONSTRAINT pk_card_types PRIMARY KEY (CARD_TYPE_ID)
);
COMMENT
ON TABLE ASOP_CARD_TYPES IS 'Типы провозных носителей: физические чипы (MIFARE), банковские карты, QR-коды, служебные.';
INSERT INTO ASOP_CARD_TYPES (CARD_TYPE_ID, CARD_TYPE_NAME)
VALUES (gen_random_uuid(), 'MIFARE DESFire EV3'),
       (gen_random_uuid(), 'Bank Card (EMV)'),
       (gen_random_uuid(), 'QR/Virtual'),
       (gen_random_uuid(), 'Employee');

CREATE TABLE ASOP_TARIFF_TYPES
(
    TARIFF_TYPE_ID UUID         NOT NULL,
    CODE           VARCHAR(50)  NOT NULL,
    NAME           VARCHAR(100) NOT NULL,
    DESCRIPTION    TEXT,
    CONSTRAINT pk_tariff_types PRIMARY KEY (TARIFF_TYPE_ID),
    CONSTRAINT uq_tariff_types_code UNIQUE (CODE)
);
COMMENT
ON TABLE ASOP_TARIFF_TYPES IS 'Виды тарифных продуктов: пакет поездок, безлимит по времени, электронный кошелёк.';
INSERT INTO ASOP_TARIFF_TYPES (TARIFF_TYPE_ID, CODE, NAME, DESCRIPTION)
VALUES (gen_random_uuid(), 'RIDES_PACKAGE', 'Пакет поездок до даты',
        'Фиксированное количество поездок, ограниченное сроком.'),
       (gen_random_uuid(), 'UNLIMITED_TILL_DATE', 'Безлимит до даты',
        'Безлимитные поездки в заданном периоде (день/месяц/год).'),
       (gen_random_uuid(), 'WALLET', 'Пополняемый кошелёк', 'Списание средств за каждую поездку с баланса карты.');

CREATE TABLE ASOP_EVENT_TYPES
(
    EVENT_TYPE      CHAR(4)      NOT NULL,
    EVENT_TYPE_NAME VARCHAR(128) NOT NULL,
    CONSTRAINT pk_event_types PRIMARY KEY (EVENT_TYPE)
);
COMMENT
ON TABLE ASOP_EVENT_TYPES IS 'Классификатор системных событий для аудита: создание сущностей, смена смен, валидация, ошибки.';
INSERT INTO ASOP_EVENT_TYPES (EVENT_TYPE, EVENT_TYPE_NAME)
VALUES ('CUSR', 'Создание пользователя'),
       ('CCRD', 'Создание карты'),
       ('RPAY', 'Пополнение карты'),
       ('DUSR', 'Удаление пользователя'),
       ('DCRD', 'Удаление карты'),
       ('TACT', 'Активация терминала'),
       ('TDEC', 'Деактивация терминала'),
       ('DOPN', 'Открытие смены водителя'),
       ('DCLS', 'Закрытие смены водителя'),
       ('ROPN', 'Открыть маршрут водителем'),
       ('RCLS', 'Закрыть маршрут водителем'),
       ('TPAY', 'Оплата проезда'),
       ('COPN', 'Открыть смену кассира'),
       ('CCLS', 'Закрыть смену кассира'),
       ('OTHR', 'Другое событие'),
       ('SOPN', 'Открытие сессии'),
       ('SCLS', 'Закрытие сессии');

CREATE TABLE ASOP_TRANSACTION_TYPES
(
    TRANSACTION_TYPE_ID   UUID         NOT NULL,
    TRANSACTION_TYPE_NAME VARCHAR(255) NOT NULL,
    CONSTRAINT pk_transaction_types PRIMARY KEY (TRANSACTION_TYPE_ID)
);
COMMENT
ON TABLE ASOP_TRANSACTION_TYPES IS 'Типы финансовых операций: пополнение, списание, возврат, холдирование.';
INSERT INTO ASOP_TRANSACTION_TYPES (TRANSACTION_TYPE_ID, TRANSACTION_TYPE_NAME)
VALUES (gen_random_uuid(), 'Пополнение'),
       (gen_random_uuid(), 'Списание');

CREATE TABLE ASOP_TRANSACTION_RESULTS
(
    TRANSACTION_RESULT_ID   UUID         NOT NULL,
    TRANSACTION_RESULT_NAME VARCHAR(255) NOT NULL,
    CONSTRAINT pk_transaction_results PRIMARY KEY (TRANSACTION_RESULT_ID)
);
COMMENT
ON TABLE ASOP_TRANSACTION_RESULTS IS 'Статусы завершения финансовых операций: успех, ожидание, технические/бизнес-ошибки.';
INSERT INTO ASOP_TRANSACTION_RESULTS (TRANSACTION_RESULT_ID, TRANSACTION_RESULT_NAME)
VALUES (gen_random_uuid(), 'В обработке / Ожидание'),
       (gen_random_uuid(), 'Успех'),
       (gen_random_uuid(), 'Ошибка - карта не читается'),
       (gen_random_uuid(), 'Ошибка - недостаточно средств'),
       (gen_random_uuid(), 'Ошибка - карта заблокирована'),
       (gen_random_uuid(), 'Ошибка - таймаут эквайринга');

CREATE TABLE ASOP_ROUTE_TYPES
(
    ROUTE_TYPE_ID   UUID        NOT NULL,
    ROUTE_TYPE_NAME VARCHAR(16) NOT NULL,
    CONSTRAINT pk_route_types PRIMARY KEY (ROUTE_TYPE_ID)
);
COMMENT
ON TABLE ASOP_ROUTE_TYPES IS 'Классификация маршрутов: основной маршрут, альтернативный путь, короткий рейс.';
INSERT INTO ASOP_ROUTE_TYPES (ROUTE_TYPE_ID, ROUTE_TYPE_NAME)
VALUES (gen_random_uuid(), 'Маршрут'),
       (gen_random_uuid(), 'Путь');

-- ========================
-- 2. БАЗОВЫЕ СУЩНОСТИ
-- ========================

CREATE TABLE ASOP_CARRIERS
(
    CARRIER_ID   UUID         NOT NULL,
    CARRIER_NAME VARCHAR(255) NOT NULL,
    CONSTRAINT pk_carriers PRIMARY KEY (CARRIER_ID)
);
COMMENT
ON TABLE ASOP_CARRIERS IS 'Транспортные компании-перевозчики, эксплуатирующие транспорт и маршруты.';

CREATE TABLE ASOP_USERS
(
    USER_ID       UUID NOT NULL,
    ROLE_ID       UUID NOT NULL,
    CARRIER_ID    UUID,
    SNILS         VARCHAR(14),
    PHONE         VARCHAR(20),
    USER_FIO      VARCHAR(255),
    PASSWORD_HASH VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (USER_ID),
    CONSTRAINT fk_users_role_id FOREIGN KEY (ROLE_ID) REFERENCES ASOP_ROLES (ROLE_ID),
    CONSTRAINT fk_users_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIERS (CARRIER_ID),
    CONSTRAINT uq_users_snils UNIQUE (SNILS)
);
COMMENT
ON TABLE ASOP_USERS IS 'Участники системы: пассажиры (привязка по СНИЛС) и сотрудники перевозчиков. СНИЛС связывает учётную запись с реестром Минтруда.';
COMMENT
ON COLUMN ASOP_USERS.SNILS IS 'Уникальный идентификатор гражданина в реестре Минтруда. Используется для поиска/создания профиля при оформлении льгот.';
COMMENT
ON COLUMN ASOP_USERS.PHONE IS 'Контактный номер для уведомлений и верификации из реестра льгот.';

CREATE TABLE ASOP_VEHICLES
(
    VEHICLE_ID     UUID         NOT NULL,
    CARRIER_ID     UUID         NOT NULL,
    VEHICLE_NUMBER VARCHAR(16)  NOT NULL,
    VEHICLE_NAME   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_vehicles PRIMARY KEY (VEHICLE_ID),
    CONSTRAINT fk_vehicles_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIERS (CARRIER_ID)
);
COMMENT
ON TABLE ASOP_VEHICLES IS 'Парк транспортных средств (автобусы, трамваи, троллейбусы), закреплённый за перевозчиками.';
COMMENT
ON COLUMN ASOP_VEHICLES.VEHICLE_NUMBER IS 'Государственный или внутренний бортовой номер.';

CREATE TABLE ASOP_TERMINALS
(
    TERMINAL_ID           UUID        NOT NULL,
    VEHICLE_ID            UUID,
    CARRIER_ID            UUID,
    TERMINAL_NUMBER       VARCHAR(16) NOT NULL,
    TERMINAL_SERIAL       VARCHAR(64) NOT NULL,
    BENEFITS_SYNC_TOKEN   VARCHAR(64),
    LAST_BENEFITS_SYNC_AT TIMESTAMP,
    CONSTRAINT pk_terminals PRIMARY KEY (TERMINAL_ID),
    CONSTRAINT fk_terminals_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIERS (CARRIER_ID),
    CONSTRAINT fk_terminals_vehicle_id FOREIGN KEY (VEHICLE_ID) REFERENCES ASOP_VEHICLES (VEHICLE_ID)
);
COMMENT
ON TABLE ASOP_TERMINALS IS 'Бортовые валидаторы и кассовые устройства. Устанавливаются на транспорт или работают в кассах.';
COMMENT
ON COLUMN ASOP_TERMINALS.BENEFITS_SYNC_TOKEN IS 'Контрольная сумма последнего пакета льгот. Используется для инкрементальной доставки обновлений на устройство.';

-- ========================
-- 3. МАРШРУТЫ И ГЕО
-- ========================

CREATE TABLE ASOP_ROUTES
(
    ROUTE_ID         UUID         NOT NULL,
    ROUTE_TYPE_ID    UUID         NOT NULL,
    PARENT_ROUTE_ID  UUID,
    ROUTE_NAME       VARCHAR(128) NOT NULL,
    DESCRIPTION      VARCHAR(512),
    ROUTE_OBJECT     JSONB,
    ROUTE_START_DATE TIMESTAMP,
    ROUTE_END_DATE   TIMESTAMP,
    BENEFIT_POLICY   VARCHAR(20)  NOT NULL DEFAULT 'ALL' CHECK (BENEFIT_POLICY IN ('ALL', 'ALLOWLIST', 'NONE')),
    CREATED_AT       TIMESTAMP    NOT NULL,
    UPDATED_AT       TIMESTAMP    NOT NULL,
    CONSTRAINT pk_routes PRIMARY KEY (ROUTE_ID),
    CONSTRAINT fk_routes_type_id FOREIGN KEY (ROUTE_TYPE_ID) REFERENCES ASOP_ROUTE_TYPES (ROUTE_TYPE_ID),
    CONSTRAINT fk_routes_parent_id FOREIGN KEY (PARENT_ROUTE_ID) REFERENCES ASOP_ROUTES (ROUTE_ID),
    CONSTRAINT chk_routes_valid_dates CHECK (ROUTE_START_DATE IS NULL OR ROUTE_END_DATE IS NULL OR
                                             ROUTE_END_DATE > ROUTE_START_DATE)
);
COMMENT
ON TABLE ASOP_ROUTES IS 'Маршруты движения общественного транспорта. Определяют геометрию, расписание и правила применения социальных льгот.';
COMMENT
ON COLUMN ASOP_ROUTES.BENEFIT_POLICY IS 'Правило применения субсидий на маршруте: ALL (все льготы работают), ALLOWLIST (только явно разрешённые в ASOP_ROUTE_BENEFITS), NONE (льготы отключены).';
COMMENT
ON COLUMN ASOP_ROUTES.ROUTE_START_DATE IS 'Дата запуска маршрута в эксплуатацию.';
COMMENT
ON COLUMN ASOP_ROUTES.ROUTE_END_DATE IS 'Дата вывода маршрута из расписания. NULL означает постоянное действие.';
CREATE INDEX idx_routes_validity ON ASOP_ROUTES (ROUTE_START_DATE, ROUTE_END_DATE);
CREATE INDEX idx_routes_policy ON ASOP_ROUTES (BENEFIT_POLICY);

CREATE TABLE ASOP_FARE_ZONES
(
    ZONE_ID      UUID         NOT NULL,
    ZONE_CODE    VARCHAR(20)  NOT NULL,
    ZONE_NAME    VARCHAR(100) NOT NULL,
    DESCRIPTION  VARCHAR(256),
    ZONE_POLYGON GEOGRAPHY(POLYGON, 4326),
    CREATED_AT   TIMESTAMP    NOT NULL,
    UPDATED_AT   TIMESTAMP    NOT NULL,
    CONSTRAINT pk_fare_zones PRIMARY KEY (ZONE_ID),
    CONSTRAINT uq_fare_zones_code UNIQUE (ZONE_CODE),
    CONSTRAINT chk_fare_zones_valid_polygon CHECK (ZONE_POLYGON IS NULL OR ST_IsValid(ZONE_POLYGON))
);
COMMENT
ON TABLE ASOP_FARE_ZONES IS 'Тарифные зоны для расчёта стоимости проезда. Определяются географическими полигонами.';
COMMENT
ON COLUMN ASOP_FARE_ZONES.ZONE_POLYGON IS 'Граница зоны в координатах WGS84. Используется для определения принадлежности остановки к тарифному поясу.';
CREATE INDEX idx_fare_zones_geo ON ASOP_FARE_ZONES USING GIST (ZONE_POLYGON);

CREATE TABLE ASOP_TRANSPORT_STOPS
(
    STOP_ID      UUID         NOT NULL,
    FARE_ZONE_ID UUID,
    STOP_CODE    VARCHAR(20)  NOT NULL,
    STOP_NAME    VARCHAR(200) NOT NULL,
    STOP_ADDRESS VARCHAR(500),
    ZONE_POLYGON GEOGRAPHY(POLYGON, 4326),
    DESCRIPTION  TEXT,
    IS_ACTIVE    BOOLEAN DEFAULT true,
    CREATED_AT   TIMESTAMP    NOT NULL,
    UPDATED_AT   TIMESTAMP    NOT NULL,
    CONSTRAINT pk_transport_stops PRIMARY KEY (STOP_ID),
    CONSTRAINT uq_stop_code UNIQUE (STOP_CODE),
    CONSTRAINT fk_transport_stops_zone_id FOREIGN KEY (FARE_ZONE_ID) REFERENCES ASOP_FARE_ZONES (ZONE_ID)
);
COMMENT
ON TABLE ASOP_TRANSPORT_STOPS IS 'Остановочные пункты с геозонами посадки/высадки.';
COMMENT
ON COLUMN ASOP_TRANSPORT_STOPS.ZONE_POLYGON IS 'Локальная геозона остановки. Определяет точку фактической валидации билета при проходе через турникет или валидатор.';
CREATE INDEX idx_transport_stops_geo ON ASOP_TRANSPORT_STOPS USING GIST (ZONE_POLYGON);

CREATE TABLE ASOP_ROUTE_TRANSPORT_STOPS
(
    ROUTE_STOP_ID UUID      NOT NULL,
    STOP_ID       UUID      NOT NULL,
    ROUTE_ID      UUID      NOT NULL,
    SERIAL_NUMBER INT       NOT NULL,
    CREATED_AT    TIMESTAMP NOT NULL,
    UPDATED_AT    TIMESTAMP NOT NULL,
    CONSTRAINT pk_route_transport_stops PRIMARY KEY (ROUTE_STOP_ID),
    CONSTRAINT fk_route_transport_stops_stop_id FOREIGN KEY (STOP_ID) REFERENCES ASOP_TRANSPORT_STOPS (STOP_ID),
    CONSTRAINT fk_route_transport_stops_route_id FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTES (ROUTE_ID)
);
COMMENT
ON TABLE ASOP_ROUTE_TRANSPORT_STOPS IS 'Порядок следования остановок на конкретном маршруте. SERIAL_NUMBER задаёт последовательность.';
CREATE UNIQUE INDEX uk_route_transport_stops ON ASOP_ROUTE_TRANSPORT_STOPS (ROUTE_ID, STOP_ID);
CREATE INDEX idx_route_transport_stops_created_at ON ASOP_ROUTE_TRANSPORT_STOPS (CREATED_AT);

CREATE TABLE ASOP_TRANSPORT_STOP_ZONES
(
    STOP_ZONE_ID UUID      NOT NULL,
    STOP_ID      UUID      NOT NULL,
    ZONE_ID      UUID      NOT NULL,
    CREATED_AT   TIMESTAMP NOT NULL,
    UPDATED_AT   TIMESTAMP NOT NULL,
    CONSTRAINT pk_transport_stop_zones PRIMARY KEY (STOP_ZONE_ID),
    CONSTRAINT fk_transport_stop_zones_stop_id FOREIGN KEY (STOP_ID) REFERENCES ASOP_TRANSPORT_STOPS (STOP_ID),
    CONSTRAINT fk_transport_stop_zones_zone_id FOREIGN KEY (ZONE_ID) REFERENCES ASOP_FARE_ZONES (ZONE_ID)
);
COMMENT
ON TABLE ASOP_TRANSPORT_STOP_ZONES IS 'Связь остановок с несколькими тарифными зонами (для сложных перекрёстков или кольцевых маршрутов).';
CREATE UNIQUE INDEX uk_transport_stop_zones ON ASOP_TRANSPORT_STOP_ZONES (STOP_ID, ZONE_ID);
CREATE INDEX idx_transport_stop_zones_created_at ON ASOP_TRANSPORT_STOP_ZONES (CREATED_AT);

-- ========================
-- 4. МОДЕЛЬ ТАРИФОВ
-- ========================

CREATE TABLE ASOP_TARIFF_RATES
(
    TARIFF_RATE_ID UUID           NOT NULL,
    TARIFF_TYPE_ID UUID           NOT NULL,
    CARRIER_ID     UUID,
    ZONE_ID        UUID,
    ROUTE_ID       UUID,
    PRICE          NUMERIC(10, 2) NOT NULL,
    DESCRIPTION    TEXT,
    IS_ACTIVE      BOOLEAN DEFAULT true,
    CREATED_AT     TIMESTAMP      NOT NULL,
    UPDATED_AT     TIMESTAMP      NOT NULL,
    CONSTRAINT pk_tariff_rates PRIMARY KEY (TARIFF_RATE_ID),
    CONSTRAINT fk_tariff_rates_type FOREIGN KEY (TARIFF_TYPE_ID) REFERENCES ASOP_TARIFF_TYPES (TARIFF_TYPE_ID),
    CONSTRAINT fk_tariff_rates_carrier FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIERS (CARRIER_ID),
    CONSTRAINT fk_tariff_rates_zone FOREIGN KEY (ZONE_ID) REFERENCES ASOP_FARE_ZONES (ZONE_ID),
    CONSTRAINT fk_tariff_rates_route FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTES (ROUTE_ID),
    CONSTRAINT uq_tariff_rates_context UNIQUE (TARIFF_TYPE_ID, CARRIER_ID, ZONE_ID, ROUTE_ID)
);
COMMENT
ON TABLE ASOP_TARIFF_RATES IS 'Актуальные цены на тарифные продукты. Поддерживает кастомизацию по маршрутам, зонам и перевозчикам.';
COMMENT
ON COLUMN ASOP_TARIFF_RATES.ROUTE_ID IS 'Привязка цены к конкретному маршруту. NULL означает цену для всех маршрутов.';
COMMENT
ON COLUMN ASOP_TARIFF_RATES.CARRIER_ID IS 'Привязка к перевозчику. NULL означает общесистемную цену.';
COMMENT
ON COLUMN ASOP_TARIFF_RATES.ZONE_ID IS 'Привязка к тарифной зоне. NULL означает универсальную цену.';
CREATE INDEX idx_tariff_rates_type ON ASOP_TARIFF_RATES (TARIFF_TYPE_ID);
CREATE INDEX idx_tariff_rates_carrier ON ASOP_TARIFF_RATES (CARRIER_ID);
CREATE INDEX idx_tariff_rates_zone ON ASOP_TARIFF_RATES (ZONE_ID);
CREATE INDEX idx_tariff_rates_route ON ASOP_TARIFF_RATES (ROUTE_ID);
CREATE INDEX idx_tariff_rates_active ON ASOP_TARIFF_RATES (IS_ACTIVE) WHERE IS_ACTIVE = true;

CREATE TABLE ASOP_CARRIER_ZONES
(
    CARRIER_ZONE_ID UUID           NOT NULL,
    CARRIER_ID      UUID           NOT NULL,
    ZONE_ID         UUID           NOT NULL,
    BASE_FARE       DECIMAL(10, 2) NOT NULL,
    CREATED_AT      TIMESTAMP      NOT NULL,
    UPDATED_AT      TIMESTAMP      NOT NULL,
    CONSTRAINT pk_carrier_zones PRIMARY KEY (CARRIER_ZONE_ID),
    CONSTRAINT fk_carrier_zones_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIERS (CARRIER_ID),
    CONSTRAINT fk_carrier_zones_zone_id FOREIGN KEY (ZONE_ID) REFERENCES ASOP_FARE_ZONES (ZONE_ID)
);
COMMENT
ON TABLE ASOP_CARRIER_ZONES IS 'Базовая тарифная сетка перевозчика по зонам. Используется как резерв при отсутствии явной ставки в ASOP_TARIFF_RATES.';
CREATE UNIQUE INDEX uk_carrier_zones ON ASOP_CARRIER_ZONES (CARRIER_ID, ZONE_ID);

-- ========================
-- 5. КАРТЫ И ЛЬГОТЫ
-- ========================

CREATE TABLE ASOP_CARDS
(
    CARD_ID               UUID      NOT NULL,
    CARD_TYPE_ID          UUID      NOT NULL,
    USER_ID               UUID,
    REGISTERED_AT         TIMESTAMP,
    REGISTERED_BY_USER_ID UUID,
    CREATED_AT            TIMESTAMP NOT NULL,
    UPDATED_AT            TIMESTAMP NOT NULL,
    CONSTRAINT pk_cards PRIMARY KEY (CARD_ID),
    CONSTRAINT fk_cards_type_id FOREIGN KEY (CARD_TYPE_ID) REFERENCES ASOP_CARD_TYPES (CARD_TYPE_ID),
    CONSTRAINT fk_cards_user_id FOREIGN KEY (USER_ID) REFERENCES ASOP_USERS (USER_ID),
    CONSTRAINT fk_cards_registered_by FOREIGN KEY (REGISTERED_BY_USER_ID) REFERENCES ASOP_USERS (USER_ID)
);
COMMENT
ON TABLE ASOP_CARDS IS 'Провозные носители. Изначально анонимны. Привязка к СНИЛС выполняется кассиром/админом для активации льгот.';
COMMENT
ON COLUMN ASOP_CARDS.USER_ID IS 'Владелец карты. NULL до момента регистрации в кассе или личном кабинете.';
COMMENT
ON COLUMN ASOP_CARDS.REGISTERED_AT IS 'Дата привязки карты к профилю льготника.';
COMMENT
ON COLUMN ASOP_CARDS.REGISTERED_BY_USER_ID IS 'Сотрудник, выполнивший привязку. Фиксируется для аудита и отчётности Минтруда.';

CREATE TABLE ASOP_CARD_MIFARES
(
    CARD_ID          UUID  NOT NULL,
    UID              BYTEA NOT NULL,
    ATQA             SMALLINT,
    SAK              SMALLINT,
    PROTOCOL_VERSION INT,
    MEMORY_MAP       JSONB,
    CONSTRAINT pk_card_mifares PRIMARY KEY (CARD_ID),
    CONSTRAINT uq_mifare_uid UNIQUE (UID),
    CONSTRAINT fk_mifares_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARDS (CARD_ID) ON DELETE CASCADE
);
COMMENT
ON TABLE ASOP_CARD_MIFARES IS 'Технические параметры NFC-чипов MIFARE. UID используется для быстрой идентификации в валидаторе.';
COMMENT
ON COLUMN ASOP_CARD_MIFARES.UID IS 'Аппаратный серийный номер чипа.';
COMMENT
ON COLUMN ASOP_CARD_MIFARES.ATQA IS 'Ответ чипа на запрос SELECT. Определяет тип и поколение.';
COMMENT
ON COLUMN ASOP_CARD_MIFARES.SAK IS 'Код подтверждения выбора чипа.';
COMMENT
ON COLUMN ASOP_CARD_MIFARES.PROTOCOL_VERSION IS 'Версия криптографического протокола чипа.';
COMMENT
ON COLUMN ASOP_CARD_MIFARES.MEMORY_MAP IS 'Карта приложений и секторов чипа. Хранит структуру записанных тарифов.';

CREATE TABLE ASOP_CARD_BANKS
(
    CARD_ID      UUID         NOT NULL,
    PAN_TOKEN    VARCHAR(256) NOT NULL,
    PAN_LAST4    CHAR(4),
    BIN          CHAR(6),
    IS_TOKENIZED BOOLEAN DEFAULT false,
    CONSTRAINT pk_card_banks PRIMARY KEY (CARD_ID),
    CONSTRAINT fk_banks_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARDS (CARD_ID) ON DELETE CASCADE
);
COMMENT
ON TABLE ASOP_CARD_BANKS IS 'Данные банковских карт. PAN хранится только в виде токена/криптотекста (соответствие PCI DSS).';
COMMENT
ON COLUMN ASOP_CARD_BANKS.PAN_TOKEN IS 'Токенизированный номер карты. Используется для проведения транзакций через эквайринг.';
COMMENT
ON COLUMN ASOP_CARD_BANKS.PAN_LAST4 IS 'Последние 4 цифры номера. Разрешено к отображению в чеках и интерфейсе.';
COMMENT
ON COLUMN ASOP_CARD_BANKS.BIN IS 'Банковский идентификационный номер. Определяет эквайринг-партнёра и тип карты.';
COMMENT
ON COLUMN ASOP_CARD_BANKS.IS_TOKENIZED IS 'Признак цифрового кошелька (Apple/Google/Mir Pay). Требует проверки криптограммы при оплате.';

CREATE TABLE ASOP_CARD_TARIFFS
(
    CARD_TARIFF_ID          UUID      NOT NULL,
    CARD_ID                 UUID      NOT NULL,
    TARIFF_TYPE_ID          UUID      NOT NULL,
    BALANCE                 NUMERIC(10, 2),
    TRAVEL_COUNT            INT,
    MAX_TRAVEL_COUNT        INT,
    EXPIRATION_DATE         TIMESTAMP,
    ACTIVATED_AT            TIMESTAMP,
    PURCHASE_TRANSACTION_ID UUID,
    IS_ACTIVE               BOOLEAN DEFAULT true,
    CREATED_AT              TIMESTAMP NOT NULL,
    UPDATED_AT              TIMESTAMP NOT NULL,
    CONSTRAINT pk_card_tariffs PRIMARY KEY (CARD_TARIFF_ID),
    CONSTRAINT fk_card_tariffs_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARDS (CARD_ID) ON DELETE CASCADE,
    CONSTRAINT fk_card_tariffs_type FOREIGN KEY (TARIFF_TYPE_ID) REFERENCES ASOP_TARIFF_TYPES (TARIFF_TYPE_ID)
);
COMMENT
ON TABLE ASOP_CARD_TARIFFS IS 'Активные тарифные продукты, записанные на карту. Одна карта может содержать несколько продуктов одновременно.';
COMMENT
ON COLUMN ASOP_CARD_TARIFFS.BALANCE IS 'Остаток средств (для кошелька).'
COMMENT ON COLUMN ASOP_CARD_TARIFFS.TRAVEL_COUNT IS 'Остаток поездок (для пакетов/безлимита).';
COMMENT
ON COLUMN ASOP_CARD_TARIFFS.PURCHASE_TRANSACTION_ID IS 'Ссылка на транзакцию оплаты/пополнения данного тарифа.';

CREATE TABLE ASOP_BLACKLISTS
(
    CARD_ID    UUID        NOT NULL,
    BLOCK_TYPE VARCHAR(20) NOT NULL,
    BLOCKED_AT TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pk_blacklists PRIMARY KEY (CARD_ID),
    CONSTRAINT fk_blacklists_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARDS (CARD_ID),
    CONSTRAINT chk_blacklist_type CHECK (BLOCK_TYPE IN ('PERMANENT', 'NEGATIVE_BALANCE'))
);
COMMENT
ON TABLE ASOP_BLACKLISTS IS 'Реестр заблокированных карт. Терминалы получают этот список для мгновенного запрета прохода.';
COMMENT
ON COLUMN ASOP_BLACKLISTS.BLOCK_TYPE IS 'Причина блокировки: PERMANENT (утеря/кража/мошенничество) или NEGATIVE_BALANCE (технический минус).';
CREATE INDEX idx_blacklists_type ON ASOP_BLACKLISTS (BLOCK_TYPE);

-- ========================
-- 5.1 ЛЬГОТЫ МИНТРУДА
-- ========================

CREATE TABLE ASOP_BENEFITS
(
    BENEFIT_ID   UUID         NOT NULL,
    BENEFIT_CODE VARCHAR(50)  NOT NULL,
    BENEFIT_NAME VARCHAR(100) NOT NULL,
    REGION_CODE  VARCHAR(50)  NOT NULL,
    DESCRIPTION  TEXT,
    IS_ACTIVE    BOOLEAN DEFAULT true,
    CREATED_AT   TIMESTAMP    NOT NULL,
    UPDATED_AT   TIMESTAMP    NOT NULL,
    CONSTRAINT pk_benefits PRIMARY KEY (BENEFIT_ID),
    CONSTRAINT uq_benefits_code UNIQUE (BENEFIT_CODE)
);
COMMENT
ON TABLE ASOP_BENEFITS IS 'Справочник государственных субсидий, загружаемый из реестра Минтруда/Соцзащиты.';
COMMENT
ON COLUMN ASOP_BENEFITS.REGION_CODE IS 'Код региона действия льготы (например, субъект РФ или муниципалитет).';

CREATE TABLE ASOP_BENEFIT_STEPS
(
    STEP_ID             UUID          NOT NULL,
    BENEFIT_ID          UUID          NOT NULL,
    STEP_ORDER          INT           NOT NULL,
    TRIP_THRESHOLD_FROM INT           NOT NULL DEFAULT 0,
    TRIP_THRESHOLD_TO   INT,
    DISCOUNT_SHARE      NUMERIC(4, 2) NOT NULL CHECK (DISCOUNT_SHARE BETWEEN 0 AND 1),
    PERIOD_TYPE         VARCHAR(20)   NOT NULL DEFAULT 'MONTHLY',
    CONSTRAINT pk_benefit_steps PRIMARY KEY (STEP_ID),
    CONSTRAINT fk_steps_benefit FOREIGN KEY (BENEFIT_ID) REFERENCES ASOP_BENEFITS (BENEFIT_ID) ON DELETE CASCADE,
    CONSTRAINT uq_benefit_steps_order UNIQUE (BENEFIT_ID, STEP_ORDER),
    CONSTRAINT chk_steps_range CHECK (TRIP_THRESHOLD_TO IS NULL OR TRIP_THRESHOLD_TO > TRIP_THRESHOLD_FROM)
);
COMMENT
ON TABLE ASOP_BENEFIT_STEPS IS 'Ступенчатая модель скидок. Определяет, какая доля стоимости оплачивается государством на определённом количестве поездок в месяц.';
COMMENT
ON COLUMN ASOP_BENEFIT_STEPS.TRIP_THRESHOLD_FROM IS 'Нижняя граница диапазона поездок (включительно).';
COMMENT
ON COLUMN ASOP_BENEFIT_STEPS.TRIP_THRESHOLD_TO IS 'Верхняя граница. NULL означает "до конца периода".';
COMMENT
ON COLUMN ASOP_BENEFIT_STEPS.DISCOUNT_SHARE IS 'Доля скидки: 0.0 = бесплатно, 0.5 = скидка 50%, 1.0 = полная оплата.';

-- Привязка льгот к пользователям
CREATE TABLE ASOP_USER_BENEFITS
(
    ASSIGNMENT_ID UUID      NOT NULL,
    USER_ID       UUID      NOT NULL,
    BENEFIT_ID    UUID      NOT NULL,
    VALID_FROM    TIMESTAMP NOT NULL,
    VALID_UNTIL   TIMESTAMP,
    SYNC_VERSION  INT       NOT NULL DEFAULT 1,
    CREATED_AT    TIMESTAMP NOT NULL,
    UPDATED_AT    TIMESTAMP NOT NULL,
    CONSTRAINT pk_user_benefits PRIMARY KEY (ASSIGNMENT_ID),
    CONSTRAINT fk_ub_user FOREIGN KEY (USER_ID) REFERENCES ASOP_USERS (USER_ID) ON DELETE CASCADE,
    CONSTRAINT fk_ub_benefit FOREIGN KEY (BENEFIT_ID) REFERENCES ASOP_BENEFITS (BENEFIT_ID),
    CONSTRAINT chk_ub_validity CHECK (VALID_UNTIL IS NULL OR VALID_UNTIL > VALID_FROM)
);
COMMENT
ON TABLE ASOP_USER_BENEFITS IS 'Активные льготы, закреплённые за профилем пассажира. Назначаются вручную после предоставления СНИЛС.';
COMMENT
ON COLUMN ASOP_USER_BENEFITS.VALID_FROM IS 'Дата начала действия социальной программы.';
COMMENT
ON COLUMN ASOP_USER_BENEFITS.VALID_UNTIL IS 'Дата окончания. NULL означает бессрочное действие до отзыва.';
COMMENT
ON COLUMN ASOP_USER_BENEFITS.SYNC_VERSION IS 'Версия записи. Увеличивается при любом изменении для отслеживания устаревших данных на терминалах.';

-- Разрешённые льготы для маршрутов с политикой ALLOWLIST
CREATE TABLE ASOP_ROUTE_BENEFITS
(
    ROUTE_BENEFIT_ID UUID NOT NULL,
    ROUTE_ID         UUID NOT NULL,
    BENEFIT_ID       UUID NOT NULL,
    CONSTRAINT pk_route_benefits PRIMARY KEY (ROUTE_BENEFIT_ID),
    CONSTRAINT fk_rb_route FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTES (ROUTE_ID) ON DELETE CASCADE,
    CONSTRAINT fk_rb_benefit FOREIGN KEY (BENEFIT_ID) REFERENCES ASOP_BENEFITS (BENEFIT_ID) ON DELETE CASCADE,
    CONSTRAINT uq_route_benefit UNIQUE (ROUTE_ID, BENEFIT_ID)
);
COMMENT
ON TABLE ASOP_ROUTE_BENEFITS IS 'Белый список льгот для маршрутов. Используется только если у маршрута BENEFIT_POLICY = ''ALLOWLIST''.';
COMMENT
ON COLUMN ASOP_ROUTE_BENEFITS.BENEFIT_ID IS 'Конкретная льгота, разрешённая к применению на данном маршруте.';
CREATE INDEX idx_rb_route ON ASOP_ROUTE_BENEFITS (ROUTE_ID);
CREATE INDEX idx_rb_benefit ON ASOP_ROUTE_BENEFITS (BENEFIT_ID);

-- ========================
-- 6. СЕССИИ, СОБЫТИЯ, ТРАНЗАКЦИИ
-- ========================

CREATE TABLE ASOP_USER_SESSIONS
(
    SESSION_ID        UUID      NOT NULL,
    TERMINAL_ID       UUID      NOT NULL,
    OPENED_BY_USER_ID UUID,
    CLOSED_BY_USER_ID UUID,
    CARD_ID           UUID,
    STARTED_AT        TIMESTAMP NOT NULL,
    CLOSED_AT         TIMESTAMP,
    STARTED_AT_LOCAL  TIMESTAMP NOT NULL,
    CLOSED_AT_LOCAL   TIMESTAMP,
    EXPIRATION_TIME   TIMESTAMP NOT NULL,
    CONSTRAINT pk_user_sessions PRIMARY KEY (SESSION_ID),
    CONSTRAINT fk_sessions_terminal_id FOREIGN KEY (TERMINAL_ID) REFERENCES ASOP_TERMINALS (TERMINAL_ID),
    CONSTRAINT fk_sessions_opened_by FOREIGN KEY (OPENED_BY_USER_ID) REFERENCES ASOP_USERS (USER_ID) ON DELETE SET NULL,
    CONSTRAINT fk_sessions_closed_by FOREIGN KEY (CLOSED_BY_USER_ID) REFERENCES ASOP_USERS (USER_ID) ON DELETE SET NULL,
    CONSTRAINT fk_sessions_card_id FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARDS (CARD_ID)
);
COMMENT
ON TABLE ASOP_USER_SESSIONS IS 'Рабочие смены терминалов. Фиксирует время работы, ответственных лиц и привязку к карте (если требуется).';
COMMENT
ON COLUMN ASOP_USER_SESSIONS.OPENED_BY_USER_ID IS 'Сотрудник, открывший смену. Может отличаться от закрывшего.';
COMMENT
ON COLUMN ASOP_USER_SESSIONS.STARTED_AT_LOCAL IS 'Время открытия по локальным часам терминала (для сверки при рассинхроне NTP).';

CREATE TABLE ASOP_EVENTS
(
    EVENT_ID          UUID      NOT NULL,
    EVENT_TIME        TIMESTAMP NOT NULL,
    EVENT_LOCAL_TIME  TIMESTAMP NOT NULL,
    EVENT_TYPE        CHAR(4)   NOT NULL,
    USER_ID           UUID,
    SESSION_ID        UUID,
    REFERENCE_TYPE_ID INT,
    REFERENCE_ID      UUID,
    EVENT_DETAILS     VARCHAR(256),
    EVENT_OBJECT      JSONB,
    CONSTRAINT pk_events PRIMARY KEY (EVENT_ID),
    CONSTRAINT fk_events_type FOREIGN KEY (EVENT_TYPE) REFERENCES ASOP_EVENT_TYPES (EVENT_TYPE),
    CONSTRAINT fk_events_session_id FOREIGN KEY (SESSION_ID) REFERENCES ASOP_USER_SESSIONS (SESSION_ID),
    CONSTRAINT fk_events_user_id FOREIGN KEY (USER_ID) REFERENCES ASOP_USERS (USER_ID)
);
COMMENT
ON TABLE ASOP_EVENTS IS 'Журнал всех системных действий. Используется для аудита, разбора инцидентов и отчётности.';
COMMENT
ON COLUMN ASOP_EVENTS.REFERENCE_ID IS 'Идентификатор связанной сущности (карты, транзакции, тарифа). Тип определяется в REFERENCE_TYPE_ID.';

CREATE TABLE ASOP_TRANSACTIONS
(
    TRANSACTION_ID        UUID           NOT NULL,
    STARTED_AT            TIMESTAMP      NOT NULL,
    COMPLETED_AT          TIMESTAMP,
    TERMINAL_ID           UUID           NOT NULL,
    SESSION_ID            UUID,
    TRIP_ID               UUID,
    TRANSACTION_TYPE_ID   UUID           NOT NULL,
    TRANSACTION_RESULT_ID UUID           NOT NULL,
    AMOUNT                NUMERIC(10, 2) NOT NULL DEFAULT 0,
    CURRENCY              CHAR(3)                 DEFAULT 'RUB',
    ACQUIRER_REFERENCE    VARCHAR(128),
    ERROR_CODE            VARCHAR(50),
    ERROR_MESSAGE         VARCHAR(512),
    METADATA              JSONB,
    CONSTRAINT pk_transactions PRIMARY KEY (TRANSACTION_ID),
    CONSTRAINT fk_transactions_terminal FOREIGN KEY (TERMINAL_ID) REFERENCES ASOP_TERMINALS (TERMINAL_ID),
    CONSTRAINT fk_transactions_session FOREIGN KEY (SESSION_ID) REFERENCES ASOP_USER_SESSIONS (SESSION_ID),
    CONSTRAINT fk_transactions_trip FOREIGN KEY (TRIP_ID) REFERENCES ASOP_TRIPS (TRIP_ID),
    CONSTRAINT fk_transactions_type FOREIGN KEY (TRANSACTION_TYPE_ID) REFERENCES ASOP_TRANSACTION_TYPES (TRANSACTION_TYPE_ID),
    CONSTRAINT fk_transactions_result FOREIGN KEY (TRANSACTION_RESULT_ID) REFERENCES ASOP_TRANSACTION_RESULTS (TRANSACTION_RESULT_ID)
);
COMMENT
ON TABLE ASOP_TRANSACTIONS IS 'Финансовые и тарифные проводки. Хранятся ВСЕ попытки оплаты: успешные, отклонённые, ожидающие.';
COMMENT
ON COLUMN ASOP_TRANSACTIONS.STARTED_AT IS 'Момент начала обработки операции (прикладывание карты/запрос к эквайрингу).';
COMMENT
ON COLUMN ASOP_TRANSACTIONS.COMPLETED_AT IS 'Момент финализации. NULL означает процесс валидации или ожидание ответа банка.';
COMMENT
ON COLUMN ASOP_TRANSACTIONS.AMOUNT IS 'Итоговая сумма проводки. Учитывает применённые скидки и ступени льгот.';
COMMENT
ON COLUMN ASOP_TRANSACTIONS.ACQUIRER_REFERENCE IS 'Уникальный номер операции в платёжной системе банка. Нужен для сверки и возвратов.';
COMMENT
ON COLUMN ASOP_TRANSACTIONS.ERROR_CODE IS 'Код ошибки от терминала, банка или расчётного ядра.';

CREATE TABLE ASOP_TRANSACTION_CARDS
(
    TRANSACTION_CARD_ID UUID        NOT NULL,
    TRANSACTION_ID      UUID        NOT NULL,
    CARD_ID             UUID        NOT NULL,
    CARD_ROLE           VARCHAR(20) NOT NULL,
    TARIFF_APPLIED_ID   UUID,
    BALANCE_BEFORE      NUMERIC(10, 2),
    BALANCE_AFTER       NUMERIC(10, 2),
    CONSTRAINT pk_transaction_cards PRIMARY KEY (TRANSACTION_CARD_ID),
    CONSTRAINT fk_tc_transaction FOREIGN KEY (TRANSACTION_ID) REFERENCES ASOP_TRANSACTIONS (TRANSACTION_ID) ON DELETE CASCADE,
    CONSTRAINT fk_tc_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARDS (CARD_ID),
    CONSTRAINT fk_tc_tariff FOREIGN KEY (TARIFF_APPLIED_ID) REFERENCES ASOP_CARD_TARIFFS (CARD_TARIFF_ID)
);
COMMENT
ON TABLE ASOP_TRANSACTION_CARDS is 'Связка карт с транзакцией. Позволяет фиксировать несколько носителей в одной операции.';
COMMENT
ON COLUMN ASOP_TRANSACTION_CARDS.CARD_ROLE IS 'Роль носителя: PAYER (оплата), IDENTIFIER (идентификация/льгота), FALLBACK (резервная карта).';
COMMENT
ON COLUMN ASOP_TRANSACTION_CARDS.BALANCE_BEFORE IS 'Баланс карты до операции. Фиксируется для аудита и отмены транзакций.';
CREATE UNIQUE INDEX uk_transaction_cards_main ON ASOP_TRANSACTION_CARDS (TRANSACTION_ID, CARD_ID);

ALTER TABLE ASOP_CARD_TARIFFS
    ADD CONSTRAINT fk_tariff_purchase FOREIGN KEY (PURCHASE_TRANSACTION_ID) REFERENCES ASOP_TRANSACTIONS (TRANSACTION_ID);

CREATE TABLE ASOP_CARRIER_ROUTES
(
    CARRIER_ROUTE_ID UUID      NOT NULL,
    CARRIER_ID       UUID      NOT NULL,
    ROUTE_ID         UUID      NOT NULL,
    CREATED_AT       TIMESTAMP NOT NULL,
    UPDATED_AT       TIMESTAMP NOT NULL,
    CONSTRAINT pk_carrier_routes PRIMARY KEY (CARRIER_ROUTE_ID),
    CONSTRAINT fk_carrier_routes_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIERS (CARRIER_ID),
    CONSTRAINT fk_carrier_routes_route_id FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTES (ROUTE_ID)
);
COMMENT
ON TABLE ASOP_CARRIER_ROUTES IS 'Договорные связи: какие маршруты эксплуатируются конкретным перевозчиком.';
CREATE UNIQUE INDEX uk_carrier_routes ON ASOP_CARRIER_ROUTES (CARRIER_ID, ROUTE_ID);

CREATE TABLE ASOP_TRIPS
(
    TRIP_ID          UUID      NOT NULL,
    ROUTE_ID         UUID      NOT NULL,
    SESSION_ID       UUID      NOT NULL,
    STARTED_AT       TIMESTAMP NOT NULL,
    CLOSED_AT        TIMESTAMP,
    STARTED_AT_LOCAL TIMESTAMP NOT NULL,
    CLOSED_AT_LOCAL  TIMESTAMP,
    CONSTRAINT pk_trips PRIMARY KEY (TRIP_ID),
    CONSTRAINT fk_trips_route_id FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTES (ROUTE_ID),
    CONSTRAINT fk_trips_session_id FOREIGN KEY (SESSION_ID) REFERENCES ASOP_USER_SESSIONS (SESSION_ID)
);
COMMENT
ON TABLE ASOP_TRIPS IS 'Фактические рейсы транспортных средств. Связывает маршрут, смену водителя и временные интервалы.';
COMMENT
ON COLUMN ASOP_TRIPS.CLOSED_AT IS 'Время прибытия на конечную/закрытия рейса. NULL означает рейс в пути.';

-- ========================
-- 7. ИНДЕКСЫ
-- ========================
CREATE INDEX idx_cards_type_id ON ASOP_CARDS (CARD_TYPE_ID);
CREATE INDEX idx_cards_user_id ON ASOP_CARDS (USER_ID) WHERE USER_ID IS NOT NULL;
CREATE INDEX idx_cards_registered_at ON ASOP_CARDS (REGISTERED_AT) WHERE REGISTERED_AT IS NOT NULL;
CREATE INDEX idx_cards_mifare_uid ON ASOP_CARD_MIFARES (UID);
CREATE INDEX idx_cards_bank_token ON ASOP_CARD_BANKS (PAN_TOKEN);
CREATE INDEX idx_card_tariffs_card ON ASOP_CARD_TARIFFS (CARD_ID);
CREATE INDEX idx_card_tariffs_type ON ASOP_CARD_TARIFFS (TARIFF_TYPE_ID);
CREATE INDEX idx_card_tariffs_expiry ON ASOP_CARD_TARIFFS (EXPIRATION_DATE) WHERE IS_ACTIVE = true;
CREATE INDEX idx_terminals_number ON ASOP_TERMINALS (TERMINAL_NUMBER);
CREATE INDEX idx_terminals_carrier_id ON ASOP_TERMINALS (CARRIER_ID);
CREATE INDEX idx_terminals_vehicle_id ON ASOP_TERMINALS (VEHICLE_ID);
CREATE INDEX idx_users_snils ON ASOP_USERS (SNILS) WHERE SNILS IS NOT NULL;
CREATE INDEX idx_user_sessions_opened_by ON ASOP_USER_SESSIONS (OPENED_BY_USER_ID);
CREATE INDEX idx_user_sessions_closed_by ON ASOP_USER_SESSIONS (CLOSED_BY_USER_ID);
CREATE INDEX idx_user_sessions_terminal ON ASOP_USER_SESSIONS (TERMINAL_ID);
CREATE INDEX idx_user_sessions_started ON ASOP_USER_SESSIONS (STARTED_AT);
CREATE INDEX idx_user_sessions_card ON ASOP_USER_SESSIONS (CARD_ID);
CREATE INDEX idx_events_time ON ASOP_EVENTS (EVENT_TIME, USER_ID);
CREATE INDEX idx_events_local_time ON ASOP_EVENTS (EVENT_LOCAL_TIME, USER_ID);
CREATE INDEX idx_events_user_id ON ASOP_EVENTS (USER_ID, SESSION_ID);
CREATE INDEX idx_events_session_id ON ASOP_EVENTS (SESSION_ID);
CREATE INDEX idx_events_type_id ON ASOP_EVENTS (EVENT_TYPE);
CREATE INDEX idx_transactions_terminal ON ASOP_TRANSACTIONS (TERMINAL_ID);
CREATE INDEX idx_transactions_session ON ASOP_TRANSACTIONS (SESSION_ID);
CREATE INDEX idx_transactions_type ON ASOP_TRANSACTIONS (TRANSACTION_TYPE_ID);
CREATE INDEX idx_transactions_result ON ASOP_TRANSACTIONS (TRANSACTION_RESULT_ID);
CREATE INDEX idx_transactions_started_at ON ASOP_TRANSACTIONS (STARTED_AT);
CREATE INDEX idx_transactions_completed_at ON ASOP_TRANSACTIONS (COMPLETED_AT) WHERE COMPLETED_AT IS NULL;
CREATE INDEX idx_transactions_acquirer_ref ON ASOP_TRANSACTIONS (ACQUIRER_REFERENCE) WHERE ACQUIRER_REFERENCE IS NOT NULL;
CREATE INDEX idx_tc_transaction_id ON ASOP_TRANSACTION_CARDS (TRANSACTION_ID);
CREATE INDEX idx_tc_card_id ON ASOP_TRANSACTION_CARDS (CARD_ID);
CREATE INDEX idx_tc_role ON ASOP_TRANSACTION_CARDS (CARD_ROLE);
CREATE INDEX idx_tc_tariff_id ON ASOP_TRANSACTION_CARDS (TARIFF_APPLIED_ID);
CREATE INDEX idx_routes_validity ON ASOP_ROUTES (ROUTE_START_DATE, ROUTE_END_DATE);
CREATE INDEX idx_routes_policy ON ASOP_ROUTES (BENEFIT_POLICY);
CREATE INDEX idx_routes_parent_id ON ASOP_ROUTES (PARENT_ROUTE_ID);
CREATE INDEX idx_routes_created_at ON ASOP_ROUTES (CREATED_AT);
CREATE INDEX idx_carrier_routes_carrier_id ON ASOP_CARRIER_ROUTES (CARRIER_ID);
CREATE INDEX idx_carrier_routes_route_id ON ASOP_CARRIER_ROUTES (ROUTE_ID);
CREATE INDEX idx_carrier_zones_carrier_id ON ASOP_CARRIER_ZONES (CARRIER_ID);
CREATE INDEX idx_carrier_zones_zone_id ON ASOP_CARRIER_ZONES (ZONE_ID);
CREATE INDEX idx_transport_stops_zone_id ON ASOP_TRANSPORT_STOPS (FARE_ZONE_ID);
CREATE INDEX idx_transport_stops_created_at ON ASOP_TRANSPORT_STOPS (CREATED_AT);
CREATE INDEX idx_route_transport_stops_created_at ON ASOP_ROUTE_TRANSPORT_STOPS (CREATED_AT);
CREATE INDEX idx_trips_started_at ON ASOP_TRIPS (STARTED_AT);
CREATE INDEX idx_trips_started_at_local ON ASOP_TRIPS (STARTED_AT_LOCAL);
CREATE INDEX idx_trips_route_id ON ASOP_TRIPS (ROUTE_ID);
CREATE INDEX idx_trips_session_id ON ASOP_TRIPS (SESSION_ID);
CREATE INDEX idx_transport_stop_zones_created_at ON ASOP_TRANSPORT_STOP_ZONES (CREATED_AT);
CREATE INDEX idx_benefits_region ON ASOP_BENEFITS (REGION_CODE);
CREATE INDEX idx_steps_benefit ON ASOP_BENEFIT_STEPS (BENEFIT_ID);
CREATE INDEX idx_ub_user ON ASOP_USER_BENEFITS (USER_ID);
CREATE INDEX idx_ub_benefit ON ASOP_USER_BENEFITS (BENEFIT_ID);
CREATE INDEX idx_ub_validity ON ASOP_USER_BENEFITS (VALID_FROM, VALID_UNTIL);
CREATE INDEX idx_rb_route ON ASOP_ROUTE_BENEFITS (ROUTE_ID);
CREATE INDEX idx_rb_benefit ON ASOP_ROUTE_BENEFITS (BENEFIT_ID);

-- ========================
-- 8. ДОКУМЕНТАЦИЯ
-- ========================
COMMENT
ON TABLE ASOP_EVENTS IS 'Журнал системных действий. EVENT_OBJECT хранит детализированный контекст в JSON.';
COMMENT
ON TABLE ASOP_TRANSACTIONS IS 'Реестр финансовых операций. Служит источником данных для биллинга, отчётности и сверки с банками.';
COMMENT
ON TABLE ASOP_TRANSACTION_CARDS IS 'Участники транзакции. Фиксирует баланс и роль каждого носителя.';
COMMENT
ON TABLE ASOP_CARD_MIFARES IS 'Технические профили NFC-карт.';
COMMENT
ON TABLE ASOP_CARD_BANKS IS 'Профили банковских карт. PAN токенизирован.';
COMMENT
ON TABLE ASOP_USER_SESSIONS IS 'Учёт рабочего времени терминалов и персонала.';
COMMENT
ON TABLE ASOP_TARIFF_RATES IS 'Актуальные тарифы. Иерархия: Маршрут → Зона → Перевозчик → Система.';
COMMENT
ON TABLE ASOP_FARE_ZONES IS 'Географические тарифные пояса.';
COMMENT
ON TABLE ASOP_TRANSPORT_STOPS IS 'Остановочные пункты.';
COMMENT
ON TABLE ASOP_ROUTE_TRANSPORT_STOPS IS 'Порядок следования остановок.';
COMMENT
ON TABLE ASOP_TRANSPORT_STOP_ZONES IS 'Карта принадлежности остановок к зонам.';
COMMENT
ON TABLE ASOP_BLACKLISTS IS 'Список недействительных карт.';
COMMENT
ON TABLE ASOP_ROUTES IS 'Маршрутная сеть. BENEFIT_POLICY управляет доступностью субсидий.';
COMMENT
ON TABLE ASOP_BENEFITS IS 'Справочник государственных программ поддержки.';
COMMENT
ON TABLE ASOP_BENEFIT_STEPS IS 'Параметры ступенчатого расчёта скидок.';
COMMENT
ON TABLE ASOP_USER_BENEFITS IS 'Персональные льготы пассажиров.';
COMMENT
ON TABLE ASOP_ROUTE_BENEFITS IS 'Разрешённые льготы для маршрутов с политикой ALLOWLIST.';