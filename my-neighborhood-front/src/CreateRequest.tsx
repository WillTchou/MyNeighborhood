import {
  FormControl,
  InputLabel,
  MenuItem,
  Paper,
  Select,
  SelectChangeEvent,
  Stack
} from '@mui/material';
import makeStyles from '@mui/styles/makeStyles';
import { Header } from './Header';
import { ChangeEvent, useState } from 'react';
import { RequestPost, Type, TypeEnum } from './models';
import { TextInput } from './TextInput';
import setBodyColor from './setBody';
import { ButtonClick } from './ButtonClick';
import { useNavigate } from 'react-router-dom';
import { useLoadScript } from '@react-google-maps/api';
import { useRedirectToSignInPage } from './useRedirectToSignInPage';
import PlacesAutocomplete, {
  geocodeByAddress,
  getLatLng
} from 'react-places-autocomplete';
import { requestService } from './requestService';

export const CreateRequest = () => {
  const classes = useStyles();
  const navigate = useNavigate();
  useRedirectToSignInPage();

  const initialValue = {
    type: 'MaterialNeed' as Type,
    latitude: null,
    longitude: null,
    description: ''
  };

  const [request, setRequest] = useState<RequestPost>(initialValue);
  const [location, setLocation] = useState('');

  setBodyColor({ color: '#DDFFF7' });

  const { isLoaded } = useLoadScript({
    googleMapsApiKey: process.env.REACT_APP_GOOGLE_MAPS_API_KEY,
    libraries: ['places']
  });

  if (!isLoaded) return <div>Loading...</div>;

  const isValid =
    !!request.description && !!request.latitude && !!request.longitude;

  const handleTypeChange = (event: SelectChangeEvent) => {
    setRequest({ ...request, type: event.target.value as Type });
  };

  const handleDescriptionChange = (event: ChangeEvent<HTMLInputElement>) => {
    setRequest({ ...request, description: event.target.value });
  };

  const redirectHomePage = () => {
    navigate('/');
  };

  const onSubmit = (e: SubmitEvent) => {
    e.preventDefault();
    requestService.createRequest(request).then(() => redirectHomePage());
  };

  const onLocationChange = (value) => {
    setLocation(value);
  };

  const handleSelect = (address) => {
    geocodeByAddress(address)
      .then((results) => getLatLng(results[0]))
      .then(({ lat, lng }) => {
        setLocation(address);
        setRequest({ ...request, latitude: lat, longitude: lng });
      })
      .catch((error) => console.error('Error', error));
  };

  return (
    <div>
      <Header />
      <Paper elevation={2} className={classes.form}>
        <h1 className={classes.title}>Create Request</h1>
        <Stack
          spacing={5}
          direction="row"
          className={classes.formContainer}
          useFlexGap
          flexWrap="wrap"
        >
          <FormControl>
            <InputLabel id="select-request-type">
              Type of the request
            </InputLabel>
            <Select
              labelId="select-request-type"
              value={request.type}
              label="Type of the request"
              onChange={handleTypeChange}
            >
              <MenuItem value={'MaterialNeed'}>
                {TypeEnum.MaterialNeed}
              </MenuItem>
              <MenuItem value={'OneTimeTask'}>{TypeEnum.OneTimeTask}</MenuItem>
            </Select>
          </FormControl>
          <PlacesAutocomplete
            value={location}
            onChange={onLocationChange}
            onSelect={handleSelect}
          >
            {({
              getInputProps,
              suggestions,
              getSuggestionItemProps,
              loading
            }) => (
              <div>
                <input
                  {...getInputProps({
                    placeholder: 'Search Places ...',
                    className: 'location-search-input'
                  })}
                />
                <div className="autocomplete-dropdown-container">
                  {loading && <div>Loading...</div>}
                  {suggestions.map((suggestion) => {
                    const className = suggestion.active
                      ? 'suggestion-item--active'
                      : 'suggestion-item';
                    const style = suggestion.active
                      ? { backgroundColor: '#fafafa', cursor: 'pointer' }
                      : { backgroundColor: '#ffffff', cursor: 'pointer' };
                    return (
                      <div
                        {...getSuggestionItemProps(suggestion, {
                          className,
                          style
                        })}
                      >
                        <span>{suggestion.description}</span>
                      </div>
                    );
                  })}
                </div>
              </div>
            )}
          </PlacesAutocomplete>
          <TextInput
            label="Description"
            onChange={handleDescriptionChange}
            type="text"
            placeholder="Description of the request (300 characters maximum)"
            rows={8}
            fullwidth
            multiline
            maxLength={300}
          />
        </Stack>
        <Stack
          spacing={2}
          direction="row"
          justifyContent="center"
          className={classes.submitButtonStack}
        >
          <ButtonClick
            type="submit"
            variant="contained"
            backgroundColor="#FFA69E"
            size="large"
            onClick={onSubmit}
            disabled={!isValid}
          >
            Create request
          </ButtonClick>
        </Stack>
      </Paper>
    </div>
  );
};

const useStyles = makeStyles({
  title: {
    display: 'flex',
    justifyContent: 'center'
  },
  form: {
    width: '600px',
    margin: 'auto',
    marginTop: '150px',
    padding: '60px'
  },
  formContainer: {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center'
  },
  submitButtonStack: {
    marginTop: '18px'
  }
});
