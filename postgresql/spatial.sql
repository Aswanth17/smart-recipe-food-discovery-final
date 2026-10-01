-- ============================================================
-- SMART RECIPE & FOOD DISCOVERY SYSTEM
-- POSTGIS SPATIAL IMPLEMENTATION
-- Database: smart_recipe_db
--
-- IMPORTANT:
-- Restaurant coordinates are DEMO/SAMPLE DATA for the project.
-- Spatial calculations are performed by PostGIS.
-- ============================================================


-- ============================================================
-- 1. ENABLE POSTGIS
-- ============================================================

CREATE EXTENSION IF NOT EXISTS postgis;


-- ============================================================
-- 2. ADD SPATIAL LOCATION COLUMN
--
-- GEOGRAPHY(POINT, 4326)
--
-- POINT      = one geographic location
-- SRID 4326  = WGS84 latitude/longitude coordinate system
-- GEOGRAPHY  = measurements are made over the earth's surface
-- ============================================================

ALTER TABLE restaurants
ADD COLUMN IF NOT EXISTS location GEOGRAPHY(POINT, 4326);


-- Location must exist for every active restaurant after seeding.
-- We add NOT NULL later after the seed locations are inserted.


-- ============================================================
-- 3. SAMPLE RESTAURANTS
--
-- Coordinates are sample/demo points around Coimbatore.
--
-- ST_MakePoint expects:
--     longitude FIRST
--     latitude SECOND
-- ============================================================

INSERT INTO restaurants (
    restaurant_id,
    name,
    address,
    phone,
    rating,
    opening_time,
    closing_time,
    is_active,
    location
)
VALUES

(
    201,
    'Spice Route Kitchen',
    'Gandhipuram, Coimbatore, Tamil Nadu',
    '+91-9000000201',
    4.5,
    '10:00',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9674, 11.0168),
        4326
    )::geography
),

(
    202,
    'Royal Biryani House',
    'RS Puram, Coimbatore, Tamil Nadu',
    '+91-9000000202',
    4.6,
    '11:00',
    '23:30',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9558, 11.0087),
        4326
    )::geography
),

(
    203,
    'Green Leaf Vegetarian',
    'Saibaba Colony, Coimbatore, Tamil Nadu',
    '+91-9000000203',
    4.4,
    '07:30',
    '22:30',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9520, 11.0262),
        4326
    )::geography
),

(
    204,
    'Dragon Wok',
    'Peelamedu, Coimbatore, Tamil Nadu',
    '+91-9000000204',
    4.3,
    '11:30',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(77.0087, 11.0264),
        4326
    )::geography
),

(
    205,
    'Little Italy Corner',
    'Race Course, Coimbatore, Tamil Nadu',
    '+91-9000000205',
    4.7,
    '11:00',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9770, 11.0015),
        4326
    )::geography
),

(
    206,
    'Mexicana Grill',
    'Avinashi Road, Coimbatore, Tamil Nadu',
    '+91-9000000206',
    4.2,
    '12:00',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(77.0025, 11.0184),
        4326
    )::geography
),

(
    207,
    'Continental Cafe',
    'Lakshmi Mills, Coimbatore, Tamil Nadu',
    '+91-9000000207',
    4.4,
    '08:00',
    '22:30',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9928, 11.0126),
        4326
    )::geography
),

(
    208,
    'Tandoori Junction',
    'Town Hall, Coimbatore, Tamil Nadu',
    '+91-9000000208',
    4.3,
    '11:00',
    '23:30',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9612, 10.9946),
        4326
    )::geography
),

(
    209,
    'South Spice Cafe',
    'Ramanathapuram, Coimbatore, Tamil Nadu',
    '+91-9000000209',
    4.5,
    '07:00',
    '22:30',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(77.0101, 10.9987),
        4326
    )::geography
),

(
    210,
    'Urban Bowl',
    'Singanallur, Coimbatore, Tamil Nadu',
    '+91-9000000210',
    4.1,
    '10:30',
    '22:30',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(77.0294, 11.0007),
        4326
    )::geography
),

(
    211,
    'Pasta Street',
    'Hope College, Coimbatore, Tamil Nadu',
    '+91-9000000211',
    4.6,
    '11:00',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(77.0386, 11.0251),
        4326
    )::geography
),

(
    212,
    'Healthy Harvest',
    'Saravanampatti, Coimbatore, Tamil Nadu',
    '+91-9000000212',
    4.4,
    '08:00',
    '22:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9993, 11.0802),
        4326
    )::geography
),

(
    213,
    'Wok and Roll',
    'Ganapathy, Coimbatore, Tamil Nadu',
    '+91-9000000213',
    4.2,
    '11:00',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9977, 11.0454),
        4326
    )::geography
),

