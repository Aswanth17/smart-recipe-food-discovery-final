import {
  useEffect,
  useState
} from "react";

import {
  useNavigate,
  useParams
} from "react-router-dom";

import api from "../api/api";
import { useAuth } from "../context/AuthContext";


function RecipeDetailsPage() {

  const { id } =
    useParams();

  const navigate =
    useNavigate();


  const { user } =
    useAuth();

  const userId =
    user?.userId;


  const [recipe, setRecipe] =
    useState(null);

  const [averageRating, setAverageRating] =
    useState(null);

  const [reviews, setReviews] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");

  const [
    favoriteMessage,
    setFavoriteMessage
  ] = useState("");

  const [
    favoriteLoading,
    setFavoriteLoading
  ] = useState(false);


  useEffect(() => {

    const loadRecipe =
      async () => {

        try {

          setLoading(true);

          setError("");


          const [
            recipeResponse,
            ratingResponse,
            reviewsResponse
          ] =
            await Promise.all([

              api.get(
                `/recipes/${id}`
              ),

              api
                .get(
                  `/recipes/${id}/average-rating`
                )
                .catch(
                  () => ({
                    data: null
                  })
                ),

              api
                .get(
                  `/recipes/${id}/reviews`
                )
                .catch(
                  () => ({
                    data: []
                  })
                ),

            ]);


          setRecipe(
            recipeResponse.data
          );


          const ratingData =
            ratingResponse.data;


          setAverageRating(
            ratingData?.averageRating
            ??
            ratingData?.average_rating
            ??
            ratingData
          );


          setReviews(
            Array.isArray(
              reviewsResponse.data
            )
              ? reviewsResponse.data
              : []
          );

        } catch (error) {

          console.error(
            "Recipe details error:",
            error
          );


          setError(
            "Unable to load recipe."
          );

        } finally {

          setLoading(false);
        }
      };


    loadRecipe();

  }, [id]);


  const addFavorite =
    async () => {

      if (!userId) {

        navigate(
          "/login"
        );

        return;
      }


      try {

        setFavoriteLoading(true);

        setFavoriteMessage("");


        await api.post(
          `/users/${userId}/favorites`,
          {
            recipeId: id,
          }
        );


        setFavoriteMessage(
          "Recipe added to favorites."
        );

      } catch (error) {

        console.error(
          "Favorite error:",
          error
        );


        setFavoriteMessage(
          error.response?.data?.message
          ||
          "Unable to add recipe to favorites."
        );

      } finally {

        setFavoriteLoading(false);
      }
    };


  if (loading) {

    return (

      <div className="page-container">

        <h2>
          Loading recipe...
        </h2>

      </div>
    );
  }


  if (error || !recipe) {

    return (

      <div className="page-container">

        <div className="error-message">

          {
            error
            ||
            "Recipe not found."
          }

        </div>

      </div>
    );
  }


  return (

    <div className="page-container">

      <section className="recipe-detail-header">

        <p className="page-kicker">

          {
            recipe.id
          }

        </p>


        <h1>

          {
            recipe.name
          }

        </h1>


        <p>

          {
            recipe.description
          }

        </p>


        <div className="recipe-badges">

          {
            recipe.cuisine && (

              <span className="recipe-badge">

                {
                  recipe.cuisine
                }

              </span>

            )
          }


          {
            recipe.category && (

              <span className="recipe-badge">

                {
                  recipe.category
                }

              </span>

            )
          }


          {
            recipe.dietaryType && (

              <span className="recipe-badge">

                {
                  recipe.dietaryType
                }

              </span>

            )
          }


          {
            recipe.difficulty && (

              <span className="recipe-badge">

                {
                  recipe.difficulty
                }

              </span>

            )
          }

        </div>


        <button
          type="button"
          className="primary-button"
          onClick={
            addFavorite
          }
          disabled={
            favoriteLoading
          }
        >

          {
            favoriteLoading
              ? "Adding..."
              : "Add to Favorites"
          }

        </button>


        {
          favoriteMessage && (

            <p className="favorite-message">

              {
                favoriteMessage
              }

            </p>

          )
        }

      </section>


      <section className="recipe-info-grid">

        <div className="recipe-info-card">

          <strong>
            Cooking Time
          </strong>

          <span>

            {
              recipe.cookingTime
            } minutes

          </span>

        </div>


        <div className="recipe-info-card">

          <strong>
            Difficulty
          </strong>

          <span>

            {
              recipe.difficulty
            }

          </span>

        </div>


        <div className="recipe-info-card">

          <strong>
            Calories
          </strong>

          <span>

            {
              recipe.nutrition
                ?.calories
              ??
              recipe.calories
              ??
              "-"
            }

          </span>

        </div>


        <div className="recipe-info-card">

          <strong>
            Average Rating
          </strong>

          <span>

            {
              averageRating !== null
                ? Number(
                    averageRating
                  ).toFixed(1)
                : "No ratings"
            }

          </span>

        </div>

      </section>


      <section className="recipe-section">

        <h2>
          Ingredients
        </h2>


        <ul>

          {
            recipe.ingredients
              ?.map(
                (
                  ingredient,
                  index
                ) => (

                  <li
                    key={
                      index
                    }
                  >

                    {
                      typeof ingredient
                      === "string"
                        ? ingredient
                        : `${ingredient.name}${
                            ingredient.quantity
                              ? ` - ${ingredient.quantity}`
                              : ""
                          }${
                            ingredient.unit
                              ? ` ${ingredient.unit}`
                              : ""
                          }`
                    }

                  </li>

                )
              )
          }

        </ul>

      </section>


      <section className="recipe-section">

        <h2>
          Preparation
        </h2>


        <ol>

          {
            recipe.steps
              ?.map(
                (
                  step,
                  index
                ) => (

                  <li
                    key={
                      index
                    }
                  >
                    {step}
                  </li>

                )
              )
          }

        </ol>

      </section>


      {
        recipe.nutrition && (

          <section className="recipe-section">

            <h2>
              Nutrition
            </h2>


            <div className="nutrition-grid">

              <div>

                <strong>
                  Calories
                </strong>

                <p>
                  {
                    recipe.nutrition
                      .calories
                  }
                </p>

              </div>


              <div>

                <strong>
                  Protein
                </strong>

                <p>
                  {
                    recipe.nutrition
                      .protein
                  }
                </p>

              </div>


              <div>

                <strong>
                  Carbohydrates
                </strong>

                <p>
                  {
                    recipe.nutrition
                      .carbs
                  }
                </p>

              </div>


              <div>

                <strong>
                  Fat
                </strong>

                <p>
                  {
                    recipe.nutrition
                      .fat
                  }
                </p>

              </div>

            </div>

          </section>

        )
      }


      {
        recipe.allergens
        &&
        recipe.allergens.length > 0
        && (

          <section className="recipe-section">

            <h2>
              Allergens
            </h2>

            <p>

              {
                recipe.allergens.join(
                  ", "
                )
              }

            </p>

          </section>

        )
      }


      {
        recipe.tags
        &&
        recipe.tags.length > 0
        && (

          <section className="recipe-section">

            <h2>
              Tags
            </h2>

            <div className="recipe-badges">

              {
                recipe.tags.map(
                  (
                    tag,
                    index
                  ) => (

                    <span
                      key={
                        index
                      }
                      className="recipe-badge"
                    >
                      {tag}
                    </span>

                  )
                )
              }

            </div>

          </section>

        )
      }


      <section className="recipe-section">

        <h2>
          User Reviews
        </h2>


        {
          reviews.length === 0
            ? (

              <p>
                No reviews yet.
              </p>

            )
            : (

              <div className="reviews-list">

                {
                  reviews.map(
                    (
                      review,
                      index
                    ) => (

                      <div
                        key={
                          review.reviewId
                          ||
                          index
                        }
                        className="review-card"
                      >

                        <strong>

                          {
                            review.fullName
                            ||
                            review.userName
                            ||
                            "User"
                          }

                        </strong>


                        {
                          review.rating
                          &&
                          (

                            <p>

                              Rating:{" "}

                              {
                                review.rating
                              }/5

                            </p>

                          )
                        }


                        <p>

                          {
                            review.comment
                            ||
                            review.reviewText
                            ||
                            review.review
                          }

                        </p>

                      </div>

                    )
                  )
                }

              </div>

            )
        }

      </section>

    </div>
  );
}


export default RecipeDetailsPage;