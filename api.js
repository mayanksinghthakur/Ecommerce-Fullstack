import axios from "axios";
// Import the function to get your token (adjust path based on your project structure)
// If you are using LocalStorage directly:
// import { getToken } from "../utils/auth"; // Hypothetical utility, see below

const api = axios.create({
  baseURL: `${import.meta.env.VITE_BACK_END_URL}/api`,
  // IMPORTANT: If you are switching to JWT Headers,
  // you might not need withCredentials anymore unless you use HttpOnly cookies for refresh tokens.
  // We will keep it for now, but the header is what matters for 401.
  withCredentials: true,
});
api.interceptors.request.use(
  (config) => {
    // 1. Get raw string
    const authString = localStorage.getItem("auth");
    console.log("Raw Auth String from Storage:", authString); 

    // 2. Parse it
    const authUser = authString ? JSON.parse(authString) : null;
    console.log("Parsed Auth User Object:", authUser);

    // 3. Check for token
    const token = authUser?.jwt || authUser?.jwtToken || authUser?.accessToken || authUser?.token;
    console.log("Extracted Token Value:", token);

    // 4. Attach it
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
      console.log("Token attached to header!"); 
    } else {
      console.warn("NO TOKEN FOUND. Request will likely fail.");
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
export default api;