(
    214,
    'Casa Mexico',
    'Vadavalli, Coimbatore, Tamil Nadu',
    '+91-9000000214',
    4.3,
    '12:00',
    '23:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9067, 11.0259),
        4326
    )::geography
),

(
    215,
    'Basil and Bread',
    'Ukkadam, Coimbatore, Tamil Nadu',
    '+91-9000000215',
    4.1,
    '09:00',
    '22:00',
    TRUE,
    ST_SetSRID(
        ST_MakePoint(76.9623, 10.9865),
        4326
    )::geography
)

ON CONFLICT (restaurant_id)
DO UPDATE SET

    name = EXCLUDED.name,
    address = EXCLUDED.address,
    phone = EXCLUDED.phone,
    rating = EXCLUDED.rating,
    opening_time = EXCLUDED.opening_time,
    closing_time = EXCLUDED.closing_time,
    is_active = EXCLUDED.is_active,
    location = EXCLUDED.location;


-- ============================================================
-- 4. MAKE LOCATION REQUIRED
-- ============================================================

ALTER TABLE restaurants
ALTER COLUMN location SET NOT NULL;


-- ============================================================
-- 5. RESTAURANT-DISH RELATIONSHIPS
--
-- recipe_id references recipe_metadata.
--
-- Complete recipe content remains in BaseX.
-- ============================================================

INSERT INTO restaurant_dishes (
    restaurant_id,
    recipe_id,
    dish_name,
    price,
    is_available
)
VALUES

-- Spice Route Kitchen
(201, 'R001', 'Chicken Biryani',       280.00, TRUE),
(201, 'R002', 'Paneer Butter Masala', 240.00, TRUE),
(201, 'R005', 'Chana Masala',         180.00, TRUE),
(201, 'R027', 'Paneer Tikka',         220.00, TRUE),

-- Royal Biryani House
(202, 'R001', 'Chicken Biryani',       320.00, TRUE),
(202, 'R006', 'Chicken Tikka',         290.00, TRUE),
(202, 'R003', 'Vegetable Pulao',       200.00, TRUE),

-- Green Leaf Vegetarian
(203, 'R002', 'Paneer Butter Masala', 230.00, TRUE),
(203, 'R003', 'Vegetable Pulao',      190.00, TRUE),
(203, 'R004', 'Palak Paneer',         220.00, TRUE),
(203, 'R005', 'Chana Masala',         170.00, TRUE),
(203, 'R007', 'Masala Dosa',          110.00, TRUE),

-- Dragon Wok
(204, 'R008', 'Egg Fried Rice',       190.00, TRUE),
(204, 'R009', 'Vegetable Noodles',    180.00, TRUE),
(204, 'R010', 'Chilli Chicken',       260.00, TRUE),
(204, 'R025', 'Chinese Vegetable Soup',150.00, TRUE),
(204, 'R026', 'Kung Pao Chicken',     290.00, TRUE),

-- Little Italy Corner
(205, 'R011', 'Margherita Pizza',     310.00, TRUE),
(205, 'R012', 'Spaghetti Aglio e Olio',280.00, TRUE),
(205, 'R013', 'Penne Arrabbiata',     290.00, TRUE),
(205, 'R014', 'Chicken Alfredo Pasta',360.00, TRUE),
(205, 'R015', 'Vegetable Lasagna',    340.00, TRUE),

-- Mexicana Grill
(206, 'R016', 'Chicken Tacos',        270.00, TRUE),
(206, 'R017', 'Vegetable Burrito',    240.00, TRUE),
(206, 'R018', 'Cheese Quesadilla',    230.00, TRUE),
(206, 'R019', 'Mexican Bean Salad',   210.00, TRUE),
(206, 'R030', 'Mexican Rice',         220.00, TRUE),

-- Continental Cafe
(207, 'R020', 'Grilled Chicken Salad',310.00, TRUE),
(207, 'R021', 'Cream of Mushroom Soup',190.00, TRUE),
(207, 'R022', 'French Toast',         170.00, TRUE),
(207, 'R023', 'Vegan Buddha Bowl',    290.00, TRUE),
(207, 'R024', 'Garlic Bread',         140.00, TRUE),
(207, 'R029', 'Avocado Toast',        210.00, TRUE),

-- Tandoori Junction
(208, 'R001', 'Chicken Biryani',      300.00, TRUE),
(208, 'R006', 'Chicken Tikka',        280.00, TRUE),
(208, 'R027', 'Paneer Tikka',         240.00, TRUE),

-- South Spice Cafe
(209, 'R003', 'Vegetable Pulao',      180.00, TRUE),
(209, 'R005', 'Chana Masala',         170.00, TRUE),
(209, 'R007', 'Masala Dosa',          100.00, TRUE),

