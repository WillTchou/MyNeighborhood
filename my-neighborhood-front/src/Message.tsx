import { authService } from './authService';
import { ChatMessageGet } from './models';
import { Avatar } from '@mui/material';
import makeStyles from '@mui/styles/makeStyles/makeStyles';

type MessageProps = {
  chatMessage: ChatMessageGet;
};

export const Message = ({ chatMessage }: MessageProps) => {
  const classes = useStyles();

  return (
    <li
      className={
        chatMessage.sender.id === authService.getUserId()
          ? classes.messageItemSender
          : classes.messageItemRecipient
      }
    >
      {chatMessage.sender.id !== authService.getUserId() && (
        <Avatar alt={chatMessage.sender.firstname} />
      )}
      <div
        className={
          chatMessage.sender.id === authService.getUserId()
            ? classes.messageDataSender
            : classes.messageDataRecipient
        }
      >
        {chatMessage.content}
      </div>
    </li>
  );
};

const useStyles = makeStyles({
  messageItemSender: {
    display: 'flex',
    justifyContent: 'flex-end',
    marginTop: '9px'
  },
  messageItemRecipient: {
    display: 'flex',
    justifyContent: 'flex-start',
    marginTop: '9px'
  },
  messageDataSender: {
    padding: '14px',
    borderRadius: '24px',
    backgroundColor: '#93E1D8'
  },
  messageDataRecipient: {
    padding: '14px',
    borderRadius: '24px',
    marginLeft: '9px',
    backgroundColor: '#DDFFF7'
  }
});
