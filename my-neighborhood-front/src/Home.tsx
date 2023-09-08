import { Header } from './Header';
import { useLoadScript } from '@react-google-maps/api';
import { Map } from './Map';
import { useRedirectToSignInPage } from './useRedirectToSignInPage';

export const Home = () => {
  useRedirectToSignInPage();

  const { isLoaded } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY
  });

  if (!isLoaded) return <div>Loading...</div>;

  return (
    <div>
      <Header />
      <Map />
    </div>
  );
};
