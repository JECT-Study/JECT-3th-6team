import { useState, useCallback } from 'react';

interface UseMapSearchReturn {
  searchValue: string;
  isSearchFocused: boolean;
  handleSearchFocus: () => void;
  handleSearchBlur: () => void;
  handleChange: (value: string) => void;
}

export default function useMapSearch(): UseMapSearchReturn {
  const [isSearchFocused, setIsSearchFocused] = useState(false);
  const [searchValue, setSearchValue] = useState('');

  const handleSearchFocus = useCallback(() => {
    setIsSearchFocused(true);
  }, []);

  const handleSearchBlur = useCallback(() => {
    setIsSearchFocused(false);
  }, []);

  const handleChange = useCallback((value: string) => {
    setSearchValue(value);
    // TODO : 검색 로직 구현
    console.log('search', value);
  }, []);

  return {
    searchValue,
    isSearchFocused,
    handleSearchFocus,
    handleSearchBlur,
    handleChange,
  };
}
