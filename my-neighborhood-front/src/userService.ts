import Axios from './callerService';

let getUserById = (id) => {
  return Axios.get(`users/${id}`);
};

let updateUser = (id, credentials) => {
  return Axios.put(`users/${id}`, credentials);
};

export const userService = {
  getUserById,
  updateUser
};
