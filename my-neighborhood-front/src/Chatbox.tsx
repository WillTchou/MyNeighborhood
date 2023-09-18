import { Header } from './Header';
import { useEffect, useState } from 'react';
import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { useRedirectToSignInPage } from './useRedirectToSignInPage';
import { ChatMessageGet, ChatMessagePost, MessageType, User } from './models';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';
import { userService } from './userService';
import setBodyColor from './setBody';
import { authService } from './authService';
import { ChatDrawer } from './ChatDrawer';
import { ChatMessagesFlow } from './ChatMessagesFlow';
import { chatMessageService } from './chatMessageService';

var stompClient = null;

let initialValueSender = {
  id: authService.getUserId(),
  firstname: '',
  lastname: '',
  email: '',
  password: '',
  address: '',
  governmentIdentityId: '',
  profilePictureId: ''
};

let initialValueRecipient = {
  id: '',
  firstname: '',
  lastname: '',
  email: '',
  password: '',
  address: '',
  governmentIdentityId: '',
  profilePictureId: ''
};

export const Chatbox = () => {
  const classes = useStyles();

  useRedirectToSignInPage();
  setBodyColor({ color: '#DDFFF7' });

  const [privateChats, setPrivateChats] = useState<ChatMessageGet[]>([]);
  const [sender, setSender] = useState<User>(initialValueSender);
  const [recipient, setRecipient] = useState<User>(initialValueRecipient);
  const [tab, setTab] = useState('');
  const [message, setMessage] = useState('');

  const onConnected = () => {
    setTimeout(
      () =>
        stompClient.subscribe(
          '/user/' + authService.getUserId() + '/private',
          onPrivateMessage
        ),
      800
    );
  };

  const onError = (error) => {
    console.log(error);
  };

  const connect = async () => {
    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
  };

  useEffect(() => {
    connect();
    return () => console.log('disconnect');
  }, []);

  useEffect(() => {
    userService
      .getUserById(authService.getUserId())
      .then((res) => res.data)
      .then((result) => setSender(result));
  }, []);

  const handleMessage = (event) => {
    const { value } = event.target;
    setMessage(value);
  };

  const emptyMessage = () => {
    setMessage('');
  };

  const onKeyDown = (event) => {
    if (event.keyCode === 13) {
      sendPrivateValue(event);
    }
  };

  const sendPrivateValue = (event) => {
    event.preventDefault();
    if (stompClient && message !== '') {
      var chatMessagePost: ChatMessagePost = {
        sender: sender,
        recipient: recipient,
        content: message,
        flow:
          sender.id < recipient.id
            ? sender.id + '/' + recipient.id
            : recipient.id + '/' + sender.id,
        messageType: MessageType.CHAT
      };
      stompClient.send(
        '/myNeighborhood/private-message',
        {},
        JSON.stringify(chatMessagePost)
      );
      let chatMessageGet: ChatMessageGet = {
        ...chatMessagePost,
        date: new Date()
      };
      emptyMessage();
      setPrivateChats([...privateChats, chatMessageGet]);
    }
  };

  const onPrivateMessage = (payload) => {
    let messageData: ChatMessageGet = JSON.parse(payload.body);
    setTimeout(() => {
      chatMessageService
        .getChatFlow(messageData.sender.id)
        .then((res) => res.data)
        .then((result) => {
          setPrivateChats(result);
        })
        .catch((err) => console.log(err));
    }, 1000);
  };

  return (
    <div>
      <Header />
      <div className={classes.chatbox}>
        <ChatDrawer
          privateChats={privateChats}
          setPrivateChats={setPrivateChats}
          setRecipient={setRecipient}
          setTab={setTab}
        />
        {tab && (
          <ChatMessagesFlow
            privateChats={privateChats}
            message={message}
            handleMessage={handleMessage}
            onKeyDown={onKeyDown}
            send={sendPrivateValue}
          />
        )}
      </div>
    </div>
  );
};

const useStyles = makeStyles({
  chatbox: {
    display: 'flex'
  }
});
