import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

import api from "../api/api";
import { useAuth } from "../context/AuthContext";


function RecommendationsPage() {

  const { user } = useAuth();

  const userId = user?.userId;


  const [ingredients, setIngredients] =
    useState(
      "Paneer,Tomato,Spinach"
    );


  const [
    recommendations,
    setRecommendations
  ] = useState([]);


  const [loading, setLoading] =
    useState(false);

  const [error, setError] =
    useState("");


  const loadRecommendations =
    async () => {

      if (!userId) {
        return;
      }


      try {

        setLoading(true);

        setError("");


        const response =
          await api.get(
            `/recommendations/${userId}`,
            {
              params: {
                ingredients,
              },
            }
          );


        const data =
          response.data;


        if (Array.isArray(data)) {

          setRecommendations(
            data
          );

        } else if (
          Array.isArray(
            data?.recommendations
          )
        ) {

          setRecommendations(
            data.recommendations
          );

        } else {

          setRecommendations([]);
        }

      } catch (error) {

        console.error(
          "Recommendation error:",
          error
        );


        setError(
          error.response?.data?.message
          ||
          "Unable to load recommendations."
        );

      } finally {

        setLoading(false);
      }
    };


  useEffect(() => {

    if (userId) {

      loadRecommendations();
    }

  }, [userId]);


  const handleSubmit =
    async (event) => {

      event.preventDefault();

      await loadRecommendations();
    };


  return (

    <div className="page-container">

      <div className="page-header">

        <p className="page-kicker">
          PERSONALIZED DISCOVERY
        </p>

        <h1>
          Recipe Recommendations
        </h1>

        <p>
          Recommendations are generated
          using your preferences,
          favorites, interactions and
          available ingredients.
        </p>

      </div>


      <form
        className="recommendation-form"
        onSubmit={handleSubmit}
      >

        <label htmlFor="ingredients">

          Available Ingredients

        </label>


        <input
          id="ingredients"
          type="text"
          value={ingredients}
          onChange={
            (event) =>
              setIngredients(
                event.target.value
              )
          }
          placeholder="Example: Paneer, Tomato, Spinach"
        />


        <button
          type="submit"
          className="primary-button"
          disabled={loading}
        >

          {
            loading
              ? "Calculating..."
              : "Get Recommendations"
          }

        </button>

      </form>


      {error && (

        <div className="error-message">

          {error}

        </div>

      )}


      {!loading &&
        !error &&
        recommendations.length === 0 && (

          <div className="empty-state">

            <h3>
              No recommendations found
            </h3>

            <p>
              Try changing the available
              ingredients.
            </p>

          </div>

        )}


      <div className="recommendation-list">

        {recommendations.map(
          (item, index) => {

            const recipe =
              item.recipe
              ||
              item;


            return (

              <div
                key={
                  recipe.id
                  ||
                  `${index}`
                }
                className="recommendation-card"
              >

                <div className="recommendation-rank">

                  #{index + 1}

                </div>


                <div className="recommendation-content">

                  <h2>

                    {
                      recipe.name
                      ||
                      item.recipeName
                    }

                  </h2>


                  <div className="recipe-badges">

                    {
                      (
                        recipe.cuisine
                        ||
                        item.cuisine
                      ) && (

                        <span className="recipe-badge">

                          {
                            recipe.cuisine
                            ||
                            item.cuisine
                          }

                        </span>

                      )
                    }


                    {
                      (
                        recipe.dietaryType
                        ||
                        item.dietaryType
                      ) && (

                        <span className="recipe-badge">

                          {
                            recipe.dietaryType
                            ||
                            item.dietaryType
                          }

                        </span>

                      )
                    }

                  </div>


                  <p>

                    Recommendation Score:

                    {" "}

                    <strong>

                      {
                        item.score
                        ??
                        item.totalScore
                        ??
                        0
                      }

                      /100

                    </strong>

                  </p>


                  {
                    Array.isArray(
                      item.reasons
                    )
                    &&
                    item.reasons.length > 0
                    && (

                      <div>

                        <strong>
                          Why recommended:
                        </strong>

                        <ul>

                          {
                            item.reasons.map(
                              (
                                reason,
                                reasonIndex
                              ) => (

                                <li
                                  key={
                                    reasonIndex
                                  }
                                >
                                  {reason}
                                </li>

                              )
                            )
                          }

                        </ul>

                      </div>

                    )
                  }


                  {(recipe.id ||
                    item.recipeId) && (

                    <Link
                      to={
                        `/recipes/${
                          recipe.id
                          ||
                          item.recipeId
                        }`
                      }
                      className="primary-button"
                    >
                      View Recipe
                    </Link>

                  )}

                </div>

              </div>

            );
          }
        )}

      </div>

    </div>
  );
}


export default RecommendationsPage;