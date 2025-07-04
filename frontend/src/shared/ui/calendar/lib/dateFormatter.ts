// date 객체를 받아서 {년,월,일}로 반환합니다.
export const dateFormatter = (date: Date) => {
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();

  return { year, month, day };
};
