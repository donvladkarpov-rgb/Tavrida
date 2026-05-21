-- ============================================================
-- FILE: asop_schema.sql
-- DATABASE: TAVRIDA (PostgreSQL)
-- ОПИСАНИЕ: Схема БД АСОП. Префикс ASOP_, единственное число, UPPER_CASE
--           Сессии: OPENED_BY/CLOSED_BY nullable
--           Тарифы: ASOP_TARIFF_RATE (base + carrier-specific)
--           Гео: полигоны зон и остановок, поддержка больших контуров
-- ============================================================

CREATE EXTENSION IF NOT EXISTS postgis;

-- ========================
-- 1. СПРАВОЧНИКИ
-- ========================

CREATE TABLE ASOP_ROLE (
                           ROLE_ID INT NOT NULL,
                           ROLE_NAME VARCHAR(255) NOT NULL,
                           CONSTRAINT pk_role PRIMARY KEY (ROLE_ID)
);
INSERT INTO ASOP_ROLE (ROLE_ID, ROLE_NAME) VALUES
                                               (1, 'Администратор'), (2, 'Администратор перевозчика'), (3, 'Кассир'),
                                               (4, 'Водитель'), (5, 'Диспетчер'), (6, 'Контролер'), (7, 'Ревизор');

CREATE TABLE ASOP_CARD_TYPE (
                                CARD_TYPE_ID INT NOT NULL,
                                CARD_TYPE_NAME VARCHAR(255) NOT NULL,
                                CONSTRAINT pk_card_type PRIMARY KEY (CARD_TYPE_ID)
);
INSERT INTO ASOP_CARD_TYPE (CARD_TYPE_ID, CARD_TYPE_NAME) VALUES
                                                              (1, 'MIFARE DESFire EV3'), (2, 'Bank Card (EMV)'), (3, 'QR/Virtual'), (4, 'Employee');

CREATE TABLE ASOP_TARIFF_TYPE (
                                  TARIFF_TYPE_ID INT NOT NULL,
                                  CODE VARCHAR(50) NOT NULL,
                                  NAME VARCHAR(100) NOT NULL,
                                  DESCRIPTION TEXT,
                                  CONSTRAINT pk_tariff_type PRIMARY KEY (TARIFF_TYPE_ID),
                                  CONSTRAINT uq_tariff_type_code UNIQUE (CODE)
);
INSERT INTO ASOP_TARIFF_TYPE (TARIFF_TYPE_ID, CODE, NAME, DESCRIPTION) VALUES
                                                                           (1, 'RIDES_PACKAGE', 'Пакет поездок до даты', 'Фиксированное количество поездок...'),
                                                                           (2, 'UNLIMITED_TILL_DATE', 'Безлимит до даты', 'Безлимитное количество поездок...'),
                                                                           (3, 'WALLET', 'Пополняемый кошелёк', 'Деньги списываются с баланса...');

CREATE TABLE ASOP_EVENT_TYPE (
                                 EVENT_TYPE CHAR(4) NOT NULL,
                                 EVENT_TYPE_NAME VARCHAR(128) NOT NULL,
                                 CONSTRAINT pk_event_type PRIMARY KEY (EVENT_TYPE)
);
INSERT INTO ASOP_EVENT_TYPE (EVENT_TYPE, EVENT_TYPE_NAME) VALUES
                                                              ('CUSR', 'Создание пользователя'), ('CCRD', 'Создание карты'), ('RPAY', 'Пополнение карты'),
                                                              ('DUSR', 'Удаление пользователя'), ('DCRD', 'Удаление карты'), ('TACT', 'Активация терминала'),
                                                              ('TDEC', 'Деактивация терминала'), ('DOPN', 'Открытие смены водителя'), ('DCLS', 'Закрытие смены водителя'),
                                                              ('ROPN', 'Открыть маршрут водителем'), ('RCLS', 'Закрыть маршрут водителем'), ('TPAY', 'Оплата проезда'),
                                                              ('COPN', 'Открыть смену кассира'), ('CCLS', 'Закрыть смену кассира'), ('OTHR', 'Другое событие'),
                                                              ('SOPN', 'Открытие сессии'), ('SCLS', 'Закрытие сессии');

CREATE TABLE ASOP_TRANSACTION_TYPE (
                                       TRANSACTION_TYPE_ID INT NOT NULL,
                                       TRANSACTION_TYPE_NAME VARCHAR(255) NOT NULL,
                                       CONSTRAINT pk_transaction_type PRIMARY KEY (TRANSACTION_TYPE_ID)
);
INSERT INTO ASOP_TRANSACTION_TYPE (TRANSACTION_TYPE_ID, TRANSACTION_TYPE_NAME) VALUES (1, 'Пополнение'), (2, 'Списание');

