import {
  useEffect,
  useState
} from "react";

import api from "../api/api";


function AnalyticsPage() {

  const [summary, setSummary] =
    useState(null);

  const [topRated, setTopRated] =
    useState([]);

  const [cuisinePopularity, setCuisinePopularity] =
    useState([]);

  const [mostViewed, setMostViewed] =
    useState([]);

  const [restaurantDishCounts, setRestaurantDishCounts] =
    useState([]);

  const [loading, setLoading] =
    useState(true);

  const [error, setError] =
    useState("");


  useEffect(() => {

    loadAnalytics();

  }, []);


  async function loadAnalytics() {

    try {

      setLoading(true);
      setError("");


      const [
        summaryResponse,
        topRatedResponse,
        cuisineResponse,
        viewedResponse,
        restaurantResponse
      ] =
        await Promise.all([

          api.get(
            "/analytics/summary"
          ),

          api.get(
            "/analytics/top-rated-recipes"
          ),

          api.get(
            "/analytics/cuisine-popularity"
          ),

          api.get(
            "/analytics/most-viewed-recipes"
          ),

          api.get(
            "/analytics/restaurant-dish-counts"
          )

        ]);


      setSummary(
        summaryResponse.data
      );

      setTopRated(
        topRatedResponse.data
      );

      setCuisinePopularity(
        cuisineResponse.data
      );

      setMostViewed(
        viewedResponse.data
      );

      setRestaurantDishCounts(
        restaurantResponse.data
      );


    } catch (err) {

      console.error(err);

      setError(
        "Unable to load analytics data."
      );


    } finally {

      setLoading(false);
    }
  }


  if (loading) {

    return (
      <div className="page">

        <p className="status-message">
          Loading analytics...
        </p>

      </div>
    );
  }


  return (
    <div className="page">

      <div className="page-heading">

        <p className="eyebrow">
          POSTGRESQL ANALYTICS
        </p>

        <h1>
          Analytics Dashboard
        </h1>

        <p>
          Aggregated statistics from users,
          recipes, ratings, reviews,
          interactions and restaurant data.
        </p>

      </div>


      {error && (

        <p className="error-message">
          {error}
        </p>

      )}


      {summary && (

        <section className="analytics-summary-grid">

          <article className="summary-card">
            <span>
              Users
            </span>

            <strong>
              {summary.users}
            </strong>
          </article>


          <article className="summary-card">
            <span>
              Recipes
            </span>

            <strong>
              {summary.recipes}
            </strong>
          </article>


          <article className="summary-card">
            <span>
              Restaurants
            </span>

            <strong>
              {summary.restaurants}
            </strong>
          </article>


          <article className="summary-card">
            <span>
              Ratings
            </span>

            <strong>
              {summary.ratings}
            </strong>
          </article>


          <article className="summary-card">
            <span>
              Reviews
            </span>

            <strong>
              {summary.reviews}
            </strong>
          </article>


          <article className="summary-card">
            <span>
              Activity Events
            </span>

            <strong>
              {summary.activityEvents}
            </strong>
          </article>

        </section>

      )}


      <section className="analytics-section">

        <div className="section-heading">

          <h2>
            Top Rated Recipes
          </h2>

          <p>
            Average ratings calculated
            from PostgreSQL rating records.
          </p>

        </div>


        <div className="analytics-table-wrapper">

          <table className="analytics-table">

            <thead>

              <tr>

                <th>
                  Recipe ID
                </th>

                <th>
                  Average Rating
                </th>

                <th>
                  Rating Count
                </th>

              </tr>

            </thead>


            <tbody>

              {topRated.map(
                (
                  item,
                  index
                ) => (

                  <tr
                    key={
                      item.recipe_id ??
                      index
                    }
                  >

                    <td>
                      {item.recipe_id}
                    </td>

                    <td>
                      ⭐ {
                        item.average_rating
                      }
                    </td>

                    <td>
                      {item.rating_count}
                    </td>

                  </tr>

                )
              )}

            </tbody>

          </table>

        </div>

      </section>


      <section className="analytics-section">

        <div className="section-heading">

          <h2>
            Cuisine Popularity
          </h2>

          <p>
            Interaction events grouped
            by recipe cuisine.
          </p>

        </div>


        <div className="analytics-list">

          {cuisinePopularity.map(
            (
              item,
              index
            ) => (

              <article
                key={
                  item.cuisine ??
                  index
                }
                className="analytics-list-card"
              >

                <div>

                  <strong>
                    {item.cuisine}
                  </strong>

                  <span>
                    Interactions
                  </span>

                </div>

                <div className="analytics-value">

                  {
                    item.interaction_count
                  }

                </div>

              </article>

            )
          )}

        </div>

      </section>


      <section className="analytics-section">

        <div className="section-heading">

          <h2>
            Most Viewed Recipes
          </h2>

          <p>
            Recipe views derived from
            activity event records.
          </p>

        </div>


        <div className="analytics-table-wrapper">

          <table className="analytics-table">

            <thead>

              <tr>

                <th>
                  Recipe ID
                </th>

                <th>
                  View Count
                </th>

              </tr>

            </thead>


            <tbody>

              {mostViewed.map(
                (
                  item,
                  index
                ) => (

                  <tr
                    key={
                      item.recipe_id ??
                      index
                    }
                  >

                    <td>
                      {item.recipe_id}
                    </td>

                    <td>
                      {item.view_count}
                    </td>

                  </tr>

                )
              )}

            </tbody>

          </table>

        </div>

      </section>


      <section className="analytics-section">

        <div className="section-heading">

          <h2>
            Restaurant Dish Distribution
          </h2>

          <p>
            Number of mapped recipe dishes
            available at each restaurant.
          </p>

        </div>


        <div className="restaurant-analytics-grid">

          {restaurantDishCounts.map(
            (
              item,
              index
            ) => (

              <article
                key={
                  item.restaurant_id ??
                  index
                }
                className="restaurant-analytics-card"
              >

                <h3>
                  {item.name}
                </h3>

                <p>
                  Restaurant ID:
                  {" "}
                  {item.restaurant_id}
                </p>

                <strong>
                  {item.dish_count}
                </strong>

                <span>
                  mapped dishes
                </span>

              </article>

            )
          )}

        </div>

      </section>

    </div>
  );
}


export default AnalyticsPage;