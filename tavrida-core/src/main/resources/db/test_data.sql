INSERT INTO CARRIERS (CARRIER_ID, CARRIER_NAME) VALUES
    (1, 'Крымские тачанки');

INSERT INTO USERS (USER_ID, USER_ROLE_ID, USER_FIO, USER_PASSWORD_HASH) VALUES
    (1, 1, 'root', 'changeit'),
    (2, 2, 'Потап Админович Перевозчика', 'FIRST_PASSWORD'),
    (3, 3, 'Алексей Петрович Кассир', 'FIRST_PASSWORD'),
    (4, 4, 'Водил Водилович Водила', 'FIRST_PASSWORD');

INSERT INTO TRANSPORTS (TRANSPORT_ID, TRANSPORT_GUID, CARRIER_ID, TRANSPORT_NUMBER, TRANSPORT_NAME) VALUES
    (1, '264bfe9d-98c5-4cfc-a27b-9b6234054580', 1, '1', 'трамвай');

INSERT INTO TERMINALS (TERMINAL_ID, TRANSPORT_ID, TERMINAL_GUID, TERMINAL_NUMBER, TERMINAL_SERIAL) VALUES
    (1, 1, '3f2dd185-0540-4d82-ad26-e6a181cb1893', 'A1234', 'A1234_1234');

INSERT INTO CARDS (CARD_ID, CARD_GUID, CARD_TYPE_ID, USER_ID, UNIQUE_TRAVEL_COUNT, MAXIMUM_UNIQUE_COUNT, AVAILABLE_TRAVEL_COUNT, EXPIRATION_DATE) VALUES
    (1, '123e4567-e89b-42d3-a456-556642440000', 1, 1, 0, 10000, 1000, CURRENT_DATE + INTERVAL '3 years'),
    (2, '123e4567-e89b-42d3-a456-756642440001', 2, 3, 0, 10000, 1000, CURRENT_DATE + INTERVAL '3 years'),
    (3, '123e4567-e89b-42d3-a456-556642440002', 3, 4, 0, 10000, 1000, CURRENT_DATE + INTERVAL '3 years'),
    (4, '123e4567-e89b-42d3-a456-556642440003', 4, NULL, 0, 10000, 1000, CURRENT_DATE + INTERVAL '3 years'),
    (5, '123e4567-e89b-42d3-a456-556642440004', 5, 2, 0, 10000, 1000, CURRENT_DATE + INTERVAL '3 years'),
    (6, '123e4567-e89b-42d3-a456-556642440005', 6, 2, 0, 10000, 1000, CURRENT_DATE + INTERVAL '3 years');

-- 1. Родительский маршрут (уровень 1)
INSERT INTO ROUTES (
    ROUTE_ID,
    ROUTE_GUID,
    ROUTE_TYPES_ID,
    PARENT_ROUTE_ID,
    ROUTE_NAME,
    DESCRIPTION,
    ROUTE_OBJECT,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    1,
    'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11',
    1, -- Маршрут (тип 1)
    NULL, -- Основной маршрут без родителя (уровень 1)
    'Автобусный маршрут №25 "Центр-Север"',
    'Основной городской маршрут от ЖД вокзала до Северного района через центр города',
    '{
        "vehicle_type": "bus",
        "total_stops": 12,
        "total_length_km": 15.3,
        "total_travel_time_minutes": 52,
        "operating_hours": {"first_departure": "06:00", "last_departure": "23:00"},
        "base_fare": 50,
        "interval_minutes": 12,
        "is_active": true,
        "requires_driver_shift": true
    }'::jsonb,
    '2024-01-15 09:00:00',
    '2024-01-15 09:00:00'
);