-- Urban Bowl
(210, 'R019', 'Mexican Bean Salad',   220.00, TRUE),
(210, 'R020', 'Grilled Chicken Salad',290.00, TRUE),
(210, 'R023', 'Vegan Buddha Bowl',    270.00, TRUE),
(210, 'R029', 'Avocado Toast',        200.00, TRUE),

-- Pasta Street
(211, 'R011', 'Margherita Pizza',     300.00, TRUE),
(211, 'R013', 'Penne Arrabbiata',     270.00, TRUE),
(211, 'R014', 'Chicken Alfredo Pasta',350.00, TRUE),
(211, 'R028', 'Tomato Basil Pasta',   260.00, TRUE),

-- Healthy Harvest
(212, 'R019', 'Mexican Bean Salad',   200.00, TRUE),
(212, 'R023', 'Vegan Buddha Bowl',    260.00, TRUE),
(212, 'R029', 'Avocado Toast',        190.00, TRUE),

-- Wok and Roll
(213, 'R008', 'Egg Fried Rice',       180.00, TRUE),
(213, 'R009', 'Vegetable Noodles',    170.00, TRUE),
(213, 'R010', 'Chilli Chicken',       250.00, TRUE),
(213, 'R026', 'Kung Pao Chicken',     280.00, TRUE),

-- Casa Mexico
(214, 'R016', 'Chicken Tacos',        260.00, TRUE),
(214, 'R017', 'Vegetable Burrito',    230.00, TRUE),
(214, 'R018', 'Cheese Quesadilla',    220.00, TRUE),
(214, 'R030', 'Mexican Rice',         210.00, TRUE),

-- Basil and Bread
(215, 'R011', 'Margherita Pizza',     290.00, TRUE),
(215, 'R012', 'Spaghetti Aglio e Olio',260.00, TRUE),
(215, 'R024', 'Garlic Bread',         130.00, TRUE),
(215, 'R028', 'Tomato Basil Pasta',   250.00, TRUE)

ON CONFLICT (restaurant_id, recipe_id)
DO UPDATE SET

    dish_name = EXCLUDED.dish_name,
    price = EXCLUDED.price,
    is_available = EXCLUDED.is_available;


-- ============================================================
-- 6. SPATIAL INDEX
--
-- GiST allows PostGIS to efficiently search geographical areas.
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_restaurants_location_gist
ON restaurants
USING GIST(location);


-- Other useful index

CREATE INDEX IF NOT EXISTS idx_restaurant_dishes_recipe_available
ON restaurant_dishes(recipe_id, is_available);


-- ============================================================
-- 7. KEEP IDENTITY SEQUENCE CORRECT
-- ============================================================

SELECT setval(
    pg_get_serial_sequence('restaurants', 'restaurant_id'),
    COALESCE((SELECT MAX(restaurant_id) FROM restaurants), 1),
    TRUE
);


-- ============================================================
-- 8. FUNCTION:
-- FIND GENERAL NEARBY RESTAURANTS
--
-- radius is supplied in METERS.
-- ============================================================

CREATE OR REPLACE FUNCTION find_nearby_restaurants(
    p_latitude DOUBLE PRECISION,
    p_longitude DOUBLE PRECISION,
    p_radius_m INTEGER
)
RETURNS TABLE (

    restaurant_id BIGINT,
    restaurant_name VARCHAR,
    restaurant_address VARCHAR,
    restaurant_rating NUMERIC,
    opening_time TIME,
    closing_time TIME,
    is_open BOOLEAN,
    distance_meters NUMERIC

)
LANGUAGE SQL
STABLE
AS $$

    SELECT

        r.restaurant_id,

        r.name,

        r.address,

        r.rating,

        r.opening_time,

        r.closing_time,

        CASE
            WHEN LOCALTIME BETWEEN r.opening_time AND r.closing_time
            THEN TRUE
            ELSE FALSE
        END AS is_open,

        ROUND(
            ST_Distance(
                r.location,

                ST_SetSRID(
                    ST_MakePoint(
                        p_longitude,
                        p_latitude
                    ),
                    4326
                )::geography

            )::numeric,
            2
        ) AS distance_meters

    FROM restaurants r

    WHERE
        r.is_active = TRUE

        AND ST_DWithin(

            r.location,

            ST_SetSRID(
                ST_MakePoint(
                    p_longitude,
                    p_latitude
                ),
                4326
            )::geography,

            p_radius_m
        )

    ORDER BY distance_meters;

$$;


-- ============================================================
-- 9. FUNCTION:
-- FIND RESTAURANTS SERVING A SELECTED RECIPE/DISH
-- WITHIN A SPECIFIED RADIUS
-- ============================================================

