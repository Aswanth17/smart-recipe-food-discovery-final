db = db.getSiblingDB("smart_recipe_users");


// ============================================================
// USERS INDEXES
// ============================================================

db.users.createIndex(
    { userId: 1 },
    {
        unique: true,
        name: "ux_users_userId"
    }
);

db.users.createIndex(
    { email: 1 },
    {
        unique: true,
        name: "ux_users_email"
    }
);


// ============================================================
// PREFERENCES INDEXES
// ============================================================

db.preferences.createIndex(
    { userId: 1 },
    {
        unique: true,
        name: "ux_preferences_userId"
    }
);

db.preferences.createIndex(
    { preferredCuisines: 1 },
    {
        name: "idx_preferences_cuisines"
    }
);

db.preferences.createIndex(
    { favoriteIngredients: 1 },
    {
        name: "idx_preferences_favoriteIngredients"
    }
);


// ============================================================
// FAVORITES INDEXES
// ============================================================

db.favorites.createIndex(
    { userId: 1 },
    {
        unique: true,
        name: "ux_favorites_userId"
    }
);

db.favorites.createIndex(
    { recipeIds: 1 },
    {
        name: "idx_favorites_recipeIds"
    }
);


// ============================================================
// INTERACTIONS INDEXES
// ============================================================

db.interactions.createIndex(
    { userId: 1 },
    {
        name: "idx_interactions_userId"
    }
);

db.interactions.createIndex(
    {
        userId: 1,
        createdAt: -1
    },
    {
        name: "idx_interactions_user_date"
    }
);

db.interactions.createIndex(
    { recipeId: 1 },
    {
        name: "idx_interactions_recipeId"
    }
);

db.interactions.createIndex(
    { action: 1 },
    {
        name: "idx_interactions_action"
    }
);


print("MongoDB indexes created successfully.");