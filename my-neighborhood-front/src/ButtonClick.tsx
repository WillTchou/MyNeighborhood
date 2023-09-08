import {
  Button,
  ButtonPropsSizeOverrides,
  ButtonPropsVariantOverrides
} from '@mui/material';
import { OverridableStringUnion } from '@mui/types';

type ButtonClickProps = {
  children: string;
  type?: 'button' | 'reset' | 'submit';
  variant?: OverridableStringUnion<
    'text' | 'contained' | 'outlined',
    ButtonPropsVariantOverrides
  >;
  backgroundColor?: string;
  color?: string;
  size?: OverridableStringUnion<
    'small' | 'medium' | 'large',
    ButtonPropsSizeOverrides
  >;
  onClick?: (event) => void;
  disabled?: boolean;
  borderRadius?: number;
  padding?: number;
  boxShadow?: boolean;
};

export const ButtonClick = ({
  children,
  type,
  variant,
  backgroundColor,
  color,
  size,
  onClick,
  disabled,
  borderRadius,
  boxShadow,
  padding
}: ButtonClickProps) => {
  return (
    <Button
      type={type}
      variant={variant}
      style={{
        backgroundColor: backgroundColor,
        color: color,
        borderRadius: borderRadius + 'px',
        boxShadow: boxShadow && '0px 15px 20px rgba(0, 0, 0, 0.4)',
        padding: padding + 'px'
      }}
      size={size}
      onClick={onClick}
      disabled={disabled}
    >
      {children}
    </Button>
  );
};
