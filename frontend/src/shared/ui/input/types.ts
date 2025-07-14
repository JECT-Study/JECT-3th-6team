export type TextInputType = 'text' | 'search' | 'email' | 'tel' | 'password';
export type TextInputModeType =
  | 'text'
  | 'search'
  | 'email'
  | 'tel'
  | 'url'
  | 'none';

export interface TextInputProps {
  id: string;
  label: string;
  value: string;
  onChange: (value: string) => void;
  placeholder: string;
  error?: boolean;
  errorMessage?: string;
  disabled?: boolean;
  type?: TextInputType;
  inputMode?: TextInputModeType;
  isEditable?: boolean;
}

export interface NumberInputProps {
  value: number;
  label: string;
  max: number;
  min: number;
  onChange: (value: number) => void;
  errorMessage?: string;
}
