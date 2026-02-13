import 'package:flutter_dotenv/flutter_dotenv.dart';

class EnvConfig {
  static String get webUrl => dotenv.env['WEB_URL'] ?? 'https://localhost:3000';
}