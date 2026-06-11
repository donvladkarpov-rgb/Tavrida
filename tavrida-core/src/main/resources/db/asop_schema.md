# Документация схемы БД АСОП

> **Версия:** 1.0  
> **СУБД:** PostgreSQL 14+ с PostGIS  
> **Соглашение:** Префикс `ASOP_`, UPPER_CASE, множественное число  
> **PK:** UUID (генерируются на уровне приложения)  
> **Всего таблиц:** 47  
> **Мультирегиональность:** Поле `REGION_ID` во всех бизнес-таблицах

---

## Оглавление

1. [0. Регионы](#0-регионы)
2. [1. Справочники](#1-справочники)
3. [2. Базовые сущности](#2-базовые-сущности)
4. [3. Маршруты и расписание](#3-маршруты-и-расписание)
5. [4. Карты, льготы, тарифы](#4-карты-льготы-тарифы)
6. [5. Сессии, транзакции, аудит, КРС](#5-сессии-транзакции-аудит-крс)
7. [Сводная таблица индексов](#сводная-таблица-индексов)

---

## 0. Регионы

### `ASOP_REGIONS`
**Описание:** Справочник регионов для мультирегионального разделения данных и шардинга. Поддерживает иерархию (область → район → город).
**TODO: Добавить фиасные поля из общероссийского справочника**

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `REGION_ID` | UUID | PK | Первичный ключ региона |
| `REGION_CODE` | VARCHAR(20) | UNIQUE, NOT NULL | Уникальный код (например, `RU-KRM`) |
| `REGION_NAME` | VARCHAR(255) | NOT NULL | Наименование региона |
| `PARENT_REGION_ID` | UUID | FK → ASOP_REGIONS | Ссылка на родительский регион (иерархия) |
| `DESCRIPTION` | TEXT | | Текстовое описание |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

---

## 1. Справочники

### `ASOP_ROLES`
**Описание:** Роли доступа в системе (Администратор, Кассир, Водитель, Диспетчер, Контролер, Ревизор).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROLE_ID` | UUID | PK | Первичный ключ роли |
| `ROLE_NAME` | VARCHAR(255) | NOT NULL | Наименование роли |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_CARD_TYPES`
**Описание:** Типы провозных носителей (MIFARE DESFire EV3, Bank Card EMV).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_TYPE_ID` | UUID | PK | Первичный ключ |
| `CARD_TYPE_NAME` | VARCHAR(255) | NOT NULL | Название типа карты |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TARIFF_TYPES`
**Описание:** Типы тарифов (Пакет поездок, Безлимит, Кошелёк).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TARIFF_TYPE_ID` | UUID | PK | Первичный ключ |
| `CODE` | VARCHAR(50) | UNIQUE, NOT NULL | Системный код |
| `NAME` | VARCHAR(100) | NOT NULL | Наименование |
| `DESCRIPTION` | TEXT | | Описание |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_SESSION_TYPES`
**Описание:** Типы рабочих сессий — определяет, что хранится в `ASOP_SESSIONS`.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SESSION_TYPE_ID` | UUID | PK | Первичный ключ |
| `SESSION_TYPE_CODE` | VARCHAR(30) | UNIQUE, NOT NULL | Код (`DRIVER_SHIFT`, `PASSENGER_TRIP`, `KRS_AUDIT`) |
| `SESSION_TYPE_NAME` | VARCHAR(100) | NOT NULL | Название |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_EVENT_TYPES`
**Описание:** Типы системных событий для журнала аудита.
**TODO: убрать от сюда REGION_ID**

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `EVENT_TYPE` | CHAR(4) | PK | Системный код (`CUSR`, `TPAY`, `SOPN`) |
| `EVENT_TYPE_NAME` | VARCHAR(128) | NOT NULL | Название |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TRANSACTION_TYPES`
**Описание:** Типы финансовых операций (Нал, безнал, спб, майфер).
**TODO: убрать от сюда REGION_ID**

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_TYPE_ID` | UUID | PK | Первичный ключ |
| `TRANSACTION_TYPE_NAME` | VARCHAR(255) | NOT NULL | Название |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TRANSACTION_RESULTS`
**Описание:** Результаты выполнения транзакций (Успех, Недостаточно средств, Карта заблокирована).
**TODO: убрать от сюда REGION_ID**

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_RESULT_ID` | UUID | PK | Первичный ключ |
| `TRANSACTION_RESULT_NAME` | VARCHAR(255) | NOT NULL | Название результата |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_ROUTE_TYPES`
**Описание:** Типы маршрутов (Маршрут, Путь).
**маршрут и путь разные сущности, маршрут имеют 2 пути - прямой и обратны, у кольцевого только прямой маршрут**

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_TYPE_ID` | UUID | PK | Первичный ключ |
| `ROUTE_TYPE_NAME` | VARCHAR(16) | NOT NULL | Название |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_ORGANIZERS`
**Описание:** Организаторы перевозок (муниципалитеты, транспортные управления, частные холдинги).
**REGION_CODE лишний**
CREATED_AT ??

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ORGANIZER_ID` | UUID | PK | Первичный ключ |
| `ORGANIZER_NAME` | VARCHAR(255) | NOT NULL | Наименование |
| `REGION_CODE` | VARCHAR(50) | | Региональный код |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |

### `ASOP_TERRITORIES`
**Описание:** Административно-территориальные единицы (города, районы) для маршрутизации и бюджетирования.
CADASTRAL_NUMBER ??
ASOP_ORGANIZERS много - ASOP_TERRITORIES много

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TERRITORY_ID` | UUID | PK | Первичный ключ |
| `TERRITORY_NAME` | VARCHAR(255) | NOT NULL | Наименование |
| `CADASTRAL_NUMBER` | VARCHAR(50) | | Кадастровый номер |
| `FIAS_CODE` | VARCHAR(50) | | Код ФИАС |
| `ORGANIZER_ID` | UUID | FK → ASOP_ORGANIZERS | Ссылка на организатора |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_DISPATCH_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности диспетчеризации |

### `ASOP_SERVICES`
**Описание:** Классификатор платных услуг (Проезд, Детский, Багаж).   Это называется "Услуга" и так и в чеке выбивается
SERVICE_CODE - лишний
убрвть
| `IS_DEFAULT` | BOOLEAN | DEFAULT false | По умолчанию |
| `ALLOW_DISCOUNTS` | BOOLEAN | DEFAULT false | Разрешены скидки |
| `ALLOW_BENEFITS` | BOOLEAN | DEFAULT false | Разрешены льготы |
| `USE_ZONES` | BOOLEAN | DEFAULT false | Используется зонирование |
| `ROUTES_LIMIT` | INT | | Лимит маршрутов |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

приоритет уникальный

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SERVICE_ID` | UUID | PK | Первичный ключ |
| `SERVICE_CODE` | VARCHAR(20) | UNIQUE, NOT NULL | Уникальный код |
| `SERVICE_NAME` | VARCHAR(100) | NOT NULL | Название |
| `DESCRIPTION` | TEXT | | Описание |
| `IS_DEFAULT` | BOOLEAN | DEFAULT false | По умолчанию |
| `ALLOW_DISCOUNTS` | BOOLEAN | DEFAULT false | Разрешены скидки |
| `ALLOW_BENEFITS` | BOOLEAN | DEFAULT false | Разрешены льготы |
| `USE_ZONES` | BOOLEAN | DEFAULT false | Используется зонирование |
| `PRIORITY` | INT | DEFAULT 0 | Приоритет |
| `ROUTES_LIMIT` | INT | | Лимит маршрутов |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |

### `ASOP_FARE_ZONES`
**Описание:** Тарифные зоны с географическими полигонами (PostGIS).
Тарифная зона не имеет географических координат
Тарифная зона - таблица связей - одна остановка может находиться только в одной тарифной зоне , но в одной зоне моет быть много остановок

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ZONE_ID` | UUID | PK | Первичный ключ |
| `ZONE_CODE` | VARCHAR(20) | UNIQUE, NOT NULL | Код зоны |
| `ZONE_NAME` | VARCHAR(100) | NOT NULL | Название |
| `DESCRIPTION` | VARCHAR(256) | | Описание |
| `ZONE_POLYGON` | GEOGRAPHY(POLYGON, 4326) | CHECK ST_IsValid | Гео-полигон |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TRAVEL_ZONES`
**Описание:** Зоны проезда внутри тарифных зон (детализация для аналитики).
Зона (свободный объект), потом для ограничения скорости использовалось, потом эти зоны потеряли свой смысл, в асопе они не используются

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRAVEL_ZONE_ID` | UUID | PK | Первичный ключ |
| `FARE_ZONE_ID` | UUID | FK → ASOP_FARE_ZONES, NOT NULL | Ссылка на тарифную зону |
| `TRAVEL_ZONE_CODE` | VARCHAR(30) | UNIQUE, NOT NULL | Код зоны проезда |
| `TRAVEL_ZONE_NAME` | VARCHAR(100) | NOT NULL | Название |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

---

## 2. Базовые сущности

### `ASOP_CARRIERS`
**Описание:** Перевозчики (транспортные компании, ГУП, ИП).
инн, договор, срок действия договора у перевозчика много договоров (как правило один договор на маршрут или на несколько)
скатать из асоп все нужные реквизиты

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARRIER_ID` | UUID | PK | Первичный ключ |
| `CARRIER_NAME` | VARCHAR(255) | NOT NULL | Наименование |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_USERS`
**Описание:** Пользователи системы (водители, контролеры, кассиры, администраторы).
У пользователя может быть несколько ролей и несколько перевозчиков.
Если у человека не назначен перевозчик, значит он имеет доступ ко всем перевозчикам.
Если у человека не назначен регион, значит он имеет доступ ко всем регионам
Снил - это персональная информация, которую в открытом виде хранить нельзя, что будем делать?
Телефон - пусть будет.
ФИО - нет, имя и первые буквы отчества и фамилии.
Хэш пароля убираем - делайм взаимодействие с кийклок (Keycloak).
У пользователя может быть несколько регионов.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `USER_ID` | UUID | PK | Первичный ключ |
| `ROLE_ID` | UUID | FK → ASOP_ROLES, NOT NULL | Ссылка на роль |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Ссылка на перевозчика |
| `SNILS` | VARCHAR(14) | UNIQUE | СНИЛС |
| `PHONE` | VARCHAR(20) | | Телефон |
| `USER_FIO` | VARCHAR(255) | | ФИО |
| `PASSWORD_HASH` | VARCHAR(255) | | Хэш пароля |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_VEHICLES`
**Описание:** Транспортные средства.
не хватает типа транспортного средства
модель тс должен быть справочник
тип тс  должен быть справочник
регион точно нет

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `VEHICLE_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Ссылка на перевозчика |
| `VEHICLE_NUMBER` | VARCHAR(16) | NOT NULL | ГРЗ (гос. регистрационный знак) |
| `VEHICLE_NAME` | VARCHAR(255) | NOT NULL | Внутреннее наименование |
| `VEHICLE_MODEL` | VARCHAR(100) | | Модель ТС |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TERMINALS`
**Описание:** Терминалы оплаты. Связь с ТС — только через сессию-рейс (`ASOP_SESSIONS`).


| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TERMINAL_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Ссылка на перевозчика |
| `TERMINAL_NUMBER` | VARCHAR(16) | NOT NULL | Инвентарный номер |
| `TERMINAL_SERIAL` | VARCHAR(64) | NOT NULL | Серийный номер (SN) |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TIDS`
**Описание:** Пул эквайринговых идентификаторов терминалов (TID). 1:N к перевозчику. Привязка к терминалу происходит в рейсе (сессии).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TID_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Ссылка на перевозчика |
| `TID_VALUE` | VARCHAR(20) | UNIQUE, NOT NULL | Значение TID от банка |
| `STATUS` | VARCHAR(20) | DEFAULT 'UNUSED', CHECK | Статус (`UNUSED`, `ASSIGNED`, `REVOKED`) |
| `ASSIGNED_AT` | TIMESTAMP | | Дата назначения |
| `UNASSIGNED_AT` | TIMESTAMP | | Дата отзыва |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

---

## 3. Маршруты и расписание

### `ASOP_ROUTES`
**Описание:** Маршруты движения с привязкой к реестру Минтранса, категориям и организаторам.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_ID` | UUID | PK | Первичный ключ |
| `ROUTE_TYPE_ID` | UUID | FK → ASOP_ROUTE_TYPES, NOT NULL | Тип маршрута |
| `PARENT_ROUTE_ID` | UUID | FK → ASOP_ROUTES | Родительский маршрут (ветвление) |
| `ROUTE_NAME` | VARCHAR(128) | NOT NULL | Название |
| `MINISTRY_REGISTRY_NO` | VARCHAR(50) | | Номер в реестре Минтранса |
| `ROUTE_CATEGORY` | VARCHAR(30) | CHECK | Категория (`CITY`, `SUBURBAN`, `INTERCITY`, `EXPRESS`) |
| `TERRITORY_ID` | UUID | FK → ASOP_TERRITORIES | Территория |
| `ORGANIZER_ID` | UUID | FK → ASOP_ORGANIZERS | Организатор |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `ROUTE_START_DATE` | TIMESTAMP | | Дата начала действия |
| `ROUTE_END_DATE` | TIMESTAMP | | Дата окончания действия |
| `BENEFIT_POLICY` | VARCHAR(20) | DEFAULT 'ALL', NOT NULL, CHECK | Политика льгот |
| `ROUTE_OBJECT` | JSONB | | JSON-геометрия/конфиг |
| `DESCRIPTION` | VARCHAR(512) | | Описание |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_TRANSPORT_STOPS`
**Описание:** Справочник остановок общественного транспорта.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `STOP_ID` | UUID | PK | Первичный ключ |
| `FARE_ZONE_ID` | UUID | FK → ASOP_FARE_ZONES | Тарифная зона |
| `TRAVEL_ZONE_ID` | UUID | FK → ASOP_TRAVEL_ZONES | Зона проезда |
| `STOP_CODE` | VARCHAR(20) | UNIQUE, NOT NULL | Код остановки |
| `STOP_NAME` | VARCHAR(200) | NOT NULL | Название |
| `STOP_ADDRESS` | VARCHAR(500) | | Адрес |
| `ZONE_POLYGON` | GEOGRAPHY(POLYGON, 4326) | | Гео-полигон |
| `DESCRIPTION` | TEXT | | Описание |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_ROUTE_TRANSPORT_STOPS`
**Описание:** Связь маршрутов и остановок (порядок следования).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_STOP_ID` | UUID | PK | Первичный ключ |
| `STOP_ID` | UUID | FK → ASOP_TRANSPORT_STOPS, NOT NULL | Остановка |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL | Маршрут |
| `SERIAL_NUMBER` | INT | NOT NULL | Порядковый номер |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_SCHEDULE`
**Описание:** Расписание прибытия на остановки по дням недели.
время отправления

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SCHEDULE_ID` | UUID | PK | Первичный ключ |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL, CASCADE | Маршрут |
| `STOP_ID` | UUID | FK → ASOP_TRANSPORT_STOPS, NOT NULL | Остановка |
| `DAY_MASK` | INT | DEFAULT 127, NOT NULL, CHECK 1-127 | Маска дней недели |
| `ARRIVAL_TIME` | TIME | NOT NULL | Время прибытия |
| `DWELL_TIME_SEC` | INT | DEFAULT 30 | Время стоянки (сек) |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |

### `ASOP_ROUTE_SERVICES`
**Описание:** Услуги, доступные на конкретном маршруте.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_SERVICE_ID` | UUID | PK | Первичный ключ |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL, CASCADE | Маршрут |
| `SERVICE_ID` | UUID | FK → ASOP_SERVICES, NOT NULL | Услуга |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_ROUTE_CARD_TYPES`
**Описание:** Типы карт, принимаемые к оплате на маршруте.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_CARD_TYPE_ID` | UUID | PK | Первичный ключ |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL, CASCADE | Маршрут |
| `CARD_TYPE_ID` | UUID | FK → ASOP_CARD_TYPES, NOT NULL | Тип карты |
| `IS_ENABLED` | BOOLEAN | DEFAULT true | Флаг доступности |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_ROUTE_DISCOUNTS`
**Описание:** Скидки, действующие на маршруте.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_DISCOUNT_ID` | UUID | PK | Первичный ключ |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL, CASCADE | Маршрут |
| `DISCOUNT_NAME` | VARCHAR(100) | NOT NULL | Название |
| `DISCOUNT_PERCENT` | NUMERIC(5,2) | NOT NULL, CHECK 0-100 | Процент скидки |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `VALID_FROM` | TIMESTAMP | NOT NULL | Дата начала |
| `VALID_UNTIL` | TIMESTAMP | | Дата окончания |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |

---

## 4. Карты, льготы, тарифы

### `ASOP_CARDS`
**Описание:** Транспортные и банковские карты, зарегистрированные в системе.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK | Первичный ключ |
| `CARD_TYPE_ID` | UUID | FK → ASOP_CARD_TYPES, NOT NULL | Тип карты |
| `USER_ID` | UUID | FK → ASOP_USERS | Владелец |
| `REGISTERED_AT` | TIMESTAMP | | Дата регистрации |
| `REGISTERED_BY_USER_ID` | UUID | | Кто зарегистрировал |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_CARD_MIFARES`
**Описание:** Технические параметры MIFARE-карт.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK, FK → ASOP_CARDS, CASCADE | Ссылка на карту |
| `UID` | BYTEA | UNIQUE, NOT NULL | Уникальный идентификатор чипа |
| `ATQA` | SMALLINT | | Ответ на запрос типа A |
| `SAK` | SMALLINT | | Код выбора приложения |
| `PROTOCOL_VERSION` | INT | | Версия протокола |
| `MEMORY_MAP` | JSONB | | JSON-карта памяти |

### `ASOP_CARD_BANKS`
**Описание:** Данные привязанных банковских карт.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK, FK → ASOP_CARDS, CASCADE | Ссылка на карту |
| `PAN_TOKEN` | VARCHAR(256) | NOT NULL | Токенизированный PAN |
| `PAN_LAST4` | CHAR(4) | | Последние 4 цифры |
| `BIN` | CHAR(6) | | BIN-код |
| `IS_TOKENIZED` | BOOLEAN | DEFAULT false | Флаг токенизации |

### `ASOP_CARD_TARIFFS`
**Описание:** Активные тарифы на картах (баланс, количество поездок, срок действия).

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
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_BLACKLISTS`
**Описание:** Заблокированные карты (мошенничество, отрицательный баланс).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARD_ID` | UUID | PK, FK → ASOP_CARDS | Карта |
| `BLOCK_TYPE` | VARCHAR(20) | NOT NULL, CHECK | Тип блокировки |
| `BLOCKED_AT` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP, NOT NULL | Дата блокировки |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_BENEFITS`
**Описание:** Справочник льготных категорий (пенсионеры, студенты, дети и т.д.).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `BENEFIT_ID` | UUID | PK | Первичный ключ |
| `BENEFIT_CODE` | VARCHAR(50) | UNIQUE, NOT NULL | Код |
| `BENEFIT_NAME` | VARCHAR(100) | NOT NULL | Название |
| `REGION_CODE` | VARCHAR(50) | NOT NULL | Региональный код |
| `DESCRIPTION` | TEXT | | Описание |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_BENEFIT_STEPS`
**Описание:** Шаги накопительных льгот (пороги поездок, размер скидки, период).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `STEP_ID` | UUID | PK | Первичный ключ |
| `BENEFIT_ID` | UUID | FK → ASOP_BENEFITS, NOT NULL, CASCADE | Льгота |
| `STEP_ORDER` | INT | NOT NULL | Порядок шага |
| `TRIP_THRESHOLD_FROM` | INT | DEFAULT 0, NOT NULL | Нижняя граница |
| `TRIP_THRESHOLD_TO` | INT | | Верхняя граница |
| `DISCOUNT_SHARE` | NUMERIC(4,2) | NOT NULL, CHECK 0-1 | Доля скидки |
| `PERIOD_TYPE` | VARCHAR(20) | DEFAULT 'MONTHLY', NOT NULL | Тип периода |

### `ASOP_USER_BENEFITS`
**Описание:** Привязка льгот к пользователям с датами действия.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ASSIGNMENT_ID` | UUID | PK | Первичный ключ |
| `USER_ID` | UUID | FK → ASOP_USERS, NOT NULL, CASCADE | Пользователь |
| `BENEFIT_ID` | UUID | FK → ASOP_BENEFITS, NOT NULL | Льгота |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `VALID_FROM` | TIMESTAMP | NOT NULL | Дата начала |
| `VALID_UNTIL` | TIMESTAMP | | Дата окончания |
| `SYNC_VERSION` | INT | DEFAULT 1, NOT NULL | Версия синхронизации |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_ROUTE_BENEFITS`
**Описание:** Льготы, действующие на конкретных маршрутах.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `ROUTE_BENEFIT_ID` | UUID | PK | Первичный ключ |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL, CASCADE | Маршрут |
| `BENEFIT_ID` | UUID | FK → ASOP_BENEFITS, NOT NULL, CASCADE | Льгота |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TARIFF_RATES`
**Описание:** Тарифные ставки (цены) по зонам, маршрутам и перевозчикам.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TARIFF_RATE_ID` | UUID | PK | Первичный ключ |
| `TARIFF_TYPE_ID` | UUID | FK → ASOP_TARIFF_TYPES, NOT NULL | Тип тарифа |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Перевозчик |
| `ZONE_ID` | UUID | FK → ASOP_FARE_ZONES | Тарифная зона |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES | Маршрут |
| `PRICE` | NUMERIC(10,2) | NOT NULL | Стоимость |
| `DESCRIPTION` | TEXT | | Описание |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_CARRIER_ZONES`
**Описание:** Базовые тарифы перевозчиков по зонам.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARRIER_ZONE_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Перевозчик |
| `ZONE_ID` | UUID | FK → ASOP_FARE_ZONES, NOT NULL | Зона |
| `BASE_FARE` | DECIMAL(10,2) | NOT NULL | Базовая стоимость |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

---

## 5. Сессии, транзакции, аудит, КРС

### `ASOP_SESSIONS`
**Описание:** Иерархические сессии. Заменяет рейсы. В рейсе связывается транспорт, терминал, TID и водитель. `ATTRIBUTES` хранит контекст.

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
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES | Маршрут |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES | ТС |
| `STARTED_AT` | TIMESTAMP | NOT NULL | Время начала (UTC) |
| `CLOSED_AT` | TIMESTAMP | | Время окончания |
| `STARTED_AT_LOCAL` | TIMESTAMP | NOT NULL | Локальное время начала |
| `CLOSED_AT_LOCAL` | TIMESTAMP | | Локальное время окончания |
| `EXPIRATION_TIME` | TIMESTAMP | NOT NULL | Время истечения |
| `STATUS` | VARCHAR(20) | DEFAULT 'IN_PROGRESS', CHECK | Статус |
| `ATTRIBUTES` | JSONB | | Специфичные данные |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_AUDIT_SERVICES`
**Описание:** Справочник контрольно-ревизионных служб (КРС). Создаются организацией или перевозчиком.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `AUDIT_SERVICE_ID` | UUID | PK | Первичный ключ |
| `SERVICE_CODE` | VARCHAR(50) | UNIQUE, NOT NULL | Код службы |
| `SERVICE_NAME` | VARCHAR(255) | NOT NULL | Наименование |
| `ISSUER_TYPE` | VARCHAR(20) | NOT NULL, CHECK | Кто создал (`ORGANIZER`, `CARRIER`) |
| `ORGANIZER_ID` | UUID | FK → ASOP_ORGANIZERS | Организатор |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS | Перевозчик |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `IS_ACTIVE` | BOOLEAN | DEFAULT true | Флаг активности |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_AUDIT_SERVICE_TERRITORIES`
**Описание:** Территории, которые имеет право контролировать конкретная служба КРС.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `SERVICE_TERRITORY_ID` | UUID | PK | Первичный ключ |
| `AUDIT_SERVICE_ID` | UUID | FK → ASOP_AUDIT_SERVICES, NOT NULL, CASCADE | Служба КРС |
| `TERRITORY_ID` | UUID | FK → ASOP_TERRITORIES, NOT NULL | Территория |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_AUDIT_TASKS`
**Описание:** Задания на проведение проверок. Выдаются организацией или перевозчиком.

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
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_AUDIT_TASK_ROUTES`
**Описание:** Список маршрутов, охваченных заданием КРС (1:N).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TASK_ROUTE_ID` | UUID | PK | Первичный ключ |
| `TASK_ID` | UUID | FK → ASOP_AUDIT_TASKS, NOT NULL, CASCADE | Задание |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL | Маршрут |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_AUDIT_BRIGADES`
**Описание:** Бригады контролеров, сформированные под задание.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `BRIGADE_ID` | UUID | PK | Первичный ключ |
| `TASK_ID` | UUID | FK → ASOP_AUDIT_TASKS, NOT NULL | Задание |
| `FOREMAN_USER_ID` | UUID | FK → ASOP_USERS, SET NULL | Бригадир |
| `BRIGADE_STATUS` | VARCHAR(20) | DEFAULT 'FORMING', NOT NULL, CHECK | Статус |
| `STARTED_AT` | TIMESTAMP | | Время начала |
| `CLOSED_AT` | TIMESTAMP | | Время закрытия |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_AUDIT_BRIGADE_MEMBERS`
**Описание:** Состав бригады (бригадир и контролеры).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `MEMBER_ID` | UUID | PK | Первичный ключ |
| `BRIGADE_ID` | UUID | FK → ASOP_AUDIT_BRIGADES, NOT NULL, CASCADE | Бригада |
| `USER_ID` | UUID | FK → ASOP_USERS, NOT NULL | Пользователь |
| `ROLE` | VARCHAR(20) | NOT NULL, CHECK | Роль (`FOREMAN`, `CONTROLLER`) |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_AUDIT_INSPECTIONS`
**Описание:** Акты проведенных проверок. TERMINAL/VEHICLE/ROUTE/DRIVER вытягиваются из `CONTROLLER_SESSION_ID`. Связь с заданиями — через `ASOP_AUDIT_INSPECTION_TASKS` (M2M).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `INSPECTION_ID` | UUID | PK | Первичный ключ |
| `BRIGADE_ID` | UUID | FK → ASOP_AUDIT_BRIGADES, NOT NULL | Бригада |
| `CONTROLLER_SESSION_ID` | UUID | FK → ASOP_SESSIONS, NOT NULL | Сессия контролера |
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
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_AUDIT_INSPECTION_TASKS`
**Описание:** Связь M2M: одна инспекция может быть проведена по нескольким заданиям КРС.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `INSPECTION_TASK_ID` | UUID | PK | Первичный ключ |
| `INSPECTION_ID` | UUID | FK → ASOP_AUDIT_INSPECTIONS, NOT NULL, CASCADE | Инспекция |
| `TASK_ID` | UUID | FK → ASOP_AUDIT_TASKS, NOT NULL | Задание |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TRANSACTIONS`
**Описание:** Финансовые проводки. TERMINAL_ID убран — ходим через `SESSION_ID`.

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
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_TRANSACTION_CARDS`
**Описание:** Детализация транзакций по картам (баланс до/после, примененный тариф).

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `TRANSACTION_CARD_ID` | UUID | PK | Первичный ключ |
| `TRANSACTION_ID` | UUID | FK → ASOP_TRANSACTIONS, NOT NULL, CASCADE | Транзакция |
| `CARD_ID` | UUID | FK → ASOP_CARDS, NOT NULL | Карта |
| `CARD_ROLE` | VARCHAR(20) | NOT NULL | Роль карты (`PAYER`, `REFUND`, `BENEFIT`) |
| `TARIFF_APPLIED_ID` | UUID | FK → ASOP_CARD_TARIFFS | Примененный тариф |
| `BALANCE_BEFORE` | NUMERIC(10,2) | | Баланс до |
| `BALANCE_AFTER` | NUMERIC(10,2) | | Баланс после |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_DISPATCH_POSITIONS`
**Описание:** GPS-трекер транспорта.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `POSITION_ID` | UUID | PK | Первичный ключ |
| `VEHICLE_ID` | UUID | FK → ASOP_VEHICLES, NOT NULL, CASCADE | ТС |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL | Маршрут |
| `SESSION_ID` | UUID | FK → ASOP_SESSIONS | Текущая сессия-рейс |
| `GPS_COORD` | GEOGRAPHY(POINT, 4326) | | Координаты |
| `RECORDED_AT` | TIMESTAMP | NOT NULL | Время записи |
| `SPEED_KMH` | NUMERIC(5,2) | | Скорость (км/ч) |
| `STATUS` | VARCHAR(30) | DEFAULT 'MOVING' | Статус движения |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

### `ASOP_CARRIER_ROUTES`
**Описание:** Закрепление маршрутов за перевозчиками.

| Поле | Тип | Ограничения | Описание |
|------|-----|-------------|----------|
| `CARRIER_ROUTE_ID` | UUID | PK | Первичный ключ |
| `CARRIER_ID` | UUID | FK → ASOP_CARRIERS, NOT NULL | Перевозчик |
| `ROUTE_ID` | UUID | FK → ASOP_ROUTES, NOT NULL | Маршрут |
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |
| `CREATED_AT` | TIMESTAMP | NOT NULL | Дата создания |
| `UPDATED_AT` | TIMESTAMP | NOT NULL | Дата обновления |

### `ASOP_EVENTS`
**Описание:** Журнал системных действий и аудит событий.

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
| `REGION_ID` | UUID | FK → ASOP_REGIONS, NOT NULL | Привязка к региону |

---

## Сводная таблица индексов

| Имя индекса | Таблица | Поля | Тип |
|-------------|---------|------|-----|
| `idx_regions_parent` | ASOP_REGIONS | PARENT_REGION_ID | BTREE |
| `idx_roles_region` | ASOP_ROLES | REGION_ID | BTREE |
| `idx_card_types_region` | ASOP_CARD_TYPES | REGION_ID | BTREE |
| `idx_tariff_types_region` | ASOP_TARIFF_TYPES | REGION_ID | BTREE |
| `idx_session_types_region` | ASOP_SESSION_TYPES | REGION_ID | BTREE |
| `idx_event_types_region` | ASOP_EVENT_TYPES | REGION_ID | BTREE |
| `idx_transaction_types_region` | ASOP_TRANSACTION_TYPES | REGION_ID | BTREE |
| `idx_transaction_results_region` | ASOP_TRANSACTION_RESULTS | REGION_ID | BTREE |
| `idx_route_types_region` | ASOP_ROUTE_TYPES | REGION_ID | BTREE |
| `idx_organizers_region` | ASOP_ORGANIZERS | REGION_ID | BTREE |
| `idx_territories_region` | ASOP_TERRITORIES | REGION_ID | BTREE |
| `idx_services_region` | ASOP_SERVICES | REGION_ID | BTREE |
| `idx_fare_zones_geo` | ASOP_FARE_ZONES | ZONE_POLYGON | GIST |
| `idx_fare_zones_region` | ASOP_FARE_ZONES | REGION_ID | BTREE |
| `idx_travel_zones_region` | ASOP_TRAVEL_ZONES | REGION_ID | BTREE |
| `idx_carriers_region` | ASOP_CARRIERS | REGION_ID | BTREE |
| `idx_users_snils` | ASOP_USERS | SNILS | BTREE (частичный) |
| `idx_users_region` | ASOP_USERS | REGION_ID | BTREE |
| `idx_vehicles_region` | ASOP_VEHICLES | REGION_ID | BTREE |
| `idx_terminals_region` | ASOP_TERMINALS | REGION_ID | BTREE |
| `idx_tids_carrier` | ASOP_TIDS | CARRIER_ID | BTREE |
| `idx_tids_region` | ASOP_TIDS | REGION_ID | BTREE |
| `idx_routes_validity` | ASOP_ROUTES | ROUTE_START_DATE, ROUTE_END_DATE | BTREE |
| `idx_routes_policy` | ASOP_ROUTES | BENEFIT_POLICY | BTREE |
| `idx_routes_category` | ASOP_ROUTES | ROUTE_CATEGORY | BTREE |
| `idx_routes_region` | ASOP_ROUTES | REGION_ID | BTREE |
| `idx_transport_stops_geo` | ASOP_TRANSPORT_STOPS | ZONE_POLYGON | GIST |
| `idx_transport_stops_region` | ASOP_TRANSPORT_STOPS | REGION_ID | BTREE |
| `idx_transport_stops_travel_zone` | ASOP_TRANSPORT_STOPS | TRAVEL_ZONE_ID | BTREE |
| `uk_route_transport_stops` | ASOP_ROUTE_TRANSPORT_STOPS | ROUTE_ID, STOP_ID | UNIQUE BTREE |
| `idx_sched_route_stop` | ASOP_SCHEDULE | ROUTE_ID, STOP_ID | BTREE |
| `idx_sched_time` | ASOP_SCHEDULE | ARRIVAL_TIME | BTREE |
| `idx_sched_region` | ASOP_SCHEDULE | REGION_ID | BTREE |
| `uq_route_service` | ASOP_ROUTE_SERVICES | ROUTE_ID, SERVICE_ID | UNIQUE BTREE |
| `uq_route_card_type` | ASOP_ROUTE_CARD_TYPES | ROUTE_ID, CARD_TYPE_ID | UNIQUE BTREE |
| `idx_rd_route_active` | ASOP_ROUTE_DISCOUNTS | ROUTE_ID, IS_ACTIVE | BTREE (частичный) |
| `idx_rd_region` | ASOP_ROUTE_DISCOUNTS | REGION_ID | BTREE |
| `idx_cards_region` | ASOP_CARDS | REGION_ID | BTREE |
| `uq_mifare_uid` | ASOP_CARD_MIFARES | UID | UNIQUE BTREE |
| `idx_card_tariffs_region` | ASOP_CARD_TARIFFS | REGION_ID | BTREE |
| `idx_card_tariffs_expiry` | ASOP_CARD_TARIFFS | EXPIRATION_DATE | BTREE (частичный) |
| `idx_blacklists_type` | ASOP_BLACKLISTS | BLOCK_TYPE | BTREE |
| `idx_blacklists_region` | ASOP_BLACKLISTS | REGION_ID | BTREE |
| `idx_benefits_region` | ASOP_BENEFITS | REGION_ID | BTREE |
| `uq_benefit_steps_order` | ASOP_BENEFIT_STEPS | BENEFIT_ID, STEP_ORDER | UNIQUE BTREE |
| `idx_ub_user` | ASOP_USER_BENEFITS | USER_ID | BTREE |
| `idx_ub_region` | ASOP_USER_BENEFITS | REGION_ID | BTREE |
| `idx_rb_route` | ASOP_ROUTE_BENEFITS | ROUTE_ID | BTREE |
| `idx_rb_region` | ASOP_ROUTE_BENEFITS | REGION_ID | BTREE |
| `idx_tariff_rates_active` | ASOP_TARIFF_RATES | IS_ACTIVE | BTREE (частичный) |
| `idx_tariff_rates_region` | ASOP_TARIFF_RATES | REGION_ID | BTREE |
| `uk_carrier_zones` | ASOP_CARRIER_ZONES | CARRIER_ID, ZONE_ID | UNIQUE BTREE |
| `idx_carrier_zones_region` | ASOP_CARRIER_ZONES | REGION_ID | BTREE |
| `idx_sessions_parent` | ASOP_SESSIONS | PARENT_SESSION_ID | BTREE |
| `idx_sessions_type_status` | ASOP_SESSIONS | SESSION_TYPE_ID, STATUS | BTREE |
| `idx_sessions_terminal` | ASOP_SESSIONS | TERMINAL_ID | BTREE |
| `idx_sessions_tid` | ASOP_SESSIONS | TID_ID | BTREE |
| `idx_sessions_region` | ASOP_SESSIONS | REGION_ID | BTREE |
| `idx_audit_services_region` | ASOP_AUDIT_SERVICES | REGION_ID | BTREE |
| `uq_audit_service_territory` | ASOP_AUDIT_SERVICE_TERRITORIES | AUDIT_SERVICE_ID, TERRITORY_ID | UNIQUE BTREE |
| `idx_tasks_service` | ASOP_AUDIT_TASKS | ASSIGNED_AUDIT_SERVICE_ID | BTREE |
| `idx_tasks_status` | ASOP_AUDIT_TASKS | STATUS | BTREE |
| `idx_tasks_region` | ASOP_AUDIT_TASKS | REGION_ID | BTREE |
| `idx_task_routes_task` | ASOP_AUDIT_TASK_ROUTES | TASK_ID | BTREE |
| `idx_task_routes_route` | ASOP_AUDIT_TASK_ROUTES | ROUTE_ID | BTREE |
| `idx_task_routes_region` | ASOP_AUDIT_TASK_ROUTES | REGION_ID | BTREE |
| `idx_brigades_task` | ASOP_AUDIT_BRIGADES | TASK_ID | BTREE |
| `idx_brigades_status` | ASOP_AUDIT_BRIGADES | BRIGADE_STATUS | BTREE |
| `idx_brigades_region` | ASOP_AUDIT_BRIGADES | REGION_ID | BTREE |
| `uq_brigade_member` | ASOP_AUDIT_BRIGADE_MEMBERS | BRIGADE_ID, USER_ID | UNIQUE BTREE |
| `idx_abm_region` | ASOP_AUDIT_BRIGADE_MEMBERS | REGION_ID | BTREE |
| `idx_inspections_brigade` | ASOP_AUDIT_INSPECTIONS | BRIGADE_ID | BTREE |
| `idx_inspections_session` | ASOP_AUDIT_INSPECTIONS | CONTROLLER_SESSION_ID | BTREE |
| `idx_inspections_status` | ASOP_AUDIT_INSPECTIONS | STATUS | BTREE |
| `idx_inspections_region` | ASOP_AUDIT_INSPECTIONS | REGION_ID | BTREE |
| `uq_inspection_task` | ASOP_AUDIT_INSPECTION_TASKS | INSPECTION_ID, TASK_ID | UNIQUE BTREE |
| `idx_inspection_tasks_inspection` | ASOP_AUDIT_INSPECTION_TASKS | INSPECTION_ID | BTREE |
| `idx_inspection_tasks_task` | ASOP_AUDIT_INSPECTION_TASKS | TASK_ID | BTREE |
| `idx_inspection_tasks_region` | ASOP_AUDIT_INSPECTION_TASKS | REGION_ID | BTREE |
| `idx_transactions_session` | ASOP_TRANSACTIONS | SESSION_ID | BTREE |
| `idx_transactions_completed_at` | ASOP_TRANSACTIONS | COMPLETED_AT | BTREE (частичный) |
| `idx_transactions_region` | ASOP_TRANSACTIONS | REGION_ID | BTREE |
| `uk_transaction_cards_main` | ASOP_TRANSACTION_CARDS | TRANSACTION_ID, CARD_ID | UNIQUE BTREE |
| `idx_tc_region` | ASOP_TRANSACTION_CARDS | REGION_ID | BTREE |
| `idx_dp_vehicle_time` | ASOP_DISPATCH_POSITIONS | VEHICLE_ID, RECORDED_AT DESC | BTREE |
| `idx_dp_route_time` | ASOP_DISPATCH_POSITIONS | ROUTE_ID, RECORDED_AT DESC | BTREE |
| `idx_dp_geo` | ASOP_DISPATCH_POSITIONS | GPS_COORD | GIST |
| `idx_dp_region` | ASOP_DISPATCH_POSITIONS | REGION_ID | BTREE |
| `uk_carrier_routes` | ASOP_CARRIER_ROUTES | CARRIER_ID, ROUTE_ID | UNIQUE BTREE |
| `idx_carrier_routes_region` | ASOP_CARRIER_ROUTES | REGION_ID | BTREE |
| `idx_events_time` | ASOP_EVENTS | EVENT_TIME, USER_ID | BTREE |
| `idx_events_region` | ASOP_EVENTS | REGION_ID | BTREE |

---

## Ключевые архитектурные решения

1. **Мультирегиональность**: Поле `REGION_ID` присутствует во всех бизнес-таблицах → фильтрация по региону без JOIN, Row-Level Security, партиционирование.

2. **Иерархические сессии**: `ASOP_SESSIONS` заменяет отдельную таблицу рейсов. Тип сессии через `SESSION_TYPE_ID`, иерархия через `PARENT_SESSION_ID`.

3. **Нормализация workflow КРС**: Убраны транзитивные избыточные связи. Служба КРС через `TASK`, задание через `BRIGADE`. Данные о ТС/терминале/маршруте в акте проверки вытягиваются из сессии контролера.

4. **TID в сессии**: Привязка эквайрингового TID происходит в рейсе (сессии), а не в пуле терминалов.

5. **Snapshot в остановках**: `ASOP_TRANSPORT_STOPS` содержит `FARE_ZONE_ID` и `TRAVEL_ZONE_ID` для детализации аналитики.

6. **M2M инспекция ↔ задания**: `ASOP_AUDIT_INSPECTION_TASKS` позволяет одной инспекции быть привязанной к нескольким заданиям КРС.