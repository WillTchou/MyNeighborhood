import { GoogleMap } from '@react-google-maps/api';
import { useEffect, useState } from 'react';
import { requestService } from './requestService';
import { ChatMessagePost, MessageType, RequestGet, User } from './models';
import { useNavigate } from 'react-router-dom';
import { volunteerService } from './volunteerService';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { userService } from './userService';
import { RequestMarker } from './RequestMarker';
import { currentUserId, headers } from './apiModels';
import makeStyles from '@mui/styles/makeStyles/makeStyles';

var stompClient = null;
let initialValue = {
  id: '',
  firstname: '',
  lastname: '',
  email: '',
  password: '',
  address: '',
  governmentIdentityId: '',
  profilePictureId: ''
};

let MINUTE_MS = 30000;

export const Map = () => {
  const [latitude, setLatitude] = useState<number>();
  const [longitude, setLongitude] = useState<number>();
  const [requests, setRequests] = useState<RequestGet[]>([]);
  const [user, setUser] = useState<User>(initialValue);
  const [unfulfilledNumber, setUnfulfilledNumber] = useState(0);

  const classes = useStyles();
  const navigate = useNavigate();

  const getUnfulfilledRequest = () => {
    requestService
      .getRequestsNumber()
      .then((res) => res.data)
      .then((result) => {
        setUnfulfilledNumber(result);
        console.log(result);
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
    userService
      .getUserById(currentUserId)
      .then((res) => res.data)
      .then((result) => setUser(result));
  }, []);

  useEffect(() => {
    requestService
      .getRequests()
      .then((res) => res.data)
      .then((result) => setRequests(result));
  }, []);

  const center = { lat: latitude, lng: longitude };

  const sendPrivateValue = (event, request: RequestGet, message: string) => {
    event.preventDefault();
    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);
    if (stompClient) {
      var chatMessage: ChatMessagePost = {
        sender: user,
        recipient: request.requester,
        content: message,
        messageType: MessageType.CHAT,
        flow:
          user.id < request.requester.id
            ? user.id + '/' + request.requester.id
            : request.requester.id + '/' + user.id
      };
      stompClient.send(
        '/myNeighborhood/private-message',
        headers,
        JSON.stringify(chatMessage)
      );
    }
    helpRequest(request.id);
  };

  const helpRequest = (requestId: string) => {
    volunteerService
      .createVolunteer(requestId)
      .then(() => redirectChatboxPage());
  };

  const redirectChatboxPage = () => {
    navigate('/chatbox');
  };

  return (
    <GoogleMap
      zoom={12}
      center={center}
      mapContainerStyle={{ width: '100%', height: '100vh' }}
    >
      <>
        {requests &&
          requests.map((request, index) => (
            <RequestMarker
              key={index}
              request={request}
              onSend={sendPrivateValue}
            />
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
    padding: '23px',
    borderRadius: '8px'
  }
});
