export const storage = {
  setJSON<T>(key: string, value: T) {
    localStorage.setItem(key, JSON.stringify(value));
  },
  getJSON<T>(key: string): T | null {
    const value = localStorage.getItem(key);
    return value ? JSON.parse(value) : null;
  },
  clear(key: string): void {
    localStorage.removeItem(key);
  },
};
