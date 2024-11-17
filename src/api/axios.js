import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8005/auth", // Spring Boot backend base URL
  headers: {
    "Content-Type": "application/json",
  },
});

export default axiosInstance;
