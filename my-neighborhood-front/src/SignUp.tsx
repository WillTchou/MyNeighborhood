import { Paper, Stack } from '@mui/material';
import { Header } from './Header';
import { InputFile } from './InputFile';
import { makeStyles } from '@mui/styles';
import { TextInput } from './TextInput';
import setBodyColor from './setBody';
import { ButtonClick } from './ButtonClick';
import { ChangeEvent, useState } from 'react';
import { RegisterRequest } from './models';
import { authRequestRegister } from './authRequestApi';
import { useNavigate } from 'react-router-dom';
import { authService } from './authService';
import axios from 'axios';

export const SignUp = () => {
  const classes = useStyles();
  const navigate = useNavigate();

  setBodyColor({ color: '#DDFFF7' });

  const initialValue = {
    firstname: '',
    lastname: '',
    email: '',
    password: '',
    address: '',
    governmentIdentityId: ''
  };

  const [user, setUser] = useState<RegisterRequest>(initialValue);
  const [error, setError] = useState('');
  const [errorGov, setErrorGov] = useState('');
  const [governmentIdentity, setGovernmentIdentity] = useState<File>();

  const isValid =
    !!user.firstname &&
    !!user.lastname &&
    !!user.address &&
    !!user.address &&
    !!user.email &&
    !!user.password &&
    !!user.governmentIdentityId;

  const handleFirstname = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setUser({ ...user, firstname: event.target.value });
  };

  const handleLastname = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setUser({ ...user, lastname: event.target.value });
  };

  const handleEmail = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setUser({ ...user, email: event.target.value });
  };

  const handlePassword = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setUser({ ...user, password: event.target.value });
  };

  const handleAddress = (event: ChangeEvent<HTMLInputElement>) => {
    event.preventDefault();
    setUser({ ...user, address: event.target.value });
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
    authRequestRegister(
      user,
      (res) => handleThenRegister(res.data.token, res.data.userId),
      (err) => {
        console.log(err);
        setError(err.message);
        setTimeout(() => {
          setError('');
        }, 6000);
      }
    );
  };

  const sendGovernmentIdentity = (governmentIdentity: File) => {
    var data = new FormData();
    data.append('document', governmentIdentity);
    axios
      .post('http://localhost:8080/api/v1/governmentIdentity', data)
      .then((res) => setUser({ ...user, governmentIdentityId: res.data }))
      .catch((err) => {
        setErrorGov(err.message);
        setTimeout(() => {
          setError('');
        }, 6000);
      });
  };

  return (
    <div>
      <Header />
      <Paper elevation={2} className={classes.form}>
        <h1 className={classes.title}>Registration form</h1>
        <Stack spacing={2} direction="row" className={classes.formContainer}>
          <Stack spacing={2} direction="column">
            <TextInput
              label="Firstname"
              type="text"
              onChange={handleFirstname}
              value={user.firstname}
            />
            <TextInput
              label="Lastname"
              type="text"
              onChange={handleLastname}
              value={user.lastname}
            />
            <TextInput
              label="Email"
              type="email"
              onChange={handleEmail}
              value={user.email}
            />
            <TextInput
              label="Password"
              type="password"
              onChange={handlePassword}
              value={user.password}
            />
          </Stack>
          <Stack spacing={2} direction="column">
            <TextInput
              label="Address"
              type="text"
              onChange={handleAddress}
              value={user.address}
            />
            <InputFile
              file={governmentIdentity}
              setFile={setGovernmentIdentity}
              accept="image/*"
              onChange={sendGovernmentIdentity}
              error={errorGov}
              setError={setErrorGov}
            />
          </Stack>
        </Stack>
        <Stack
          className={classes.submitButtonStack}
          spacing={2}
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
          {error && (
            <>
              {error === 'Request failed with status code 400' ? (
                <div className={classes.errorConnection}>
                  Email is not valid
                </div>
              ) : (
                <div className={classes.errorConnection}>
                  Sorry your request contains errors
                </div>
              )}
            </>
          )}
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
