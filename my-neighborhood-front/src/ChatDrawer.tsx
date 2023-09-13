import MuiDrawer from '@mui/material/Drawer';
import List from '@mui/material/List';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import { CSSObject, Theme, styled } from '@mui/material/styles';
import { RecipientItem } from './RecipientItem';
import { useEffect, useState } from 'react';
import { ChatMessageGet, User } from './models';
import { authService } from './authService';
import { userService } from './userService';
import { chatMessageService } from './chatMessageService';
import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { useNavigate } from 'react-router-dom';

const drawerWidth = 240;

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

type ChatDrawerProps = {
  privateChats: ChatMessageGet[];
  setPrivateChats: React.Dispatch<React.SetStateAction<ChatMessageGet[]>>;
  setRecipient: React.Dispatch<React.SetStateAction<User>>;
  setTab: React.Dispatch<React.SetStateAction<string>>;
};

export const ChatDrawer = ({
  privateChats,
  setPrivateChats,
  setRecipient,
  setTab
}: ChatDrawerProps) => {
  const classes = useStyles();
  const navigate = useNavigate();

  const [open, setOpen] = useState(false);
  const [selectedIndex, setSelectedIndex] = useState(0);
  const [latestMessages, setLatestMessages] = useState<ChatMessageGet[]>([]);

  useEffect(() => {
    chatMessageService
      .getLatestChatMessagesForSender()
      .then((res) => res.data)
      .then((result: ChatMessageGet[]) => {
        if (result.length === 0) {
          navigate('/');
        }
        setLatestMessages(result);
      });
  }, [privateChats]);

  const handleDrawer = () => {
    setOpen(!open);
  };

  const handleTab = (recipientId: string, key: number) => {
    setTab(recipientId);
    setSelectedIndex(key);
    chatMessageService
      .getChatFlow(recipientId)
      .then((res) => res.data)
      .then((result) => {
        setPrivateChats(result);
      })
      .catch((err) => console.log(err));
    userService
      .getUserById(recipientId)
      .then((res) => res.data)
      .then((result) => {
        setRecipient(result);
      });
  };

  return (
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
          {latestMessages &&
            latestMessages.map((lastMessage, key) => (
              <RecipientItem
                key={key}
                primary={
                  lastMessage.sender.id === authService.getUserId()
                    ? lastMessage.recipient.firstname
                    : lastMessage.sender.firstname
                }
                secondary={lastMessage.content}
                open={open}
                onClick={() =>
                  handleTab(
                    lastMessage.sender.id === authService.getUserId()
                      ? lastMessage.recipient.id
                      : lastMessage.sender.id,
                    key
                  )
                }
                selected={selectedIndex === key}
              />
            ))}
        </List>
      </div>
    </Drawer>
  );
};

const useStyles = makeStyles({
  paper: {
    zIndex: '80 !important'
  },
  content: {
    marginTop: '65px'
  }
});
