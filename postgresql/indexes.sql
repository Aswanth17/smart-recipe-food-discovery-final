-- ============================================================
-- SMART RECIPE & FOOD DISCOVERY SYSTEM
-- Relational Indexes
-- ============================================================


-- Users

CREATE INDEX IF NOT EXISTS idx_users_email
ON users(email);


CREATE INDEX IF NOT EXISTS idx_users_role
ON users(role);


-- Recipe metadata

CREATE INDEX IF NOT EXISTS idx_recipe_metadata_category
ON recipe_metadata(category_id);


CREATE INDEX IF NOT EXISTS idx_recipe_metadata_cuisine
ON recipe_metadata(cuisine_id);


CREATE INDEX IF NOT EXISTS idx_recipe_metadata_diet
ON recipe_metadata(dietary_type);


-- Restaurants

CREATE INDEX IF NOT EXISTS idx_restaurants_name
ON restaurants(name);


CREATE INDEX IF NOT EXISTS idx_restaurants_rating
ON restaurants(rating DESC);


-- Restaurant dishes

CREATE INDEX IF NOT EXISTS idx_restaurant_dishes_restaurant
ON restaurant_dishes(restaurant_id);


CREATE INDEX IF NOT EXISTS idx_restaurant_dishes_recipe
ON restaurant_dishes(recipe_id);


CREATE INDEX IF NOT EXISTS idx_restaurant_dishes_available
ON restaurant_dishes(is_available);


-- Ratings

CREATE INDEX IF NOT EXISTS idx_ratings_recipe
ON ratings(recipe_id);


CREATE INDEX IF NOT EXISTS idx_ratings_user
ON ratings(user_id);


CREATE INDEX IF NOT EXISTS idx_ratings_created_at
ON ratings(created_at);


-- Reviews

CREATE INDEX IF NOT EXISTS idx_reviews_recipe
ON reviews(recipe_id);


CREATE INDEX IF NOT EXISTS idx_reviews_user
ON reviews(user_id);


-- Activity

CREATE INDEX IF NOT EXISTS idx_activity_user
ON activity_events(user_id);


CREATE INDEX IF NOT EXISTS idx_activity_recipe
ON activity_events(recipe_id);


CREATE INDEX IF NOT EXISTS idx_activity_restaurant
ON activity_events(restaurant_id);


CREATE INDEX IF NOT EXISTS idx_activity_event_type
ON activity_events(event_type);


CREATE INDEX IF NOT EXISTS idx_activity_created_at
ON activity_events(created_at);


CREATE INDEX IF NOT EXISTS idx_activity_metadata
ON activity_events
USING GIN(metadata);