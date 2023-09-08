import { authService } from './authService';

export let headers = {
  Authorization: 'Bearer ' + authService.getToken()
};

export let currentUserId = authService.getUserId();
