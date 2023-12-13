import 'package:flutter/material.dart';
import 'package:frontend_mobile/page/profile/profile_response.dart';
import 'package:frontend_mobile/page/profile/profile_request.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:frontend_mobile/utils/constant.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:frontend_mobile/page/profile/profile.dart';

class ProfileForm extends StatefulWidget {
  final ProfileResponse profileResponse;

  const ProfileForm({Key? key, required this.profileResponse})
      : super(key: key);

  @override
  State<ProfileForm> createState() => _ProfileFormState();
}

class _ProfileFormState extends State<ProfileForm> {
  final _formKey = GlobalKey<FormState>();
  String name = '';
  String email = '';
  String address = '';

  final TextEditingController _nameController = TextEditingController();
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _addressController = TextEditingController();

  @override
  void initState() {
    super.initState();

    _nameController.text = widget.profileResponse.name;
    _emailController.text = widget.profileResponse.email;
    _addressController.text = widget.profileResponse.address;
  }

  Future<Map<String, dynamic>> handleUpdateUser(
      String id, UserRequest userRequest) async {
    String? token = await AuthService().getTokenFromStorage();
    final response = await http.put(
      // Uri.parse('${Constant.baseUrl}/api/user/update/$id'),
      Uri.parse('http://localhost:8081/api/user/update/$id'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
        'Authorization': 'Bearer $token'
      },
      body: jsonEncode(<String, String>{
        'name': userRequest.name,
        'username': userRequest.username,
        'email': userRequest.email,
        'address': userRequest.address,
      }),
    );

    if (response.statusCode == 200) {
      return jsonDecode(response.body) as Map<String, dynamic>;
    } else {
      throw Exception(
          'Failed to update data user with status code: ${response.statusCode}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Update Profile Data"),
      ),
      body: Form(
        key: _formKey,
        child: SingleChildScrollView(
          child: Container(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              children: [
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: TextFormField(
                    controller: _nameController,
                    autocorrect: false,
                    decoration: InputDecoration(
                      labelText: "Name",
                      icon: const Icon(Icons.people),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                      ),
                    ),
                    validator: (value) {
                      if (value == null || value.isEmpty) {
                        return '*Required';
                      } else if (!value
                          .toString()
                          .contains(RegExp(r'^(?![\s.]+$)[a-zA-Z\s.]*$'))) {
                        return "Insert the right name!";
                      }
                      return null;
                    },
                    onChanged: (String? value) {
                      setState(() {
                        name = value!;
                      });
                    },
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: TextFormField(
                    controller: _emailController,
                    decoration: InputDecoration(
                      labelText: "Email",
                      icon: const Icon(Icons.email),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                      ),
                    ),
                    validator: (value) {
                      if (value!.isEmpty) {
                        return 'Email cannot be empty';
                      }
                      return null;
                    },
                    onChanged: (String? value) {
                      setState(() {
                        email = value!;
                      });
                    },
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: TextFormField(
                    controller: _addressController,
                    decoration: InputDecoration(
                      labelText: "Address",
                      icon: const Icon(Icons.map),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(5.0),
                      ),
                    ),
                    validator: (value) {
                      if (value!.isEmpty) {
                        return 'Address cannot be empty';
                      }
                      return null;
                    },
                    onChanged: (String? value) {
                      setState(() {
                        address = value!;
                      });
                    },
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: ElevatedButton(
                    style: ElevatedButton.styleFrom(
                      backgroundColor: Colors.blue, // Background color
                    ),
                    onPressed: () async {
                      try {
                        if (_formKey.currentState!.validate()) {
                          // Perform actions when the form is valid (backend actions removed)
                          UserRequest userRequest = UserRequest(
                              name: _nameController.text,
                              // TODO: sementara mengambil data dari ProfileResponse dulu karena tidak ada inputan username, sedangkan backend membutuhkan field ini
                              username: widget.profileResponse.username,
                              email: _emailController.text,
                              address: _addressController.text);
                          Map<String, dynamic> response =
                          await handleUpdateUser(
                              widget.profileResponse.id, userRequest);

                          if (!context.mounted) return;

                          if (response['id'] != null) {
                            Navigator.pushReplacement(
                              context,
                              MaterialPageRoute(
                                builder: (context) => const ProfilePage(),
                              ),
                            );
                          } else {
                            ScaffoldMessenger.of(context)
                                .showSnackBar(const SnackBar(
                                backgroundColor: Colors.red,
                                content: Text(
                                  'Error Update Data User',
                                  textAlign: TextAlign.center,
                                )));
                          }
                        }
                      } catch (e) {
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(
                            backgroundColor: Colors.red,
                            content: Text(
                              'Error: $e',
                              textAlign: TextAlign.center,
                            )));
                      }
                    },
                    child: const Text(
                      "Save Updated Data",
                      style: TextStyle(color: Colors.white),
                    ),
                  ),
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}


// import 'package:flutter/material.dart';
// import 'package:http/http.dart' as http;
// import 'dart:convert';
// import 'package:provider/provider.dart';
// import 'package:frontend_mobile/common/cookie_request.dart';
// import 'package:frontend_mobile/page/profile/profile.dart';
// import 'package:frontend_mobile/service/auth_service.dart';
//
//
// class ProfileForm extends StatefulWidget {
//   static const routeName = 'user/update-profile';
//
//   const ProfileForm({Key? key}) : super(key: key);
//   @override
//   _ProfileFormState createState() => _ProfileFormState();
// }
//
// class _ProfileFormState extends State<ProfileForm> {
//   final _formKey = GlobalKey<FormState>();
//
//   String name = '';
//   String email = '';
//   String address = '';
//   String username = '';
//
//   Future<bool> loginApi(String email, String password) async {
//     try {
//       // Panggil API login dengan email dan password
//       final response = await http.post(
//         Uri.parse(
//             "http://localhost:8081/api/authentication/authenticate"), // Ganti dengan URL endpoint login
//         headers: <String, String>{
//           'Content-Type': 'application/json',
//         },
//         body: jsonEncode(<String, String>{
//           'email': email,
//           'password': password,
//         }),
//       );
//
//       if (response.statusCode == 200) {
//         // Jika respons dari API adalah 200 OK, simpan token dan lakukan navigasi ke halaman BukuScreen
//         final token = jsonDecode(response.body)['token'];
//         await AuthService().saveTokenToStorage(token);
//         Navigator.pushReplacement(
//           context,
//           MaterialPageRoute(
//               builder: (context) =>
//                   ProfilePage()), // Tambahkan parameter jika diperlukan
//         );
//         return true;
//       } else {
//         // Jika respons bukan 200 OK, tampilkan pesan kesalahan
//         Map<String, dynamic> errorResponse = jsonDecode(response.body);
//         String errorMessage = errorResponse['message'];
//         throw Exception(errorMessage);
//       }
//     } catch (error) {
//       print('Error during login: $error');
//       return false; // Login gagal karena ada error
//     }
//   }
//
// //   Future<Map<String, dynamic>> fetchData() async {
// //     try {
// //       var url = Uri.parse('http://localhost:8081/api/user/user-loggedin');
// //
// //       http.Response response = await http.get(url);
// //
// //       if (response.statusCode == 200) {
// //         Map<String, dynamic> userLoggedIn = json.decode(response.body);
// //         return userLoggedIn;
// //       } else {
// //         print(
// //             'Failed to fetch logged-in user. Status code: ${response.statusCode}');
// //         return {'error': 'error'};
// //       }
// //     } catch (e) {
// //       print('Caught an exception: $e');
// //       return {'error': 'error'};
// //     }
// //   }
// //
// //   Future<Map<String, dynamic>> _getTokenAndFetchData(String name, String username, String email, String address) async {
// //     AuthService authService = AuthService();
// //     String? token =  await authService.getTokenFromStorage();
// //     try {
// //
// //       // http.Response response = await http.put(
// //       final response = await http.put(
// //         Uri.parse("http://localhost:8081/api/user/update/{id}}"),
// //         headers: <String, String>{
// //           'Content-Type': 'application/json',
// //         },
// //         body: jsonEncode(<String, String>{
// //           'name': name,
// //           'username': username,
// //           'email': email,
// //           'address': address
// //         }
// //         ),
// //
// //       );
// //       if (response.statusCode == 200) {
// //         // Jika respons dari API adalah 200 OK, simpan token dan lakukan navigasi ke halaman BukuScreen
// //         final token = jsonDecode(response.body)['token'];
// //         await AuthService().saveTokenToStorage(token);
// //
// //         return token;
// //       } else {
// //         // Jika respons bukan 200 OK, tampilkan pesan kesalahan
// //         Map<String, dynamic> errorResponse = jsonDecode(response.body);
// //         String errorMessage = errorResponse['message'];
// //         throw Exception(errorMessage);
// //       }
// //     } catch (error) {
// //       print('Error during login: $error');
// //       return {'error': 'error'}; // Login gagal karena ada error
// //     }
// //   }
// //
// // //   Future<Customer> fetchCustomer(CookieRequest request) async {
// // //   const host =
// // //   String.fromEnvironment('host', defaultValue: "http://localhost:8080");
// // //   final response = await request.get(host + '/api/v1/customer/');
// // //   if (response.statusCode == 200) {
// // //     return Customer.fromJson(jsonDecode(response.body));
// // //   } else {
// // //     throw Exception('Failed to load customer');
// // //   }
// // // }
//
//   final TextEditingController _nameController = TextEditingController();
//   final TextEditingController _emailController = TextEditingController();
//   final TextEditingController _addressController = TextEditingController();
//   final TextEditingController _usernameController = TextEditingController();
//
//   @override
//   void dispose() {
//     _nameController.dispose();
//     _emailController.dispose();
//     _addressController.dispose();
//     _usernameController.dispose();
//     super.dispose();
//   }
//
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(
//         title: const Text("Update Profile Data"),
//       ),
//       body: Form(
//         key: _formKey,
//         child: SingleChildScrollView(
//           child: Container(
//             padding: const EdgeInsets.all(20.0),
//             child: Column(
//               children: [
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child:
//                   TextFormField(
//                       key: _formKey,
//                       controller: _nameController,
//                       autocorrect: false,
//                       decoration: InputDecoration(
//                         labelText: "Name",
//                         icon: const Icon(Icons.people),
//                         border: OutlineInputBorder(
//                           borderRadius: BorderRadius.circular(5.0),
//                         ),
//                       ),
//                       validator: (value) {
//                         if (value == null || value.isEmpty) {
//                           return '*Required';
//                         } else if (!value.toString().contains(
//                             RegExp(r'^(?![\s.]+$)[a-zA-Z\s.]*$'))) {
//                           return "Masukkan nama yang benar!";
//                         }
//                         return null;
//                       },
//                       onChanged: (String? value) {
//                         setState(() {
//                           name = value!;
//                         });
//                       },
//                     ),
//                   ),
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child: TextFormField(
//                     key: _formKey,
//                     controller: _emailController,
//                     decoration: InputDecoration(
//                       labelText: "Email",
//                       icon: const Icon(Icons.email),
//                       border: OutlineInputBorder(
//                         borderRadius: BorderRadius.circular(5.0),
//                       ),
//                     ),
//                     validator: (value) {
//                       if (value!.isEmpty) {
//                         return 'Email cannot be empty';
//                       }
//                       return null;
//                     },
//                     onChanged: (String? value) {
//                       setState(() {
//                         email = value!;
//                       });
//                     },
//                   ),
//                 ),
//                 Padding(
//                   padding: const EdgeInsets.all(8.0),
//                   child: TextFormField(
//                     key: _formKey,
//                     controller: _addressController,
//                     decoration: InputDecoration(
//                       labelText: "Address",
//                       icon: const Icon(Icons.map),
//                       border: OutlineInputBorder(
//                         borderRadius: BorderRadius.circular(5.0),
//                       ),
//                     ),
//                     validator: (value) {
//                       if (value!.isEmpty) {
//                         return 'Address cannot be empty';
//                       }
//                       return null;
//                     },
//                     onChanged: (String? value) {
//                       setState(() {
//                         address = value!;
//                       });
//                     },
//                   ),
//                 ),
//               Padding(
//                 padding: const EdgeInsets.all(8.0),
//                 child: ElevatedButton(
//                   child: const Text(
//                     "Save Updated Data",
//                     style: TextStyle(color: Colors.white),
//                   ),
//                   onPressed: () {
//                     if (_formKey.currentState!.validate()) {
//                       // Perform actions when the form is valid (backend actions removed)
//                       Navigator.pushReplacement(
//                         context,
//                         MaterialPageRoute(
//                           builder: (context) => const ProfilePage(),
//                         ),
//                       );
//                     }
//                   },
//                 ),
//               )
//               ],
//             ),
//           ),
//         ),
//       ),
//     );
//   }
// }


//
// class ProfileForm extends StatefulWidget {
//   const ProfileForm({Key? key}) : super(key: key);
//   @override
//   _ProfileFormState createState() => _ProfileFormState();
// }
//
// class _ProfileFormState extends State<ProfileForm> {
//     final _formKey = GlobalKey<FormState>();
//     String name = '';
//     String email = '';
//     String address = '';
//
//     final TextEditingController _nameController = TextEditingController();
//     final TextEditingController _emailController = TextEditingController();
//     final TextEditingController _nomorController = TextEditingController();
//     final TextEditingController _addressController = TextEditingController();
//
//     bool initialized = false;
//     bool nilaiCheckBox = false;
//     bool nilaiSwitch = true;
//     late DateTime selectedData;
//
//     // void setInitState() async {
//     //   final request = context.watch<CookieRequest>();
//     //   String usn = request.username;
//     //   String url = 'http://127.0.0.1:8000/profile/user/flutter/' + usn;
//     //   Map<String, dynamic> data = {};
//     //   try {
//     //     final response = await http.get(Uri.parse(url));
//     //     data['profile'] = jsonDecode(response.body);
//     //
//     //     _nameController.text = data['profile'][1]['fields']['nameLengkap'];
//     //     name = _nameController.text;
//     //
//     //     _emailController.text = data['profile'][1]['fields']['NIK'].toString();
//     //     email = _emailController.text;
//     //
//     //     _nomorController.text = data['profile'][1]['fields']['nomorHandphone'].toString();
//     //     nomorHP = _nomorController.text;
//     //
//     //     _addressController.text = data['profile'][1]['fields']['address'].toString();
//     //     address = _addressController.text;
//     //   }
//     //   catch (error) {
//     //     // print(error);
//     //   }
//     //
//     //   initialized = true;
//     // }
//     //
//     // @override
//     // void dispose() {
//     //   // Clean up the controller when the widget is disposed.
//     //   _nameController.dispose();
//     //   _emailController.dispose();
//     //   super.dispose();
//     // }
//
//
//     // void printHasil() {
//     //     print("Nama Peserta: " + name);
//     //     print("NIK: " + email);
//     //     print("Nomor HP: " + nomorHP);
//     //     print("Tanggal Lahir: " + selectedDate.toString());
//     //     print("Gender: " + gender);
//     //     print("Alamat: " + address);
//     //   }
//
//     Future<Map<String, dynamic>> fetchData(CookieRequest request) async {
//
//     //   String usn = request.username;
//     //   String url = 'http://127.0.0.1:8000/profil-penerima/user/flutter/' + usn;
//     //
//     //   try {
//     //     Map<String, dynamic> extractedData = {};
//     //
//     //     final response = await http.get(Uri.parse(url));
//     //     extractedData['profile'] = jsonDecode(response.body);
//     //
//     //     return extractedData;
//     //   }
//     //
//     //   catch (error) {
//     //     // print(error);
//     //     return {"Error" : "Sorry"};
//     //   }
//     // }
//     //
//     // Future<String> postData(CookieRequest request) async {
//     //   // request.isBiodata = true;
//     //   String usn = request.username;
//     //   final response = await http.post(
//     //       Uri.parse("http://127.0.0.1:8000/profil-penerima/profile/edit/flutter/" + usn),
//     //       headers: <String, String>{
//     //         'Content-Type': 'application/json; charset=UTF-8',
//     //       },
//     //       body: jsonEncode(<String, String>{
//     //         'name': name,
//     //         'email': email,
//     //         'address': address,
//     //         'username': request.username
//     //       }));
//     //
//     //   Map<String, dynamic> extractedData = jsonDecode(response.body);
//     //   // print(extractedData);
//     //   return extractedData["msg"];
//     // }
//
//     @override
//     Widget build(BuildContext context) {
//       // final request = context.watch<CookieRequest>();
//       // if(!initialized){
//       //   setInitState();
//       // }
//
//       return Scaffold(
//         appBar: AppBar(
//           title: const Text("Edit Profil"),
//         ),
//         body: Form(
//           key: _formKey,
//           child: SingleChildScrollView(
//             child: Container(
//               padding: const EdgeInsets.all(20.0),
//               child: Column(
//                 children: [
//                   Padding(
//                     padding: const EdgeInsets.all(8.0),
//                     child: TextFormField(
//                       controller: _nameController,
//                       autocorrect: false,
//                       decoration: InputDecoration(
//                         labelText: "Name",
//                         icon: const Icon(Icons.people),
//                         border: OutlineInputBorder(
//                             borderRadius: BorderRadius.circular(5.0)),
//                       ),
//                       validator: (value) {
//                         if (value == null || value.isEmpty) {
//                           return '*Required';
//                           // sources https://stackoverflow.com/questions/43935255/regular-expression-for-name-with-spaces-allowed-in-between-the-text-and-avoid-sp
//                         } else if (!value
//                             .toString()
//                             .contains(RegExp(r'^(?![\s.]+$)[a-zA-Z\s.]*$'))) {
//                           return "Insert the right name!";
//                         }
//                         return null;
//                       },
//                       onChanged: (String? value) {
//                         setState(() {
//                           name = value!;
//                         });
//                       },
//                     ),
//                   ),
//                   Padding(
//                     padding: const EdgeInsets.all(8.0),
//                     child: TextFormField(
//                       controller: _addressController,
//                       decoration: InputDecoration(
//                         labelText: "Address",
//                         icon: const Icon(Icons.streetview),
//                         border: OutlineInputBorder(
//                             borderRadius: BorderRadius.circular(5.0)),
//                       ),
//                       validator: (value) {
//                         if (value!.isEmpty) {
//                           return 'Address cannot be empty';
//                         }
//                         return null;
//                       },
//                       onChanged: (String? value) {
//                         setState(() {
//                           address = value!;
//                         });
//                       },
//                     ),
//                   ),
//                   ElevatedButton(
//                     child: const Text(
//                       "Save Updated Data",
//                       style: TextStyle(color: Colors.white),
//                     ),
//                     onPressed: () {
//                       if (_formKey.currentState!.validate()) {
//                         // printHasil();
//                         // postData(request);
//                         Navigator.pushReplacement(
//                           context,
//                           MaterialPageRoute(
//                               builder: (context) =>
//                               const ProfilePage()
//                           ),
//                         );
//                       }
//                     },
//                   ),
//                 ],
//               ),
//             ),
//           ),
//         ),
//       );
//     }
//   }
// }