CREATE TABLE ASOP_TRANSACTION_RESULT (
                                         TRANSACTION_RESULT_ID INT NOT NULL,
                                         TRANSACTION_RESULT_NAME VARCHAR(255) NOT NULL,
                                         CONSTRAINT pk_transaction_result PRIMARY KEY (TRANSACTION_RESULT_ID)
);
INSERT INTO ASOP_TRANSACTION_RESULT (TRANSACTION_RESULT_ID, TRANSACTION_RESULT_NAME) VALUES
                                                                                         (11, 'Успех'), (12, 'Ошибка - карта не читается'), (13, 'Ошибка - недостаточно средств'), (14, 'Ошибка - карта заблокирована');

CREATE TABLE ASOP_ROUTE_TYPE (
                                 ROUTE_TYPE_ID BIGINT NOT NULL,
                                 ROUTE_TYPE_NAME VARCHAR(16) NOT NULL,
                                 CONSTRAINT pk_route_type PRIMARY KEY (ROUTE_TYPE_ID)
);
INSERT INTO ASOP_ROUTE_TYPE (ROUTE_TYPE_ID, ROUTE_TYPE_NAME) VALUES (1, 'Маршрут'), (2, 'Путь');

-- ========================
-- 1.1 МОДЕЛЬ ТАРИФОВ (БАЗОВЫЕ + КАСТОМНЫЕ ПЕРЕВОЗЧИКА)
-- ========================

CREATE TABLE ASOP_TARIFF_RATE (
                                  TARIFF_RATE_ID BIGSERIAL NOT NULL,
                                  TARIFF_TYPE_ID INT NOT NULL,
                                  CARRIER_ID BIGINT, -- NULL = базовый общесистемный тариф
                                  ZONE_ID BIGINT,    -- NULL = без привязки к тарифной зоне (универсальный)
                                  PRICE NUMERIC(10,2) NOT NULL,
                                  DESCRIPTION TEXT,
                                  IS_ACTIVE BOOLEAN DEFAULT true,
                                  CREATED_AT TIMESTAMP NOT NULL,
                                  UPDATED_AT TIMESTAMP NOT NULL,
                                  CONSTRAINT pk_tariff_rate PRIMARY KEY (TARIFF_RATE_ID),
                                  CONSTRAINT fk_tariff_rate_type FOREIGN KEY (TARIFF_TYPE_ID) REFERENCES ASOP_TARIFF_TYPE(TARIFF_TYPE_ID),
                                  CONSTRAINT fk_tariff_rate_carrier FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIER(CARRIER_ID),
                                  CONSTRAINT fk_tariff_rate_zone FOREIGN KEY (ZONE_ID) REFERENCES ASOP_FARE_ZONE(ZONE_ID)
);
ALTER SEQUENCE asop_tariff_rate_tariff_rate_id_seq RESTART WITH 1000;

CREATE INDEX idx_tariff_rate_type ON ASOP_TARIFF_RATE(TARIFF_TYPE_ID);
CREATE INDEX idx_tariff_rate_carrier ON ASOP_TARIFF_RATE(CARRIER_ID);
CREATE INDEX idx_tariff_rate_active ON ASOP_TARIFF_RATE(IS_ACTIVE) WHERE IS_ACTIVE = true;
COMMENT ON TABLE ASOP_TARIFF_RATE IS 'Ценовые правила тарифов. CARRIER_ID NULL = базовый тариф для всех. Приоритет поиска: 1) Carrier-specific, 2) Base (NULL).';
COMMENT ON COLUMN ASOP_TARIFF_RATE.CARRIER_ID IS 'NULL = общесистемный базовый тариф. NOT NULL = кастомный тариф/цена конкретного перевозчика.';
COMMENT ON COLUMN ASOP_TARIFF_RATE.ZONE_ID IS 'NULL = цена действует во всех зонах. NOT NULL = зональная цена.';

-- ========================
-- 2. БАЗОВЫЕ СУЩНОСТИ
-- ========================

