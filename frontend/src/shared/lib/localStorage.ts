export const storage = {
  setJSON<T>(key: string, value: T) {
    if (typeof window === 'undefined') return;
    localStorage.setItem(key, JSON.stringify(value));
  },
  getJSON<T>(key: string): T | null {
    if (typeof window === 'undefined') return null;
    const value = localStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  },
  clear(key: string): void {
    if (typeof window === 'undefined') return;
    localStorage.removeItem(key);
  },
};
