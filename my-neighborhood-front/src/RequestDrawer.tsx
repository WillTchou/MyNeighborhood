import { Avatar, Box, Drawer, Stack } from '@mui/material';
import { Label } from './Label';
import { ButtonClick } from './ButtonClick';
import { RequestGet, Status, TypeEnum } from './models';
import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { useState } from 'react';
import { FulFillRequestPopin } from './FulFillRequestPopin';

type RequestDrawerProps = {
  open: boolean;
  onClose: () => void;
  request: RequestGet;
  onSend: (event: any, request: RequestGet, message: string) => void;
};

export const RequestDrawer = ({
  open,
  onClose,
  request,
  onSend
}: RequestDrawerProps) => {
  const [openPopin, setOpenPopin] = useState(false);

  const classes = useStyles();

  const handleClickOpen = () => {
    setOpenPopin(true);
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
        <p className={classes.description}>{request.description}</p>
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
              onSend={onSend}
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
  description: {
    width: '85%',
    marginTop: '25px'
  },
  fulfill: {
    marginTop: '30px'
  }
});