CREATE TABLE ASOP_CARRIER (
                              CARRIER_ID BIGSERIAL NOT NULL,
                              CARRIER_NAME VARCHAR(255) NOT NULL,
                              CONSTRAINT pk_carrier PRIMARY KEY (CARRIER_ID)
);
ALTER SEQUENCE asop_carrier_carrier_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_USER (
                           USER_ID BIGSERIAL NOT NULL,
                           ROLE_ID INT NOT NULL,
                           USER_FIO VARCHAR(255),
                           CARRIER_ID BIGINT,
                           PASSWORD_HASH VARCHAR(255),
                           CONSTRAINT pk_user PRIMARY KEY (USER_ID),
                           CONSTRAINT fk_user_role_id FOREIGN KEY (ROLE_ID) REFERENCES ASOP_ROLE(ROLE_ID),
                           CONSTRAINT fk_user_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIER(CARRIER_ID)
);
ALTER SEQUENCE asop_user_user_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_VEHICLE (
                              VEHICLE_ID BIGSERIAL NOT NULL,
                              VEHICLE_GUID UUID NOT NULL,
                              CARRIER_ID BIGINT NOT NULL,
                              VEHICLE_NUMBER VARCHAR(16) NOT NULL,
                              VEHICLE_NAME VARCHAR(255) NOT NULL,
                              CONSTRAINT pk_vehicle PRIMARY KEY (VEHICLE_ID),
                              CONSTRAINT uq_vehicle_guid UNIQUE (VEHICLE_GUID),
                              CONSTRAINT fk_vehicle_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIER(CARRIER_ID)
);
ALTER SEQUENCE asop_vehicle_vehicle_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_TERMINAL (
                               TERMINAL_ID BIGSERIAL NOT NULL,
                               VEHICLE_ID BIGINT,
                               CARRIER_ID BIGINT,
                               TERMINAL_GUID UUID NOT NULL,
                               TERMINAL_NUMBER VARCHAR(16) NOT NULL,
                               TERMINAL_SERIAL VARCHAR(64) NOT NULL,
                               CONSTRAINT pk_terminal PRIMARY KEY (TERMINAL_ID),
                               CONSTRAINT uq_terminal_guid UNIQUE (TERMINAL_GUID),
                               CONSTRAINT fk_terminal_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIER(CARRIER_ID),
                               CONSTRAINT fk_terminal_vehicle_id FOREIGN KEY (VEHICLE_ID) REFERENCES ASOP_VEHICLE(VEHICLE_ID)
);
ALTER SEQUENCE asop_terminal_terminal_id_seq RESTART WITH 1000;

-- ========================
-- 3. КАРТЫ (ЯДРО + СПЕЦИФИКА)
-- ========================

CREATE TABLE ASOP_CARD (
                           CARD_ID BIGSERIAL NOT NULL,
                           CARD_GUID UUID NOT NULL,
                           CARD_TYPE_ID INT NOT NULL,
                           USER_ID BIGINT,
                           STATUS VARCHAR(20) DEFAULT 'ACTIVE',
                           ISSUE_DATE TIMESTAMP NOT NULL,
                           EXPIRATION_DATE TIMESTAMP NOT NULL,
                           CREATED_AT TIMESTAMP NOT NULL,
                           UPDATED_AT TIMESTAMP NOT NULL,
                           CONSTRAINT pk_card PRIMARY KEY (CARD_ID),
                           CONSTRAINT uq_card_guid UNIQUE (CARD_GUID),
                           CONSTRAINT fk_card_type_id FOREIGN KEY (CARD_TYPE_ID) REFERENCES ASOP_CARD_TYPE(CARD_TYPE_ID),
                           CONSTRAINT fk_card_user_id FOREIGN KEY (USER_ID) REFERENCES ASOP_USER(USER_ID)
);
ALTER SEQUENCE asop_card_card_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_CARD_MIFARE (
                                  CARD_ID BIGINT NOT NULL,
                                  UID BYTEA NOT NULL,
                                  ATQA SMALLINT,
                                  SAK SMALLINT,
                                  PROTOCOL_VERSION INT,
                                  MEMORY_MAP JSONB,
                                  CONSTRAINT pk_card_mifare PRIMARY KEY (CARD_ID),
                                  CONSTRAINT uq_mifare_uid UNIQUE (UID),
                                  CONSTRAINT fk_mifare_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARD(CARD_ID) ON DELETE CASCADE
);

COMMENT ON COLUMN ASOP_CARD_MIFARE.CARD_ID IS 'FK на ASOP_CARD. Связь 1:1 (ядро карты + техническая спецификация).';
COMMENT ON COLUMN ASOP_CARD_MIFARE.UID IS 'Аппаратный UID чипа (7-10 байт). Используется для быстрой проверки в ЧС, защиты от клонов и логирования прикладываний без расшифровки секторов.';
COMMENT ON COLUMN ASOP_CARD_MIFARE.ATQA IS 'Answer To Question (ISO/IEC 14443-3). Определяет тип NFC-карты и выбирает протокол аутентификации терминалом.';
COMMENT ON COLUMN ASOP_CARD_MIFARE.SAK IS 'Select Acknowledge. Вместе с ATQA однозначно определяет поколение чипа (DESFire EV2/EV3, Classic и т.д.).';
COMMENT ON COLUMN ASOP_CARD_MIFARE.PROTOCOL_VERSION IS 'Версия ОС/крипто-стэка чипа. Позволяет терминалу отклонить устаревшие или небезопасные прошивки.';
COMMENT ON COLUMN ASOP_CARD_MIFARE.MEMORY_MAP IS 'Логическая структура приложений и файлов чипа (JSON). Хранит номера тарифных приложений, настройки офлайн-валидации и метаданные provisioning.';

