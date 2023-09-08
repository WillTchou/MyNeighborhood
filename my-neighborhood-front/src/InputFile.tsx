import { makeStyles } from '@mui/styles';
import CloudUpload from '@mui/icons-material/CloudUpload';
import CloudDoneIcon from '@mui/icons-material/CloudDone';
import DeleteIcon from '@mui/icons-material/Delete';
import { Dispatch, SetStateAction, useState } from 'react';
import { Stack } from '@mui/material';

type InputFileProps = {
  file: File;
  setFile: Dispatch<SetStateAction<File>>;
  accept: string;
  onChange: (file: File) => void;
};

export const InputFile = ({
  accept,
  file,
  setFile,
  onChange
}: InputFileProps) => {
  const classes = useStyles();
  const [fileName, setFileName] = useState('No selected file');

  const handleDelete = () => {
    setFileName('No selected file');
    setFile(null);
  };

  return (
    <>
      <form
        action=""
        style={
          file ? { backgroundColor: '#23DC3D' } : { backgroundColor: '#93E1D8' }
        }
        className={classes.inputFile}
        onClick={() => {
          let element: HTMLElement = document.getElementsByClassName(
            'input-field'
          )[0] as HTMLElement;
          element.click();
        }}
      >
        <input
          accept={accept}
          type="file"
          className="input-field"
          hidden
          onChange={({ target: { files } }) => {
            files[0] && setFileName(files[0].name);
            if (files) {
              setFile(files[0]);
              onChange(files[0]);
            }
          }}
        />
        {file ? (
          <Stack direction="column" alignItems="center" gap={1}>
            <CloudDoneIcon sx={{ fontSize: 60 }} />
            <span>{fileName}</span>
          </Stack>
        ) : (
          <Stack direction="column" alignItems="center" gap={1}>
            <CloudUpload sx={{ fontSize: 60 }} />
            <span>Browse a file to upload</span>
          </Stack>
        )}
      </form>
      <span className={classes.fileName}>
        {fileName}{' '}
        <DeleteIcon className={classes.deleteIcon} onClick={handleDelete} />
      </span>
    </>
  );
};

const useStyles = makeStyles({
  inputFile: {
    display: 'flex',
    textAlign: 'center',
    justifyContent: 'center',
    alignItems: 'center',
    cursor: 'pointer',
    height: '150px',
    width: '300px',
    border: '3px dashed #FFA69E',
    borderRadius: '5px'
  },
  deleteIcon: {
    cursor: 'pointer'
  },
  fileName: {
    fontSize: '12px'
  }
});
