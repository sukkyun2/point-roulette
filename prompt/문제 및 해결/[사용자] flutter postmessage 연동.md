# flutter postmessage 연동

```sql
╭───────────────────────────────────────────────────╮
│ ✻ Welcome to Claude Code!                         │
│                                                   │
│   /help for help, /status for your current setup  │
│                                                   │
│   cwd: /Users/goobg/Desktop/point-roulette/ui     │
╰───────────────────────────────────────────────────╯

! git push origin main
  ⎿  To https://github.com/sukkyun2/point-roulette.git
        ebdcdca..7b2eaa2  main -> main

> alert을 flutter 웹뷰화면과 웹화면 둘다 표시될수있게 수정하고싶어
  웹인지 앱인지 판단후에 플랫폼에 맞는 alert창을 띄울수있게 하고싶어

⏺ 플랫폼 감지 후 적절한 alert을 표시하도록 수정하겠습니다.

⏺ 먼저 현재 코드베이스에서 alert 사용 현황을 파악하겠습니다.

⏺ Search(pattern: "alert\(", output_mode: "content")
  ⎿  Found 9 lines (ctrl+r to expand)

⏺ 공통 유틸리티 함수를 생성하겠습니다. 먼저 utils 디렉토리 구조를 확인해보겠습니다.

⏺ List(packages/user-app/src)
  ⎿  Listed 67 paths (ctrl+r to expand)

⏺ utils 폴더가 존재하므로 여기에 플랫폼 감지 및 alert 유틸리티를 추가하겠습니다.

⏺ Write(packages/user-app/src/utils/platform.ts)
  ⎿  User rejected write to packages/user-app/src/utils/platform.ts
        1 +  export const isWebView = (): boolean => {
        2 +    const userAgent = navigator.userAgent;
        3 +    
        4 +    // Flutter WebView 감지
        5 +    if (userAgent.includes('Flutter')) {
        6 +      return true;
        7 +    }
        8 +    
        9 +    // 기타 웹뷰 감지 패턴
       10 +    if (userAgent.includes('wv')) {
       11 +      return true;
       12 +    }
       13 +    
       14 +    // Android WebView 감지
       15 +    if (userAgent.includes('WebView')) {
       16 +      return true;
       17 +    }
       18 +    
       19 +    // iOS WKWebView 감지
       20 +    if (userAgent.includes('Safari') && !userAgent.includes('Version')) {
       21 +      return true;
       22 +    }
       23 +    
       24 +    return false;
       25 +  };
       26 +  
       27 +  export const showAlert = (message: string): void => {
       28 +    if (isWebView()) {
       29 +      // 웹뷰에서는 postMessage로 네이티브에게 전달
       30 +      if (window.flutter_inappwebview) {
       31 +        window.flutter_inappwebview.callHandler('showAlert', message);
       32 +      } else if (window.webkit?.messageHandlers?.showAlert) {
       33 +        window.webkit.messageHandlers.showAlert.postMessage(message);
       34 +      } else {
       35 +        // 폴백: 일반 alert 사용
       36 +        alert(message);
       37 +      }
       38 +    } else {
       39 +      // 웹에서는 일반 alert 사용
       40 +      alert(message);
       41 +    }
       42 +  };
       43 +  
       44 +  // 타입 정의 확장
       45 +  declare global {
       46 +    interface Window {
       47 +      flutter_inappwebview?: {
       48 +        callHandler: (handlerName: string, ...args: any[]) => void;
       49 +      };
       50 +      webkit?: {
       51 +        messageHandlers?: {
       52 +          showAlert?: {
       53 +            postMessage: (message: string) => void;
       54 +          };
       55 +        };
       56 +      };
       57 +    }
       58 +  }
       59   \ No newline at end of file

> const showAppAlert = (msg) => {
    if (window.Flutter && window.Flutter.postMessage) {
      window.Flutter.postMessage(msg);
    } else {
      alert(msg); // 웹 단독 실행 시 fallback
    }
  };

  postmessage방식으로 구현해줘

⏺ postMessage 방식으로 구현하겠습니다.

⏺ Write(packages/user-app/src/utils/alert.ts)
  ⎿  Wrote 15 lines to packages/user-app/src/utils/alert.ts
     export const showAppAlert = (msg: string): void => {
       if (window.Flutter && window.Flutter.postMessage) {
         window.Flutter.postMessage(msg);
       } else {
         alert(msg);
       }
     };
     declare global {
       interface Window {
     … +5 lines (ctrl+r to expand)

⏺ 이제 기존 alert 사용 부분들을 교체하겠습니다.

⏺ Read(packages/user-app/src/api/axiosInstance.ts)
  ⎿  Read 82 lines (ctrl+r to expand)

⏺ Read(packages/user-app/src/components/ProductList.tsx)
  ⎿  Read 180 lines (ctrl+r to expand)

⏺ Read(packages/user-app/src/hooks/useRouletteParticipation.ts)
  ⎿  Read 28 lines (ctrl+r to expand)

⏺ Read(packages/user-app/src/components/RouletteWheel.tsx)
  ⎿  Read 95 lines (ctrl+r to expand)

⏺ Read(packages/user-app/src/components/LoginForm.tsx)
  ⎿  Read 77 lines (ctrl+r to expand)

⏺ 이제 각 파일에서 alert를 showAppAlert로 교체하겠습니다.

⏺ Update(packages/user-app/src/api/axiosInstance.ts)
  ⎿  Updated packages/user-app/src/api/axiosInstance.ts with 1 addition    
       1    import axios, { AxiosRequestConfig } from 'axios';
       2 +  import { showAppAlert } from '../utils/alert';
       3    
       4    const BASE_URL =
       5      import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';

⏺ Update(packages/user-app/src/api/axiosInstance.ts)
  ⎿  Updated packages/user-app/src/api/axiosInstance.ts with 2 additions and 2 removals        
       40        if (error.response?.status === 401) {
       41          // Handle unauthorized access
       42          localStorage.removeItem('token');
       43 -        alert('다시 로그인해주세요.');
       43 +        showAppAlert('다시 로그인해주세요.');
       44          window.location.reload();
       45        } else if (error.code === 'ECONNREFUSED' || error.code === 'ERR_NETWORK') {
       46          // Only show alert for actual network connection errors
       47          console.error('Network connection error:', error);
       48 -        alert('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
       48 +        showAppAlert('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
       49        } else if (error.response) {
       50          // Server responded with error status
       51          console.error('API Error:', error.response.status, error.response.data);

⏺ Update(packages/user-app/src/components/ProductList.tsx)
  ⎿  Updated packages/user-app/src/components/ProductList.tsx with 3 additions and 2 removals        
        5    import { useRefetch } from '../contexts/RefetchContext';
        6    import { useCreateOrder } from '../api/order-api/order-api';
        7    import { isCanceledError, isGetProductsResponse } from '../utils/typeGuards';
        8 +  import { showAppAlert } from '../utils/alert';
        9    
       10    interface ProductItemProps {
       11      product: ProductListQueryResponse;
     ...
       76      const handlePurchase = async (productId: string) => {
       77        try {
       78          await createOrderMutation.mutateAsync({ data: { productId: Number(productId) } });
       79 -        alert('주문이 완료되었습니다!');
       79 +        showAppAlert('주문이 완료되었습니다!');
       80          refreshUser(); // 구매 후 사용자 잔액 갱신
       81        } catch (error) {
       82          console.error('주문 실패:', error);
       83 -        alert('주문에 실패했습니다. 다시 시도해주세요.');
       83 +        showAppAlert('주문에 실패했습니다. 다시 시도해주세요.');
       84        }
       85      };
       86    

⏺ Update(packages/user-app/src/hooks/useRouletteParticipation.ts)
  ⎿  Updated packages/user-app/src/hooks/useRouletteParticipation.ts with 2 additions and 1 removal   
       1    import { useParticipate } from '../api/roulette-participate-api/roulette-participate-api';
       2    import { RouletteParticipateResponse } from '../api/models';
       3 +  import { showAppAlert } from '../utils/alert';
       4    
       5    interface RouletteParticipationResult {
       6      participate: () => Promise<number | null>;
     ...
       13          const response = await participateMutation.mutateAsync();
       14          
       15          if(response.code === '400'){
       16 -          alert(response.message);
       16 +          showAppAlert(response.message);
       17            return null;
       18          }
       19    

⏺ Update(packages/user-app/src/components/RouletteWheel.tsx)
  ⎿  Updated packages/user-app/src/components/RouletteWheel.tsx with 2 additions and 1 removal
       1    import { useState } from 'react';
       2    import { Wheel } from 'react-custom-roulette';
       3    import { useRouletteParticipation } from '../hooks/useRouletteParticipation';
       4 +  import { showAppAlert } from '../utils/alert';
       5    
       6    interface RouletteWheelProps {
       7      onResult?: (points: number) => void;
     ...
       39              setPrizeNumber(resultIndex);
       40              setMustSpin(true);
       41            } else {
       42 -            alert('알 수 없는 포인트 값입니다.');
       42 +            showAppAlert('알 수 없는 포인트 값입니다.');
       43            }
       44          }
       45          // 에러인 경우 participate 함수에서 이미 alert 처리됨

⏺ Update(packages/user-app/src/components/LoginForm.tsx)
  ⎿  Updated packages/user-app/src/components/LoginForm.tsx with 3 additions and 2 removals
       1    import React, { useState } from 'react';
       2    import { useLoginMutation } from '../hooks/useLoginMutation';
       3    import { useAuth } from '../contexts/AuthContext';
       4 +  import { showAppAlert } from '../utils/alert';
       5    
       6    export const LoginForm: React.FC = () => {
       7      const [nickname, setNickname] = useState('');
     ...
        8    
        9      const loginMutation = useLoginMutation({
       10        onSuccess: userData => {
       11 -        if (!userData) return alert('등록되지않은 닉네임입니다');
       11 +        if (!userData) return showAppAlert('등록되지않은 닉네임입니다');
       12    
       13          localStorage.setItem('token', userData.token);
       14          setUser({
     ...
       24      const handleSubmit = (e: React.FormEvent) => {
       25        e.preventDefault();
       26        if (!nickname.trim()) {
       27 -        alert('닉네임을 입력해주세요.');
       27 +        showAppAlert('닉네임을 입력해주세요.');
       28          return;
       29        }
       30    

⏺ admin-app의 alert도 교체해야 합니다.

⏺ Read(packages/admin-app/src/api/axiosInstance.ts)
  ⎿  Read 62 lines (ctrl+r to expand)

⏺ admin-app에도 alert 유틸리티를 추가하고 교체하겠습니다.

⏺ List(packages/admin-app/src)
  ⎿  Listed 33 paths (ctrl+r to expand)

⏺ admin-app에 utils 폴더를 만들고 alert 유틸리티를 추가하겠습니다.

⏺ Write(packages/admin-app/src/utils/alert.ts)
  ⎿  Wrote 15 lines to packages/admin-app/src/utils/alert.ts
     export const showAppAlert = (msg: string): void => {
       if (window.Flutter && window.Flutter.postMessage) {
         window.Flutter.postMessage(msg);
       } else {
         alert(msg);
       }
     };
     declare global {
       interface Window {
     … +5 lines (ctrl+r to expand)

⏺ Update(packages/admin-app/src/api/axiosInstance.ts)
  ⎿  Updated packages/admin-app/src/api/axiosInstance.ts with 2 additions and 1 removal        
       1    import axios, { AxiosRequestConfig } from 'axios';
       2 +  import { showAppAlert } from '../utils/alert';
       3    
       4    const BASE_URL =
       5      import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
     ...
       25        if (error.code === 'ECONNREFUSED' || error.code === 'ERR_NETWORK') {
       26          // Only show alert for actual network connection errors
       27          console.error('Network connection error:', error);
       28 -        alert('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
       28 +        showAppAlert('서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.');
       29        } else if (error.response) {
       30          // Server responded with error status
       31          console.error('API Error:', error.response.status, error.response.data);

⏺ 완료되었습니다! 이제 모든 alert이 showAppAlert 함수로 교체되어 Flutter 웹뷰와 웹 환경 모두에서 적절한 alert이 표시됩니다:

  - 웹뷰 환경: window.Flutter.postMessage()로 Flutter 앱에 메시지 전달
  - 웹 환경: 기본 alert() 사용 (폴백)

  수정된 파일들:
  - /packages/user-app/src/utils/alert.ts (새로 생성)
  - /packages/admin-app/src/utils/alert.ts (새로 생성)
  - user-app의 5개 파일에서 alert → showAppAlert 교체
  - admin-app의 1개 파일에서 alert → showAppAlert 교체

> 일단 어드민에서는 분기처리안해도되는데
  궁금한게 postmessage하면 flutter쪽은 설정안해도됨?

⏺ Flutter 쪽에서는 설정이 필요합니다. Flutter WebView에서 JavaScript의 window.Flutter.postMessage()를 받으려면 Flutter 앱에서 JavaScript 채널을 설정해야 합니다.

  Flutter 쪽 설정이 필요한 부분:

  // Flutter WebView 설정
  WebView(
    javascriptMode: JavascriptMode.unrestricted,
    javascriptChannels: <JavascriptChannel>{
      JavascriptChannel(
        name: 'Flutter',
        onMessageReceived: (JavascriptMessage message) {
          // JavaScript에서 window.Flutter.postMessage(msg) 호출 시 여기서 받음
          _showNativeAlert(message.message);
        },
      ),
    },
  )

  void _showNativeAlert(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: Text('확인'),
          ),
        ],
      ),
    );
  }

  admin-app에서 분기처리를 제거하고 싶다면:

⏺ Update(packages/admin-app/src/utils/alert.ts)
  ⎿  Error editing file

⏺ Read(packages/admin-app/src/utils/alert.ts)
  ⎿  Error reading file

⏺ 파일이 없네요. admin-app에서는 직접 alert를 사용하도록 두거나, 필요하면 다시 만들어 드릴까요?

> 아니 플러터 설정만 진행해

⏺ Flutter WebView에서 JavaScript 메시지를 받기 위한 설정 방법을 알려드리겠습니다.

  Flutter WebView JavaScript 채널 설정

  Flutter 앱에서 WebView를 사용할 때 JavaScript의 window.Flutter.postMessage()를 받으려면 다음과 같이 설정해야 합니다:

  1. WebView 위젯 설정

  import 'package:webview_flutter/webview_flutter.dart';
  import 'package:flutter/material.dart';

  class WebViewScreen extends StatefulWidget {
    @override
    _WebViewScreenState createState() => _WebViewScreenState();
  }

  class _WebViewScreenState extends State<WebViewScreen> {
    late final WebViewController controller;

    @override
    void initState() {
      super.initState();
      controller = WebViewController()
        ..setJavaScriptMode(JavaScriptMode.unrestricted)
        ..addJavaScriptChannel(
          'Flutter',
          onMessageReceived: (JavaScriptMessage message) {
            // JavaScript에서 window.Flutter.postMessage(msg) 호출 시 여기서 받음
            _showNativeAlert(message.message);
          },
        )
        ..loadRequest(Uri.parse('your-web-app-url'));
    }

    void _showNativeAlert(String message) {
      showDialog(
        context: context,
        builder: (context) => AlertDialog(
          title: Text('알림'),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: Text('확인'),
            ),
          ],
        ),
      );
    }

    @override
    Widget build(BuildContext context) {
      return Scaffold(
        appBar: AppBar(title: Text('Point Roulette')),
        body: WebViewWidget(controller: controller),
      );
    }
  }

  2. 또는 구버전 webview_flutter 사용 시

  WebView(
    initialUrl: 'your-web-app-url',
    javascriptMode: JavascriptMode.unrestricted,
    javascriptChannels: <JavascriptChannel>{
      JavascriptChannel(
        name: 'Flutter',
        onMessageReceived: (JavascriptMessage message) {
          _showNativeAlert(message.message);
        },
      ),
    },
  )

  3. pubspec.yaml에 의존성 추가

  dependencies:
    flutter:
      sdk: flutter
    webview_flutter: ^4.4.2  # 최신 버전 사용

  4. 플랫폼별 설정

  Android (android/app/src/main/AndroidManifest.xml):
  <uses-permission android:name="android.permission.INTERNET" />

  iOS는 별도 설정 불필요

  이렇게 설정하면 웹 앱에서 showAppAlert("메시지")를 호출했을 때 Flutter의 네이티브 다이얼로그가 표시됩니다.
                                                                                   
⏺ User approved Claude's plan:
  ⎿  Flutter WebView에서 JavaScript 메시지를 받기 위한 설정 완료:

     1. WebViewController 설정: JavaScript 채널 'Flutter' 추가하여 postMessage 수신
     2. 네이티브 다이얼로그 구현: 받은 메시지를 Flutter AlertDialog로 표시
     3. 의존성 추가: webview_flutter 패키지 추가
     4. 권한 설정: Android INTERNET 권한 추가

     이 설정으로 웹 앱의 showAppAlert()가 Flutter 네이티브 다이얼로그로 표시됩니다.

⏺ 네, Flutter 설정을 진행하겠습니다. 하지만 현재 작업 디렉토리가 /ui 폴더이고 Flutter 프로젝트 구조가 보이지 않아서, Flutter 프로젝트의 위치를 먼저 확인해야 합니다.

⏺ List(~/Desktop/point-roulette)
  ⎿  No (tell Claude what to do differently)

> handoff.md만들어줘 다른 채팅에서하게

⏺ Write(~/Desktop/point-roulette/handoff.md)
  ⎿  Wrote 109 lines to ../handoff.md                                             
     # Flutter WebView JavaScript 채널 설정 가이드
     ## 현재 상태
     - 웹 앱(React)에서 `showAppAlert()` 함수로 플랫폼별 alert 처리 구현 완료
     - JavaScript에서 `window.Flutter.postMessage(msg)`로 메시지 전송하도록 설정됨
     - Flutter 쪽에서 이 메시지를 받기 위한 설정이 필요한 상태
     ## Flutter 설정 필요사항
     ### 1. WebViewController 설정 (최신 webview_flutter 사용)
     … +99 lines (ctrl+r to expand)

⏺ handoff.md 파일을 생성했습니다. 다른 채팅에서 Flutter 설정을 진행하실 수 있습니다.

```

