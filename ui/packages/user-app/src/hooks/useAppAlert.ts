import { useCallback } from 'react';

interface AppAlertOptions {
  title?: string;
}

interface UseAppAlert {
  showAlert: (message: string, options?: AppAlertOptions) => void;
}

declare global {
  interface Window {
    flutter_inappwebview?: {
      callHandler: (handlerName: string, ...args: any[]) => Promise<any>;
    };
  }
}

export const useAppAlert = (): UseAppAlert => {
  const isFlutterApp = typeof window !== 'undefined' && window.flutter_inappwebview !== undefined;

  const showAlert = useCallback((message: string, options?: AppAlertOptions) => {
    if (isFlutterApp && window.flutter_inappwebview) {
      window.flutter_inappwebview.callHandler('showAlert', {
        message,
        title: options?.title || '알림'
      });
    } else {
      alert(message);
    }
  }, [isFlutterApp]);

  return {
    showAlert
  };
};