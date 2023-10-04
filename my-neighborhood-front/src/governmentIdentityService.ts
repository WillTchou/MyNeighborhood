import { apiHost } from './callerService';
import axios from 'axios';

let url= `${apiHost}/api/v1/governmentIdentity`;
let headers = {
  headers: { 'Content-Type': 'multipart/form-data' }
};

let uploadGovernmentIdentity = (governmentIdentity: File) => {
  return axios.post(url, governmentIdentity, headers);
};

export const governmentIdentityService = {
  uploadGovernmentIdentity
};
