import { TextField } from '@mui/material';
import makeStyles from '@mui/styles/makeStyles';
import PlacesAutocomplete from 'react-places-autocomplete';

type GooglePlacesAutocompleteProps = {
  location: string;
  onLocationChange: (value: any) => void;
  handleSelect: (address: any) => void;
};

export const GooglePlacesAutocomplete = ({
  location,
  onLocationChange,
  handleSelect
}: GooglePlacesAutocompleteProps) => {
  const classes = useStyles();

  return (
    <PlacesAutocomplete
      value={location}
      onChange={onLocationChange}
      onSelect={handleSelect}
    >
      {({ getInputProps, suggestions, getSuggestionItemProps, loading }) => (
        <div>
          <TextField
            {...getInputProps({
              placeholder: 'Search Places ...',
              className: 'location-search-input'
            })}
            label="Address"
          />
          <div className="autocomplete-dropdown-container">
            {loading && <div>Loading...</div>}
            {suggestions.map((suggestion) => {
              const className = suggestion.active
                ? 'suggestion-item--active'
                : 'suggestion-item';
              const style = suggestion.active
                ? { backgroundColor: '#FFA69E', cursor: 'pointer' }
                : { backgroundColor: '#ffffff', cursor: 'pointer' };
              return (
                <div
                  {...getSuggestionItemProps(suggestion, {
                    className,
                    style
                  })}
                  className={classes.suggestions}
                >
                  <span>{suggestion.description}</span>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </PlacesAutocomplete>
  );
};

const useStyles = makeStyles({
  suggestions: {
    borderRadius: '6px',
    padding: '8px',
    width: '200px',
    boxShadow: 'rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px'
  }
});
