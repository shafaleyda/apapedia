import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:frontend_mobile/main.dart';
import 'package:frontend_mobile/page/profile/profile_response.dart';
import 'package:frontend_mobile/page/profile/topUpBalance.dart';
import 'package:frontend_mobile/page/profile/updateProfile.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:http/http.dart' as http;

class ProfilePage extends StatefulWidget {
  const ProfilePage({Key? key}) : super(key: key);

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  late Future<ProfileResponse> profileFuture;

  @override
  void initState() {
    super.initState();
    profileFuture = fetchLoggedInUser();
  }

  Future<ProfileResponse> fetchLoggedInUser() async {
    try {
      String? token = await AuthService().getTokenFromStorage();

      var url = Uri.parse('http://localhost:8081/api/user/user-loggedin');

      http.Response response = await http.get(
        url,
        headers: <String, String>{
          'Authorization': 'Bearer $token',
        },
      );

      if (response.statusCode == 200) {
        return ProfileResponse.fromJson(jsonDecode(response.body));
      } else {
        print(
            'Failed to fetch logged-in user. Status code: ${response.statusCode}');
        throw Exception('Failed to fetch logged-in user');
      }
    } catch (e) {
      print('Caught an exception: $e');
      throw Exception('Server error');
    }
  }

  Future<void> signOut() async {
    try {
      String? token = await AuthService().getTokenFromStorage();

      var url = Uri.parse('http://localhost:8081/api/user/user-logout');

      await http.get(
        url,
        headers: <String, String>{
          'Authorization': 'Bearer $token',
        },
      );

    } catch (e) {
      print('Caught an exception: $e');
      throw Exception('Server error');
    }
  }

  Future<void> deleteUser(String id) async {
    try {
      String? token = await AuthService().getTokenFromStorage();

      var url = Uri.parse('http://localhost:8081/api/user/delete/$id');

      await http.delete(
        url,
        headers: <String, String>{
          'Authorization': 'Bearer $token',
        },
      );

    } catch (e) {
      print('Caught an exception: $e');
      throw Exception('Server error');
    }
  }

  void _refreshProfilePage() {
    setState(() {
      profileFuture = fetchLoggedInUser();
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        resizeToAvoidBottomInset: false,
        appBar: AppBar(
          title: const Text(
            'Customer Profile',
            style: TextStyle(color: Colors.white),
          ),
          backgroundColor: Colors.blue,
          leading: BackButton(
              color: Colors.white,
              onPressed: () =>
                  Navigator.of(context).pushReplacement(MaterialPageRoute(
                    builder: (context) => const MyApp(),
                  ))),
        ),
        body: FutureBuilder(
          future: profileFuture,
          builder: (context, snapshot) {
            if (snapshot.hasData) {
              return SingleChildScrollView(
                child: Padding(
                  padding: const EdgeInsets.all(50.0),
                  child: SizedBox(
                      height: MediaQuery.of(context).size.height,
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        mainAxisSize: MainAxisSize.min,

                        children: <Widget>[
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: <Widget>[
                              // ------ TODO: ADD BOX FOR USER INFORMATION DETAIL SECTION ------
                              const SizedBox(height: 24),
                              Text('Information',
                                  textAlign: TextAlign.center,
                                  style:
                                  Theme.of(context).textTheme.titleLarge),
                              Padding(
                                padding: const EdgeInsets.all(12),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Text(
                                      // '@${snapshot.data!.username}',
                                      'User Name: ${snapshot.data!.name}',
                                      style: TextStyle(
                                          fontSize: 14,
                                          color: Colors.grey[700]),
                                    ),
                                  ],
                                ),
                              ),

                              // ------ User Email ------
                              Padding(
                                padding: const EdgeInsets.all(12),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      // '@${snapshot.data!.email}',
                                      'User Email: ${snapshot.data!.email}',
                                      style: TextStyle(
                                          fontSize: 14,
                                          color: Colors.grey[700]),
                                    ),
                                  ],
                                ),
                              ),

                              // ------ User Address ------
                              Padding(
                                padding: const EdgeInsets.all(12),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      // '@${snapshot.data!.address}',
                                      'User Address: ${snapshot.data!.address}',
                                      style: TextStyle(
                                          fontSize: 14,
                                          color: Colors.grey[700]),
                                    ),
                                  ],
                                ),
                              ),

                              TextButton.icon(
                                icon: const Icon(
                                  Icons.edit,
                                  color: Colors.white,
                                  size: 16,
                                ),
                                label: const Text(
                                  "Update Profile Data",
                                ),
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(
                                        builder: (context) => ProfileForm(
                                          profileResponse: snapshot.data!,
                                        )),
                                  );
                                },
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.blue,
                                  foregroundColor: Colors.white,
                                  padding: const EdgeInsets.symmetric(
                                      horizontal: 12, vertical: 8),
                                ),
                              ),

