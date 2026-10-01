import {
  createBrowserRouter,
  RouterProvider
} from "react-router-dom";


import MainLayout
  from "./layouts/MainLayout";

import HomePage
  from "./pages/HomePage";

import RecipeSearchPage
  from "./pages/RecipeSearchPage";

import RecipeDetailsPage
  from "./pages/RecipeDetailsPage";

import RecommendationsPage
  from "./pages/RecommendationsPage";

import FavoritesPage
  from "./pages/FavoritesPage";

import RestaurantsPage
  from "./pages/RestaurantsPage";

import AnalyticsPage
  from "./pages/AnalyticsPage";

import LoginPage
  from "./pages/LoginPage";

import RegisterPage
  from "./pages/RegisterPage";

import ProfilePage
  from "./pages/ProfilePage";

import ProtectedRoute
  from "./components/ProtectedRoute";


const router =
  createBrowserRouter([

    {

      path: "/",

      element: (
        <MainLayout />
      ),

      children: [

        {
          index: true,

          element: (
            <HomePage />
          ),
        },


        {
          path: "recipes",

          element: (
            <RecipeSearchPage />
          ),
        },


        {
          path: "recipes/:id",

          element: (
            <RecipeDetailsPage />
          ),
        },


        {
          path: "recommendations",

          element: (

            <ProtectedRoute>

              <RecommendationsPage />

            </ProtectedRoute>

          ),
        },


        {
          path: "favorites",

          element: (

            <ProtectedRoute>

              <FavoritesPage />

            </ProtectedRoute>

          ),
        },


        {
          path: "restaurants",

          element: (
            <RestaurantsPage />
          ),
        },


        {
          path: "analytics",

          element: (
            <AnalyticsPage />
          ),
        },


        {
          path: "login",

          element: (
            <LoginPage />
          ),
        },


        {
          path: "register",

          element: (
            <RegisterPage />
          ),
        },


        {
          path: "profile",

          element: (

            <ProtectedRoute>

              <ProfilePage />

            </ProtectedRoute>

          ),
        },

      ],
    },

  ]);


function App() {

  return (

    <RouterProvider
      router={router}
    />

  );
}


export default App;