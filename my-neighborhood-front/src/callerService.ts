import axios from 'axios';
import { authService } from './authService';

const Axios = axios.create({
  baseURL: 'http://localhost:8080/api/v1/'
});

Axios.interceptors.request.use((req) => {
  if (authService.isLogged()) {
    req.headers.Authorization = 'Bearer ' + authService.getToken();
    req.headers.userId = localStorage.getItem('userId');
  }
  return req;
});

export default Axios;
