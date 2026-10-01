import { Link } from "react-router-dom";

function HomePage() {
  return (
    <div className="home-page">

      <section className="hero">

        <div className="hero-content">

          <p className="eyebrow">
            SMART FOOD DISCOVERY
          </p>

          <h1>
            Find recipes using
            what you already have.
          </h1>

          <p className="hero-description">
            Search recipes by ingredients,
            cuisine, calories, cooking time
            and dietary preference.
            Get personalized recommendations
            and discover nearby restaurants.
          </p>

          <div className="hero-actions">

            <Link
              to="/recipes"
              className="primary-button"
            >
              Search Recipes
            </Link>

            <Link
              to="/recommendations"
              className="secondary-button"
            >
              Get Recommendations
            </Link>

          </div>

        </div>

      </section>


      <section className="feature-section">

        <h2>
          What SmartRecipe can do
        </h2>

        <div className="feature-grid">

          <article className="feature-card">
            <h3>
              Ingredient Search
            </h3>

            <p>
              Search the BaseX XML recipe
              database using ingredients
              already available at home.
            </p>
          </article>


          <article className="feature-card">
            <h3>
              Personalized Suggestions
            </h3>

            <p>
              Recommendation scores use
              diet, cuisine, favorites,
              ingredients and user history.
            </p>
          </article>


          <article className="feature-card">
            <h3>
              Nearby Restaurants
            </h3>

            <p>
              PostGIS spatial queries help
              locate nearby restaurants
              and dishes.
            </p>
          </article>


          <article className="feature-card">
            <h3>
              Analytics
            </h3>

            <p>
              View ratings, reviews,
              popular cuisines and food
              discovery statistics.
            </p>
          </article>

        </div>

      </section>

    </div>
  );
}

export default HomePage;