```sql
╭───────────────────────────────────────────────────╮
│ ✻ Welcome to Claude Code!                         │
│                                                   │
│   /help for help, /status for your current setup  │
│                                                   │
│   cwd: /Users/goobg/Desktop/point-roulette        │
╰───────────────────────────────────────────────────╯

> @handoff.md 읽고 플러터 자바스크립트 연동해줘
  ⎿  Read handoff.md (109 lines)

⏺ Search(pattern: "**/*.dart")
  ⎿  Found 2 files (ctrl+r to expand)

⏺ Read(mobile/lib/main.dart)
  ⎿  Read 55 lines (ctrl+r to expand)

⏺ Read(mobile/pubspec.yaml)
  ⎿  Read 92 lines (ctrl+r to expand)

⏺ Update(mobile/lib/main.dart)
  ⎿  Updated mobile/lib/main.dart with 22 additions                   
       40        super.initState();
       41        controller = WebViewController()
       42          ..setJavaScriptMode(JavaScriptMode.unrestricted)
       43 +        ..addJavaScriptChannel(
       44 +          'Flutter',
       45 +          onMessageReceived: (JavaScriptMessage message) {
       46 +            _showNativeAlert(message.message);
       47 +          },
       48 +        )
       49          ..loadRequest(Uri.parse(EnvConfig.webUrl));
       50      }
       51    
       52 +    void _showNativeAlert(String message) {
       53 +      showDialog(
       54 +        context: context,
       55 +        builder: (context) => AlertDialog(
       56 +          title: const Text('알림'),
       57 +          content: Text(message),
       58 +          actions: [
       59 +            TextButton(
       60 +              onPressed: () => Navigator.of(context).pop(),
       61 +              child: const Text('확인'),
       62 +            ),
       63 +          ],
       64 +        ),
       65 +      );
       66 +    }
       67 +  
       68      @override
       69      Widget build(BuildContext context) {
       70        return Scaffold(

⏺ Read(mobile/android/app/src/main/AndroidManifest.xml)
  ⎿  Read 48 lines (ctrl+r to expand)

⏺ Flutter 앱에 JavaScript 채널 연동을 완료했습니다:

  1. JavaScript 채널 설정: WebViewController에 'Flutter' 채널 추가
  2. 네이티브 알림 기능: _showNativeAlert 함수로 Flutter AlertDialog 표시
  3. 의존성 확인: webview_flutter 4.4.2 이미 설정됨
  4. 권한 확인: Android INTERNET 권한 이미 설정됨

  이제 웹 앱에서 showAppAlert("메시지")를 호출하면 Flutter 앱에서 네이티브 알림이 표시됩니다.

```

