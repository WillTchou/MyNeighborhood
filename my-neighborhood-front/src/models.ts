export type RegisterRequest = {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  address: string;
  governmentIdentityId: string;
};

export type AuthRequest = {
  email: string;
  password: string;
};

export type User = {
  id: string;
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  address: string;
  governmentIdentityId: string;
  profilePictureId: string;
};

export type RequestPost = {
  type: Type;
  latitude: number;
  longitude: number;
  description: string;
};

export type RequestGet = {
  id: string;
  type: Type;
  latitude: number;
  longitude: number;
  description: string;
  status: Status;
  requester: User;
};

export type ChatMessagePost = {
  sender: User;
  recipient: User;
  content: string;
  messageType: MessageType;
  flow:string
};

export type ChatMessageGet = {
  id?: number;
  sender: User;
  recipient: User;
  content: string;
  date: Date;
  messageType: MessageType;
  flow?:string;
};

export enum TypeEnum {
  OneTimeTask = 'One-time task',
  MaterialNeed = 'Material need'
}

export enum Status {
  Fulfilled = 'Fulfilled',
  Unfulfilled = 'Unfulfilled'
}

export enum MessageType {
  CHAT = 'CHAT',
  JOIN = 'JOIN',
  LEAVE = 'LEAVE'
}

export type Type = keyof typeof TypeEnum;
