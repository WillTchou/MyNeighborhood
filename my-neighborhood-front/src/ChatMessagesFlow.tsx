import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { Box, InputAdornment } from '@mui/material';
import { ButtonClick } from './ButtonClick';
import { TextInput } from './TextInput';
import { Message } from './Message';
import { ChatMessageGet } from './models';
import { useEffect, useRef } from 'react';

type ChatMessagesFlowProps = {
  privateChats: ChatMessageGet[];
  message: string;
  handleMessage: (event: any) => void;
  onKeyDown: (event: any) => void;
  send: (event: any) => void;
};

export const ChatMessagesFlow = ({
  privateChats,
  message,
  handleMessage,
  onKeyDown,
  send
}: ChatMessagesFlowProps) => {
  const classes = useStyles();
  const messagesEndRef = useRef(null);

  useEffect(() => {
    scrollToBottom();
  }, [privateChats]);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };
  return (
    <Box className={classes.conversation}>
      {privateChats &&
        privateChats.map((chat, index) => (
          <ul className={classes.messageList} ref={messagesEndRef}>
            <Message key={index} chatMessage={chat} />
          </ul>
        ))}
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
                  onClick={send}
                >
                  Send
                </ButtonClick>
              </InputAdornment>
            )
          }}
        />
      </div>
    </Box>
  );
};

const useStyles = makeStyles({
  conversation: {
    display: 'flex',
    flexDirection: 'column',
    flexGrow: 1,
    padding: '22px',
    zIndex: '80 !important',
    backgroundColor: 'white',
    overflowY: 'scroll',
    borderLeft: '1px solid #FADADD',
    height: '100vh',
    marginTop: '55px'
  },
  messageFlow: {
    flexGrow: 1,
    overflow: 'auto'
  },
  messageList: {
    listStyle: 'none'
  }
});