-- 2-5. Пути как дочерние элементы маршрута (уровень 2, тип 2 - Путь)
-- Путь 1: Центральный сегмент
INSERT INTO ROUTES (
    ROUTE_ID,
    ROUTE_GUID,
    ROUTE_TYPES_ID,
    PARENT_ROUTE_ID,
    ROUTE_NAME,
    DESCRIPTION,
    ROUTE_OBJECT,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    2,
    'b1ffc999-9c0b-4ef8-bb6d-6bb9bd380a12',
    2, -- Путь (тип 2)
    1, -- Дочерний от маршрута 1
    'Центральный сегмент',
    'Начальный участок маршрута через исторический центр города',
    '{
        "segment_type": "central",
        "segment_order": 1,
        "from_stop": "ЖД Вокзал",
        "to_stop": "Площадь Ленина",
        "intermediate_stops": ["Улица Мира", "Центральный рынок"],
        "length_km": 3.2,
        "travel_time_minutes": 10,
        "zones": ["A"],
        "fare_segment": 50,
        "is_boarding_allowed": true,
        "is_alighting_allowed": true
    }'::jsonb,
    '2024-01-15 09:05:00',
    '2024-01-15 09:05:00'
);

-- Путь 2: Торговый сегмент
INSERT INTO ROUTES (
    ROUTE_ID,
    ROUTE_GUID,
    ROUTE_TYPES_ID,
    PARENT_ROUTE_ID,
    ROUTE_NAME,
    DESCRIPTION,
    ROUTE_OBJECT,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    3,
    'c2eecc99-9c0b-4ef8-bb6d-6bb9bd380a13',
    2, -- Путь (тип 2)
    1, -- Дочерний от маршрута 1
    'Торговый сегмент',
    'Участок через торговые районы и бизнес-центры',
    '{
        "segment_type": "commercial",
        "segment_order": 2,
        "from_stop": "Площадь Ленина",
        "to_stop": "Торговый центр",
        "intermediate_stops": ["Банковский квартал", "Офисный район"],
        "length_km": 4.1,
        "travel_time_minutes": 15,
        "zones": ["A", "B"],
        "fare_segment": 75,
        "is_boarding_allowed": true,
        "is_alighting_allowed": true,
        "peak_hours_congestion": "high"
    }'::jsonb,
    '2024-01-15 09:10:00',
    '2024-01-15 09:10:00'
);

-- Путь 3: Жилой сегмент
INSERT INTO ROUTES (
    ROUTE_ID,
    ROUTE_GUID,
    ROUTE_TYPES_ID,
    PARENT_ROUTE_ID,
    ROUTE_NAME,
    DESCRIPTION,
    ROUTE_OBJECT,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    4,
    'd3ffdd99-9c0b-4ef8-bb6d-6bb9bd380a14',
    2, -- Путь (тип 2)
    1, -- Дочерний от маршрута 1
    'Жилой сегмент',
    'Участок через спальные районы и жилые массивы',
    '{
        "segment_type": "residential",
        "segment_order": 3,
        "from_stop": "Торговый центр",
        "to_stop": "Микрорайон Северный-1",
        "intermediate_stops": ["Школа №15", "Поликлиника", "Спортивный комплекс"],
        "length_km": 5.8,
        "travel_time_minutes": 18,
        "zones": ["B"],
        "fare_segment": 100,
        "is_boarding_allowed": true,
        "is_alighting_allowed": true,
        "passenger_flow": "medium"
    }'::jsonb,
    '2024-01-15 09:15:00',
    '2024-01-15 09:15:00'
);

-- Путь 4: Конечный сегмент
INSERT INTO ROUTES (
    ROUTE_ID,
    ROUTE_GUID,
    ROUTE_TYPES_ID,
    PARENT_ROUTE_ID,
    ROUTE_NAME,
    DESCRIPTION,
    ROUTE_OBJECT,
    CREATED_AT,
    UPDATED_AT
) VALUES (
    5,
    'e4eeee99-9c0b-4ef8-bb6d-6bb9bd380a15',
    2, -- Путь (тип 2)
    1, -- Дочерний от маршрута 1
    'Конечный сегмент',
    'Финальный участок маршрута до северной конечной остановки',
    '{
        "segment_type": "final",
        "segment_order": 4,
        "from_stop": "Микрорайон Северный-1",
        "to_stop": "Конечная Северная",
        "intermediate_stops": ["Микрорайон Северный-2", "Автостанция Северная"],
        "length_km": 2.2,
        "travel_time_minutes": 9,
        "zones": ["B"],
        "fare_segment": 50,
        "is_boarding_allowed": true,
        "is_alighting_allowed": true,
        "is_terminal_stop": true
    }'::jsonb,
    '2024-01-15 09:20:00',
    '2024-01-15 09:20:00'
);


