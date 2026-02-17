import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_dotenv/flutter_dotenv.dart';
import 'package:webview_flutter/webview_flutter.dart';
import 'config/env_config.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await dotenv.load(fileName: '.env');
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: '포인트 룰렛',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const WebViewPage(),
    );
  }
}

class WebViewPage extends StatefulWidget {
  const WebViewPage({super.key});

  @override
  State<WebViewPage> createState() => _WebViewPageState();
}

class _WebViewPageState extends State<WebViewPage> {
  late final WebViewController controller;
  bool _hasError = false;
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    controller = WebViewController()
      ..setJavaScriptMode(JavaScriptMode.unrestricted)
      ..setNavigationDelegate(
        NavigationDelegate(
          onWebResourceError: (WebResourceError error) {
            print('WebView Error: ${error.description}');
            print('Error code: ${error.errorCode}');
            print('Error type: ${error.errorType}');
            setState(() {
              _hasError = true;
              _isLoading = false;
            });
            _loadFallbackPage();
          },
          onPageStarted: (String url) {
            print('Page started loading: $url');
            setState(() {
              _hasError = false;
              _isLoading = true;
            });
          },
          onPageFinished: (String url) {
            print('Page finished loading: $url');
            setState(() {
              _isLoading = false;
            });
          },
        ),
      )
      ..addJavaScriptChannel(
        'Flutter',
        onMessageReceived: (JavaScriptMessage message) {
          if (message.message == 'retry') {
            _retryConnection();
          } else {
            _showNativeAlert(message.message);
          }
        },
      )
      ..loadRequest(Uri.parse(EnvConfig.webUrl));
  }

  void _showNativeAlert(String message) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('알림'),
        content: Text(message),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('확인'),
          ),
        ],
      ),
    );
  }

  Future<void> _loadFallbackPage() async {
    final String fallbackHtml = await rootBundle.loadString('assets/fallback.html');
    await controller.loadHtmlString(fallbackHtml);
  }

  void _retryConnection() {
    setState(() {
      _hasError = false;
    });
    controller.loadRequest(Uri.parse(EnvConfig.webUrl));
  }

  Future<bool> _onWillPop() async {
    if (await controller.canGoBack()) {
      controller.goBack();
      return false;
    }
    return true;
  }

  @override
  Widget build(BuildContext context) {
    return PopScope(
      canPop: false,
      onPopInvoked: (didPop) async {
        if (didPop) return;
        
        final shouldPop = await _onWillPop();
        if (shouldPop && context.mounted) {
          Navigator.of(context).pop();
        }
      },
      child: Scaffold(
        body: SafeArea(
          child: Stack(
            children: [
              WebViewWidget(controller: controller),
              if (_isLoading)
                const Center(
                  child: CircularProgressIndicator(),
                ),
              if (_hasError)
                Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      const Icon(
                        Icons.error_outline,
                        size: 64,
                        color: Colors.red,
                      ),
                      const SizedBox(height: 16),
                      const Text(
                        '페이지를 불러올 수 없습니다',
                        style: TextStyle(fontSize: 18),
                      ),
                      const SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: _retryConnection,
                        child: const Text('다시 시도'),
                      ),
                    ],
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
