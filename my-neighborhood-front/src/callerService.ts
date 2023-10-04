import axios from 'axios';
import { authService } from './authService';

export const apiHost = process.env.REACT_APP_API_HOST

const Axios = axios.create({
  baseURL: `${apiHost}/api/v1/`
});

Axios.interceptors.request.use((req) => {
  if (authService.isLogged()) {
    req.headers.Authorization = 'Bearer ' + authService.getToken();
    req.headers.userId = localStorage.getItem('userId');
  }
  return req;
});

export default Axios;
