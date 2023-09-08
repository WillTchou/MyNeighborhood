import Axios from './callerService';
import { AuthRequest, RegisterRequest } from './models';

let login = (credentials: AuthRequest) => {
  return Axios.post('auth/authentication', credentials);
};

let signUp = (credentials: RegisterRequest) => {
  return Axios.post('auth/register', credentials);
};

let saveCredentials = (token, userId) => {
  localStorage.setItem('token', token);
  localStorage.setItem('userId', userId);
};

let logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('userId');
};

let isLogged = () => {
  let token = localStorage.getItem('token');
  return !!token;
};

let getToken = () => {
  return localStorage.getItem('token');
};

let getUserId = () => {
  return localStorage.getItem('userId');
};

export const authService = {
  login,
  signUp,
  saveCredentials,
  logout,
  isLogged,
  getToken,
  getUserId
};