CREATE TABLE ASOP_CARD_BANK (
                                CARD_ID BIGINT NOT NULL,
                                PAN_TOKEN VARCHAR(256) NOT NULL,
                                PAN_LAST4 CHAR(4),
                                BIN CHAR(6),
                                EXPIRY_MONTH CHAR(2),
                                EXPIRY_YEAR CHAR(4),
                                ISSUER_NAME VARCHAR(100),
                                IS_TOKENIZED BOOLEAN DEFAULT false,
                                CONSTRAINT pk_card_bank PRIMARY KEY (CARD_ID),
                                CONSTRAINT fk_bank_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARD(CARD_ID) ON DELETE CASCADE
);

COMMENT ON COLUMN ASOP_CARD_BANK.CARD_ID IS 'FK на ASOP_CARD. Связь 1:1 (ядро карты + платежная спецификация).';
COMMENT ON COLUMN ASOP_CARD_BANK.PAN_TOKEN IS 'Зашифрованный PAN или платежный токен (Network Token / KMS ciphertext). Никогда не хранить номер карты в открытом виде. Соответствует PCI DSS.';
COMMENT ON COLUMN ASOP_CARD_BANK.PAN_LAST4 IS 'Последние 4 цифры PAN. Единственное поле, разрешенное PCI DSS для отображения в UI, чеках и саппорте (например, **** 4242).';
COMMENT ON COLUMN ASOP_CARD_BANK.BIN IS 'Bank Identification Number (первые 6 цифр). Используется для маршрутизации эквайринга, применения партнерских тарифов и фильтрации неподдерживаемых карт.';
COMMENT ON COLUMN ASOP_CARD_BANK.EXPIRY_MONTH IS 'Месяц истечения срока действия (01-12). Раздельное хранение ускоряет индексацию и валидацию.';
COMMENT ON COLUMN ASOP_CARD_BANK.EXPIRY_YEAR IS 'Год истечения срока действия (YYYY).';
COMMENT ON COLUMN ASOP_CARD_BANK.ISSUER_NAME IS 'Наименование банка-эмитента. Заполняется автоматически при привязке через платежный шлюз или BIN-базу.';
COMMENT ON COLUMN ASOP_CARD_BANK.IS_TOKENIZED IS 'Флаг использования цифрового кошелька (Apple/Google/Mir Pay). При TRUE терминал проверяет платежную криптограмму, а не магнитную полосу/EMV-чип.';

CREATE TABLE ASOP_CARD_TARIFF (
                                  CARD_TARIFF_ID BIGSERIAL NOT NULL,
                                  CARD_ID BIGINT NOT NULL,
                                  TARIFF_TYPE_ID INT NOT NULL,
                                  BALANCE NUMERIC(10,2),
                                  TRAVEL_COUNT INT,
                                  MAX_TRAVEL_COUNT INT,
                                  EXPIRATION_DATE TIMESTAMP,
                                  ACTIVATED_AT TIMESTAMP,
                                  PURCHASE_TRANSACTION_ID BIGINT,
                                  IS_ACTIVE BOOLEAN DEFAULT true,
                                  CREATED_AT TIMESTAMP NOT NULL,
                                  UPDATED_AT TIMESTAMP NOT NULL,
                                  CONSTRAINT pk_card_tariff PRIMARY KEY (CARD_TARIFF_ID),
                                  CONSTRAINT fk_card_tariff_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARD(CARD_ID) ON DELETE CASCADE,
                                  CONSTRAINT fk_card_tariff_type FOREIGN KEY (TARIFF_TYPE_ID) REFERENCES ASOP_TARIFF_TYPE(TARIFF_TYPE_ID)
);
ALTER SEQUENCE asop_card_tariff_card_tariff_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_BLACKLIST (
                                CARD_GUID UUID NOT NULL,
                                CONSTRAINT pk_blacklist PRIMARY KEY (CARD_GUID),
                                CONSTRAINT fk_blacklist_card FOREIGN KEY (CARD_GUID) REFERENCES ASOP_CARD(CARD_GUID)
);

-- ========================
-- 4. СЕССИИ, СОБЫТИЯ, ТРАНЗАКЦИИ
-- ========================

CREATE TABLE ASOP_USER_SESSION (
                                   SESSION_ID UUID NOT NULL,
                                   TERMINAL_ID BIGINT NOT NULL,
                                   OPENED_BY_USER_ID BIGINT,  -- Кто открыл сессию
                                   CLOSED_BY_USER_ID BIGINT,  -- Кто закрыл сессию (может отличаться)
                                   CARD_ID BIGINT,
                                   STARTED_AT TIMESTAMP NOT NULL,
                                   CLOSED_AT TIMESTAMP,
                                   STARTED_AT_LOCAL TIMESTAMP NOT NULL,
                                   CLOSED_AT_LOCAL TIMESTAMP,
                                   EXPIRATION_TIME TIMESTAMP NOT NULL,
                                   CONSTRAINT pk_user_session PRIMARY KEY (SESSION_ID),
                                   CONSTRAINT fk_session_terminal_id FOREIGN KEY (TERMINAL_ID) REFERENCES ASOP_TERMINAL(TERMINAL_ID),
                                   CONSTRAINT fk_session_opened_by FOREIGN KEY (OPENED_BY_USER_ID) REFERENCES ASOP_USER(USER_ID) ON DELETE SET NULL,
                                   CONSTRAINT fk_session_closed_by FOREIGN KEY (CLOSED_BY_USER_ID) REFERENCES ASOP_USER(USER_ID) ON DELETE SET NULL,
                                   CONSTRAINT fk_session_card_id FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARD(CARD_ID)
);

