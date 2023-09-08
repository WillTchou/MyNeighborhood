import ListItem from '@mui/material/ListItem';
import ListItemButton from '@mui/material/ListItemButton';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Avatar from '@mui/material/Avatar';

type RecipientItemProps = {
  primary: string;
  secondary: string;
  open: boolean;
  onClick: (value) => void;
  selected: boolean;
};

export const RecipientItem = ({
  primary,
  secondary,
  open,
  onClick,
  selected
}: RecipientItemProps) => {
  return (
    <ListItem key={primary} disablePadding sx={{ display: 'block' }}>
      <ListItemButton
        sx={{
          minHeight: 48,
          justifyContent: open ? 'initial' : 'center',
          px: 2.5
        }}
        onClick={onClick}
        selected={selected}
      >
        <ListItemIcon
          sx={{
            minWidth: 0,
            mr: open ? 3 : 'auto',
            justifyContent: 'center'
          }}
        >
          <Avatar alt={primary} />
        </ListItemIcon>
        <ListItemText
          primary={primary}
          secondary={secondary}
          sx={{ opacity: open ? 1 : 0 }}
        />
      </ListItemButton>
    </ListItem>
  );
};
