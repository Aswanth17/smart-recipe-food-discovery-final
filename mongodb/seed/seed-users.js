db = db.getSiblingDB("smart_recipe_users");

db.users.deleteMany({});
db.preferences.deleteMany({});
db.favorites.deleteMany({});
db.interactions.deleteMany({});


// ============================================================
// USERS
// ============================================================

db.users.insertMany([
    {
        userId: 101,
        fullName: "Arun Kumar",
        email: "arun@example.com",
        role: "USER",
        createdAt: new Date()
    },
    {
        userId: 102,
        fullName: "Meera Nair",
        email: "meera@example.com",
        role: "USER",
        createdAt: new Date()
    },
    {
        userId: 103,
        fullName: "Rahul Sharma",
        email: "rahul@example.com",
        role: "USER",
        createdAt: new Date()
    },
    {
        userId: 104,
        fullName: "Priya Iyer",
        email: "priya@example.com",
        role: "USER",
        createdAt: new Date()
    },
    {
        userId: 105,
        fullName: "System Administrator",
        email: "admin@smartrecipe.local",
        role: "ADMIN",
        createdAt: new Date()
    }
]);


// ============================================================
// PREFERENCES
// ============================================================

db.preferences.insertMany([
    {
        userId: 101,
        diet: "Non-Vegetarian",
        preferredCuisines: [
            "Indian",
            "Chinese"
        ],
        maxCalories: 800,
        allergies: [],
        dislikedIngredients: [
            "Mushroom"
        ],
        favoriteIngredients: [
            "Chicken",
            "Rice",
            "Onion"
        ],
        preferredDifficulty: [
            "Easy",
            "Medium"
        ],
        updatedAt: new Date()
    },

    {
        userId: 102,
        diet: "Vegetarian",
        preferredCuisines: [
            "Indian",
            "Italian"
        ],
        maxCalories: 650,
        allergies: [
            "Peanut"
        ],
        dislikedIngredients: [
            "Mushroom"
        ],
        favoriteIngredients: [
            "Paneer",
            "Tomato",
            "Spinach"
        ],
        preferredDifficulty: [
            "Easy",
            "Medium"
        ],
        updatedAt: new Date()
    },

    {
        userId: 103,
        diet: "Non-Vegetarian",
        preferredCuisines: [
            "Chinese",
            "Mexican"
        ],
        maxCalories: 750,
        allergies: [],
        dislikedIngredients: [
            "Avocado"
        ],
        favoriteIngredients: [
            "Chicken",
            "Egg",
            "Capsicum"
        ],
        preferredDifficulty: [
            "Easy",
            "Medium"
        ],
        updatedAt: new Date()
    },

    {
        userId: 104,
        diet: "Vegan",
        preferredCuisines: [
            "Indian",
            "Mexican",
            "Continental"
        ],
        maxCalories: 550,
        allergies: [
            "Dairy"
        ],
        dislikedIngredients: [
            "Egg"
        ],
        favoriteIngredients: [
            "Chickpeas",
            "Avocado",
            "Tomato"
        ],
        preferredDifficulty: [
            "Easy"
        ],
        updatedAt: new Date()
    },

    {
        userId: 105,
        diet: null,
        preferredCuisines: [],
        maxCalories: null,
        allergies: [],
        dislikedIngredients: [],
        favoriteIngredients: [],
        preferredDifficulty: [],
        updatedAt: new Date()
    }
]);


// ============================================================
// FAVORITES
// ============================================================

db.favorites.insertMany([
    {
        userId: 101,
        recipeIds: [
            "R001",
            "R006",
            "R027"
        ],
        updatedAt: new Date()
    },

    {
        userId: 102,
        recipeIds: [
            "R002",
            "R004",
            "R011"
        ],
        updatedAt: new Date()
    },

    {
        userId: 103,
        recipeIds: [
            "R008",
            "R010",
            "R026"
        ],
        updatedAt: new Date()
    },

    {
        userId: 104,
        recipeIds: [
            "R003",
            "R019",
            "R023"
        ],
        updatedAt: new Date()
    },

    {
        userId: 105,
        recipeIds: [],
        updatedAt: new Date()
    }
]);


// ============================================================
// INTERACTIONS
// ============================================================

db.interactions.insertMany([
    {
        userId: 101,
        recipeId: "R001",
        action: "VIEW",
        rating: null,
        source: "SEARCH",
        metadata: {
            cuisine: "Indian",
            matchedIngredient: "Chicken"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 20)
    },

    {
        userId: 101,
        recipeId: "R001",
        action: "LIKE",
        rating: 5,
        source: "RECIPE_DETAILS",
        metadata: {
            reason: "Favorite chicken recipe"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 19)
    },

    {
        userId: 101,
        recipeId: "R027",
        action: "FAVORITE_ADD",
        rating: null,
        source: "RECOMMENDATION",
        metadata: {
            recommendationScore: 90
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 6)
    },

    {
        userId: 102,
        recipeId: "R002",
        action: "LIKE",
        rating: 5,
        source: "RECIPE_DETAILS",
        metadata: {
            preferredIngredient: "Paneer"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 16)
    },

    {
        userId: 102,
        recipeId: "R004",
        action: "FAVORITE_ADD",
        rating: null,
        source: "SEARCH",
        metadata: {
            cuisine: "Indian"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 12)
    },

    {
        userId: 103,
        recipeId: "R010",
        action: "LIKE",
        rating: 5,
        source: "SEARCH",
        metadata: {
            cuisine: "Chinese"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 10)
    },

    {
        userId: 103,
        recipeId: "R026",
        action: "FAVORITE_ADD",
        rating: null,
        source: "RECOMMENDATION",
        metadata: {
            recommendationScore: 85
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 4)
    },

    {
        userId: 104,
        recipeId: "R023",
        action: "LIKE",
        rating: 5,
        source: "SEARCH",
        metadata: {
            diet: "Vegan"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 7)
    },

    {
        userId: 104,
        recipeId: "R019",
        action: "FAVORITE_ADD",
        rating: null,
        source: "RECIPE_DETAILS",
        metadata: {
            cuisine: "Mexican"
        },
        createdAt: new Date(Date.now() - 1000 * 60 * 60 * 24 * 3)
    }
]);

print("MongoDB sample data inserted successfully.");

print("Users: " + db.users.countDocuments());
print("Preferences: " + db.preferences.countDocuments());
print("Favorites: " + db.favorites.countDocuments());
print("Interactions: " + db.interactions.countDocuments());