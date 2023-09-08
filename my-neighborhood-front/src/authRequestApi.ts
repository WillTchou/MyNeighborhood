import { authService } from './authService';
import { AuthRequest, RegisterRequest } from './models';

export const authRequestLogin = (
  authRequest: AuthRequest,
  handleThen?: any,
  handleError?: any
) => {
  authService.login(authRequest).then(handleThen).catch(handleError);
};

export const authRequestRegister = (
  authRequest: RegisterRequest,
  handleThen?: any,
  handleError?: any
) => {
  authService.signUp(authRequest).then(handleThen).catch(handleError);
};