CREATE OR REPLACE FUNCTION find_restaurants_serving_recipe(
    p_latitude DOUBLE PRECISION,
    p_longitude DOUBLE PRECISION,
    p_radius_m INTEGER,
    p_recipe_id VARCHAR
)
RETURNS TABLE (

    restaurant_id BIGINT,
    restaurant_name VARCHAR,
    restaurant_address VARCHAR,
    restaurant_rating NUMERIC,
    recipe_id VARCHAR,
    dish_name VARCHAR,
    price NUMERIC,
    is_open BOOLEAN,
    distance_meters NUMERIC

)
LANGUAGE SQL
STABLE
AS $$

    SELECT

        r.restaurant_id,

        r.name,

        r.address,

        r.rating,

        rd.recipe_id,

        rd.dish_name,

        rd.price,

        CASE
            WHEN LOCALTIME BETWEEN r.opening_time AND r.closing_time
            THEN TRUE
            ELSE FALSE
        END AS is_open,

        ROUND(
            ST_Distance(
                r.location,

                ST_SetSRID(
                    ST_MakePoint(
                        p_longitude,
                        p_latitude
                    ),
                    4326
                )::geography

            )::numeric,
            2
        ) AS distance_meters

    FROM restaurants r

    INNER JOIN restaurant_dishes rd
        ON r.restaurant_id = rd.restaurant_id

    WHERE
        r.is_active = TRUE

        AND rd.is_available = TRUE

        AND rd.recipe_id = p_recipe_id

        AND ST_DWithin(

            r.location,

            ST_SetSRID(
                ST_MakePoint(
                    p_longitude,
                    p_latitude
                ),
                4326
            )::geography,

            p_radius_m
        )

    ORDER BY distance_meters;

$$;


-- ============================================================
-- 10. FUNCTION:
-- FIND NEAREST RESTAURANT
-- ============================================================

CREATE OR REPLACE FUNCTION find_nearest_restaurant(
    p_latitude DOUBLE PRECISION,
    p_longitude DOUBLE PRECISION
)
RETURNS TABLE (

    restaurant_id BIGINT,
    restaurant_name VARCHAR,
    restaurant_address VARCHAR,
    restaurant_rating NUMERIC,
    distance_meters NUMERIC

)
LANGUAGE SQL
STABLE
AS $$

    SELECT

        r.restaurant_id,

        r.name,

        r.address,

        r.rating,

        ROUND(
            ST_Distance(
                r.location,

                ST_SetSRID(
                    ST_MakePoint(
                        p_longitude,
                        p_latitude
                    ),
                    4326
                )::geography

            )::numeric,
            2
        )

    FROM restaurants r

    WHERE r.is_active = TRUE

    ORDER BY r.location <->

        ST_SetSRID(
            ST_MakePoint(
                p_longitude,
                p_latitude
            ),
            4326
        )::geography

    LIMIT 1;

$$;


-- ============================================================
-- 11. FUNCTION:
-- FIND NEAREST RESTAURANT SERVING A RECIPE
-- ============================================================

CREATE OR REPLACE FUNCTION find_nearest_restaurant_for_recipe(
    p_latitude DOUBLE PRECISION,
    p_longitude DOUBLE PRECISION,
    p_recipe_id VARCHAR
)
RETURNS TABLE (

    restaurant_id BIGINT,
    restaurant_name VARCHAR,
    restaurant_address VARCHAR,
    restaurant_rating NUMERIC,
    recipe_id VARCHAR,
    dish_name VARCHAR,
    price NUMERIC,
    distance_meters NUMERIC

)
LANGUAGE SQL
STABLE
AS $$

    SELECT

        r.restaurant_id,

        r.name,

        r.address,

        r.rating,

        rd.recipe_id,

        rd.dish_name,

        rd.price,

        ROUND(
            ST_Distance(
                r.location,

                ST_SetSRID(
                    ST_MakePoint(
                        p_longitude,
                        p_latitude
                    ),
                    4326
                )::geography

            )::numeric,
            2
        )

    FROM restaurants r

    INNER JOIN restaurant_dishes rd
        ON r.restaurant_id = rd.restaurant_id

    WHERE
        r.is_active = TRUE

        AND rd.is_available = TRUE

        AND rd.recipe_id = p_recipe_id

    ORDER BY r.location <->

        ST_SetSRID(
            ST_MakePoint(
                p_longitude,
                p_latitude
            ),
            4326
        )::geography

    LIMIT 1;

$$;


-- ============================================================
-- 12. VERIFICATION SUMMARY
-- ============================================================

SELECT
    COUNT(*) AS total_restaurants
FROM restaurants;


SELECT
    COUNT(*) AS total_restaurant_dishes
FROM restaurant_dishes;


SELECT
    COUNT(*) AS restaurants_with_location
FROM restaurants
WHERE location IS NOT NULL;