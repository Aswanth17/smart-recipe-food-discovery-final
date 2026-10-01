import {
  useEffect,
  useState
} from "react";

import api from "../api/api";

import {
  useAuth
} from "../context/AuthContext";


function ProfilePage() {

  const {
    user
  } = useAuth();


  const userId =
    user?.userId;


  const [profile, setProfile] =
    useState(null);


  const [preferences, setPreferences] =
    useState({

      diet: "",

      preferredCuisines: [],

      allergies: [],

      dislikedIngredients: [],

      favoriteIngredients: [],

      preferredDifficulty: []

    });


  const [loading, setLoading] =
    useState(true);


  const [saving, setSaving] =
    useState(false);


  const [error, setError] =
    useState("");


  const [message, setMessage] =
    useState("");


  const [cuisinesText, setCuisinesText] =
    useState("");


  const [allergiesText, setAllergiesText] =
    useState("");


  const [
    dislikedIngredientsText,
    setDislikedIngredientsText
  ] = useState("");


  const [
    favoriteIngredientsText,
    setFavoriteIngredientsText
  ] = useState("");


  const [
    difficultyText,
    setDifficultyText
  ] = useState("");


  useEffect(() => {

    const loadData =
      async () => {

        if (!userId) {
          return;
        }


        try {

          setLoading(true);

          setError("");


          const [
            profileResponse,
            preferenceResponse
          ] =
            await Promise.all([

              api.get(
                "/profile"
              ),

              api.get(
                `/users/${userId}/preferences`
              )

            ]);


          setProfile(
            profileResponse.data
          );


          const data =
            preferenceResponse.data
            || {};


          const loadedPreferences = {

            diet:
              data.diet
              || "",

            preferredCuisines:
              Array.isArray(
                data.preferredCuisines
              )
                ? data.preferredCuisines
                : [],

            allergies:
              Array.isArray(
                data.allergies
              )
                ? data.allergies
                : [],

            dislikedIngredients:
              Array.isArray(
                data.dislikedIngredients
              )
                ? data.dislikedIngredients
                : [],

            favoriteIngredients:
              Array.isArray(
                data.favoriteIngredients
              )
                ? data.favoriteIngredients
                : [],

            preferredDifficulty:
              Array.isArray(
                data.preferredDifficulty
              )
                ? data.preferredDifficulty
                : []

          };


          setPreferences(
            loadedPreferences
          );


          setCuisinesText(
            loadedPreferences
              .preferredCuisines
              .join(", ")
          );


          setAllergiesText(
            loadedPreferences
              .allergies
              .join(", ")
          );


          setDislikedIngredientsText(
            loadedPreferences
              .dislikedIngredients
              .join(", ")
          );


          setFavoriteIngredientsText(
            loadedPreferences
              .favoriteIngredients
              .join(", ")
          );


          setDifficultyText(
            loadedPreferences
              .preferredDifficulty
              .join(", ")
          );

        } catch (error) {

          console.error(
            "Profile load error:",
            error
          );


          setError(
            error.response?.data?.message
            ||
            "Unable to load profile information."
          );

        } finally {

          setLoading(false);
        }
      };


    loadData();

  }, [userId]);


  const textToArray =
    (value) => {

      return value
        .split(",")
        .map(
          (item) =>
            item.trim()
        )
        .filter(
          Boolean
        );
    };


  const savePreferences =
    async (event) => {

      event.preventDefault();


      if (!userId) {
        return;
      }


      try {

        setSaving(true);

        setError("");

        setMessage("");


        const updatedPreferences = {

          diet:
            preferences.diet,

          preferredCuisines:
            textToArray(
              cuisinesText
            ),

          allergies:
            textToArray(
              allergiesText
            ),

          dislikedIngredients:
            textToArray(
              dislikedIngredientsText
            ),

          favoriteIngredients:
            textToArray(
              favoriteIngredientsText
            ),

          preferredDifficulty:
            textToArray(
              difficultyText
            )

        };


        const response =
          await api.put(
            `/users/${userId}/preferences`,
            updatedPreferences
          );


        setPreferences(
          response.data
          || updatedPreferences
        );


        setMessage(
          "Preferences saved successfully."
        );

      } catch (error) {

        console.error(
          "Preference update error:",
          error
        );


        setError(
          error.response?.data?.message
          ||
          "Unable to save preferences."
        );

      } finally {

        setSaving(false);
      }
    };


  if (loading) {

    return (

      <div className="page-container">

        <h2>
          Loading profile...
        </h2>

      </div>
    );
  }


  const currentUser =
    profile
    || user;


  return (

    <div className="page-container">

      <div className="profile-header">

        <p className="page-kicker">
          USER ACCOUNT
        </p>


        <h1>
          My Profile
        </h1>


        <p>
          Manage your account and
          recipe preferences.
        </p>

      </div>


      {error && (

        <div className="error-message">

          {error}

        </div>

      )}


      {message && (

        <div className="success-message">

          {message}

        </div>

      )}


      <div className="profile-card">

        <div className="profile-avatar">

          {
            currentUser
              ?.fullName
              ?.charAt(0)
              ?.toUpperCase()
          }

        </div>


        <div className="profile-details">

          <h2>
            {
              currentUser
                ?.fullName
            }
          </h2>


          <div className="profile-row">

            <strong>
              User ID
            </strong>

            <span>
              {
                currentUser
                  ?.userId
              }
            </span>

          </div>


          <div className="profile-row">

            <strong>
              Email
            </strong>

            <span>
              {
                currentUser
                  ?.email
              }
            </span>

          </div>


          <div className="profile-row">

            <strong>
              Account Status
            </strong>

            <span>

              {
                currentUser
                  ?.active
                  ? "Active"
                  : "Inactive"
              }

            </span>

          </div>

        </div>

      </div>


      <section className="preferences-section">

        <div className="preferences-header">

          <p className="page-kicker">
            PERSONALIZATION
          </p>

          <h2>
            Food Preferences
          </h2>

          <p>
            These preferences are used
            by the recommendation engine
            to personalize recipe results.
          </p>

        </div>


        <form
          className="preferences-form"
          onSubmit={
            savePreferences
          }
        >

          <div className="preference-field">

            <label htmlFor="diet">
              Dietary Type
            </label>


            <select
              id="diet"
              value={
                preferences.diet
              }
              onChange={
                (event) =>
                  setPreferences(
                    {
                      ...preferences,

                      diet:
                        event.target.value
                    }
                  )
              }
            >

              <option value="">
                No Preference
              </option>

              <option value="Vegetarian">
                Vegetarian
              </option>

              <option value="Vegan">
                Vegan
              </option>

              <option value="Non-Vegetarian">
                Non-Vegetarian
              </option>

            </select>

          </div>


          <div className="preference-field">

            <label htmlFor="cuisines">
              Preferred Cuisines
            </label>


            <input
              id="cuisines"
              type="text"
              value={
                cuisinesText
              }
              onChange={
                (event) =>
                  setCuisinesText(
                    event.target.value
                  )
              }
              placeholder="Indian, Italian, Chinese"
            />


            <small>
              Separate multiple values with commas.
            </small>

          </div>


          <div className="preference-field">

            <label htmlFor="favoriteIngredients">
              Favorite Ingredients
            </label>


            <input
              id="favoriteIngredients"
              type="text"
              value={
                favoriteIngredientsText
              }
              onChange={
                (event) =>
                  setFavoriteIngredientsText(
                    event.target.value
                  )
              }
              placeholder="Paneer, Tomato, Spinach"
            />

          </div>


          <div className="preference-field">

            <label htmlFor="allergies">
              Allergies
            </label>


            <input
              id="allergies"
              type="text"
              value={
                allergiesText
              }
              onChange={
                (event) =>
                  setAllergiesText(
                    event.target.value
                  )
              }
              placeholder="Peanut, Milk"
            />

          </div>


          <div className="preference-field">

            <label htmlFor="dislikedIngredients">
              Disliked Ingredients
            </label>


            <input
              id="dislikedIngredients"
              type="text"
              value={
                dislikedIngredientsText
              }
              onChange={
                (event) =>
                  setDislikedIngredientsText(
                    event.target.value
                  )
              }
              placeholder="Mushroom, Onion"
            />

          </div>


          <div className="preference-field">

            <label htmlFor="difficulty">
              Preferred Difficulty
            </label>


            <input
              id="difficulty"
              type="text"
              value={
                difficultyText
              }
              onChange={
                (event) =>
                  setDifficultyText(
                    event.target.value
                  )
              }
              placeholder="Easy, Medium"
            />

          </div>


          <button
            type="submit"
            className="primary-button"
            disabled={
              saving
            }
          >

            {
              saving
                ? "Saving..."
                : "Save Preferences"
            }

          </button>

        </form>

      </section>

    </div>
  );
}


export default ProfilePage;