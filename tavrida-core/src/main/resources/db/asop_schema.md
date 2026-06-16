# Полная документация схемы базы данных АСОП (TAVRIDA)

> **Назначение:** Высокоуровневая карта архитектуры базы данных для навигации и детальное описание всех полей.  
> **СУБД:** PostgreSQL 14+ с расширением PostGIS

---

## 📑 Оглавление

### [0. Регионы и Территории (ФИАС/ГАР)](#0-регионы-и-территории-фиасгар)
* [`ASOP_REGIONS`](#asop_regions)
* [`ASOP_TERRITORIES`](#asop_territories)
* [`ASOP_ORGANIZERS`](#asop_organizers)
* [`ASOP_ORGANIZER_TERRITORIES`](#asop_organizer_territories)

### [1. Справочники](#1-справочники)
* [`ASOP_ROLES`](#asop_roles)
* [`ASOP_CARD_TYPES`](#asop_card_types)
* [`ASOP_TARIFF_TYPES`](#asop_tariff_types)
* [`ASOP_SESSION_TYPES`](#asop_session_types)
* [`ASOP_EVENT_TYPES`](#asop_event_types)
* [`ASOP_TRANSACTION_TYPES`](#asop_transaction_types)
* [`ASOP_TRANSACTION_RESULTS`](#asop_transaction_results)
* [`ASOP_SERVICES`](#asop_services)

### [2. Маршруты и Пути](#2-маршруты-и-пути)
* [`ASOP_ROUTES`](#asop_routes)
* [`ASOP_PATHS`](#asop_paths)
* [`ASOP_FARE_ZONES`](#asop_fare_zones)
* [`ASOP_TRANSPORT_STOPS`](#asop_transport_stops)
* [`ASOP_PATH_TRANSPORT_STOPS`](#asop_path_transport_stops)
* [`ASOP_SCHEDULE`](#asop_schedule)

### [3. Перевозчики, Договоры и ТС](#3-перевозчики-договоры-и-тс)
* [`ASOP_CARRIERS`](#asop_carriers)
* [`ASOP_CARRIER_CONTRACTS`](#asop_carrier_contracts)
* [`ASOP_CONTRACT_ROUTES`](#asop_contract_routes)
* [`ASOP_VEHICLE_TYPES`](#asop_vehicle_types)
* [`ASOP_VEHICLE_MODELS`](#asop_vehicle_models)
* [`ASOP_VEHICLES`](#asop_vehicles)

### [4. Пользователи и Безопасность](#4-пользователи-и-безопасность)
* [`ASOP_USERS`](#asop_users)
* [`ASOP_USER_ROLES`](#asop_user_roles)
* [`ASOP_USER_CARRIERS`](#asop_user_carriers)
* [`ASOP_USER_REGIONS`](#asop_user_regions)

### [5. Оборудование: Терминалы, TID, Профили, ПО](#5-оборудование-терминалы-tid-профили-по)
* [`ASOP_TERMINAL_PROFILES`](#asop_terminal_profiles)
* [`ASOP_TERMINAL_SOFTWARE`](#asop_terminal_software)
* [`ASOP_TIDS`](#asop_tids)
* [`ASOP_TERMINALS`](#asop_terminals)

### [6. Карты, Льготы, Тарифы и Платежи](#6-карты-льготы-тарифы-и-платежи)
* [`ASOP_CARDS`](#asop_cards)
* [`ASOP_CARD_MIFARES`](#asop_card_mifares)
* [`ASOP_CARD_BANKS`](#asop_card_banks)
* [`ASOP_CARD_TARIFFS`](#asop_card_tariffs)
* [`ASOP_BLACKLISTS`](#asop_blacklists)
* [`ASOP_BENEFITS`](#asop_benefits)
* [`ASOP_BENEFIT_STEPS`](#asop_benefit_steps)
* [`ASOP_USER_BENEFITS`](#asop_user_benefits)
* [`ASOP_TARIFF_RATES`](#asop_tariff_rates)
* [`ASOP_PAYMENTS`](#asop_payments) *(Новая: таблица поступлений/пополнений)*

#### [6.1 Ценообразование](#61-ценообразование)
* [`ASOP_PATH_SERVICES`](#asop_path_services)
* [`ASOP_PATH_DISCOUNTS`](#asop_path_discounts)
* [`ASOP_PATH_BENEFITS`](#asop_path_benefits)

### [7. Сессии, Транзакции, Аудит, КРС](#7-сессии-транзакции-аудит-крс)
* [`ASOP_SESSIONS`](#asop_sessions)
* [`ASOP_AUDIT_SERVICES`](#asop_audit_services)
* [`ASOP_AUDIT_TASKS`](#asop_audit_tasks)
* [`ASOP_AUDIT_TASK_PATHS`](#asop_audit_task_paths)
* [`ASOP_AUDIT_BRIGADES`](#asop_audit_brigades)
* [`ASOP_AUDIT_BRIGADE_MEMBERS`](#asop_audit_brigade_members)
* [`ASOP_AUDIT_INSPECTIONS`](#asop_audit_inspections)
* [`ASOP_AUDIT_INSPECTION_TASKS`](#asop_audit_inspection_tasks)
* [`ASOP_TRANSACTIONS`](#asop_transactions)
* [`ASOP_TRANSACTION_CARDS`](#asop_transaction_cards)
* [`ASOP_GPS_TRACKING`](#asop_gps_tracking) *(Переименовано из ASOP_DISPATCH_POSITIONS)*
* [`ASOP_EVENTS`](#asop_events)

---

## 0. Регионы и Территории (ФИАС/ГАР)

<a id="asop_regions"></a>
### `ASOP_REGIONS`
Справочник регионов на основе данных ФИАС/ГАР для мультирегионального разделения.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `REGION_ID` | UUID | PK | Первичный ключ региона |
| `MUNICIPAL_DIVISION` | VARCHAR(255) | | Муниципальное деление |
| `ADMIN_DIVISION` | VARCHAR(255) | | Административно-территориальное деление |
| `FEDERAL_DISTRICT` | VARCHAR(255) | | Федеральный округ |
| `IFNS_FL_CODE` | VARCHAR(4) | | Код ИФНС ФЛ |
| `IFNS_UL_CODE` | VARCHAR(4) | | Код ИФНС ЮЛ |
| `OKATO_CODE` | VARCHAR(11) | | Код ОКАТО |
| `OKTMO_CODE` | VARCHAR(11) | | Код ОКТМО |
| `OKTMO_BUDGET_CODE` | VARCHAR(11) | | Код ОКТМО бюджетополучателя |
| `FIAS_ID` | VARCHAR(36) | UNIQUE | Уникальный номер в ГАР (ID FIAS) |
| `REGISTRY_RECORD_ID` | VARCHAR(30) | | Уникальный номер реестровой записи |

<a id="asop_territories"></a>
### `ASOP_TERRITORIES`
Административно-территориальные единицы с гео-полигонами и реквизитами ФИАС.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TERRITORY_ID` | UUID | PK | Первичный ключ территории |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `MUNICIPAL_DIVISION` | VARCHAR(255) | | Муниципальное деление |
| `ADMIN_DIVISION` | VARCHAR(255) | | Административно-территориальное деление |
| `FEDERAL_DISTRICT` | VARCHAR(255) | | Федеральный округ |
| `IFNS_FL_CODE` | VARCHAR(4) | | Код ИФНС ФЛ |
| `IFNS_UL_CODE` | VARCHAR(4) | | Код ИФНС ЮЛ |
| `OKATO_CODE` | VARCHAR(11) | | Код ОКАТО |
| `OKTMO_CODE` | VARCHAR(11) | | Код ОКТМО |
| `OKTMO_BUDGET_CODE` | VARCHAR(11) | | Код ОКТМО бюджетополучателя |
| `FIAS_ID` | VARCHAR(36) | UNIQUE | Уникальный номер в ГАР (ID FIAS) |
| `REGISTRY_RECORD_ID` | VARCHAR(30) | | Уникальный номер реестровой записи |
| `GEO_POLYGON` | GEOGRAPHY | | Географический полигон территории (PostGIS) |

<a id="asop_organizers"></a>
### `ASOP_ORGANIZERS`
Организаторы перевозок.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ORGANIZER_ID` | UUID | PK | Первичный ключ |
| `ORGANIZER_NAME` | VARCHAR(255) | NOT NULL | Наименование |

<a id="asop_organizer_territories"></a>
### `ASOP_ORGANIZER_TERRITORIES`
Связь Многие-ко-многим между организаторами и территориями.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ORGANIZER_ID` | UUID | PK, FK → ASOP_ORGANIZERS | Идентификатор организатора |
| `TERRITORY_ID` | UUID | PK, FK → ASOP_TERRITORIES | Идентификатор территории |

[↑ Наверх](#-оглавление)

---

## 1. Справочники

<a id="asop_roles"></a>
### `ASOP_ROLES`
Роли доступа в системе.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROLE_ID` | UUID | PK | Первичный ключ роли |
| `ROLE_NAME` | VARCHAR(255) | NOT NULL | Наименование роли |

<a id="asop_card_types"></a>
### `ASOP_CARD_TYPES`
Типы провозных носителей.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_TYPE_ID` | UUID | PK | Первичный ключ |
| `CARD_TYPE_NAME` | VARCHAR(255) | NOT NULL | Название типа карты |

<a id="asop_tariff_types"></a>
### `ASOP_TARIFF_TYPES`
Типы тарифов.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TARIFF_TYPE_ID` | UUID | PK | Первичный ключ |
| `CODE` | VARCHAR(50) | UNIQUE, NOT NULL | Системный код |
| `NAME` | VARCHAR(100) | NOT NULL | Наименование |
| `DESCRIPTION` | TEXT | | Описание |

<a id="asop_session_types"></a>
### `ASOP_SESSION_TYPES`
Типы рабочих сессий.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SESSION_TYPE_ID` | UUID | PK | Первичный ключ |
| `SESSION_TYPE_CODE` | VARCHAR(30) | UNIQUE, NOT NULL | Код (DRIVER_SHIFT, PASSENGER_TRIP, KRS_AUDIT) |
| `SESSION_TYPE_NAME` | VARCHAR(100) | NOT NULL | Название |

<a id="asop_event_types"></a>
### `ASOP_EVENT_TYPES`
Типы системных событий для журнала аудита.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `EVENT_TYPE` | CHAR(4) | PK | Системный код (CUSR, TPAY, SOPN) |
| `EVENT_TYPE_NAME` | VARCHAR(128) | NOT NULL | Название |

<a id="asop_transaction_types"></a>
### `ASOP_TRANSACTION_TYPES`
Типы финансовых операций.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_TYPE_ID` | UUID | PK | Первичный ключ |
| `TRANSACTION_TYPE_NAME` | VARCHAR(255) | NOT NULL | Название |

<a id="asop_transaction_results"></a>
### `ASOP_TRANSACTION_RESULTS`
Результаты выполнения транзакций.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_RESULT_ID` | UUID | PK | Первичный ключ |
| `TRANSACTION_RESULT_NAME` | VARCHAR(255) | NOT NULL | Название результата |

<a id="asop_services"></a>
### `ASOP_SERVICES`
Классификатор платных услуг ("Услуга" в чеке).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SERVICE_ID` | UUID | PK | Первичный ключ |
| `SERVICE_NAME` | VARCHAR(100) | NOT NULL | Название услуги |
| `DESCRIPTION` | TEXT | | Описание |
| `PRIORITY` | INT | UNIQUE, NOT NULL | Уникальный приоритет |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

[↑ Наверх](#-оглавление)

---

## 2. Маршруты и Пути

<a id="asop_routes"></a>
### `ASOP_ROUTES`
Справочник маршрутов (номер, название, категория).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_ID` | UUID | PK | Первичный ключ |
| `ROUTE_NUMBER` | VARCHAR(50) | NOT NULL | Номер маршрута |
| `ROUTE_NAME` | VARCHAR(255) | NOT NULL | Название маршрута |
| `ORGANIZER_ID` | UUID | FK → ASOP_ORGANIZERS | Организатор |
| `MINISTRY_REGISTRY_NO` | VARCHAR(50) | | Номер в реестре Минтранса |
| `ROUTE_CATEGORY` | VARCHAR(30) | CHECK | Категория (CITY, SUBURBAN, INTERCITY, EXPRESS) |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

<a id="asop_paths"></a>
### `ASOP_PATHS`
Физические пути (направления) маршрута с начальной и конечной остановками.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PATH_ID` | UUID | PK | Первичный ключ |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL | Ссылка на справочник маршрутов |
| `PATH_NAME` | VARCHAR(100) | NOT NULL | Название пути (напр., "Прямой", "Обратный") |
| `START_STOP_ID` | UUID | FK → ASOP_TRANSPORT_STOPS | Начальная остановка |
| `END_STOP_ID` | UUID | FK → ASOP_TRANSPORT_STOPS | Конечная остановка |
| `ROUTE_OBJECT` | JSONB | | JSON-геометрия/конфиг пути |
| `BENEFIT_POLICY` | VARCHAR(20) | DEFAULT 'ALL', CHECK | Политика льгот |
| `PATH_START_DATE` | TIMESTAMP | | Дата начала действия |
| `PATH_END_DATE` | TIMESTAMP | | Дата окончания действия |
| `DESCRIPTION` | VARCHAR(512) | | Описание |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

<a id="asop_fare_zones"></a>
### `ASOP_FARE_ZONES`
Тарифные зоны.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ZONE_ID` | UUID | PK | Первичный ключ |
| `ZONE_CODE` | VARCHAR(20) | UNIQUE, NOT NULL | Код зоны |
| `ZONE_NAME` | VARCHAR(100) | NOT NULL | Название |
| `DESCRIPTION` | VARCHAR(256) | | Описание |
| `ZONE_POLYGON` | GEOGRAPHY | | Гео-полигон зоны |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

<a id="asop_transport_stops"></a>
### `ASOP_TRANSPORT_STOPS`
Справочник остановок общественного транспорта.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `STOP_ID` | UUID | PK | Первичный ключ |
| `FARE_ZONE_ID` | UUID | FK → ASOP_FARE_ZONES | Тарифная зона |
| `STOP_CODE` | VARCHAR(20) | UNIQUE, NOT NULL | Код остановки |
| `STOP_NAME` | VARCHAR(200) | NOT NULL | Название |
| `STOP_ADDRESS` | VARCHAR(500) | | Адрес |
| `ZONE_POLYGON` | GEOGRAPHY | | Гео-полигон зоны остановки |
| `DESCRIPTION` | TEXT | | Описание |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_path_transport_stops"></a>
### `ASOP_PATH_TRANSPORT_STOPS`
Связь Пути и Остановки (порядок следования).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PATH_STOP_ID` | UUID | PK | Первичный ключ |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL | Путь |
| `STOP_ID` | UUID | FK → ASOP_TRANSPORT_STOPS, NOT NULL | Остановка |
| `SERIAL_NUMBER` | INT | NOT NULL | Порядковый номер |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_schedule"></a>
### `ASOP_SCHEDULE`
Расписание прибытия на остановки по дням недели.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SCHEDULE_ID` | UUID | PK | Первичный ключ |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL, CASCADE | Путь |
| `STOP_ID` | UUID | FK → ASOP_TRANSPORT_STOPS, NOT NULL | Остановка |
| `DAY_MASK` | INT | DEFAULT 127, CHECK 1-127 | Маска дней недели |
| `ARRIVAL_TIME` | TIME | NOT NULL | Время прибытия |
| `DWELL_TIME_SEC` | INT | DEFAULT 30 | Время стоянки (сек) |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |

[↑ Наверх](#-оглавление)

---

## 3. Перевозчики, Договоры и ТС

<a id="asop_carriers"></a>
### `ASOP_CARRIERS`
Перевозчики (транспортные компании, ГУП, ИП).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARRIER_ID` | UUID | PK | Первичный ключ |
| `CARRIER_NAME` | VARCHAR(255) | NOT NULL | Наименование |
| `INN` | VARCHAR(12) | UNIQUE, NOT NULL | ИНН организации |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

<a id="asop_carrier_contracts"></a>
### `ASOP_CARRIER_CONTRACTS`
Договоры перевозчиков.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CONTRACT_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Перевозчик |
| `CONTRACT_NUMBER` | VARCHAR(100) | NOT NULL | Номер договора |
| `START_DATE` | DATE | NOT NULL | Дата начала действия |
| `END_DATE` | DATE | | Дата окончания действия |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

<a id="asop_contract_routes"></a>
### `ASOP_CONTRACT_ROUTES`
Связь договора с маршрутами из справочника.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CONTRACT_ID` | UUID | PK, FK → ASOP_CARRIER_CONTRACTS | Договор |
| `ROUTE_ID` | UUID | PK, FK → ASOP_ROUTES | Маршрут |

<a id="asop_vehicle_types"></a>
### `ASOP_VEHICLE_TYPES`
Справочник типов транспортных средств.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `VEHICLE_TYPE_ID` | UUID | PK | Первичный ключ |
| `TYPE_NAME` | VARCHAR(100) | NOT NULL | Название типа |

<a id="asop_vehicle_models"></a>
### `ASOP_VEHICLE_MODELS`
Справочник моделей транспортных средств.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `VEHICLE_MODEL_ID` | UUID | PK | Первичный ключ |
| `MODEL_NAME` | VARCHAR(255) | NOT NULL | Название модели |

<a id="asop_vehicles"></a>
### `ASOP_VEHICLES`
Транспортные средства.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `VEHICLE_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Перевозчик |
| `VEHICLE_TYPE_ID` | UUID | FK → ASOP_VEHICLE_TYPES, NOT NULL | Тип ТС (справочник) |
| `VEHICLE_MODEL_ID` | UUID | FK → ASOP_VEHICLE_MODELS, NOT NULL | Модель ТС (справочник) |
| `VEHICLE_NUMBER` | VARCHAR(16) | NOT NULL | ГРЗ (гос. регистрационный знак) |
| `VEHICLE_NAME` | VARCHAR(255) | NOT NULL | Внутреннее наименование |

[↑ Наверх](#-оглавление)

---

## 4. Пользователи и Безопасность

<a id="asop_users"></a>
### `ASOP_USERS`
Пользователи системы. ПДн защищены: СНИЛС хэшируется и шифруется, ФИО сокращено, аутентификация через Keycloak.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `USER_ID` | UUID | PK | Первичный ключ |
| `FIRST_NAME` | VARCHAR(100) | NOT NULL | Имя |
| `LAST_NAME_INITIAL` | CHAR(1) | NOT NULL | Первая буква фамилии |
| `PATRONYMIC_INITIAL` | CHAR(1) | | Первая буква отчества |
| `PHONE` | VARCHAR(20) | | Телефон |
| `SNILS_HASH` | VARCHAR(64) | UNIQUE | Хэш СНИЛС (для проверки уникальности без раскрытия ПДн) |
| `SNILS_ENCRYPTED` | BYTEA | | Зашифрованное значение СНИЛС (расшифровка на стороне приложения/KMS) |
| `KEYCLOAK_ID` | VARCHAR(255) | UNIQUE | Идентификатор во внешней системе аутентификации (Keycloak) |

<a id="asop_user_roles"></a>
### `ASOP_USER_ROLES`
Связь пользователей и ролей (Многие-ко-многим).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `USER_ID` | UUID | PK, FK → ASOP_USERS | Пользователь |
| `ROLE_ID` | UUID | PK, FK → ASOP_ROLES | Роль |

<a id="asop_user_carriers"></a>
### `ASOP_USER_CARRIERS`
Связь пользователей и перевозчиков (Многие-ко-многим).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `USER_ID` | UUID | PK, FK → ASOP_USERS | Пользователь |
| `CARRIER_ID` | UUID | PK, FK → ASOP_CARRIERS | Перевозчик |

<a id="asop_user_regions"></a>
### `ASOP_USER_REGIONS`
Связь пользователей и регионов (Многие-ко-многим).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `USER_ID` | UUID | PK, FK → ASOP_USERS | Пользователь |
| `REGION_ID` | UUID | PK, FK → ASOP_REGIONS | Регион |

[↑ Наверх](#-оглавление)

---

## 5. Оборудование: Терминалы, TID, Профили, ПО

<a id="asop_terminal_profiles"></a>
### `ASOP_TERMINAL_PROFILES`
Справочник профилей настроек терминалов.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PROFILE_ID` | UUID | PK | Первичный ключ |
| `PROFILE_NAME` | VARCHAR(100) | UNIQUE, NOT NULL | Имя профиля |
| `PROFILE_PARAMS` | JSONB | | Параметры конфигурации в формате JSON |

<a id="asop_terminal_software"></a>
### `ASOP_TERMINAL_SOFTWARE`
Справочник версий программного обеспечения терминалов.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SOFTWARE_VERSION_ID` | UUID | PK | Первичный ключ |
| `TERMINAL_TYPE` | VARCHAR(100) | NOT NULL | Тип терминала (например, 'Azur', 'Feithen') |
| `VERSION` | VARCHAR(100) | NOT NULL | Версия ПО (например, '2.1.2.bc1a6f5e') |
| `FILE_PATH` | VARCHAR(500) | | Путь к файлу прошивки/ПО |
| `UPDATE_DATE` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_tids"></a>
### `ASOP_TIDS`
Пул эквайринговых идентификаторов терминалов (TID).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TID_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Перевозчик |
| `TERMINAL_ID` | UUID | FK → ASOP_TERMINALS | Терминал |
| `TID_VALUE` | VARCHAR(20) | UNIQUE, NOT NULL | Значение TID от банка |
| `STATUS` | VARCHAR(20) | DEFAULT 'UNUSED', CHECK | Статус (UNUSED, ASSIGNED, REVOKED) |
| `ASSIGNED_AT` | TIMESTAMP | | Дата назначения |
| `UNASSIGNED_AT` | TIMESTAMP | | Дата отзыва |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_terminals"></a>
### `ASOP_TERMINALS`
Терминалы оплаты и валидаторы. Связь с ТС — через это поле (исторически) и через сессию-рейс.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TERMINAL_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Перевозчик |
| `TERMINAL_NUMBER` | VARCHAR(16) | NOT NULL | Инвентарный номер |
| `TERMINAL_SERIAL` | VARCHAR(64) | NOT NULL | Серийный номер (SN) |
| `TERMINAL_MODEL` | VARCHAR(100) | | Модель терминала (AZUR, Feithen и т.п.) |
| `STATUS` | VARCHAR(50) | DEFAULT 'WAREHOUSE' | Статус (WAREHOUSE, ISSUED_TO_ENGINEER, IN_OPERATION, REPAIR) |
| `MOL_USER_ID` | UUID | FK → ASOP_USERS | Материально-ответственное лицо |
| `PARENT_TERMINAL_ID` | UUID | FK → ASOP_TERMINALS | Ссылка на родителя (для валидаторов) |
| `TID_ID` | UUID | FK → ASOP_TIDS | Ссылка на TID (для валидатора всегда NULL) |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES | Ссылка на ТС. Меняется при начале смены, исторически не очищается |
| `PROFILE_ID` | UUID | FK → ASOP_TERMINAL_PROFILES | Ссылка на профиль настроек (для валидатора всегда NULL) |
| `SOFTWARE_VERSION_ID` | UUID | FK → ASOP_TERMINAL_SOFTWARE | Ссылка на версию ПО |
| `BENEFITS_SYNC_TOKEN` | VARCHAR(64) | | Токен синхронизации льгот |
| `LAST_BENEFITS_SYNC_AT` | TIMESTAMP | | Время последней синхронизации |

[↑ Наверх](#-оглавление)

---

## 6. Карты, Льготы, Тарифы и Платежи

<a id="asop_cards"></a>
### `ASOP_CARDS`
Транспортные и банковские карты, зарегистрированные в системе.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK | Первичный ключ |
| `CARD_TYPE_ID` | UUID | FK → ASOP_CARD_TYPES, NOT NULL | Тип карты |
| `USER_ID` | UUID | FK → ASOP_USERS | Владелец |
| `IS_PRIMARY` | BOOLEAN | DEFAULT false | Флаг основной карты (для применения льгот) |
| `LAST_SYNC_RECEIPT_TIME` | INT | DEFAULT 0 | Время (Unix INT) последней успешной синхронизации платежа с чипом карты |
| `REGISTERED_AT` | TIMESTAMP | | Дата регистрации |
| `REGISTERED_BY_USER_ID` | UUID | | Кто зарегистрировал |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_card_mifares"></a>
### `ASOP_CARD_MIFARES`
Технические параметры MIFARE-карт.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK, FK → ASOP_CARDS, CASCADE | Ссылка на карту |
| `UID` | BYTEA | UNIQUE, NOT NULL | Уникальный идентификатор чипа |
| `ATQA` | SMALLINT | | Ответ на запрос типа A |
| `SAK` | SMALLINT | | Код выбора приложения |
| `PROTOCOL_VERSION` | INT | | Версия протокола |
| `MEMORY_MAP` | JSONB | | JSON-карта памяти |

<a id="asop_card_banks"></a>
### `ASOP_CARD_BANKS`
Данные привязанных банковских карт.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK, FK → ASOP_CARDS, CASCADE | Ссылка на карту |
| `PAN_TOKEN` | VARCHAR(256) | NOT NULL | Токенизированный PAN |
| `PAN_LAST4` | CHAR(4) | | Последние 4 цифры |
| `BIN` | CHAR(6) | | BIN-код |
| `IS_TOKENIZED` | BOOLEAN | DEFAULT false | Флаг токенизации |

<a id="asop_card_tariffs"></a>
### `ASOP_CARD_TARIFFS`
Активные тарифы на картах.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_TARIFF_ID` | UUID | PK | Первичный ключ |
| `CARD_ID` | UUID | FK → ASOP_CARDS, NOT NULL, CASCADE | Карта |
| `TARIFF_TYPE_ID` | UUID | FK → ASOP_TARIFF_TYPES, NOT NULL | Тип тарифа |
| `BALANCE` | NUMERIC(10,2) | | Денежный баланс |
| `TRAVEL_COUNT` | INT | | Текущее кол-во поездок |
| `MAX_TRAVEL_COUNT` | INT | | Лимит поездок |
| `EXPIRATION_DATE` | TIMESTAMP | | Дата окончания |
| `ACTIVATED_AT` | TIMESTAMP | | Дата активации |
| `PURCHASE_TRANSACTION_ID` | UUID | FK → ASOP_TRANSACTIONS | Транзакция покупки |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_blacklists"></a>
### `ASOP_BLACKLISTS`
Заблокированные карты.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK, FK → ASOP_CARDS | Карта |
| `BLOCK_TYPE` | VARCHAR(20) | NOT NULL, CHECK | Тип блокировки (PERMANENT, NEGATIVE_BALANCE) |
| `BLOCKED_AT` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP, NOT NULL | Дата блокировки |

<a id="asop_benefits"></a>
### `ASOP_BENEFITS`
Справочник льготных категорий.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `BENEFIT_ID` | UUID | PK | Первичный ключ |
| `BENEFIT_CODE` | VARCHAR(50) | UNIQUE, NOT NULL | Код |
| `BENEFIT_NAME` | VARCHAR(100) | NOT NULL | Название |
| `REGION_CODE` | VARCHAR(50) | NOT NULL | Региональный код |
| `DESCRIPTION` | TEXT | | Описание |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_benefit_steps"></a>
### `ASOP_BENEFIT_STEPS`
Шаги накопительных льгот.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `STEP_ID` | UUID | PK | Первичный ключ |
| `BENEFIT_ID` | UUID | FK → ASOP_BENEFITS, NOT NULL, CASCADE | Льгота |
| `STEP_ORDER` | INT | NOT NULL | Порядок шага |
| `TRIP_THRESHOLD_FROM` | INT | DEFAULT 0, NOT NULL | Нижняя граница |
| `TRIP_THRESHOLD_TO` | INT | | Верхняя граница |
| `DISCOUNT_SHARE` | NUMERIC(4,2) | NOT NULL, CHECK 0-1 | Доля скидки |
| `PERIOD_TYPE` | VARCHAR(20) | DEFAULT 'MONTHLY', NOT NULL | Тип периода |

<a id="asop_user_benefits"></a>
### `ASOP_USER_BENEFITS`
Привязка льгот к пользователям с датами действия.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ASSIGNMENT_ID` | UUID | PK | Первичный ключ |
| `USER_ID` | UUID | FK → ASOP_USERS, NOT NULL, CASCADE | Пользователь |
| `BENEFIT_ID` | UUID | FK → ASOP_BENEFITS, NOT NULL | Льгота |
| `VALID_FROM` | TIMESTAMP | NOT NULL | Дата начала |
| `VALID_UNTIL` | TIMESTAMP | | Дата окончания |
| `SYNC_VERSION` | INT | DEFAULT 1, NOT NULL | Версия синхронизации |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_tariff_rates"></a>
### `ASOP_TARIFF_RATES`
Тарифные ставки (цены) по зонам, путям и перевозчикам.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TARIFF_RATE_ID` | UUID | PK | Первичный ключ |
| `TARIFF_TYPE_ID` | UUID | FK → ASOP_TARIFF_TYPES, NOT NULL | Тип тарифа |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Перевозчик |
| `ZONE_ID` | UUID | FK → ASOP_FARE_ZONES | Тарифная зона |
| `PATH_ID` | UUID | FK → ASOP_PATHS | Путь |
| `PRICE` | NUMERIC(10,2) | NOT NULL | Стоимость |
| `DESCRIPTION` | TEXT | | Описание |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_payments"></a>
### `ASOP_PAYMENTS`
Поступления (пополнения) на карту: деньги или поездки. Поддержка Offline Top-Up.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PAYMENT_ID` | UUID | PK | Первичный ключ |
| `CARD_ID` | UUID | FK → ASOP_CARDS, NOT NULL | Карта, на которую идет пополнение |
| `RECEIPT_UNIX_TIME` | INT | NOT NULL | Порядковый номер поступления (Unix time в секундах, 32-бит для совместимости с MIFARE) |
| `SESSION_ID` | UUID | FK → ASOP_SESSIONS | Сессия (если пополнение произошло в терминале) |
| `EVENT_ID` | UUID | FK → ASOP_EVENTS | Системное событие создания платежа (для аудита) |
| `USER_ID` | UUID | FK → ASOP_USERS | Пользователь, инициировавший (если известно, например, через приложение) |
| `AMOUNT` | NUMERIC(10,2) | DEFAULT 0 | Сумма деньгами |
| `TRIPS_ADDED` | INT | DEFAULT 0 | Количество добавленных поездок (если тарификация в поездках) |
| `PAYMENT_METHOD` | VARCHAR(50) | | Способ оплаты (CASH, CARD, ONLINE, AUTO_TOPUP) |
| `STATUS` | VARCHAR(20) | DEFAULT 'PENDING', CHECK | Статус (PENDING, APPLIED, FAILED, REFUNDED) |
| `EXTERNAL_REF` | VARCHAR(128) | | Ссылка на внешний чек, ID банковской транзакции или фискального чека |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |

#### 6.1 Ценообразование

<a id="asop_path_services"></a>
### `ASOP_PATH_SERVICES`
Дополнительные услуги на пути. Цена и доступность могут зависеть от перевозчика, ТС или тарифа.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PATH_SERVICE_ID` | UUID | PK | Первичный ключ |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL, CASCADE | Путь |
| `SERVICE_ID` | UUID | FK → ASOP_SERVICES, NOT NULL | Услуга |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Опционально: только для конкретного перевозчика |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES | Опционально: только для конкретного ТС |
| `TARIFF_TYPE_ID` | UUID | FK → ASOP_TARIFF_TYPES | Опционально: только для конкретного типа тарифа |
| `PRICE` | NUMERIC(10,2) | NOT NULL | Стоимость услуги при данных условиях |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |

<a id="asop_path_discounts"></a>
### `ASOP_PATH_DISCOUNTS`
Скидки на пути. Поддерживает фиксированные суммы и проценты, может быть уточнена по перевозчику, ТС или тарифу.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PATH_DISCOUNT_ID` | UUID | PK | Первичный ключ |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL, CASCADE | Путь |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Опционально |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES | Опционально |
| `TARIFF_TYPE_ID` | UUID | FK → ASOP_TARIFF_TYPES | Опционально |
| `DISCOUNT_NAME` | VARCHAR(100) | NOT NULL | Название |
| `DISCOUNT_TYPE` | VARCHAR(20) | DEFAULT 'PERCENT', CHECK | Тип: PERCENT или FIXED |
| `DISCOUNT_VALUE` | NUMERIC(10,2) | NOT NULL, CHECK >= 0 | Значение скидки (10 для 10%, или 10.00 для 10 руб.) |
| `VALID_FROM` | TIMESTAMP | NOT NULL | Дата начала |
| `VALID_UNTIL` | TIMESTAMP | | Дата окончания |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |

<a id="asop_path_benefits"></a>
### `ASOP_PATH_BENEFITS`
Льготы, действующие на конкретных путях.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `PATH_BENEFIT_ID` | UUID | PK | Первичный ключ |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL, CASCADE | Путь |
| `BENEFIT_ID` | UUID | FK → ASOP_BENEFITS, NOT NULL, CASCADE | Льгота |

[↑ Наверх](#-оглавление)

---

## 7. Сессии, Транзакции, Аудит, КРС

<a id="asop_sessions"></a>
### `ASOP_SESSIONS`
Иерархические сессии. Привязаны к PATH_ID (конкретному пути). ATTRIBUTES хранит контекст.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SESSION_ID` | UUID | PK | Первичный ключ |
| `SESSION_TYPE_ID` | UUID | FK → ASOP_SESSION_TYPES, NOT NULL | Тип сессии |
| `PARENT_SESSION_ID` | UUID | FK → ASOP_SESSIONS, SET NULL | Родительская сессия (иерархия) |
| `TERMINAL_ID` | UUID | FK → ASOP_TERMINALS | Терминал |
| `TID_ID` | UUID | FK → ASOP_TIDS | TID (эквайринг) |
| `OPENED_BY_USER_ID` | UUID | FK → ASOP_USERS, SET NULL | Кто открыл |
| `CLOSED_BY_USER_ID` | UUID | FK → ASOP_USERS, SET NULL | Кто закрыл |
| `CARD_ID` | UUID | FK → ASOP_CARDS | Карта (для поездок) |
| `PATH_ID` | UUID | FK → ASOP_PATHS | Конкретный путь |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES | ТС |
| `STARTED_AT` | TIMESTAMP | NOT NULL | Время начала (UTC) |
| `CLOSED_AT` | TIMESTAMP | | Время окончания |
| `STARTED_AT_LOCAL` | TIMESTAMP | NOT NULL | Локальное время начала |
| `CLOSED_AT_LOCAL` | TIMESTAMP | | Локальное время окончания |
| `EXPIRATION_TIME` | TIMESTAMP | NOT NULL | Время истечения |
| `STATUS` | VARCHAR(20) | DEFAULT 'IN_PROGRESS', CHECK | Статус (IN_PROGRESS, CLOSED, CANCELLED, CONFIRMED, NOT_CONFIRMED) |
| `ATTRIBUTES` | JSONB | | Специфичные данные (остановки, штрафы, пробег) |

<a id="asop_audit_services"></a>
### `ASOP_AUDIT_SERVICES`
Справочник контрольно-ревизионных служб (КРС).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `AUDIT_SERVICE_ID` | UUID | PK | Первичный ключ |
| `SERVICE_CODE` | VARCHAR(50) | UNIQUE, NOT NULL | Код службы |
| `SERVICE_NAME` | VARCHAR(255) | NOT NULL | Наименование |
| `ISSUER_TYPE` | VARCHAR(20) | NOT NULL, CHECK | Кто создал (ORGANIZER, CARRIER) |
| `ORGANIZER_ID` | UUID | FK → ASOP_ORGANIZERS | Организатор |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Перевозчик |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_audit_tasks"></a>
### `ASOP_AUDIT_TASKS`
Задания на проведение проверок.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TASK_ID` | UUID | PK | Первичный ключ |
| `TASK_NUMBER` | VARCHAR(50) | NOT NULL | Номер задания |
| `ISSUER_TYPE` | VARCHAR(20) | NOT NULL, CHECK | Тип инициатора |
| `ORGANIZER_ID` | UUID | FK → ASOP_ORGANIZERS | Организатор |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Перевозчик |
| `ASSIGNED_AUDIT_SERVICE_ID` | UUID | FK → ASOP_AUDIT_SERVICES, NOT NULL | Назначенная служба КРС |
| `TASK_START_DATE` | TIMESTAMP | NOT NULL | Дата начала |
| `TASK_END_DATE` | TIMESTAMP | | Дата окончания |
| `STATUS` | VARCHAR(20) | DEFAULT 'DRAFT', NOT NULL, CHECK | Статус |
| `DESCRIPTION` | TEXT | | Описание |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_audit_task_paths"></a>
### `ASOP_AUDIT_TASK_PATHS`
Список путей, охваченных заданием КРС (1:N).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TASK_PATH_ID` | UUID | PK | Первичный ключ |
| `TASK_ID` | UUID | FK → ASOP_AUDIT_TASKS, NOT NULL, CASCADE | Задание |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL | Путь |

<a id="asop_audit_brigades"></a>
### `ASOP_AUDIT_BRIGADES`
Бригады контролеров, сформированные под задание.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `BRIGADE_ID` | UUID | PK | Первичный ключ |
| `TASK_ID` | UUID | FK → ASOP_AUDIT_TASKS, NOT NULL | Задание |
| `FOREMAN_USER_ID` | UUID | FK → ASOP_USERS, SET NULL | Бригадир |
| `BRIGADE_STATUS` | VARCHAR(20) | DEFAULT 'FORMING', NOT NULL, CHECK | Статус |
| `STARTED_AT` | TIMESTAMP | | Время начала |
| `CLOSED_AT` | TIMESTAMP | | Время закрытия |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_audit_brigade_members"></a>
### `ASOP_AUDIT_BRIGADE_MEMBERS`
Состав бригады (бригадир и контролеры).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `MEMBER_ID` | UUID | PK | Первичный ключ |
| `BRIGADE_ID` | UUID | FK → ASOP_AUDIT_BRIGADES, NOT NULL, CASCADE | Бригада |
| `USER_ID` | UUID | FK → ASOP_USERS, NOT NULL | Пользователь |
| `ROLE` | VARCHAR(20) | NOT NULL, CHECK | Роль (FOREMAN, CONTROLLER) |

<a id="asop_audit_inspections"></a>
### `ASOP_AUDIT_INSPECTIONS`
Акты проведенных проверок. Данные о ТС/терминале/пути вытягиваются из CONTROLLER_SESSION_ID.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `INSPECTION_ID` | UUID | PK | Первичный ключ |
| `BRIGADE_ID` | UUID | FK → ASOP_AUDIT_BRIGADES, NOT NULL | Бригада |
| `CONTROLLER_SESSION_ID` | UUID | FK → ASOP_SESSIONS, NOT NULL | Сессия контролера |
| `PATH_ID` | UUID | FK → ASOP_PATHS | Путь, на котором проведена проверка |
| `INSPECTION_START` | TIMESTAMP | NOT NULL | Время начала проверки |
| `INSPECTION_END` | TIMESTAMP | | Время окончания |
| `DURATION` | INTERVAL | | Длительность |
| `PATH_NAME` | VARCHAR(255) | | Направление/путь |
| `PASSENGERS_CHECKED` | INT | DEFAULT 0 | Всего проверено |
| `PASSENGERS_PAID` | INT | DEFAULT 0 | С оплатой |
| `PASSENGERS_COMPENSATED` | INT | DEFAULT 0 | С компенсацией |
| `PASSENGERS_UNPAID` | INT | DEFAULT 0 | Без оплаты |
| `FINES_COUNT` | INT | DEFAULT 0 | Количество штрафов |
| `STATUS` | VARCHAR(20) | DEFAULT 'DRAFT', NOT NULL, CHECK | Статус акта |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

<a id="asop_audit_inspection_tasks"></a>
### `ASOP_AUDIT_INSPECTION_TASKS`
Связь M2M: одна инспекция может быть проведена по нескольким заданиям КРС.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `INSPECTION_TASK_ID` | UUID | PK | Первичный ключ |
| `INSPECTION_ID` | UUID | FK → ASOP_AUDIT_INSPECTIONS, NOT NULL, CASCADE | Инспекция |
| `TASK_ID` | UUID | FK → ASOP_AUDIT_TASKS, NOT NULL | Задание |

<a id="asop_transactions"></a>
### `ASOP_TRANSACTIONS`
Финансовые проводки (списания). METADATA хранит детали расчета (зоны, скидки, льготы).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_ID` | UUID | PK | Первичный ключ |
| `STARTED_AT` | TIMESTAMP | NOT NULL | Время начала |
| `COMPLETED_AT` | TIMESTAMP | | Время завершения |
| `SESSION_ID` | UUID | FK → ASOP_SESSIONS | Сессия |
| `TRANSACTION_TYPE_ID` | UUID | FK → ASOP_TRANSACTION_TYPES, NOT NULL | Тип транзакции |
| `TRANSACTION_RESULT_ID` | UUID | FK → ASOP_TRANSACTION_RESULTS, NOT NULL | Результат |
| `AMOUNT` | NUMERIC(10,2) | DEFAULT 0, NOT NULL | Сумма |
| `CURRENCY` | CHAR(3) | DEFAULT 'RUB' | Валюта |
| `ACQUIRER_REFERENCE` | VARCHAR(128) | | Ссылка от эквайера |
| `ERROR_CODE` | VARCHAR(50) | | Код ошибки |
| `ERROR_MESSAGE` | VARCHAR(512) | | Сообщение об ошибке |
| `METADATA` | JSONB | | Дополнительные данные |

<a id="asop_transaction_cards"></a>
### `ASOP_TRANSACTION_CARDS`
Детализация транзакций по картам.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_CARD_ID` | UUID | PK | Первичный ключ |
| `TRANSACTION_ID` | UUID | FK → ASOP_TRANSACTIONS, NOT NULL, CASCADE | Транзакция |
| `CARD_ID` | UUID | FK → ASOP_CARDS, NOT NULL | Карта |
| `CARD_ROLE` | VARCHAR(20) | NOT NULL | Роль карты (PAYER, REFUND, BENEFIT) |
| `TARIFF_APPLIED_ID` | UUID | FK → ASOP_CARD_TARIFFS | Примененный тариф |
| `BALANCE_BEFORE` | NUMERIC(10,2) | | Баланс до |
| `BALANCE_AFTER` | NUMERIC(10,2) | | Баланс после |

<a id="asop_gps_tracking"></a>
### `ASOP_GPS_TRACKING`
GPS-трекинг транспорта (поток координат).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `POSITION_ID` | UUID | PK | Первичный ключ |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES, NOT NULL, CASCADE | ТС |
| `PATH_ID` | UUID | FK → ASOP_PATHS, NOT NULL | Путь |
| `SESSION_ID` | UUID | FK → ASOP_SESSIONS | Текущая сессия-рейс |
| `GPS_COORD` | GEOGRAPHY | | Координаты (PostGIS) |
| `RECORDED_AT` | TIMESTAMP | NOT NULL | Время записи |
| `SPEED_KMH` | NUMERIC(5,2) | | Скорость (км/ч) |
| `STATUS` | VARCHAR(30) | DEFAULT 'MOVING' | Статус движения |

<a id="asop_events"></a>
### `ASOP_EVENTS`
Журнал системных действий и аудит событий.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `EVENT_ID` | UUID | PK | Первичный ключ |
| `EVENT_TIME` | TIMESTAMP | NOT NULL | Время события (UTC) |
| `EVENT_LOCAL_TIME` | TIMESTAMP | NOT NULL | Локальное время |
| `EVENT_TYPE` | CHAR(4) | FK → ASOP_EVENT_TYPES, NOT NULL | Тип события |
| `USER_ID` | UUID | FK → ASOP_USERS | Инициатор |
| `SESSION_ID` | UUID | FK → ASOP_SESSIONS | Сессия |
| `REFERENCE_TYPE_ID` | INT | | Тип связанного объекта |
| `REFERENCE_ID` | UUID | | ID связанного объекта |
| `EVENT_DETAILS` | VARCHAR(256) | | Краткое описание |
| `EVENT_OBJECT` | JSONB | | Полный контекст |

[↑ Наверх](#-оглавление)