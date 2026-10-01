import { useEffect, useState } from "react";

import api from "../api/api";
import RecipeCard from "../components/RecipeCard";

function RecipeSearchPage() {

  const [recipes, setRecipes] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");

  const [filters, setFilters] =
    useState({
      ingredients: "",
      cuisine: "",
      diet: "",
      maxCalories: "",
      maxCookingTime: "",
      difficulty: "",
    });


  useEffect(() => {
    loadRecipes();
  }, []);


  async function loadRecipes() {

    try {

      setLoading(true);

      const response =
        await api.get("/recipes");

      setRecipes(response.data);

      setError("");

    } catch (err) {

      console.error(err);

      setError(
        "Unable to load recipes."
      );

    } finally {

      setLoading(false);
    }
  }


  function handleChange(event) {

    const {
      name,
      value
    } = event.target;

    setFilters(
      previous => ({
        ...previous,
        [name]: value,
      })
    );
  }


  async function handleSearch(event) {

    event.preventDefault();

    try {

      setLoading(true);

      const body = {

        ingredients:
          filters.ingredients
            .split(",")
            .map(value => value.trim())
            .filter(Boolean),

        cuisine:
          filters.cuisine,

        diet:
          filters.diet,

        maxCalories:
          filters.maxCalories
            ? Number(filters.maxCalories)
            : null,

        maxCookingTime:
          filters.maxCookingTime
            ? Number(filters.maxCookingTime)
            : null,

        difficulty:
          filters.difficulty,
      };


      const response =
        await api.post(
          "/recipes/search",
          body
        );


      setRecipes(response.data);

      setError("");

    } catch (err) {

      console.error(err);

      setError(
        "Recipe search failed."
      );

    } finally {

      setLoading(false);
    }
  }


  function resetSearch() {

    setFilters({
      ingredients: "",
      cuisine: "",
      diet: "",
      maxCalories: "",
      maxCookingTime: "",
      difficulty: "",
    });

    loadRecipes();
  }


  return (
    <div className="page">

      <div className="page-heading">

        <p className="eyebrow">
          BASEX + XQUERY
        </p>

        <h1>
          Recipe Search
        </h1>

        <p>
          Search the live XML recipe
          database using multiple filters.
        </p>

      </div>


      <form
        className="search-panel"
        onSubmit={handleSearch}
      >

        <div className="form-group wide">

          <label>
            Ingredients
          </label>

          <input
            name="ingredients"
            value={filters.ingredients}
            onChange={handleChange}
            placeholder="Chicken, Rice, Onion"
          />

        </div>


        <div className="form-group">

          <label>
            Cuisine
          </label>

          <select
            name="cuisine"
            value={filters.cuisine}
            onChange={handleChange}
          >

            <option value="">
              Any cuisine
            </option>

            <option value="Indian">
              Indian
            </option>

            <option value="Chinese">
              Chinese
            </option>

            <option value="Italian">
              Italian
            </option>

            <option value="Mexican">
              Mexican
            </option>

            <option value="Continental">
              Continental
            </option>

          </select>

        </div>


        <div className="form-group">

          <label>
            Diet
          </label>

          <select
            name="diet"
            value={filters.diet}
            onChange={handleChange}
          >

            <option value="">
              Any diet
            </option>

            <option value="Vegetarian">
              Vegetarian
            </option>

            <option value="Non-Vegetarian">
              Non-Vegetarian
            </option>

            <option value="Vegan">
              Vegan
            </option>

          </select>

        </div>


        <div className="form-group">

          <label>
            Max calories
          </label>

          <input
            type="number"
            name="maxCalories"
            value={filters.maxCalories}
            onChange={handleChange}
            placeholder="800"
          />

        </div>


        <div className="form-group">

          <label>
            Max cooking time
          </label>

          <input
            type="number"
            name="maxCookingTime"
            value={filters.maxCookingTime}
            onChange={handleChange}
            placeholder="60"
          />

        </div>


        <div className="form-group">

          <label>
            Difficulty
          </label>

          <select
            name="difficulty"
            value={filters.difficulty}
            onChange={handleChange}
          >

            <option value="">
              Any difficulty
            </option>

            <option value="Easy">
              Easy
            </option>

            <option value="Medium">
              Medium
            </option>

            <option value="Hard">
              Hard
            </option>

          </select>

        </div>


        <div className="form-actions">

          <button
            className="primary-button"
            type="submit"
          >
            Search
          </button>

          <button
            className="secondary-button"
            type="button"
            onClick={resetSearch}
          >
            Reset
          </button>

        </div>

      </form>


      <div className="results-header">

        <h2>
          Recipes
        </h2>

        <span>
          {recipes.length} result(s)
        </span>

      </div>


      {loading && (
        <p className="status-message">
          Loading recipes...
        </p>
      )}


      {error && (
        <p className="error-message">
          {error}
        </p>
      )}


      {!loading &&
       !error &&
       recipes.length === 0 && (

        <p className="status-message">
          No recipes matched your search.
        </p>

      )}


      <div className="recipe-grid">

        {recipes.map(recipe => (

          <RecipeCard
            key={recipe.recipeId}
            recipe={recipe}
          />

        ))}

      </div>

    </div>
  );
}

export default RecipeSearchPage;