CREATE TABLE ASOP_EVENT (
                            EVENT_ID BIGSERIAL NOT NULL,
                            EVENT_TIME TIMESTAMP NOT NULL,
                            EVENT_LOCAL_TIME TIMESTAMP NOT NULL,
                            EVENT_TYPE CHAR(4) NOT NULL,
                            USER_ID BIGINT,            -- Кто выполнил конкретное действие
                            SESSION_ID UUID,
                            REFERENCE_TYPE_ID INT,
                            REFERENCE_ID BIGINT,
                            EVENT_DETAILS VARCHAR(256),
                            EVENT_OBJECT JSONB,
                            CONSTRAINT pk_event PRIMARY KEY (EVENT_ID),
                            CONSTRAINT fk_event_type FOREIGN KEY (EVENT_TYPE) REFERENCES ASOP_EVENT_TYPE(EVENT_TYPE),
                            CONSTRAINT fk_event_session_id FOREIGN KEY (SESSION_ID) REFERENCES ASOP_USER_SESSION(SESSION_ID),
                            CONSTRAINT fk_event_user_id FOREIGN KEY (USER_ID) REFERENCES ASOP_USER(USER_ID)
);
ALTER SEQUENCE asop_event_event_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_TRANSACTION (
                                  TRANSACTION_ID BIGSERIAL NOT NULL,
                                  TRANSACTION_TIME TIMESTAMP NOT NULL,
                                  CARD_ID BIGINT NOT NULL,
                                  CARD_TARIFF_ID BIGINT,
                                  BALANCE_BEFORE NUMERIC(19,2),
                                  BALANCE_AFTER NUMERIC(19,2),
                                  TRANSACTION_TYPE_ID INT NOT NULL,
                                  TRANSACTION_RESULT_ID INT NOT NULL,
                                  TERMINAL_ID BIGINT NOT NULL,
                                  CONSTRAINT pk_transaction PRIMARY KEY (TRANSACTION_ID),
                                  CONSTRAINT fk_transaction_card FOREIGN KEY (CARD_ID) REFERENCES ASOP_CARD(CARD_ID),
                                  CONSTRAINT fk_transaction_tariff FOREIGN KEY (CARD_TARIFF_ID) REFERENCES ASOP_CARD_TARIFF(CARD_TARIFF_ID),
                                  CONSTRAINT fk_transaction_terminal FOREIGN KEY (TERMINAL_ID) REFERENCES ASOP_TERMINAL(TERMINAL_ID),
                                  CONSTRAINT fk_transaction_type FOREIGN KEY (TRANSACTION_TYPE_ID) REFERENCES ASOP_TRANSACTION_TYPE(TRANSACTION_TYPE_ID),
                                  CONSTRAINT fk_transaction_result FOREIGN KEY (TRANSACTION_RESULT_ID) REFERENCES ASOP_TRANSACTION_RESULT(TRANSACTION_RESULT_ID)
);
ALTER SEQUENCE asop_transaction_transaction_id_seq RESTART WITH 1000;

ALTER TABLE ASOP_CARD_TARIFF
    ADD CONSTRAINT fk_tariff_purchase FOREIGN KEY (PURCHASE_TRANSACTION_ID) REFERENCES ASOP_TRANSACTION(TRANSACTION_ID);

-- ========================
-- 5. МАРШРУТЫ И ГЕО
-- ========================

CREATE TABLE ASOP_ROUTE (
                            ROUTE_ID BIGSERIAL NOT NULL,
                            ROUTE_GUID UUID NOT NULL,
                            ROUTE_TYPE_ID BIGINT NOT NULL,
                            PARENT_ROUTE_ID BIGINT,
                            ROUTE_NAME VARCHAR(128) NOT NULL,
                            DESCRIPTION VARCHAR(512),
                            ROUTE_OBJECT JSONB,
                            CREATED_AT TIMESTAMP NOT NULL,
                            UPDATED_AT TIMESTAMP NOT NULL,
                            CONSTRAINT pk_route PRIMARY KEY (ROUTE_ID),
                            CONSTRAINT uq_route_guid UNIQUE (ROUTE_GUID),
                            CONSTRAINT fk_route_type_id FOREIGN KEY (ROUTE_TYPE_ID) REFERENCES ASOP_ROUTE_TYPE(ROUTE_TYPE_ID),
                            CONSTRAINT fk_route_parent_id FOREIGN KEY (PARENT_ROUTE_ID) REFERENCES ASOP_ROUTE(ROUTE_ID)
);
ALTER SEQUENCE asop_route_route_id_seq RESTART WITH 1000;

