import {
  useState
} from "react";

import {
  Link,
  useNavigate
} from "react-router-dom";

import {
  useAuth
} from "../context/AuthContext";


function LoginPage() {

  const navigate =
    useNavigate();


  const {
    login
  } = useAuth();


  const [email, setEmail] =
    useState("");

  const [password, setPassword] =
    useState("");

  const [error, setError] =
    useState("");

  const [loading, setLoading] =
    useState(false);


  const handleSubmit =
    async (event) => {

      event.preventDefault();

      setError("");

      setLoading(true);


      try {

        await login(
          email,
          password
        );


        navigate(
          "/profile"
        );

      } catch (error) {

        console.error(
          error
        );


        setError(
          error.response?.data?.message
          ||
          "Invalid email or password."
        );

      } finally {

        setLoading(false);
      }
    };


  return (

    <div className="auth-page">

      <div className="auth-card">

        <h1>
          Login
        </h1>

        <p className="auth-subtitle">

          Login to access your
          personalized recipe account.

        </p>


        {error && (

          <div className="error-message">

            {error}

          </div>

        )}


        <form
          onSubmit={
            handleSubmit
          }
          className="auth-form"
        >

          <label>

            Email

          </label>

          <input
            type="email"
            value={email}
            onChange={
              (event) =>
                setEmail(
                  event.target.value
                )
            }
            placeholder="Enter your email"
            required
          />


          <label>

            Password

          </label>

          <input
            type="password"
            value={password}
            onChange={
              (event) =>
                setPassword(
                  event.target.value
                )
            }
            placeholder="Enter your password"
            required
          />


          <button
            type="submit"
            disabled={loading}
            className="primary-button"
          >

            {
              loading
                ? "Logging in..."
                : "Login"
            }

          </button>

        </form>


        <p className="auth-footer">

          Don't have an account?{" "}

          <Link to="/register">

            Register here

          </Link>

        </p>

      </div>

    </div>
  );
}


export default LoginPage;