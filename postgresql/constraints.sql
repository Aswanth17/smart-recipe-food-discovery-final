-- ============================================================
-- SMART RECIPE & FOOD DISCOVERY SYSTEM
-- Relational Constraints
-- ============================================================


-- ============================================================
-- USERS
-- ============================================================

ALTER TABLE users
DROP CONSTRAINT IF EXISTS chk_users_role;

ALTER TABLE users
ADD CONSTRAINT chk_users_role
CHECK (role IN ('USER', 'ADMIN'));


ALTER TABLE users
DROP CONSTRAINT IF EXISTS chk_users_email;

ALTER TABLE users
ADD CONSTRAINT chk_users_email
CHECK (
    email LIKE '%_@_%._%'
);


-- ============================================================
-- RECIPE METADATA FOREIGN KEYS
-- ============================================================

ALTER TABLE recipe_metadata
DROP CONSTRAINT IF EXISTS fk_recipe_category;

ALTER TABLE recipe_metadata
ADD CONSTRAINT fk_recipe_category
FOREIGN KEY (category_id)
REFERENCES categories(category_id);


ALTER TABLE recipe_metadata
DROP CONSTRAINT IF EXISTS fk_recipe_cuisine;

ALTER TABLE recipe_metadata
ADD CONSTRAINT fk_recipe_cuisine
FOREIGN KEY (cuisine_id)
REFERENCES cuisines(cuisine_id);


ALTER TABLE recipe_metadata
DROP CONSTRAINT IF EXISTS chk_recipe_metadata_id;

ALTER TABLE recipe_metadata
ADD CONSTRAINT chk_recipe_metadata_id
CHECK (
    recipe_id ~ '^R[0-9]{3}$'
);


ALTER TABLE recipe_metadata
DROP CONSTRAINT IF EXISTS chk_recipe_dietary_type;

ALTER TABLE recipe_metadata
ADD CONSTRAINT chk_recipe_dietary_type
CHECK (
    dietary_type IN (
        'Vegetarian',
        'Non-Vegetarian',
        'Vegan'
    )
);


-- ============================================================
-- RESTAURANTS
-- ============================================================

ALTER TABLE restaurants
DROP CONSTRAINT IF EXISTS chk_restaurant_rating;

ALTER TABLE restaurants
ADD CONSTRAINT chk_restaurant_rating
CHECK (
    rating >= 0
    AND rating <= 5
);


-- ============================================================
-- RESTAURANT DISHES
-- ============================================================

ALTER TABLE restaurant_dishes
DROP CONSTRAINT IF EXISTS fk_restaurant_dish_restaurant;

ALTER TABLE restaurant_dishes
ADD CONSTRAINT fk_restaurant_dish_restaurant
FOREIGN KEY (restaurant_id)
REFERENCES restaurants(restaurant_id)
ON DELETE CASCADE;


ALTER TABLE restaurant_dishes
DROP CONSTRAINT IF EXISTS fk_restaurant_dish_recipe;

ALTER TABLE restaurant_dishes
ADD CONSTRAINT fk_restaurant_dish_recipe
FOREIGN KEY (recipe_id)
REFERENCES recipe_metadata(recipe_id);


ALTER TABLE restaurant_dishes
DROP CONSTRAINT IF EXISTS chk_restaurant_dish_price;

ALTER TABLE restaurant_dishes
ADD CONSTRAINT chk_restaurant_dish_price
CHECK (price >= 0);


-- ============================================================
-- RATINGS
-- ============================================================

ALTER TABLE ratings
DROP CONSTRAINT IF EXISTS fk_rating_user;

ALTER TABLE ratings
ADD CONSTRAINT fk_rating_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;


ALTER TABLE ratings
DROP CONSTRAINT IF EXISTS fk_rating_recipe;

ALTER TABLE ratings
ADD CONSTRAINT fk_rating_recipe
FOREIGN KEY (recipe_id)
REFERENCES recipe_metadata(recipe_id);


ALTER TABLE ratings
DROP CONSTRAINT IF EXISTS chk_rating_value;

ALTER TABLE ratings
ADD CONSTRAINT chk_rating_value
CHECK (
    rating BETWEEN 1 AND 5
);


-- ============================================================
-- REVIEWS
-- ============================================================

ALTER TABLE reviews
DROP CONSTRAINT IF EXISTS fk_review_user;

ALTER TABLE reviews
ADD CONSTRAINT fk_review_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE CASCADE;


ALTER TABLE reviews
DROP CONSTRAINT IF EXISTS fk_review_recipe;

ALTER TABLE reviews
ADD CONSTRAINT fk_review_recipe
FOREIGN KEY (recipe_id)
REFERENCES recipe_metadata(recipe_id);


ALTER TABLE reviews
DROP CONSTRAINT IF EXISTS chk_review_text;

ALTER TABLE reviews
ADD CONSTRAINT chk_review_text
CHECK (
    char_length(trim(review_text)) >= 3
);


-- ============================================================
-- ACTIVITY EVENTS
-- ============================================================

ALTER TABLE activity_events
DROP CONSTRAINT IF EXISTS fk_activity_user;

ALTER TABLE activity_events
ADD CONSTRAINT fk_activity_user
FOREIGN KEY (user_id)
REFERENCES users(user_id)
ON DELETE SET NULL;


ALTER TABLE activity_events
DROP CONSTRAINT IF EXISTS fk_activity_restaurant;

ALTER TABLE activity_events
ADD CONSTRAINT fk_activity_restaurant
FOREIGN KEY (restaurant_id)
REFERENCES restaurants(restaurant_id)
ON DELETE SET NULL;


ALTER TABLE activity_events
DROP CONSTRAINT IF EXISTS fk_activity_recipe;

ALTER TABLE activity_events
ADD CONSTRAINT fk_activity_recipe
FOREIGN KEY (recipe_id)
REFERENCES recipe_metadata(recipe_id);


ALTER TABLE activity_events
DROP CONSTRAINT IF EXISTS chk_activity_event_type;

ALTER TABLE activity_events
ADD CONSTRAINT chk_activity_event_type
CHECK (
    event_type IN (
        'LOGIN',
        'LOGOUT',
        'RECIPE_VIEW',
        'RECIPE_SEARCH',
        'FAVORITE_ADD',
        'FAVORITE_REMOVE',
        'RATING_SUBMIT',
        'REVIEW_SUBMIT',
        'RECOMMENDATION_VIEW',
        'RESTAURANT_SEARCH',
        'RESTAURANT_VIEW'
    )
);