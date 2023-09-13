import { Header } from './Header';
import { Grid, Paper, Stack } from '@mui/material';
import { ButtonClick } from './ButtonClick';
import { TextInput } from './TextInput';
import setBodyColor from './setBody';
import makeStyles from '@mui/styles/makeStyles/makeStyles';
import { useNavigate } from 'react-router-dom';
import { AuthRequest } from './models';
import { ChangeEvent, useState } from 'react';
import { authRequestLogin } from './authRequestApi';
import { authService } from './authService';

export const SignIn = () => {
  const classes = useStyles();
  const navigate = useNavigate();

  const initialValue = {
    email: '',
    password: ''
  };

  const [auth, setAuth] = useState<AuthRequest>(initialValue);
  const [error, setError] = useState('');

  const isValid = !!auth.email && !!auth.password;

  const redirectSignUpPage = (e) => {
    e.preventDefault();
    navigate('/signUp');
  };

  const handleEmail = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setAuth({ ...auth, email: event.target.value });
  };

  const handlePassword = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setAuth({ ...auth, password: event.target.value });
  };

  const redirectHomePage = () => {
    navigate('/');
  };

  const handleThenRegister = (token, userId) => {
    authService.saveCredentials(token, userId);
    redirectHomePage();
  };

  const onSubmit = (e: SubmitEvent) => {
    e.preventDefault();
    authRequestLogin(
      auth,
      (res) => handleThenRegister(res.data.token, res.data.userId),
      (err) => {
        setError(err.message);
        setTimeout(() => {
          setError('');
        }, 6000);
      }
    );
  };

  const onKeyDown = (event) => {
    if (event.keyCode === 13) {
      onSubmit(event);
    }
  };

  setBodyColor({ color: '#DDFFF7' });

  return (
    <div>
      <Header />
      <Paper elevation={2} className={classes.form}>
        <h1 className={classes.title}>Login</h1>
        <Stack spacing={2} direction="row" className={classes.formContainer}>
          <Grid container direction="column" gap={2}>
            <TextInput
              label="Email"
              type="email"
              fullwidth
              onChange={handleEmail}
              value={auth.email}
              onKeyDown={onKeyDown}
            />
            <TextInput
              label="Password"
              type="password"
              fullwidth
              onChange={handlePassword}
              value={auth.password}
              onKeyDown={onKeyDown}
            />
          </Grid>
        </Stack>
        <Stack
          className={classes.submitButtonStack}
          spacing={2}
          gap={2}
          direction="column"
          justifyContent="center"
        >
          <ButtonClick
            type="submit"
            variant="contained"
            backgroundColor="#FFA69E"
            size="large"
            onClick={onSubmit}
            disabled={!isValid}
          >
            Sumbit
          </ButtonClick>
          <Stack spacing={2} direction="column" gap={2}>
            Or sign up using
            <ButtonClick
              variant="contained"
              backgroundColor="#DDFFF7"
              color="black"
              onClick={redirectSignUpPage}
            >
              Sign up
            </ButtonClick>
            {error === 'Request failed with status code 403' && (
              <div className={classes.errorConnection}>
                Sorry incorrect email or password. Please try again
              </div>
            )}
          </Stack>
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
    marginTop: '12px'
  },
  errorConnection: {
    backgroundColor: 'red',
    padding: '9px',
    borderRadius: '8px'
  }
});
