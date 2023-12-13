import 'package:jwt_decoder/jwt_decoder.dart';
import 'package:shared_preferences/shared_preferences.dart';

class AuthService {
  Future<void> saveTokenToStorage(String token) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.setString('jwtToken', token);
  }

  Future<String?> getTokenFromStorage() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('jwtToken');
  }

  Map<String, dynamic>? decodeToken(String token) {
    try {
      return JwtDecoder.decode(token);
    } catch (e) {
      print('Error decoding token: $e');
      return null;
    }
  }

  String? getUsernameFromToken(String token) {
    final decodedToken = decodeToken(token);
    return decodedToken?['username'];
  }

  Future<void> deleteTokenToStorage() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    prefs.remove('jwtToken');
  }
}
