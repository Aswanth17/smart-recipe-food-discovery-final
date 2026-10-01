import { Link } from "react-router-dom";

function RecipeCard({ recipe }) {
  return (
    <article className="recipe-card">

      <div className="recipe-card-top">

        <span className="badge">
          {recipe.cuisine}
        </span>

        <span className="badge soft">
          {recipe.dietaryType}
        </span>

      </div>

      <h3>
        {recipe.name}
      </h3>

      <p className="recipe-description">
        {recipe.description}
      </p>

      <div className="recipe-meta">

        <span>
          ⏱ {recipe.cookingTime} min
        </span>

        <span>
          🔥 {recipe.nutrition?.calories} kcal
        </span>

        <span>
          {recipe.difficulty}
        </span>

      </div>

      <Link
        to={`/recipes/${recipe.recipeId}`}
        className="card-link"
      >
        View Recipe →
      </Link>

    </article>
  );
}

export default RecipeCard;