CREATE TABLE ASOP_CARRIER_ROUTE (
                                    CARRIER_ROUTE_ID BIGSERIAL NOT NULL,
                                    CARRIER_ID BIGINT NOT NULL,
                                    ROUTE_ID BIGINT NOT NULL,
                                    CREATED_AT TIMESTAMP NOT NULL,
                                    UPDATED_AT TIMESTAMP NOT NULL,
                                    CONSTRAINT pk_carrier_route PRIMARY KEY (CARRIER_ROUTE_ID),
                                    CONSTRAINT fk_carrier_route_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIER(CARRIER_ID),
                                    CONSTRAINT fk_carrier_route_route_id FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTE(ROUTE_ID)
);
ALTER SEQUENCE asop_carrier_route_carrier_route_id_seq RESTART WITH 1000;
CREATE UNIQUE INDEX uk_carrier_route ON ASOP_CARRIER_ROUTE(CARRIER_ID, ROUTE_ID);

-- ТАРИФНЫЕ ЗОНЫ (ОБНОВЛЕНО: добавлен полигон границы)
CREATE TABLE ASOP_FARE_ZONE (
                                ZONE_ID BIGSERIAL NOT NULL,
                                ZONE_CODE VARCHAR(20) NOT NULL,
                                ZONE_NAME VARCHAR(100) NOT NULL,
                                DESCRIPTION VARCHAR(256),
                                ZONE_POLYGON GEOGRAPHY(POLYGON, 4326),
                                CREATED_AT TIMESTAMP NOT NULL,
                                UPDATED_AT TIMESTAMP NOT NULL,
                                CONSTRAINT pk_fare_zone PRIMARY KEY (ZONE_ID),
                                CONSTRAINT uq_fare_zone_code UNIQUE (ZONE_CODE),
                                CONSTRAINT chk_fare_zone_valid_polygon CHECK (ZONE_POLYGON IS NULL OR ST_IsValid(ZONE_POLYGON))
);
ALTER SEQUENCE asop_fare_zone_zone_id_seq RESTART WITH 1000;
CREATE INDEX idx_fare_zone_geo ON ASOP_FARE_ZONE USING GIST (ZONE_POLYGON);
COMMENT ON COLUMN ASOP_FARE_ZONE.ZONE_POLYGON IS 'Граница тарифной зоны (POLYGON). Поддерживает сотни/тысячи точек. Первая и последняя координаты должны совпадать.';

CREATE TABLE ASOP_CARRIER_ZONE (
                                   CARRIER_ZONE_ID BIGSERIAL NOT NULL,
                                   CARRIER_ID BIGINT NOT NULL,
                                   ZONE_ID BIGINT NOT NULL,
                                   BASE_FARE DECIMAL(10,2) NOT NULL,
                                   CREATED_AT TIMESTAMP NOT NULL,
                                   UPDATED_AT TIMESTAMP NOT NULL,
                                   CONSTRAINT pk_carrier_zone PRIMARY KEY (CARRIER_ZONE_ID),
                                   CONSTRAINT fk_carrier_zone_carrier_id FOREIGN KEY (CARRIER_ID) REFERENCES ASOP_CARRIER(CARRIER_ID),
                                   CONSTRAINT fk_carrier_zone_zone_id FOREIGN KEY (ZONE_ID) REFERENCES ASOP_FARE_ZONE(ZONE_ID)
);
ALTER SEQUENCE asop_carrier_zone_carrier_zone_id_seq RESTART WITH 1000;
CREATE UNIQUE INDEX uk_carrier_zone ON ASOP_CARRIER_ZONE(CARRIER_ID, ZONE_ID);
COMMENT ON TABLE ASOP_CARRIER_ZONE IS 'Базовая зональная стоимость поездки. Используется как fallback, если для тарифа нет записи в ASOP_TARIFF_RATE.';

CREATE TABLE ASOP_STOP (
                           STOP_ID BIGSERIAL NOT NULL,
                           FARE_ZONE_ID BIGINT,
                           STOP_CODE VARCHAR(20) NOT NULL,
                           STOP_NAME VARCHAR(200) NOT NULL,
                           STOP_ADDRESS VARCHAR(500),
                           ZONE_POLYGON GEOGRAPHY(POLYGON, 4326),
                           DESCRIPTION TEXT,
                           IS_ACTIVE BOOLEAN DEFAULT true,
                           CREATED_AT TIMESTAMP NOT NULL,
                           UPDATED_AT TIMESTAMP NOT NULL,
                           CONSTRAINT pk_stop PRIMARY KEY (STOP_ID),
                           CONSTRAINT uq_stop_code UNIQUE (STOP_CODE),
                           CONSTRAINT fk_stop_zone_id FOREIGN KEY (FARE_ZONE_ID) REFERENCES ASOP_FARE_ZONE(ZONE_ID)
);
ALTER SEQUENCE asop_stop_stop_id_seq RESTART WITH 1000;
CREATE INDEX idx_stop_geo ON ASOP_STOP USING GIST (ZONE_POLYGON);
COMMENT ON COLUMN ASOP_STOP.ZONE_POLYGON IS 'Геозона остановки (POLYGON). Минимум 4 точки: первая и последняя должны совпадать для замыкания контура.';

