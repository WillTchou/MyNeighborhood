import { authService } from './authService';
import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

export const useRedirectToSignInPage = () => {
  const navigate = useNavigate();
  useEffect(() => {
    if (!authService.isLogged()) {
      navigate('/signIn');
    }
  }, []);
};
