import { MarkerF } from '@react-google-maps/api';
import { RequestDrawer } from './RequestDrawer';
import { RequestGet, TypeEnum } from './models';
import { useState } from 'react';

type RequestMarkerProps = {
  request: RequestGet;
  onSend: (event: any, request: RequestGet, message: string) => void;
};

export const RequestMarker = ({ request, onSend }: RequestMarkerProps) => {
  const [openDrawer, setOpenDrawer] = useState(false);

  const toggleDrawer = () => {
    setOpenDrawer((prev) => !prev);
  };

  const onClose = () => {
    setOpenDrawer(false);
  };

  return (
    <div className="request-marker">
      <MarkerF
        position={{ lat: request.latitude, lng: request.longitude }}
        onClick={toggleDrawer}
        icon={
          TypeEnum[request.type] === TypeEnum.OneTimeTask
            ? 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
            : 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png'
        }
      />
      <RequestDrawer
        open={openDrawer}
        onClose={onClose}
        request={request}
        onSend={onSend}
      />
    </div>
  );
};
