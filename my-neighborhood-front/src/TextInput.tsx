import {
  FilledInputProps,
  InputProps,
  OutlinedInputProps,
  SxProps,
  Theme
} from '@mui/material';
import TextField, { TextFieldVariants } from '@mui/material/TextField';

type TextInputProps = {
  label?: string;
  onChange: (value) => void;
  fullwidth?: boolean;
  variant?: TextFieldVariants;
  type?: string;
  rows?: number;
  placeholder?: string;
  multiline?: boolean;
  maxLength?: number;
  value?: unknown;
  InputProps?:
    | Partial<FilledInputProps>
    | Partial<OutlinedInputProps>
    | Partial<InputProps>;
  disabled?: boolean;
  onKeyDown?: (event) => void;
  sx?: SxProps<Theme>;
};

export const TextInput = ({
  label,
  onChange,
  fullwidth,
  variant,
  type,
  rows,
  placeholder,
  multiline,
  maxLength,
  value,
  InputProps,
  disabled,
  onKeyDown,
  sx
}: TextInputProps) => {
  return (
    <TextField
      label={label}
      variant={variant}
      fullWidth={fullwidth}
      rows={rows}
      type={type}
      onChange={onChange}
      placeholder={placeholder}
      multiline={multiline}
      inputProps={{ maxLength: maxLength }}
      value={value}
      InputProps={InputProps}
      disabled={disabled}
      onKeyDown={onKeyDown}
      sx={sx}
    />
  );
};
