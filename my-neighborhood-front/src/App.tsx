import './App.css';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { SignUp } from './SignUp';
import { SignIn } from './SignIn';
import { CreateRequest } from './CreateRequest';
import { Home } from './Home';
import { Chatbox } from './Chatbox';
import { MyRequests } from './MyRequests';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/signUp" element={<SignUp />} />
        <Route path="/signIn" element={<SignIn />} />
        <Route path="/create-request" element={<CreateRequest />} />
        <Route path="/" element={<Home />} />
        <Route path="/chatbox" element={<Chatbox />} />
        <Route path="/my-requests" element={<MyRequests />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