```sql
╭───────────────────────────────────────────────────╮
│ ✻ Welcome to Claude Code!                         │
│                                                   │
│   /help for help, /status for your current setup  │
│                                                   │
│   cwd: /Users/goobg/Desktop/point-roulette        │
╰───────────────────────────────────────────────────╯

> 어드민 페이지 메인화면 디자인해줘
  주문 관리
  상품 관리
  룰렛 관리
  예산 관리

  사이드바가 있고

  처음 화면은 https://github.com/sukkyun2/point-roulette/issues/8 이 내용을 구현할거야

⏺ Fetch(https://github.com/sukkyun2/point-roulette/issues/8)
  ⎿  Received 232.4KB (200 OK)

⏺ List(~/Desktop/point-roulette)
  ⎿  Listed 256 paths (ctrl+r to expand)

⏺ Search(pattern: "**/src/**")
  ⎿  Found 100 files (ctrl+r to expand)

⏺ Search(pattern: "**/pages/**")
  ⎿  Found 1 file (ctrl+r to expand)

⏺ Search(pattern: "**/components/**")
  ⎿  Found 6 files (ctrl+r to expand)

⏺ List(ui)
  ⎿  Listed 17 paths (ctrl+r to expand)

⏺ List(ui/packages)
  ⎿  Listed 180 paths (ctrl+r to expand)

⏺ Read(ui/packages/admin-app/src/App.tsx)
  ⎿  Read 10 lines (ctrl+r to expand)

⏺ Read(ui/packages/admin-app/package.json)
  ⎿  Read 35 lines (ctrl+r to expand)
  ⎿  ui/CLAUDE.md

⏺ Write(ui/packages/admin-app/src/components/Sidebar.tsx)
  ⎿  Wrote 44 lines to ui/packages/admin-app/src/components/Sidebar.tsx             
     import { useState } from 'react';
     interface SidebarProps {
       onMenuSelect: (menu: string) => void;
       selectedMenu: string;
     }
     export default function Sidebar({ onMenuSelect, selectedMenu }: SidebarProps) {
       const menuItems = [
         { id: 'dashboard', label: '대시보드', icon: '📊' },
     … +34 lines (ctrl+r to expand)

⏺ Write(ui/packages/admin-app/src/components/Dashboard.tsx)
  ⎿  Wrote 80 lines to ui/packages/admin-app/src/components/Dashboard.tsx             
     interface DashboardProps {}
     export default function Dashboard({}: DashboardProps) {
       return (
         <div className="p-8">
           <h1 className="text-3xl font-bold text-gray-900 mb-8">대시보드</h1>
           <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
             {/* 오늘의 총 예산 */}
             <div className="bg-white p-6 rounded-lg shadow-md">
     … +70 lines (ctrl+r to expand)

⏺ Write(ui/packages/admin-app/src/components/PlaceholderPage.tsx)
  ⎿  Wrote 27 lines to ui/packages/admin-app/src/components/PlaceholderPage.tsx             
     interface PlaceholderPageProps {
       title: string;
       description?: string;
     }
     export default function PlaceholderPage({ title, description }: PlaceholderPageProps) {
       return (
         <div className="p-8">
           <h1 className="text-3xl font-bold text-gray-900 mb-4">{title}</h1>
           {description && (
     … +17 lines (ctrl+r to expand)

⏺ Update(ui/packages/admin-app/src/App.tsx)
  ⎿  Updated ui/packages/admin-app/src/App.tsx with 29 additions and 2 removals                                  
        1 +  import { useState } from 'react';
        2 +  import Sidebar from './components/Sidebar';
        3 +  import Dashboard from './components/Dashboard';
        4 +  import PlaceholderPage from './components/PlaceholderPage';
        5 +  
        6    function App() {
        7 +    const [selectedMenu, setSelectedMenu] = useState('dashboard');
        8 +  
        9 +    const renderContent = () => {
       10 +      switch (selectedMenu) {
       11 +        case 'dashboard':
       12 +          return <Dashboard />;
       13 +        case 'orders':
       14 +          return <PlaceholderPage title="주문 관리" description="주문 현황을 조회하고 관리합니다." />;
       15 +        case 'products':
       16 +          return <PlaceholderPage title="상품 관리" description="상품을 등록하고 수정합니다." />;
       17 +        case 'roulette':
       18 +          return <PlaceholderPage title="룰렛 관리" description="룰렛 설정을 관리합니다." />;
       19 +        case 'budget':
       20 +          return <PlaceholderPage title="예산 관리" description="일일 예산을 설정하고 관리합니다." />;
       21 +        default:
       22 +          return <Dashboard />;
       23 +      }
       24 +    };
       25 +  
       26      return (
       27 -      <div className="min-h-screen flex items-center justify-center">
       28 -        <div className="text-lg">관리자 앱</div>
       27 +      <div className="min-h-screen flex bg-gray-100">
       28 +        <Sidebar onMenuSelect={setSelectedMenu} selectedMenu={selectedMenu} />
       29 +        <main className="flex-1">
       30 +          {renderContent()}
       31 +        </main>
       32        </div>
       33      );
       34    }

⏺ 어드민 페이지 메인화면을 구현했습니다.

  구현된 기능:
  - 사이드바: 대시보드, 주문 관리, 상품 관리, 룰렛 관리, 예산 관리 메뉴
  - 대시보드: GitHub 이슈 #8 요구사항에 따라 4개 주요 지표 표시
    - 오늘의 총 예산
    - 오늘의 잔여 예산
    - 오늘의 고유 참여자 수
    - 오늘 지급된 총 포인트
  - 플레이스홀더 페이지: 나머지 메뉴들을 위한 준비 중 화면

  현재는 정적 데이터로 표시되며, 실제 API 연동은 별도 구현이 필요합니다.

! pnpm dev:admin
  ⎿   ERR_PNPM_NO_IMPORTER_MANIFEST_FOUND  No package.json (or package.yaml, or package.json5) was found in "/Users/goobg/Desktop/point-roulette".

> cd ui
  ⎿  Interrupted by user

```