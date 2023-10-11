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
  const [isConnected, setIsConnected] = useState(false);
  const [stompClient, setStompClient] = useState(null);

  const onError = (error) => {
    console.error(error);
  };

  useEffect(() => {
    let Sock = new SockJS(`https://my-neighborhood-app.com/ws`);
    const client = over(Sock);
    client.connect(
      {},
      () => {
        setIsConnected(true);
      },
      onError
    );

    setStompClient(client);

    return () => {
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, []);

  useEffect(() => {
    if (isConnected) {
      const subscription = stompClient.subscribe(
        '/user/' + authService.getUserId() + '/private',
        onPrivateMessage
      );
      return () => {
        subscription.unsubscribe();
      };
    }
  }, [isConnected]);

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
    if (!!stompClient && message !== '' && isConnected) {
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
    } else {
      setTimeout(() => sendPrivateValue(event), 300);
    }
  };

  const onPrivateMessage = (payload) => {
    if (isConnected) {
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
    } else {
      setTimeout(() => onPrivateMessage(payload), 300);
    }
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
