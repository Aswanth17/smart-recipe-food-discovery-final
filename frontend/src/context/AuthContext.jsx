import {
  createContext,
  useContext,
  useEffect,
  useState,
} from "react";

import api from "../api/api";


const AuthContext = createContext(null);


export function AuthProvider({ children }) {

  const [user, setUser] = useState(null);

  const [token, setToken] = useState(
    localStorage.getItem("token")
  );

  const [loading, setLoading] = useState(true);


  useEffect(() => {

    const loadProfile = async () => {

      const savedToken =
        localStorage.getItem("token");


      if (!savedToken) {

        setLoading(false);

        return;
      }


      try {

        const response =
          await api.get("/profile");


        setUser(response.data);

        setToken(savedToken);

      } catch (error) {

        console.error(
          "Unable to restore login:",
          error
        );

        localStorage.removeItem("token");

        setUser(null);

        setToken(null);
      }

      setLoading(false);
    };


    loadProfile();

  }, []);


  const login = async (
    email,
    password
  ) => {

    const response =
      await api.post(
        "/auth/login",
        {
          email,
          password,
        }
      );


    const authToken =
      response.data.token;


    localStorage.setItem(
      "token",
      authToken
    );


    setToken(authToken);

    setUser(
      response.data.user
    );


    return response.data;
  };


  const register = async (
    fullName,
    email,
    password
  ) => {

    const response =
      await api.post(
        "/auth/register",
        {
          fullName,
          email,
          password,
        }
      );


    const authToken =
      response.data.token;


    localStorage.setItem(
      "token",
      authToken
    );


    setToken(authToken);

    setUser(
      response.data.user
    );


    return response.data;
  };


  const logout = () => {

    localStorage.removeItem(
      "token"
    );

    setToken(null);

    setUser(null);
  };


  const isAuthenticated =
    Boolean(token && user);


  return (

    <AuthContext.Provider
      value={{
        user,
        token,
        loading,
        isAuthenticated,
        login,
        register,
        logout,
      }}
    >

      {children}

    </AuthContext.Provider>
  );
}


export function useAuth() {

  return useContext(
    AuthContext
  );
}