CREATE TABLE ASOP_ROUTE_STOP (
                                 ROUTE_STOP_ID BIGSERIAL NOT NULL,
                                 STOP_ID BIGINT NOT NULL,
                                 ROUTE_ID BIGINT NOT NULL,
                                 SERIAL_NUMBER INT NOT NULL,
                                 CREATED_AT TIMESTAMP NOT NULL,
                                 UPDATED_AT TIMESTAMP NOT NULL,
                                 CONSTRAINT pk_route_stop PRIMARY KEY (ROUTE_STOP_ID),
                                 CONSTRAINT fk_route_stop_stop_id FOREIGN KEY (STOP_ID) REFERENCES ASOP_STOP(STOP_ID),
                                 CONSTRAINT fk_route_stop_route_id FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTE(ROUTE_ID)
);
ALTER SEQUENCE asop_route_stop_route_stop_id_seq RESTART WITH 1000;
CREATE UNIQUE INDEX uk_route_stop ON ASOP_ROUTE_STOP(ROUTE_ID, STOP_ID);

CREATE TABLE ASOP_TRIP (
                           TRIP_ID BIGSERIAL NOT NULL,
                           ROUTE_ID BIGINT NOT NULL,
                           SESSION_ID UUID NOT NULL,
                           STARTED_AT TIMESTAMP NOT NULL,
                           CLOSED_AT TIMESTAMP,
                           STARTED_AT_LOCAL TIMESTAMP NOT NULL,
                           CLOSED_AT_LOCAL TIMESTAMP,
                           CONSTRAINT pk_trip PRIMARY KEY (TRIP_ID),
                           CONSTRAINT fk_trip_route_id FOREIGN KEY (ROUTE_ID) REFERENCES ASOP_ROUTE(ROUTE_ID),
                           CONSTRAINT fk_trip_session_id FOREIGN KEY (SESSION_ID) REFERENCES ASOP_USER_SESSION(SESSION_ID)
);

CREATE TABLE ASOP_STOP_ZONE (
                                STOP_ZONE_ID BIGSERIAL NOT NULL,
                                STOP_ID BIGINT NOT NULL,
                                ZONE_ID BIGINT NOT NULL,
                                CREATED_AT TIMESTAMP NOT NULL,
                                UPDATED_AT TIMESTAMP NOT NULL,
                                CONSTRAINT pk_stop_zone PRIMARY KEY (STOP_ZONE_ID),
                                CONSTRAINT fk_stop_zone_stop_id FOREIGN KEY (STOP_ID) REFERENCES ASOP_STOP(STOP_ID),
                                CONSTRAINT fk_stop_zone_zone_id FOREIGN KEY (ZONE_ID) REFERENCES ASOP_FARE_ZONE(ZONE_ID)
);
ALTER SEQUENCE asop_stop_zone_stop_zone_id_seq RESTART WITH 1000;
CREATE UNIQUE INDEX uk_stop_zone ON ASOP_STOP_ZONE(STOP_ID, ZONE_ID);

