import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/page/profile/profile.dart';

class ProfileForm extends StatefulWidget {
  const ProfileForm({Key? key}) : super(key: key);
  @override
  _ProfileFormState createState() => _ProfileFormState();
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
                      } else if (!value.toString().contains(
                          RegExp(r'^(?![\s.]+$)[a-zA-Z\s.]*$'))) {
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
                  child: const Text(
                    "Save Updated Data",
                    style: TextStyle(color: Colors.white),
                  ),
                  onPressed: () {
                    if (_formKey.currentState!.validate()) {
                      // Perform actions when the form is valid (backend actions removed)
                      Navigator.pushReplacement(
                        context,
                        MaterialPageRoute(
                          builder: (context) => const ProfilePage(),
                        ),
                      );
                    }
                  },
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