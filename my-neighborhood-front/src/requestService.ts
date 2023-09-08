import Axios from './callerService';
import { RequestPost } from './models';

let createRequest = (request: RequestPost) => {
  return Axios.post('requests', request);
};

let getRequests = () => {
  return Axios.get('requests');
};

let getRequestsNumber = () => {
  return Axios.get('requests/number');
};

let getRequestById = (id) => {
  return Axios.get(`requests/${id}`);
};

let deleteRequestById = (id) => {
  return Axios.delete(`requests/${id}`);
};

let updateRequestById = (id, request) => {
  return Axios.put(`requests/${id}`, request);
};

export const requestService = {
  createRequest,
  getRequests,
  getRequestById,
  deleteRequestById,
  updateRequestById,
  getRequestsNumber
};