                              // ------ TODO: ADD BOX FOR APAPAY BALANCE SECTION ------
                              const SizedBox(height: 24),
                              const Divider(
                                thickness: 1,
                                color: Colors.grey,
                                height: 1,
                              ),
                              const SizedBox(height: 24),
                              Text('Apapay',
                                  textAlign: TextAlign.center,
                                  style:
                                  Theme.of(context).textTheme.titleLarge),
                              const SizedBox(height: 24),
                              Text('Balance: ${snapshot.data!.balance}'),
                              Padding(
                                  padding: const EdgeInsets.all(12),
                                  child: ElevatedButton(
                                    onPressed: () async {
                                      await Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                          builder: (context) => TopUpBalancePage(
                                            onTopUpComplete: () {
                                              // Refresh ProfilePage when top-up is completed
                                              _refreshProfilePage();
                                            },
                                          ),
                                        ),
                                      );
                                    },
                                    child: const Text('+  ' 'Top Up Balance'),
                                  )),
                              // ------ TODO: ADD BOX FOR SIGN OUT BUTTON AND DELETE ACCOUNT BUTTON SECTION ------
                              const SizedBox(height: 40),
                              const Divider(
                                thickness: 1,
                                color: Colors.grey,
                                height: 1,
                              ),
                              const SizedBox(height: 24),
                              ElevatedButton.icon(
                                icon: const Icon(
                                  Icons.logout,
                                  color: Colors.white,
                                  size: 16,
                                ),
                                label: const Text(
                                  "Sign Out",
                                  style: TextStyle(
                                      fontSize: 14,
                                      fontWeight: FontWeight.bold),
                                ),
                                onPressed: () async {
                                  await signOut();
                                  await AuthService().deleteTokenToStorage();
                                  if (!context.mounted) return;
                                  Navigator.of(context)
                                      .pushReplacement(MaterialPageRoute(
                                    builder: (context) => const MyApp(),
                                  ));
                                },
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.red,
                                  foregroundColor: Colors.white,
                                  padding: const EdgeInsets.symmetric(
                                      horizontal: 24, vertical: 16),
                                ),
                              ),

                              const SizedBox(height: 12),
                              ElevatedButton.icon(
                                icon: const Icon(
                                  Icons.delete,
                                  color: Colors.white,
                                  size: 16,
                                ),
                                label: const Text(
                                  "Delete Account",
                                  style: TextStyle(
                                      fontSize: 14,
                                      fontWeight: FontWeight.bold),
                                ),
                                onPressed: () async {
                                  await deleteUser(snapshot.data!.id);
                                  await AuthService().deleteTokenToStorage();
                                  if (!context.mounted) return;
                                  Navigator.of(context)
                                      .pushReplacement(MaterialPageRoute(
                                    builder: (context) => const MyApp(),
                                  ));
                                },
                                style: ElevatedButton.styleFrom(
                                  backgroundColor: Colors.pink,
                                  foregroundColor: Colors.white,
                                  padding: const EdgeInsets.symmetric(
                                      horizontal: 24, vertical: 16),
                                ),
                              ),
                            ],
                          )
                        ],
                        // ),
                      )),
                ),
              );
            } else if (snapshot.hasError) {
              return Text('${snapshot.error}');
            }

            return const Center(child: CircularProgressIndicator());
          },
        ));
  }
}

// Future<Customer> fetchCustomer(CookieRequest request) async {
//   const host =
//   String.fromEnvironment('host', defaultValue: "http://localhost:8080");
//   final response = await request.get(host + '/api/v1/customer/');
//   if (response.statusCode == 200) {
//     return Customer.fromJson(jsonDecode(response.body));
//   } else {
//     throw Exception('Failed to load customer');
//   }
// }

// import 'package:flutter/material.dart';
// import 'package:frontend_mobile/main.dart';
// // import 'package:frontend_mobile/page/auth/login.dart';
// import 'topUpBalance.dart';
//
// class ProfilePage extends StatefulWidget {
//   const ProfilePage({Key? key}) : super(key: key);
//
//   @override
//   State<ProfilePage> createState() => _ProfilePageState();
// }
//
// class _ProfilePageState extends State<ProfilePage> {
//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//         appBar: AppBar(
//           title: const Text('Profile'),
//         ),
//         body: Column (children: [
//
//           Center(
//             child: Card(
//               child: Column(
//                   mainAxisSize: MainAxisSize.min,
//                   children: <Widget> [
//                     const ListTile(
//                       title: Text('Username'),
//                       subtitle: Text('budibudi'),
//                     ),
//                   ]),
//             ),
//           ),
//
//           Center(
//             child: Card(
//               child: Column(
//                   mainAxisSize: MainAxisSize.min,
//                   children: <Widget> [
//                     const ListTile(
//                       title: Text('Nama'),
//                       subtitle: Text('Budi Budi'),
//                     ),
//                   ]),
//             ),
//           ),
//
//           Center(
//             child: Card(
//               child: Column(
//                   mainAxisSize: MainAxisSize.min,
//                   children: <Widget> [
//                     const ListTile(
//                       title: Text('Email'),
//                       subtitle: Text('budi@gmial.com'),
//                     ),
//                   ]),
//             ),
//           ),
//
//           Center(
//             child: Card(
//               child: Column(
//                   mainAxisSize: MainAxisSize.min,
//                   children: <Widget> [
//                     const ListTile(
//                       title: Text('Balance'),
//                       subtitle: Text('Rp.500.000,00'),
//                     ),
//                     Row(
//                       mainAxisAlignment: MainAxisAlignment.center,
//                       children: [
//                         TextButton(
//                             child: Text('Top-Up'),
//                             onPressed: () {
//                               Navigator.push(
//                                 context,
//                                 MaterialPageRoute(builder: (context) => const ToUpBalancePage()),
//                               );
//                             }
//                         )
//                       ],
//                     )
//                   ]),
//             ),
//           ),
//           Center(
//             child: ElevatedButton(
//               onPressed: () async {
//                 // jwt = "";
//                 // Navigator.pushAndRemoveUntil(context,
//                 //     MaterialPageRoute(builder: (context) => LoginPage()),
//                 //     ModalRoute.withName('/')
//                 // );
//               },
//               child: Text("Log Out"),
//             ),
//           )
//
//         ],)
//     );
//   }
// }
