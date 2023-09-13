import { GoogleMap } from '@react-google-maps/api';
import { useEffect, useState } from 'react';
import { requestService } from './requestService';
import { RequestGet } from './models';
import { RequestMarker } from './RequestMarker';
import makeStyles from '@mui/styles/makeStyles/makeStyles';

let MINUTE_MS = 30000;

export const Map = () => {
  const [latitude, setLatitude] = useState<number>();
  const [longitude, setLongitude] = useState<number>();
  const [requests, setRequests] = useState<RequestGet[]>([]);
  const [unfulfilledNumber, setUnfulfilledNumber] = useState(0);

  const classes = useStyles();

  const getUnfulfilledRequest = () => {
    requestService
      .getRequestsNumber()
      .then((res) => res.data)
      .then((result) => {
        setUnfulfilledNumber(result);
      });
  };

  useEffect(() => {
    getUnfulfilledRequest();
    const interval = setInterval(() => {
      getUnfulfilledRequest();
    }, MINUTE_MS);
    return () => clearInterval(interval);
  }, [requestService]);

  useEffect(() => {
    navigator.geolocation.getCurrentPosition((position) => {
      setLatitude(position.coords.latitude);
      setLongitude(position.coords.longitude);
    });
  }, []);

  useEffect(() => {
    requestService
      .getRequests()
      .then((res) => res.data)
      .then((result) => setRequests(result));
  }, []);

  const center = { lat: latitude, lng: longitude };

  return (
    <GoogleMap
      zoom={12}
      center={center}
      mapContainerStyle={{ width: '100%', height: '100vh' }}
    >
      <>
        {requests &&
          requests.map((request, index) => (
            <RequestMarker key={index} request={request} />
          ))}
        <div className={classes.number}>
          {unfulfilledNumber} unfulfilled requests
        </div>
      </>
    </GoogleMap>
  );
};

const useStyles = makeStyles({
  number: {
    position: 'absolute',
    margin: '10vh 50vw 0',
    backgroundColor: 'white',
    padding: '18px',
    borderRadius: '8px'
  }
});
