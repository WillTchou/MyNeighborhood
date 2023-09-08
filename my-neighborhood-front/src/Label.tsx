import { Status, TypeEnum } from './models';

type LabelType = {
  children: TypeEnum | Status;
};

export const Label = ({ children }: LabelType) => {
  var backgroundColor: string;
  switch (children) {
    case TypeEnum.MaterialNeed: {
      backgroundColor = '#FDEE00';
      break;
    }
    case TypeEnum.OneTimeTask: {
      backgroundColor = '#00A0A0';
      break;
    }
    case Status.Fulfilled: {
      backgroundColor = '#32CD32';
      break;
    }
    case Status.Unfulfilled: {
      backgroundColor = '#E73131';
      break;
    }
    default: {
      console.log('Invalid choice');
      break;
    }
  }
  return (
    <span
      style={{
        borderRadius: '23px',
        padding: '8px',
        backgroundColor: backgroundColor,
        fontWeight: 'bold'
      }}
    >
      {children}
    </span>
  );
};