-- Привязка всех маршрутов (родительского и путей) к перевозчику "Крымские тачанки"
INSERT INTO CARRIER_ROUTE_MAP (
    CARRIER_ROUTE_MAP_ID,
    CARRIER_ID,
    ROUTE_ID,
    CREATED_AT,
    UPDATED_AT
) VALUES
    -- Привязка родительского маршрута (основной маршрут)
    (1, 1, 1, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
    -- Привязка путей (сегментов маршрута)
    (2, 1, 2, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
    (3, 1, 3, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
    (4, 1, 4, '2024-01-15 10:00:00', '2024-01-15 10:00:00'),
    (5, 1, 5, '2024-01-15 10:00:00', '2024-01-15 10:00:00');

INSERT INTO FARE_ZONES (ZONE_ID, ZONE_CODE, ZONE_NAME, DESCRIPTION) VALUES
    (1, 'С', 'Север', 'Север'),
    (2, 'Ю', 'Юг', 'Юг'),
    (3, 'З', 'Запад', 'Запад'),
    (4, 'В', 'Восток', 'Восток');

-- Привязка перевозчика "Крымские тачанки" к зонам оплаты
INSERT INTO CARRIER_ZONE_MAP (
    CARRIER_ZONE_MAP_ID,
    CARRIER_ID,
    ZONE_ID,
    BASE_FARE,
    CREATED_AT,
    UPDATED_AT
) VALUES
    -- Северная зона - базовый тариф 50 руб.
    (1, 1, 1, 50.00, '2024-01-15 11:00:00', '2024-01-15 11:00:00'),
    -- Южная зона - базовый тариф 55 руб.
    (2, 1, 2, 55.00, '2024-01-15 11:00:00', '2024-01-15 11:00:00'),
    -- Западная зона - базовый тариф 60 руб.
    (3, 1, 3, 60.00, '2024-01-15 11:00:00', '2024-01-15 11:00:00'),
    -- Восточная зона - базовый тариф 65 руб.
    (4, 1, 4, 65.00, '2024-01-15 11:00:00', '2024-01-15 11:00:00');

-- Генерация остановок для каждой зоны оплаты
-- Используем координаты Крыма в качестве базы

-- 1. Северная зона (координаты условные для Севера Крыма)
INSERT INTO TRANSPORT_STOPS (
    STOP_ID, FARE_ZONE_ID, STOP_CODE, STOP_NAME, STOP_ADDRESS,
    GEO_LOCATION, DESCRIPTION, IS_ACTIVE, CREATED_AT, UPDATED_AT
) VALUES
    (1, 1, 'С-001', 'Северный вокзал', 'ул. Вокзальная, 1, Джанкой',
     ST_GeogFromText('POINT(34.3923 45.7098)'), 'Главный вокзал северного направления', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (2, 1, 'С-002', 'Автостанция "Северная"', 'пр. Победы, 15, Джанкой',
     ST_GeogFromText('POINT(34.3987 45.7123)'), 'Автовокзал северной части города', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (3, 1, 'С-003', 'Рынок "Северный"', 'ул. Рыночная, 25, Джанкой',
     ST_GeogFromText('POINT(34.4012 45.7089)'), 'Центральный рынок северного района', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (4, 1, 'С-004', 'Микрорайон "Северный"', 'ул. Северная, 45, Джанкой',
     ST_GeogFromText('POINT(34.3956 45.7154)'), 'Спальный район северной части', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (5, 1, 'С-005', 'Школа №1 (Северная)', 'ул. Школьная, 10, Джанкой',
     ST_GeogFromText('POINT(34.3901 45.7105)'), 'Старейшая школа северного района', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00');

-- 2. Южная зона (координаты для ЮБК)
INSERT INTO TRANSPORT_STOPS (
    STOP_ID, FARE_ZONE_ID, STOP_CODE, STOP_NAME, STOP_ADDRESS,
    GEO_LOCATION, DESCRIPTION, IS_ACTIVE, CREATED_AT, UPDATED_AT
) VALUES
    (6, 2, 'Ю-001', 'Ялтинский автовокзал', 'ул. Московская, 8, Ялта',
     ST_GeogFromText('POINT(34.1641 44.4953)'), 'Главный автовокзал ЮБК', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (7, 2, 'Ю-002', 'Набережная Ялты', 'Набережная им. Ленина, 1, Ялта',
     ST_GeogFromText('POINT(34.1665 44.4921)'), 'Центральная набережная курорта', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (8, 2, 'Ю-003', 'Массандра (винзавод)', 'ул. Винодела Егорова, 9, Массандра',
     ST_GeogFromText('POINT(34.1889 44.5182)'), 'Знаменитый винзавод Массандра', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (9, 2, 'Ю-004', 'Ливадийский дворец', 'ул. Батурина, 44, Ливадия',
     ST_GeogFromText('POINT(34.1435 44.4675)'), 'Бывшая резиденция русских царей', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (10, 2, 'Ю-005', 'Ай-Петри (канатная дорога)', 'пос. Кореиз, канатная дорога',
     ST_GeogFromText('POINT(34.0567 44.4511)'), 'Нижняя станция канатной дороги', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00');

-- 3. Западная зона (координаты для Западного Крыма)
INSERT INTO TRANSPORT_STOPS (
    STOP_ID, FARE_ZONE_ID, STOP_CODE, STOP_NAME, STOP_ADDRESS,
    GEO_LOCATION, DESCRIPTION, IS_ACTIVE, CREATED_AT, UPDATED_AT
) VALUES
    (11, 3, 'З-001', 'Севастополь (Автовокзал)', 'ул. Вокзальная, 12, Севастополь',
     ST_GeogFromText('POINT(33.5224 44.6166)'), 'Центральный автовокзал Севастополя', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (12, 3, 'З-002', 'Площадь Нахимова', 'пл. Нахимова, Севастополь',
     ST_GeogFromText('POINT(33.5243 44.6168)'), 'Центральная площадь города-героя', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (13, 3, 'З-003', 'Херсонес Таврический', 'ул. Древняя, 1, Севастополь',
     ST_GeogFromText('POINT(33.4912 44.6105)'), 'Древнегреческий полис, музей-заповедник', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (14, 3, 'З-004', 'Балаклава (набережная)', 'наб. Назукина, Балаклава',
     ST_GeogFromText('POINT(33.5954 44.5004)'), 'Историческая набережная Балаклавы', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (15, 3, 'З-005', 'Инкерман (винзавод)', 'ул. Малиновского, 1, Инкерман',
     ST_GeogFromText('POINT(33.6087 44.6140)'), 'Знаменитый инкерманский завод марочных вин', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00');

-- 4. Восточная зона (координаты для Восточного Крыма)
INSERT INTO TRANSPORT_STOPS (
    STOP_ID, FARE_ZONE_ID, STOP_CODE, STOP_NAME, STOP_ADDRESS,
    GEO_LOCATION, DESCRIPTION, IS_ACTIVE, CREATED_AT, UPDATED_AT
) VALUES
    (16, 4, 'В-001', 'Феодосийский автовокзал', 'ул. Федько, 32, Феодосия',
     ST_GeogFromText('POINT(35.3822 45.0489)'), 'Автовокзал Феодосии', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (17, 4, 'В-002', 'Ж/д вокзал Феодосия', 'ул. Галерейная, 2, Феодосия',
     ST_GeogFromText('POINT(35.3834 45.0311)'), 'Железнодорожный вокзал города', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (18, 4, 'В-003', 'Картинная галерея Айвазовского', 'ул. Галерейная, 2, Феодосия',
     ST_GeogFromText('POINT(35.3828 45.0325)'), 'Знаменитая галерея мариниста', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (19, 4, 'В-004', 'Коктебель (автостанция)', 'ул. Ленина, 109, Коктебель',
     ST_GeogFromText('POINT(35.2458 44.9633)'), 'Автостанция курортного поселка', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00'),

    (20, 4, 'В-005', 'Судакская крепость', 'ул. Генуэзская крепость, 1, Судак',
     ST_GeogFromText('POINT(34.9576 44.8416)'), 'Генуэзская крепость - музей-заповедник', true, '2024-01-01 09:00:00', '2024-01-01 09:00:00');

-- 1. Центральный сегмент (ROUTE_ID = 2) — 4 остановки (Северная зона)
INSERT INTO STOPS_ROUTE_MAP (STOP_ID, ROUTE_ID, SERIAL_NUMBER, CREATED_AT, UPDATED_AT)
VALUES
  (1, 2, 1, NOW(), NOW()), -- Северный вокзал
  (3, 2, 2, NOW(), NOW()), -- Рынок "Северный"
  (5, 2, 3, NOW(), NOW()), -- Школа №1 (Северная)
  (2, 2, 4, NOW(), NOW()); -- Автостанция "Северная"

-- 2. Торговый сегмент (ROUTE_ID = 3) — 4 остановки (Западная зона)
INSERT INTO STOPS_ROUTE_MAP (STOP_ID, ROUTE_ID, SERIAL_NUMBER, CREATED_AT, UPDATED_AT)
VALUES
  (11, 3, 1, NOW(), NOW()), -- Севастополь (Автовокзал)
  (12, 3, 2, NOW(), NOW()), -- Площадь Нахимова
  (13, 3, 3, NOW(), NOW()), -- Херсонес Таврический
  (14, 3, 4, NOW(), NOW()); -- Балаклава (набережная)

-- 3. Жилой сегмент (ROUTE_ID = 4) — 5 остановок (Южная зона)
INSERT INTO STOPS_ROUTE_MAP (STOP_ID, ROUTE_ID, SERIAL_NUMBER, CREATED_AT, UPDATED_AT)
VALUES
  (6, 4, 1, NOW(), NOW()),  -- Ялтинский автовокзал
  (7, 4, 2, NOW(), NOW()),  -- Набережная Ялты
  (8, 4, 3, NOW(), NOW()),  -- Массандра (винзавод)
  (9, 4, 4, NOW(), NOW()),  -- Ливадийский дворец
  (10, 4, 5, NOW(), NOW()); -- Ай-Петри (канатная дорога)

-- 4. Конечный сегмент (ROUTE_ID = 5) — 4 остановки (Восточная зона)
INSERT INTO STOPS_ROUTE_MAP (STOP_ID, ROUTE_ID, SERIAL_NUMBER, CREATED_AT, UPDATED_AT)
VALUES
  (16, 5, 1, NOW(), NOW()), -- Феодосийский автовокзал
  (17, 5, 2, NOW(), NOW()), -- Ж/д вокзал Феодосия
  (18, 5, 3, NOW(), NOW()), -- Картинная галерея Айвазовского
  (19, 5, 4, NOW(), NOW()); -- Коктебель (автостанция)

-- 5. Основной маршрут (ROUTE_ID = 1) — все 20 остановок подряд
INSERT INTO STOPS_ROUTE_MAP (STOP_ID, ROUTE_ID, SERIAL_NUMBER, CREATED_AT, UPDATED_AT)
SELECT
    STOP_ID,
    1 AS ROUTE_ID,
    ROW_NUMBER() OVER (ORDER BY STOP_ID) AS SERIAL_NUMBER,
    NOW() AS CREATED_AT,
    NOW() AS UPDATED_AT
FROM TRANSPORT_STOPS
ORDER BY STOP_ID;
