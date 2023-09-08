import axios from 'axios';

let url: 'http://localhost:8080/api/v1/governmentIdentity';
let headers = {
  headers: { 'Content-Type': 'multipart/form-data' }
};

let uploadGovernmentIdentity = (governmentIdentity: File) => {
  return axios.post(url, governmentIdentity, headers);
};

export const governmentIdentityService = {
  uploadGovernmentIdentity
};
