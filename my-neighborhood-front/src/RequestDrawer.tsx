import { Avatar, Box, Drawer, Stack } from '@mui/material';
import { Label } from './Label';
import { ButtonClick } from './ButtonClick';
import {
  ChatMessagePost,
  MessageType,
  RequestGet,
  Status,
  TypeEnum,
  User
} from './models';
import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { useEffect, useState } from 'react';
import { FulFillRequestPopin } from './FulFillRequestPopin';
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { headers } from './apiModels';
import { volunteerService } from './volunteerService';
import { useNavigate } from 'react-router-dom';
import { userService } from './userService';
import { apiHost } from './callerService';
import { authService } from './authService';

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
var stompClient = null;

type RequestDrawerProps = {
  open: boolean;
  onClose: () => void;
  request: RequestGet;
};

export const RequestDrawer = ({
  open,
  onClose,
  request
}: RequestDrawerProps) => {
  const [openPopin, setOpenPopin] = useState(false);
  const [user, setUser] = useState<User>(initialValue);
  const [error, setError] = useState('');

  const classes = useStyles();
  const navigate = useNavigate();

  useEffect(() => {
    userService
      .getUserById(authService.getUserId())
      .then((res) => res.data)
      .then((result) => setUser(result));
  }, []);

  const configureSocket = async () => {
    let Sock = new SockJS(`${apiHost}/ws`);
    stompClient = over(Sock);
    stompClient.connect({}, () =>
      stompClient.subscribe('/user/' + authService.getUserId() + '/private', {})
    );
  };

  const handleClickOpen = () => {
    configureSocket();
    setOpenPopin(true);
  };

  const redirectChatboxPage = () => {
    navigate('/chatbox');
  };

  const sendMessage = (message: string) => {
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
  };

  const helpRequest = (requestId: string, message: string) => {
    volunteerService
      .createVolunteer(requestId)
      .then(() => {
        redirectChatboxPage();
        sendMessage(message);
      })
      .catch((err) => {
        setError(err.response.data.code);
        setTimeout(() => {
          setError('');
        }, 4000);
      });
  };

  const sendPrivateValue = async (
    event,
    request: RequestGet,
    message: string
  ) => {
    event.preventDefault();
    if (stompClient) {
      helpRequest(request.id, message);
    }
  };

  return (
    <Drawer
      key={request.id}
      anchor="left"
      open={open}
      onClose={onClose}
      classes={{ paper: classes.paper }}
    >
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        flexDirection="column"
        marginTop={'85px'}
      >
        <Stack direction="column">
          <Avatar
            alt={request.requester.firstname}
            src="/static/images/avatar/1.jpg"
            sx={{
              width: 86,
              height: 86,
              margin: 'auto'
            }}
          />
          <h4>
            {request.requester.firstname} {request.requester.lastname}
          </h4>
        </Stack>
        <Stack spacing={2} direction="row">
          <Label children={TypeEnum[request.type]} />
          <Label children={Status[request.status]} />
        </Stack>
        <Stack spacing={2} direction="column" className={classes.part}>
          <span className={classes.address}>{request.address}</span>
          <p className={classes.description}>{request.description}</p>
        </Stack>
        <div className={classes.fulfill}>
          <ButtonClick
            backgroundColor="#FFA69E"
            color="white"
            borderRadius={12}
            boxShadow
            padding={12}
            onClick={handleClickOpen}
          >
            Fulfill the need
          </ButtonClick>
          {openPopin && (
            <FulFillRequestPopin
              open={openPopin}
              setOpen={setOpenPopin}
              request={request}
              onSend={sendPrivateValue}
              error={error}
            />
          )}
        </div>
      </Box>
    </Drawer>
  );
};

const useStyles = makeStyles({
  paper: {
    width: 300,
    zIndex: '80 !important'
  },
  part: {
    marginTop: '22px'
  },
  address: {
    display: 'flex',
    justifyContent: 'center',
    fontWeight: 'bold',
    color: '#FFA69E'
  },
  description: {
    width: '85%',
    marginTop: '25px'
  },
  fulfill: {
    marginTop: '30px'
  }
});
