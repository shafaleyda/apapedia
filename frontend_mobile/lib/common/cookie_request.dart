import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter/foundation.dart' show kIsWeb;
import 'package:shared_preferences/shared_preferences.dart';

class CookieRequest {
  Map<String, String> headers = {};
  Map<String, String> cookies = {};
  final http.Client _client = http.Client();

  late SharedPreferences local;

  bool loggedIn = false;
  bool initialized = false;

  String username = "";
  String email = "";
  String role = "";
  String token = "";

  Future init(BuildContext context) async {
    if (!initialized) {
      local = await SharedPreferences.getInstance();
      String? savedCookies = local.getString("cookies");
      if (savedCookies != null) {
        cookies = Map<String, String>.from(json.decode(savedCookies));
        if (cookies['sessionid'] != null) {
          loggedIn = true;
          headers['cookie'] = _generateCookieHeader();
          headers['Authorization'] = 'Bearer ' + token;
          ScaffoldMessenger.of(context).showSnackBar(const SnackBar(
            content: Text("Successfully logged in. Welcome back!"),
          ));
        }
      }
    }
    initialized = true;
  }

  Future persist(String cookies) async {
    local.setString("cookies", cookies);
  }

  Future<dynamic> login(String url, dynamic data) async {
    if (kIsWeb) {
      dynamic c = _client;
      c.withCredentials = true;
    }

    headers['Content-Type'] = "application/json; charset=UTF-8";
    headers["Accept"] = "application/json";

    headers['Access-Control-Allow-Origin'] = '*';
    headers['Access-Control-Allow-Methods'] =
    'GET, POST, PATCH, PUT, DELETE, OPTIONS';
    headers['Access-Control-Allow-Headers'] =
    'Origin, Content-Type, X-Auth-Token';
    const host =
    String.fromEnvironment('host', defaultValue: "http://localhost:8081");
    http.Response response =
    await _client.post(Uri.parse(host + '/api/v1/customer/register'), body: data, headers: headers);

    _updateCookie(response);

    if (response.statusCode == 200) {
      loggedIn = true;

      token = json.decode(response.body)['token'];
      username = json.decode(response.body)['customer']['username'];
      email = json.decode(response.body)['customer']['email'];
      role = json.decode(response.body)['customer']['role'];
    } else {
      loggedIn = false;
    }

    return response; // Expects and returns JSON request body
  }

  Future<http.Response> get(String url) async {
    if (kIsWeb) {
      dynamic c = _client;
      c.withCredentials = true;
    }
    http.Response response =
    await _client.get(Uri.parse(url), headers: headers);
    _updateCookie(response);
    return response;
  }

  Future<http.Response> post(String url, dynamic data) async {
    if (kIsWeb) {
      dynamic c = _client;
      c.withCredentials = true;
    }
    http.Response response =
    await _client.post(Uri.parse(url), body: data, headers: headers);
    _updateCookie(response);
    return response;
  }

  void _updateCookie(http.Response response) {
    String? allSetCookie = response.headers['set-cookie'];

    if (allSetCookie != null) {
      var setCookies = allSetCookie.split(',');

      for (var setCookie in setCookies) {
        var cookies = setCookie.split(';');

        for (var cookie in cookies) {
          _setCookie(cookie);
        }
      }

      headers['cookie'] = _generateCookieHeader();
      String cookieObject = (const JsonEncoder()).convert(cookies);
      persist(cookieObject);
    }
  }

  void _setCookie(String rawCookie) {
    if (rawCookie.isNotEmpty) {
      var keyValue = rawCookie.split('=');
      if (keyValue.length == 2) {
        var key = keyValue[0].trim();
        var value = keyValue[1];

        // ignore keys that aren't cookies
        if (key == 'path' || key == 'expires') return;

        cookies[key] = value;
      }
    }
  }

  String _generateCookieHeader() {
    String cookie = "";

    for (var key in cookies.keys) {
      if (cookie.isNotEmpty) cookie += ";";
      String? newCookie = cookies[key];
      cookie += '$key=$newCookie';
    }

    return cookie;
  }

  Future<dynamic> logoutAccount(String url) async {
    http.Response response = await _client.post(Uri.parse(url));

    if (response.statusCode == 200) {
      loggedIn = false;
      username = "";
    } else {
      loggedIn = true;
    }

    cookies = {};

    return json.decode(response.body);
  }
}
