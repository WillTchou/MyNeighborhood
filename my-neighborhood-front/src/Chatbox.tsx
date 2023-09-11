import { Header } from './Header';
import { CSSObject, Theme, styled } from '@mui/material/styles';
import MuiDrawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import { useEffect, useState } from 'react';
import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { useRedirectToSignInPage } from './useRedirectToSignInPage';
import { RecipientItem } from './RecipientItem';
import { Box, InputAdornment } from '@mui/material';
import { ButtonClick } from './ButtonClick';
import { ChatMessageGet, ChatMessagePost, MessageType, User } from './models';
import SockJS from 'sockjs-client';
import { over } from 'stompjs';
import { TextInput } from './TextInput';
import { chatMessageService } from './chatMessageService';
import { userService } from './userService';
import { currentUserId, headers } from './apiModels';
import { Message } from './Message';
import setBodyColor from './setBody';

const drawerWidth = 240;
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

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'flex-end',
  padding: theme.spacing(0, 1),
  ...theme.mixins.toolbar
}));

const openedMixin = (theme: Theme): CSSObject => ({
  width: drawerWidth,
  transition: theme.transitions.create('width', {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.enteringScreen
  }),
  overflowX: 'hidden'
});

const closedMixin = (theme: Theme): CSSObject => ({
  transition: theme.transitions.create('width', {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen
  }),
  overflowX: 'hidden',
  width: `calc(${theme.spacing(7)} + 1px)`,
  [theme.breakpoints.up('sm')]: {
    width: `calc(${theme.spacing(8)} + 1px)`
  }
});

const Drawer = styled(MuiDrawer, {
  shouldForwardProp: (prop) => prop !== 'open'
})(({ theme, open }) => ({
  width: drawerWidth,
  flexShrink: 0,
  whiteSpace: 'nowrap',
  boxSizing: 'border-box',
  ...(open && {
    ...openedMixin(theme),
    '& .MuiDrawer-paper': openedMixin(theme)
  }),
  ...(!open && {
    ...closedMixin(theme),
    '& .MuiDrawer-paper': closedMixin(theme)
  })
}));

export const Chatbox = () => {
  const classes = useStyles();
  useRedirectToSignInPage();
  setBodyColor({ color: '#DDFFF7' });

  const [open, setOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const [message, setMessage] = useState('');
  const [latestMessages, setLatestMessages] = useState<ChatMessageGet[]>([]);
  const [privateChats, setPrivateChats] = useState<ChatMessageGet[]>([]);
  const [tab, setTab] = useState('');
  const [sender, setSender] = useState<User>(initialValue);
  const [recipient, setRecipient] = useState<User>(initialValue);

  useEffect(() => {
    connect();
    return () => console.log('disconnect');
  }, []);

  useEffect(() => {
    chatMessageService
      .getLatestChatMessagesForSender()
      .then((res) => res.data)
      .then((result) => setLatestMessages(result));
  }, [privateChats]);

  useEffect(() => {
    userService
      .getUserById(currentUserId)
      .then((res) => res.data)
      .then((result) => setSender(result));
  }, []);

  const onConnected = () => {
    stompClient.subscribe('/chatroom/public', {});
    stompClient.subscribe(
      '/user/' + currentUserId + '/private',
      onPrivateMessage
    );
  };

  const onError = (error) => {
    console.log(error);
  };

  const connect = () => {
    let Sock = new SockJS('http://localhost:8080/ws');
    stompClient = over(Sock);
    stompClient.connect({}, onConnected, onError);
  };

  const handleDrawer = () => {
    setOpen(!open);
  };

  const handleMessage = (event) => {
    const { value } = event.target;
    setMessage(value);
  };

  const handleTab = (recipientId: string, key: number) => {
    setTab(recipientId);
    setSelectedIndex(key);
    chatMessageService
      .getChatFlow(recipientId)
      .then((res) => res.data)
      .then((result) => {
        setPrivateChats(result);
      });
    userService
      .getUserById(recipientId)
      .then((res) => res.data)
      .then((result) => {
        setRecipient(result);
      });
  };

  const emptyMessage = () => {
    setMessage('');
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
        sender: sender,
        recipient: recipient,
        content: message,
        messageType: MessageType.CHAT,
        date: new Date()
      };
      emptyMessage();
      setPrivateChats([...privateChats, chatMessageGet]);
    }
  };

  const onPrivateMessage = (payload) => {
    console.log(payload.body);
    console.log(privateChats);
    var messageData = JSON.parse(payload.body);
    let chatMessage: ChatMessageGet = {
      sender: messageData.sender,
      recipient: messageData.recipient,
      content: messageData.content,
      messageType: messageData.messageType,
      date: new Date()
    };
    setPrivateChats([...privateChats, chatMessage]);
  };

  const onKeyDown = (event) => {
    if (event.key === 13) {
      sendPrivateValue(event);
    }
  };

  return (
    <div>
      <Header />
      <div className={classes.chatbox}>
        <Drawer
          variant="permanent"
          open={open}
          anchor="left"
          classes={{ paper: classes.paper }}
        >
          <div className={classes.content}>
            <DrawerHeader>
              <IconButton onClick={handleDrawer}>
                {open ? <ChevronLeftIcon /> : <ChevronRightIcon />}
              </IconButton>
            </DrawerHeader>
            <Divider />
            <List>
              {latestMessages ? (
                latestMessages.map((lastMessage, key) => (
                  <RecipientItem
                    key={key}
                    primary={
                      lastMessage.sender.id === currentUserId
                        ? lastMessage.recipient.firstname
                        : lastMessage.sender.firstname
                    }
                    secondary={lastMessage.content}
                    open={open}
                    onClick={() =>
                      handleTab(
                        lastMessage.sender.id === currentUserId
                          ? lastMessage.recipient.id
                          : lastMessage.sender.id,
                        key
                      )
                    }
                    selected={selectedIndex === key}
                  />
                ))
              ) : (
                <div>Fulfill a request</div>
              )}
            </List>
          </div>
        </Drawer>
        {tab && (
          <Box className={classes.conversation}>
            <ul className={classes.messageFlow}>
              {privateChats &&
                privateChats.map((chat, index) => (
                  <ul className={classes.messageList}>
                    <Message key={index} chatMessage={chat} />
                  </ul>
                ))}
            </ul>
            <div>
              <TextInput
                placeholder="Type your message here..."
                type="text"
                variant="outlined"
                onChange={handleMessage}
                onKeyDown={onKeyDown}
                fullwidth
                value={message}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <ButtonClick
                        type="submit"
                        variant="contained"
                        backgroundColor="#FFA69E"
                        onClick={sendPrivateValue}
                      >
                        Send
                      </ButtonClick>
                    </InputAdornment>
                  )
                }}
              />
            </div>
          </Box>
        )}
      </div>
    </div>
  );
};

const useStyles = makeStyles({
  chatbox: {
    display: 'flex'
  },
  paper: {
    zIndex: '80 !important'
  },
  content: {
    marginTop: '65px'
  },
  conversation: {
    display: 'flex',
    flexDirection: 'column',
    flexGrow: 1,
    margin: '80px',
    padding: '22px',
    height: '700px',
    zIndex: '80 !important',
    backgroundColor: 'white',
    overflowY: 'scroll'
  },
  messageFlow: {
    flexGrow: 1,
    overflow: 'auto'
  },
  messageList: {
    listStyle: 'none'
  }
});
