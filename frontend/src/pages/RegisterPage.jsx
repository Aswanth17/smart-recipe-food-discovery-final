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


function RegisterPage() {

  const navigate =
    useNavigate();


  const {
    register
  } = useAuth();


  const [fullName, setFullName] =
    useState("");

  const [email, setEmail] =
    useState("");

  const [password, setPassword] =
    useState("");

  const [
    confirmPassword,
    setConfirmPassword
  ] = useState("");

  const [error, setError] =
    useState("");

  const [loading, setLoading] =
    useState(false);


  const handleSubmit =
    async (event) => {

      event.preventDefault();

      setError("");


      if (
        password !==
        confirmPassword
      ) {

        setError(
          "Passwords do not match."
        );

        return;
      }


      if (
        password.length < 8
      ) {

        setError(
          "Password must contain at least 8 characters."
        );

        return;
      }


      setLoading(true);


      try {

        await register(
          fullName,
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
          "Unable to create account."
        );

      } finally {

        setLoading(false);
      }
    };


  return (

    <div className="auth-page">

      <div className="auth-card">

        <h1>
          Create Account
        </h1>

        <p className="auth-subtitle">

          Create your Smart Recipe
          profile.

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

            Full Name

          </label>

          <input
            type="text"
            value={fullName}
            onChange={
              (event) =>
                setFullName(
                  event.target.value
                )
            }
            placeholder="Enter your full name"
            required
          />


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
            placeholder="Minimum 8 characters"
            required
          />


          <label>

            Confirm Password

          </label>

          <input
            type="password"
            value={confirmPassword}
            onChange={
              (event) =>
                setConfirmPassword(
                  event.target.value
                )
            }
            placeholder="Re-enter your password"
            required
          />


          <button
            type="submit"
            disabled={loading}
            className="primary-button"
          >

            {
              loading
                ? "Creating account..."
                : "Register"
            }

          </button>

        </form>


        <p className="auth-footer">

          Already have an account?{" "}

          <Link to="/login">

            Login here

          </Link>

        </p>

      </div>

    </div>
  );
}


export default RegisterPage;