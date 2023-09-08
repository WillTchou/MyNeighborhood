import { RequestPost } from './models';
import { requestService } from './requestService';

export const requestPost = (
  request: RequestPost,
  handleThen?: any,
  handleError?: any
) => {
  requestService.createRequest(request).then(handleThen).catch(handleError);
};

export const getAllRequests = (handleThen?: any, handleError?: any) => {
  requestService.getRequests().then(handleThen).catch(handleError);
};
