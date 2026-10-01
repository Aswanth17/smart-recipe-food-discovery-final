import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import api from "../api/api";
import { useAuth } from "../context/AuthContext";


function FavoritesPage() {

  const { user } = useAuth();

  const userId = user?.userId;


  const [favorites, setFavorites] = useState([]);

  const [loading, setLoading] = useState(true);

  const [error, setError] = useState("");

  const [message, setMessage] = useState("");


  const loadFavorites = async () => {

    if (!userId) {
      return;
    }


    try {

      setLoading(true);

      setError("");


      // Get favorite recipe IDs from MongoDB
      const favoriteResponse =
        await api.get(
          `/users/${userId}/favorites`
        );


      const favoriteData =
        favoriteResponse.data;


      let recipeIds = [];


      if (Array.isArray(favoriteData)) {

        recipeIds = favoriteData;

      } else if (
        Array.isArray(
          favoriteData?.recipeIds
        )
      ) {

        recipeIds =
          favoriteData.recipeIds;

      } else if (
        Array.isArray(
          favoriteData?.favorites
        )
      ) {

        recipeIds =
          favoriteData.favorites;
      }


      if (recipeIds.length === 0) {

        setFavorites([]);

        return;
      }


      // Load complete recipe details from BaseX
      const recipeRequests =
        recipeIds.map(
          async (recipeItem) => {

            const recipeId =
              typeof recipeItem === "string"
                ? recipeItem
                : recipeItem.recipeId;


            try {

              const response =
                await api.get(
                  `/recipes/${recipeId}`
                );


              return response.data;

            } catch (error) {

              console.error(
                `Unable to load recipe ${recipeId}`,
                error
              );


              return null;
            }
          }
        );


      const recipes =
        await Promise.all(
          recipeRequests
        );


      setFavorites(
        recipes.filter(
          Boolean
        )
      );

    } catch (error) {

      console.error(
        "Favorites error:",
        error
      );


      setError(
        error.response?.data?.message
        ||
        "Unable to load favorites."
      );

    } finally {

      setLoading(false);
    }
  };


  useEffect(() => {

    if (!userId) {

      setLoading(false);

      return;
    }


    loadFavorites();

  }, [userId]);


  const removeFavorite =
    async (recipeId) => {

      if (!userId) {
        return;
      }


      try {

        setMessage("");


        await api.delete(
          `/users/${userId}/favorites/${recipeId}`
        );


        setFavorites(
          (currentFavorites) =>
            currentFavorites.filter(
              (recipe) =>
                recipe.id !== recipeId
            )
        );


        setMessage(
          "Recipe removed from favorites."
        );

      } catch (error) {

        console.error(
          "Remove favorite error:",
          error
        );


        setMessage(
          error.response?.data?.message
          ||
          "Unable to remove favorite."
        );
      }
    };


  if (loading) {

    return (

      <div className="page-container">

        <h2>
          Loading favorites...
        </h2>

      </div>
    );
  }


  return (

    <div className="page-container">

      <div className="page-header">

        <p className="page-kicker">
          PERSONAL COLLECTION
        </p>

        <h1>
          My Favorites
        </h1>

        <p>
          Recipes saved to your
          personal account.
        </p>

      </div>


      {message && (

        <div className="success-message">

          {message}

        </div>

      )}


      {error && (

        <div className="error-message">

          {error}

        </div>

      )}


      {!error &&
        favorites.length === 0 && (

          <div className="empty-state">

            <h2>
              No favorite recipes yet
            </h2>

            <p>
              Explore recipes and add
              something you like.
            </p>

            <Link
              to="/recipes"
              className="primary-button"
            >
              Browse Recipes
            </Link>

          </div>

        )}


      <div className="favorites-grid">

        {favorites.map(
          (recipe) => (

            <div
              key={recipe.id}
              className="favorite-card"
            >

              <div>

                <p className="recipe-id">
                  {recipe.id}
                </p>

                <h2>
                  {recipe.name}
                </h2>


                <p>
                  {
                    recipe.description
                  }
                </p>


                <div className="recipe-badges">

                  {recipe.cuisine && (

                    <span className="recipe-badge">
                      {recipe.cuisine}
                    </span>

                  )}


                  {recipe.dietaryType && (

                    <span className="recipe-badge">
                      {recipe.dietaryType}
                    </span>

                  )}


                  {recipe.difficulty && (

                    <span className="recipe-badge">
                      {recipe.difficulty}
                    </span>

                  )}

                </div>

              </div>


              <div className="favorite-actions">

                <Link
                  to={`/recipes/${recipe.id}`}
                  className="primary-button"
                >
                  View Recipe
                </Link>


                <button
                  type="button"
                  className="secondary-button"
                  onClick={() =>
                    removeFavorite(
                      recipe.id
                    )
                  }
                >
                  Remove
                </button>

              </div>

            </div>

          )
        )}

      </div>

    </div>
  );
}


export default FavoritesPage;