-- ========================
-- 6. ИНДЕКСЫ
-- ========================
CREATE INDEX idx_card_type_id ON ASOP_CARD(CARD_TYPE_ID);
CREATE INDEX idx_card_user_id ON ASOP_CARD(USER_ID);
CREATE INDEX idx_card_status ON ASOP_CARD(STATUS);
CREATE INDEX idx_card_mifare_uid ON ASOP_CARD_MIFARE(UID);
CREATE INDEX idx_card_bank_token ON ASOP_CARD_BANK(PAN_TOKEN);
CREATE INDEX idx_card_tariff_card ON ASOP_CARD_TARIFF(CARD_ID);
CREATE INDEX idx_card_tariff_type ON ASOP_CARD_TARIFF(TARIFF_TYPE_ID);
CREATE INDEX idx_card_tariff_expiry ON ASOP_CARD_TARIFF(EXPIRATION_DATE) WHERE IS_ACTIVE = true;
CREATE INDEX idx_blacklist_guid ON ASOP_BLACKLIST(CARD_GUID);
CREATE INDEX idx_terminal_guid ON ASOP_TERMINAL(TERMINAL_GUID);
CREATE INDEX idx_terminal_carrier_id ON ASOP_TERMINAL(CARRIER_ID);
CREATE INDEX idx_terminal_vehicle_id ON ASOP_TERMINAL(VEHICLE_ID);
CREATE INDEX idx_session_opened_by ON ASOP_USER_SESSION(OPENED_BY_USER_ID);
CREATE INDEX idx_session_closed_by ON ASOP_USER_SESSION(CLOSED_BY_USER_ID);
CREATE INDEX idx_session_terminal ON ASOP_USER_SESSION(TERMINAL_ID);
CREATE INDEX idx_session_started ON ASOP_USER_SESSION(STARTED_AT);
CREATE INDEX idx_session_card ON ASOP_USER_SESSION(CARD_ID);
CREATE INDEX idx_event_time ON ASOP_EVENT(EVENT_TIME, USER_ID);
CREATE INDEX idx_event_local_time ON ASOP_EVENT(EVENT_LOCAL_TIME, USER_ID);
CREATE INDEX idx_event_user_id ON ASOP_EVENT(USER_ID, SESSION_ID);
CREATE INDEX idx_event_session_id ON ASOP_EVENT(SESSION_ID);
CREATE INDEX idx_event_type_id ON ASOP_EVENT(EVENT_TYPE);
CREATE INDEX idx_transaction_card_id ON ASOP_TRANSACTION(CARD_ID);
CREATE INDEX idx_transaction_tariff_id ON ASOP_TRANSACTION(CARD_TARIFF_ID);
CREATE INDEX idx_transaction_terminal_id ON ASOP_TRANSACTION(TERMINAL_ID);
CREATE INDEX idx_transaction_type_id ON ASOP_TRANSACTION(TRANSACTION_TYPE_ID);
CREATE INDEX idx_transaction_result_id ON ASOP_TRANSACTION(TRANSACTION_RESULT_ID);
CREATE INDEX idx_route_guid ON ASOP_ROUTE(ROUTE_GUID);
CREATE INDEX idx_route_parent_id ON ASOP_ROUTE(PARENT_ROUTE_ID);
CREATE INDEX idx_route_created_at ON ASOP_ROUTE(CREATED_AT);
CREATE INDEX idx_carrier_route_carrier_id ON ASOP_CARRIER_ROUTE(CARRIER_ID);
CREATE INDEX idx_carrier_route_route_id ON ASOP_CARRIER_ROUTE(ROUTE_ID);
CREATE INDEX idx_carrier_zone_carrier_id ON ASOP_CARRIER_ZONE(CARRIER_ID);
CREATE INDEX idx_carrier_zone_zone_id ON ASOP_CARRIER_ZONE(ZONE_ID);
CREATE INDEX idx_stop_zone_id ON ASOP_STOP(FARE_ZONE_ID);
CREATE INDEX idx_stop_created_at ON ASOP_STOP(CREATED_AT);
CREATE INDEX idx_route_stop_created_at ON ASOP_ROUTE_STOP(CREATED_AT);
CREATE INDEX idx_trip_started_at ON ASOP_TRIP(STARTED_AT);
CREATE INDEX idx_trip_started_at_local ON ASOP_TRIP(STARTED_AT_LOCAL);
CREATE INDEX idx_trip_route_id ON ASOP_TRIP(ROUTE_ID);
CREATE INDEX idx_trip_session_id ON ASOP_TRIP(SESSION_ID);
CREATE INDEX idx_stop_zone_created_at ON ASOP_STOP_ZONE(CREATED_AT);

-- ========================
-- 7. ДОКУМЕНТАЦИЯ
-- ========================
COMMENT ON TABLE ASOP_EVENT IS 'Таблица для хранения сервисных событий. EVENT_OBJECT (JSONB) хранит структурированные данные.';
COMMENT ON TABLE ASOP_TRANSACTION IS 'Финансовые и тарифные проводки. Заменяет понятие Payment, покрывая пополнения, списания, холдирования и возвраты.';
COMMENT ON TABLE ASOP_CARD_MIFARE IS 'Техническая спецификация NFC-карт (MIFARE DESFire/Classic). Содержит аппаратные метаданные чипа.';
COMMENT ON TABLE ASOP_CARD_BANK IS 'Спецификация банковских/EMV карт. PAN хранится только в зашифрованном виде (токен/криптотекст).';
COMMENT ON TABLE ASOP_USER_SESSION IS 'Сессии терминалов. OPENED_BY/CLOSED_BY nullable. Позволяет разным пользователям открывать/закрывать одну сессию. События логируются в ASOP_EVENT.';
COMMENT ON TABLE ASOP_TARIFF_RATE IS 'Ценовые правила тарифов. CARRIER_ID NULL = базовый тариф для всех. Приоритет поиска: 1) Carrier-specific, 2) Base (NULL).';
COMMENT ON TABLE ASOP_FARE_ZONE IS 'Тарифные зоны. ZONE_POLYGON хранит географические границы (сотни точек допустимо). GIST-индекс ускоряет пространственные запросы.';
