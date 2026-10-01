import {
  Link,
  NavLink,
  useNavigate
} from "react-router-dom";

import {
  useAuth
} from "../context/AuthContext";


function Navbar() {

  const navigate =
    useNavigate();


  const {
    user,
    isAuthenticated,
    logout,
  } = useAuth();


  const handleLogout = () => {

    logout();

    navigate("/");
  };


  return (

    <nav className="navbar">

      <Link
        to="/"
        className="navbar-brand"
      >
        Smart Recipe
      </Link>


      <div className="navbar-links">

        <NavLink to="/">
          Home
        </NavLink>

        <NavLink to="/recipes">
          Recipes
        </NavLink>

        <NavLink to="/recommendations">
          Recommendations
        </NavLink>

        <NavLink to="/favorites">
          Favorites
        </NavLink>

        <NavLink to="/restaurants">
          Restaurants
        </NavLink>

        <NavLink to="/analytics">
          Analytics
        </NavLink>


        {
          isAuthenticated
            ? (
              <>

                <NavLink to="/profile">

                  {
                    user?.fullName
                    ||
                    "Profile"
                  }

                </NavLink>


                <button
                  type="button"
                  onClick={handleLogout}
                  className="navbar-logout-button"
                >
                  Logout
                </button>

              </>
            )
            : (
              <>

                <NavLink to="/login">
                  Login
                </NavLink>

                <NavLink to="/register">
                  Register
                </NavLink>

              </>
            )
        }

      </div>

    </nav>
  );
}


export default Navbar;