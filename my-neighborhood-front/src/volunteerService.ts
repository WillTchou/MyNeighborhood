import Axios from './callerService';

let createVolunteer = (requestId: string) => {
  return Axios.post(`volunteers/${requestId}`);
};

export const volunteerService = {
  createVolunteer
};
