import {
  Dialog,
  DialogActions,
  DialogTitle,
  FormControlLabel,
  FormGroup,
  Switch
} from '@mui/material';
import { ButtonClick } from './ButtonClick';
import { RequestGet } from './models';
import { useState } from 'react';
import { requestService } from './requestService';

type RepublishRequestType = {
  request: RequestGet;
};

export const RepublishRequest = ({ request }: RepublishRequestType) => {
  const [open, setOpen] = useState(false);
  const [newRequest, setNewRequest] = useState<RequestGet>(request);

  const handleChange = () => {
    requestService
      .updateRequestById(request.id, newRequest)
      .then(() => {
        window.location.reload();
      })
      .catch((err) => console.log(err));
  };

  const handleClose = () => {
    setOpen(false);
  };

  const openPopin = (event: React.ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    let now = new Date();
    setNewRequest({
      ...newRequest,
      displayed: true,
      creationDate: now.toJSON()
    });
    setOpen(true);
  };

  return (
    <>
      <FormGroup>
        <FormControlLabel
          control={<Switch checked={request.displayed} onChange={openPopin} />}
          label="Republish the request"
        />
      </FormGroup>
      {open && (
        <Dialog open={open} onClose={handleClose}>
          <DialogTitle>
            Do you really want to republish the request?
          </DialogTitle>
          <DialogActions>
            <ButtonClick backgroundColor="#DDFFF7" onClick={handleChange}>
              Confirm
            </ButtonClick>
          </DialogActions>
        </Dialog>
      )}
    </>
  );
};
