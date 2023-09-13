import { useEffect, useState } from 'react';
import { Header } from './Header';
import { RequestGet, TypeEnum, Status } from './models';
import { requestService } from './requestService';
import { makeStyles } from '@mui/styles';
import { Stack } from '@mui/material';
import { Label } from './Label';
import { useRedirectToSignInPage } from './useRedirectToSignInPage';
import setBodyColor from './setBody';
import dateFormat from 'dateformat';
import { RepublishRequest } from './RepublishRequest';

export const MyRequests = () => {
  const classes = useStyles();
  useRedirectToSignInPage();
  setBodyColor({ color: '#DDFFF7' });

  const [requests, setRequests] = useState<RequestGet[]>([]);

  useEffect(() => {
    requestService
      .getRequestsByUserId()
      .then((res) => setRequests(res.data))
      .catch((err) => console.log(err));
  });

  return (
    <div>
      <Header />
      <div className={classes.requestsList}>
        {requests &&
          requests.map((request, index) => (
            <div key={index} className={classes.requestItem}>
              <Stack className={classes.requestItemContent} direction="column">
                <h3>
                  Creation date:{' '}
                  {dateFormat(
                    request.creationDate,
                    'dddd, mmmm dS, yyyy, hh:MM:ss'
                  )}
                </h3>
                <Stack justifyContent="center" spacing={2} direction="row">
                  <Label children={TypeEnum[request.type]} />
                  <Label children={Status[request.status]} />
                </Stack>
                <p className={classes.description}>{request.description}</p>
                {request.status === Status.Unfulfilled &&
                  !request.displayed && <RepublishRequest request={request} />}
              </Stack>
            </div>
          ))}
      </div>
    </div>
  );
};

const useStyles = makeStyles({
  requestsList: {
    display: 'grid',
    justifyContent: 'center',
    marginTop: '120px',
    gap: '3em'
  },
  requestItem: {
    borderRadius: '8px',
    boxShadow: '0 0 0 1em #f4aab9'
  },
  requestItemContent: {
    padding: '18px',
    backgroundColor: 'white'
  },
  description: {
    display: 'flex',
    textAlign: 'center',
    justifyContent: 'center',
    marginTop: '25px'
  }
});
