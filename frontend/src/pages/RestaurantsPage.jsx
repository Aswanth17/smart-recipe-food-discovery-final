import {
  useEffect,
  useState
} from "react";

import api from "../api/api";


function RestaurantsPage() {

  const [latitude, setLatitude] =
    useState("11.0168");

  const [longitude, setLongitude] =
    useState("76.9674");

  const [radius, setRadius] =
    useState("5000");

  const [recipeId, setRecipeId] =
    useState("");

  const [restaurants, setRestaurants] =
    useState([]);

  const [nearest, setNearest] =
    useState(null);

  const [loading, setLoading] =
    useState(false);

  const [error, setError] =
    useState("");


  useEffect(() => {
    loadNearbyRestaurants();
  }, []);


  async function loadNearbyRestaurants() {

    try {

      setLoading(true);
      setError("");
      setNearest(null);


      const response =
        await api.get(
          "/restaurants/nearby",
          {
            params: {
              lat: Number(latitude),
              lon: Number(longitude),
              radius: Number(radius)
            }
          }
        );


      setRestaurants(
        response.data
      );


    } catch (err) {

      console.error(err);

      setError(
        "Unable to load nearby restaurants."
      );


    } finally {

      setLoading(false);
    }
  }


  async function findNearestRestaurant() {

    try {

      setLoading(true);
      setError("");


      const response =
        await api.get(
          "/restaurants/nearest",
          {
            params: {
              lat: Number(latitude),
              lon: Number(longitude)
            }
          }
        );


      setNearest(
        response.data
      );


    } catch (err) {

      console.error(err);

      setError(
        "Unable to find nearest restaurant."
      );


    } finally {

      setLoading(false);
    }
  }


  async function findRestaurantsForRecipe() {

    if (!recipeId.trim()) {

      setError(
        "Enter a recipe ID such as R001."
      );

      return;
    }


    try {

      setLoading(true);
      setError("");
      setNearest(null);


      const response =
        await api.get(
          `/restaurants/serving/${recipeId.trim()}`,
          {
            params: {
              lat: Number(latitude),
              lon: Number(longitude),
              radius: Number(radius)
            }
          }
        );


      setRestaurants(
        response.data
      );


    } catch (err) {

      console.error(err);

      setError(
        "Unable to find restaurants for this recipe."
      );


    } finally {

      setLoading(false);
    }
  }


  async function findNearestForRecipe() {

    if (!recipeId.trim()) {

      setError(
        "Enter a recipe ID such as R001."
      );

      return;
    }


    try {

      setLoading(true);
      setError("");


      const response =
        await api.get(
          `/restaurants/nearest/${recipeId.trim()}`,
          {
            params: {
              lat: Number(latitude),
              lon: Number(longitude)
            }
          }
        );


      setNearest(
        response.data
      );


    } catch (err) {

      console.error(err);

      setError(
        "Unable to find nearest restaurant for this recipe."
      );


    } finally {

      setLoading(false);
    }
  }


  function formatDistance(value) {

    if (
      value === null ||
      value === undefined
    ) {
      return "Distance unavailable";
    }


    const meters =
      Number(value);


    if (Number.isNaN(meters)) {
      return value;
    }


    if (meters < 1000) {

      return `${meters.toFixed(0)} m`;
    }


    return `${(
      meters / 1000
    ).toFixed(2)} km`;
  }


  function getDistance(
    restaurant
  ) {

    return (
      restaurant.distance_meters ??
      restaurant.distance ??
      restaurant.distance_m ??
      null
    );
  }


  function getName(
    restaurant
  ) {

    return (
      restaurant.name ??
      restaurant.restaurant_name ??
      "Restaurant"
    );
  }


  return (
    <div className="page">

      <div className="page-heading">

        <p className="eyebrow">
          POSTGIS SPATIAL DISCOVERY
        </p>

        <h1>
          Nearby Restaurants
        </h1>

        <p>
          Find restaurants using geographic
          distance, radius filtering and
          recipe availability.
        </p>

      </div>


      <section className="restaurant-search-panel">

        <div className="form-group">

          <label>
            Latitude
          </label>

          <input
            type="number"
            step="any"
            value={latitude}
            onChange={
              event =>
                setLatitude(
                  event.target.value
                )
            }
          />

        </div>


        <div className="form-group">

          <label>
            Longitude
          </label>

          <input
            type="number"
            step="any"
            value={longitude}
            onChange={
              event =>
                setLongitude(
                  event.target.value
                )
            }
          />

        </div>


        <div className="form-group">

          <label>
            Radius in meters
          </label>

          <select
            value={radius}
            onChange={
              event =>
                setRadius(
                  event.target.value
                )
            }
          >

            <option value="1000">
              1 km
            </option>

            <option value="3000">
              3 km
            </option>

            <option value="5000">
              5 km
            </option>

            <option value="10000">
              10 km
            </option>

          </select>

        </div>


        <div className="form-group">

          <label>
            Recipe ID
          </label>

          <input
            value={recipeId}
            onChange={
              event =>
                setRecipeId(
                  event.target.value
                )
            }
            placeholder="Example: R001"
          />

        </div>


        <div className="restaurant-search-actions">

          <button
            type="button"
            className="primary-button"
            onClick={
              loadNearbyRestaurants
            }
          >
            Find Nearby
          </button>


          <button
            type="button"
            className="secondary-button"
            onClick={
              findNearestRestaurant
            }
          >
            Find Nearest
          </button>


          <button
            type="button"
            className="secondary-button"
            onClick={
              findRestaurantsForRecipe
            }
          >
            Find Recipe Restaurants
          </button>


          <button
            type="button"
            className="secondary-button"
            onClick={
              findNearestForRecipe
            }
          >
            Nearest for Recipe
          </button>

        </div>

      </section>


      {loading && (

        <p className="status-message">
          Searching restaurants...
        </p>

      )}


      {error && (

        <p className="error-message">
          {error}
        </p>

      )}


      {nearest && (

        <section className="nearest-card">

          <p className="eyebrow">
            NEAREST MATCH
          </p>

          <h2>
            {getName(nearest)}
          </h2>


          {
            nearest.address &&
            (
              <p>
                {nearest.address}
              </p>
            )
          }


          <div className="restaurant-meta">

            {
              nearest.rating !== undefined &&
              (
                <span>
                  ⭐ {nearest.rating}
                </span>
              )
            }

            <span>
              📍 {
                formatDistance(
                  getDistance(nearest)
                )
              }
            </span>

          </div>

        </section>

      )}


      <div className="results-header">

        <h2>
          Restaurant Results
        </h2>

        <span>
          {restaurants.length} result(s)
        </span>

      </div>


      {!loading &&
       !error &&
       restaurants.length === 0 && (

        <p className="status-message">
          No restaurants matched the
          current search.
        </p>

      )}


      <div className="restaurant-grid">

        {restaurants.map(
          (
            restaurant,
            index
          ) => (

            <article
              key={
                restaurant.restaurant_id ??
                restaurant.id ??
                index
              }
              className="restaurant-card"
            >

              <h2>
                {getName(restaurant)}
              </h2>


              {
                restaurant.address &&
                (
                  <p>
                    {restaurant.address}
                  </p>
                )
              }


              <div className="restaurant-meta">

                {
                  restaurant.rating !== undefined &&
                  (
                    <span>
                      ⭐ {restaurant.rating}
                    </span>
                  )
                }


                <span>
                  📍 {
                    formatDistance(
                      getDistance(
                        restaurant
                      )
                    )
                  }
                </span>

              </div>


              {
                restaurant.recipe_id &&
                (
                  <div className="restaurant-recipe">

                    <span className="badge">
                      {
                        restaurant.recipe_id
                      }
                    </span>

                    {
                      restaurant.dish_name &&
                      (
                        <span>
                          {
                            restaurant.dish_name
                          }
                        </span>
                      )
                    }

                  </div>
                )
              }

            </article>

          )
        )}

      </div>

    </div>
  );
}


export default RestaurantsPage;