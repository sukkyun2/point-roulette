export const showAppAlert = (msg: string): void => {
  if (window.Flutter && window.Flutter.postMessage) {
    window.Flutter.postMessage(msg);
  } else {
    alert(msg);
  }
};

declare global {
  interface Window {
    Flutter?: {
      postMessage: (message: string) => void;
    };
  }
}