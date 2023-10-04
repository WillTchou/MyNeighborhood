import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import IconButton from '@mui/material/IconButton';
import Typography from '@mui/material/Typography';
import Menu from '@mui/material/Menu';
import MenuIcon from '@mui/icons-material/Menu';
import Container from '@mui/material/Container';
import Avatar from '@mui/material/Avatar';
import Tooltip from '@mui/material/Tooltip';
import MenuItem from '@mui/material/MenuItem';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { authService } from './authService';
import { ButtonClick } from './ButtonClick';
import Stack from '@mui/material/Stack';
import MenuList from '@mui/material/MenuList';

export const Header = () => {
  const navigate = useNavigate();

  const [anchorElNav, setAnchorElNav] = useState(null);
  const [anchorElUser, setAnchorElUser] = useState(null);

  const handleOpenNavMenu = (event) => {
    setAnchorElNav(event.currentTarget);
  };
  const handleOpenUserMenu = (event) => {
    setAnchorElUser(event.currentTarget);
  };

  const handleCloseNavMenu = () => {
    setAnchorElNav(null);
  };

  const handleCloseUserMenu = () => {
    setAnchorElUser(null);
  };

  const redirectCreateRequestsPage = () => {
    navigate('/create-request');
  };

  const redirectMyRequestsPage = () => {
    navigate('/my-requests');
  };

  const redirectHomePage = () => {
    navigate('/');
  };

  const redirectChatboxPage = () => {
    navigate('/chatbox');
  };

  const redirectSignInPage = () => {
    navigate('/signIn');
  };

  const redirectSignUpPage = () => {
    navigate('/signUp');
  };

  const logout = () => {
    redirectSignInPage();
    authService.logout();
  };

  return (
    <AppBar position="fixed" sx={{ zIndex: '200 !important' }}>
      <Container maxWidth="xl" sx={{ backgroundColor: '#FFA69E' }}>
        <Toolbar disableGutters>
          <Typography
            variant="h6"
            noWrap
            component="a"
            href="/"
            sx={{
              mr: 2,
              display: { xs: 'none', md: 'flex' },
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.2rem',
              color: 'inherit',
              textDecoration: 'none'
            }}
          >
            My Neighborhood
          </Typography>
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left'
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left'
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' }
              }}
            >
              {authService.isLogged() ? (
                <Stack direction="row" spacing={2}>
                  <MenuList>
                    <MenuItem onClick={handleCloseNavMenu}>
                      <Typography onClick={redirectHomePage} textAlign="center">
                        Home
                      </Typography>
                    </MenuItem>
                    <MenuItem onClick={handleCloseNavMenu}>
                      <Typography
                        onClick={redirectCreateRequestsPage}
                        textAlign="center"
                      >
                        Create a request
                      </Typography>
                    </MenuItem>
                    <MenuItem onClick={handleCloseNavMenu}>
                      <Typography
                        onClick={redirectChatboxPage}
                        textAlign="center"
                      >
                        Chatbox
                      </Typography>
                    </MenuItem>
                  </MenuList>
                </Stack>
              ) : (
                <Stack direction="row" spacing={2}>
                  <MenuList>
                    <MenuItem onClick={handleCloseNavMenu}>
                      <Typography onClick={redirectSignInPage}>
                        Sign In
                      </Typography>
                    </MenuItem>
                    <MenuItem onClick={handleCloseNavMenu}>
                      <Typography onClick={redirectSignUpPage}>
                        Sign Up
                      </Typography>
                    </MenuItem>
                  </MenuList>
                </Stack>
              )}
            </Menu>
          </Box>
          <Typography
            variant="h5"
            noWrap
            component="a"
            href=""
            sx={{
              mr: 2,
              display: { xs: 'flex', md: 'none' },
              flexGrow: 1,
              fontFamily: 'monospace',
              fontWeight: 700,
              letterSpacing: '.2rem',
              color: 'inherit',
              textDecoration: 'none'
            }}
          >
            My Neighborhood
          </Typography>
          {authService.isLogged() ? (
            <Box
              sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, gap: 2 }}
            >
              <ButtonClick
                onClick={redirectHomePage}
                color="#FFA69E"
                backgroundColor="white"
              >
                Home
              </ButtonClick>
              <ButtonClick
                onClick={redirectCreateRequestsPage}
                color="#FFA69E"
                backgroundColor="white"
              >
                Create a request
              </ButtonClick>
              <ButtonClick
                onClick={redirectChatboxPage}
                color="#FFA69E"
                backgroundColor="white"
              >
                Chatbox
              </ButtonClick>
            </Box>
          ) : (
            <Box
              sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, gap: 2 }}
            >
              <ButtonClick
                onClick={redirectSignInPage}
                color="#FFA69E"
                backgroundColor="white"
              >
                Sign in
              </ButtonClick>
              <ButtonClick
                onClick={redirectSignUpPage}
                color="#FFA69E"
                backgroundColor="white"
              >
                Sign up
              </ButtonClick>
            </Box>
          )}
          {authService.isLogged() && (
            <Box sx={{ flexGrow: 0 }}>
              <Tooltip title="Open settings">
                <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                  <Avatar alt="Remy Sharp" />
                </IconButton>
              </Tooltip>
              <Menu
                sx={{ mt: '45px' }}
                id="menu-appbar"
                anchorEl={anchorElUser}
                anchorOrigin={{
                  vertical: 'top',
                  horizontal: 'right'
                }}
                keepMounted
                transformOrigin={{
                  vertical: 'top',
                  horizontal: 'right'
                }}
                open={Boolean(anchorElUser)}
                onClose={handleCloseUserMenu}
              >
                <MenuItem onClick={handleCloseUserMenu}>
                  <Typography
                    onClick={redirectMyRequestsPage}
                    textAlign="center"
                  >
                    My Requests
                  </Typography>
                </MenuItem>
                <MenuItem onClick={handleCloseUserMenu}>
                  <Typography onClick={logout} textAlign="center">
                    Logout
                  </Typography>
                </MenuItem>
              </Menu>
            </Box>
          )}
        </Toolbar>
      </Container>
    </AppBar>
  );
};
