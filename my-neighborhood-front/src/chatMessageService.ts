import Axios from './callerService';

let getChatFlow = (recipientId: string) => {
  return Axios.get(`chat/flow/${recipientId}`);
};

let getLatestChatMessagesForSender = () => {
  return Axios.get('chat/latest');
};

export const chatMessageService = {
  getChatFlow,
  getLatestChatMessagesForSender
};
