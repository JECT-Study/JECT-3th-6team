export const FORM_STORAGE_KEY = (props: {
  formType: string;
  formKey?: string | number;
}) => `${props.formType}-${props.formKey}`;
