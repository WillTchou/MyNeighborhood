import {
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle
} from '@mui/material';
import { TextInput } from './TextInput';
import { RequestGet } from './models';
import { ButtonClick } from './ButtonClick';
import { Dispatch, SetStateAction, useState } from 'react';

type FulFillRequestPopinProps = {
  open: boolean;
  setOpen: Dispatch<SetStateAction<boolean>>;
  request: RequestGet;
  onSend: (event: any, request: RequestGet, message: string) => void;
};

export const FulFillRequestPopin = ({
  open,
  setOpen,
  request,
  onSend
}: FulFillRequestPopinProps) => {
  const [message, setMessage] = useState('');
  const [openMessage, setOpenMessage] = useState(true);

  const handleMessage = (event) => {
    event.preventDefault();
    setMessage(event.target.value);
  };

  const enableMessage = () => {
    setOpenMessage(false);
  };

  const disableMessage = () => {
    setOpenMessage(true);
  };

  const handleClose = () => {
    setOpen(false);
    disableMessage();
  };

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>One step from being a Hero !</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Do you confirm your willingness to help {request.requester.firstname}{' '}
          {request.requester.lastname} on his/her request? If yes we invite you
          to send in the messages page
        </DialogContentText>
      </DialogContent>
      <DialogContent>
        <TextInput
          label="Send your first message"
          onChange={handleMessage}
          value={message}
          disabled={openMessage}
          variant="filled"
          fullwidth={true}
        />
      </DialogContent>
      <DialogActions>
        {openMessage ? (
          <ButtonClick
            backgroundColor="#FFA69E"
            color="white"
            onClick={enableMessage}
          >
            Let’s Help !
          </ButtonClick>
        ) : (
          <ButtonClick
            backgroundColor="#FFA69E"
            color="white"
            onClick={(event) => onSend(event, request, message)}
          >
            Send message
          </ButtonClick>
        )}
        <ButtonClick color="#FFA69E" onClick={handleClose} boxShadow={true}>
          Cancel
        </ButtonClick>
      </DialogActions>
    </Dialog>
  );
};
