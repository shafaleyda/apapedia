import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/main.dart';
import 'package:frontend_mobile/page/catalogue/catalogHome.dart';
import 'package:frontend_mobile/page/customer-login/register.dart';
import 'package:frontend_mobile/page/home.dart';
import 'package:frontend_mobile/page/home_guest.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:frontend_mobile/page/profile/profile.dart';

// import 'package:bacabaca_mobile/screens/view_all_buku.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:frontend_mobile/utils/color_pallete.dart';
import 'package:flutter/material.dart';

import 'package:http/http.dart' as http;
import 'dart:convert';

class LoginFormScreen extends StatefulWidget {
  static const routeName = '/login';
  const LoginFormScreen({super.key});

  @override
  State<LoginFormScreen> createState() => _LoginFormScreenState();
}

class _LoginFormScreenState extends State<LoginFormScreen> {
  final _formKey = GlobalKey<FormState>();

  Future<bool> loginApi(String email, String password) async {
    try {
      // Panggil API login dengan email dan password
      final response = await http.post(
        Uri.parse(
            "http://localhost:8081/api/authentication/authenticate"), // Ganti dengan URL endpoint login
        headers: <String, String>{
          'Content-Type': 'application/json',
        },
        body: jsonEncode(<String, String>{
          'email': email,
          'password': password,
        }),
      );

      if (response.statusCode == 200) {
        // Jika respons dari API adalah 200 OK, simpan token dan lakukan navigasi ke halaman BukuScreen
        final token = jsonDecode(response.body)['token'];
        await AuthService().saveTokenToStorage(token);
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
              builder: (context) =>
                  ProfilePage()), // Tambahkan parameter jika diperlukan
        );
        return true;
      } else {
        // Jika respons bukan 200 OK, tampilkan pesan kesalahan
        Map<String, dynamic> errorResponse = jsonDecode(response.body);
        String errorMessage = errorResponse['message'];
        throw Exception(errorMessage);
      }
    } catch (error) {
      print('Error during login: $error');
      return false; // Login gagal karena ada error
    }
  }

  showLoading(BuildContext context) {
    return showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext context) {
          return const Center(
            child: CircularProgressIndicator(),
          );
        });
  }

  TextEditingController emailController = TextEditingController();
  TextEditingController passwordController = TextEditingController();

  @override
  void dispose() {
    emailController.dispose();
    passwordController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    bool isLoading = false;

    return PopScope(
      canPop: false,
      child: Scaffold(
          backgroundColor: Colors.grey[300],
          body: Center(
            child: SingleChildScrollView(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const Text("APAPEDIA",
                      style:
                          TextStyle(fontSize: 28, fontWeight: FontWeight.w700)),
                  const SizedBox(
                    height: 10,
                  ),
                  Image.asset(
                    'images/navbar-icon.png',
                    width: 100,
                    height: 100,
                  ),
                  const SizedBox(
                    height: 10,
                  ),
                  Container(
                    padding:
                        const EdgeInsets.symmetric(horizontal: 8, vertical: 20),
                    margin: const EdgeInsets.all(16),
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(8),
                      color: Colors.white,
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      crossAxisAlignment: CrossAxisAlignment.center,
                      children: [
                        const Text("Halo, Yuk login!",
                            style: TextStyle(
                              fontSize: 24,
                            )),
                        Form(
                            key: _formKey,
                            child: SingleChildScrollView(
                              child: Column(
                                children: [
                                  const SizedBox(
                                    height: 20,
                                  ),
                                  const SizedBox(
                                    height: 20,
                                  ),
                                  SizedBox(
                                    width:
                                        MediaQuery.of(context).size.width * 0.8,
                                    child: TextFormField(
                                      controller: emailController,
                                      decoration: const InputDecoration(
                                        border: OutlineInputBorder(),
                                        labelText: "Email",
                                      ),
                                      validator: (String? value) {
                                        if (value == null || value.isEmpty) {
                                          return 'Masukkan email anda';
                                        }
                                        return null;
                                      },
                                    ),
                                  ),
                                  const SizedBox(
                                    height: 20,
                                  ),
                                  SizedBox(
                                      width: MediaQuery.of(context).size.width *
                                          0.8,
                                      child: TextFormField(
                                        controller: passwordController,
                                        obscureText: true,
                                        decoration: const InputDecoration(
                                          border: OutlineInputBorder(),
                                          labelText: 'Password',
                                        ),
                                        validator: (String? value) {
                                          if (value == null || value.isEmpty) {
                                            return 'Masukkan password anda';
                                          }
                                          return null;
                                        },
                                      )),
                                  const SizedBox(
                                    height: 20,
                                  ),
                                  ElevatedButton(
                                    style: ElevatedButton.styleFrom(
                                      fixedSize: Size(
                                          MediaQuery.of(context).size.width *
                                              0.8,
                                          50),
                                      backgroundColor: ColorPallete.primary,
                                      shape: RoundedRectangleBorder(
                                        borderRadius: BorderRadius.circular(10),
                                      ),
                                    ),
                                    onPressed: () async {
                                      if (_formKey.currentState!.validate()) {
                                        // Ambil email dan password dari input controller
                                        String email = emailController.text;
                                        String password =
                                            passwordController.text;

                                        // Lakukan proses login dengan memanggil metode loginApi dengan email dan password
                                        bool isLoggedIn =
                                            await loginApi(email, password);

                                        // Jika login berhasil, lanjutkan ke halaman BukuScreen
                                        if (isLoggedIn) {
                                          Navigator.push(
                                            context,
                                            MaterialPageRoute(
                                                builder: (context) =>
                                                    HomePage()),
                                          );
                                        } else {
                                          showDialog(
                                            context: context,
                                            builder: (BuildContext context) {
                                              return AlertDialog(
                                                title:
                                                    const Text('Login Failed'),
                                                content: const Text(
                                                    "Invalid email or password"),
                                                actions: <Widget>[
                                                  TextButton(
                                                    onPressed: () {
                                                      Navigator.of(context)
                                                          .pop(); // Perbaikan di sini
                                                    },
                                                    child: const Text("OK"),
                                                  ),
                                                ],
                                              );
                                            },
                                          );
                                        }
                                      }
                                      // Todo
                                    },
                                    child: const Text(
                                      'Login',
                                      style: TextStyle(
                                          fontSize: 20, color: Colors.black),
                                    ),
                                  ),
                                  const SizedBox(
                                      height:
                                          10), // Menambahkan jarak vertikal di sini
                                  // Tambahkan bagian hyperlink di sini
                                  RichText(
                                    text: TextSpan(
                                      text: 'Belum punya akun? ',
                                      style: TextStyle(color: Colors.black),
                                      children: <TextSpan>[
                                        TextSpan(
                                          text: 'Register dulu disini',
                                          style: TextStyle(
                                            color: Colors.blue,
                                            decoration:
                                                TextDecoration.underline,
                                          ),
                                          recognizer: TapGestureRecognizer()
                                            ..onTap = () {
                                              // Tambahkan logika navigasi ke halaman register di sini
                                              // Misalnya:
                                              Navigator.push(
                                                  context,
                                                  MaterialPageRoute(
                                                      builder: (context) =>
                                                          RegisterFormScreen()));
                                            },
                                        ),
                                      ],
                                    ),
                                  )
                                ],
                              ),
                            ))
                      ],
                    ),
                  ),
                  const SizedBox(height: 20), // Tambahkan jarak di sini
                  ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      fixedSize:
                          Size(MediaQuery.of(context).size.width * 0.8, 50),
                      backgroundColor:
                          Colors.grey, // Sesuaikan warna dengan kebutuhan
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                      ),
                    ),
                    onPressed: () {
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => GuestHomePage(),
                        ),
                      );
                    },
                    child: const Text(
                      'Login as Guest',
                      style: TextStyle(fontSize: 20, color: Colors.black),
                    ),
                  ),
                ],
              ),
            ),
          )),
    );
  }
}
