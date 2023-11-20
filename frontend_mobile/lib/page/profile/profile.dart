import 'package:flutter/material.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/main.dart';
import 'package:frontend_mobile/home.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:frontend_mobile/page/profile/updateProfile.dart';
import 'package:provider/provider.dart';
import 'package:frontend_mobile/page/profile/topUpBalance.dart';

class ProfilePage extends StatefulWidget {
  const ProfilePage({Key? key}) : super(key: key);

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  // late Future<Customer> profilFuture;

    @override
    void initState() {
      super.initState();
      // var request = context.read<CookieRequest>();
      // profilFuture = fetchCustomer(request);
    }

    @override
    Widget build(BuildContext context) {
      return Scaffold(
          appBar: AppBar(
            title: const Text('Customer Profile'),
            leading: BackButton(
                onPressed: () => Navigator.of(context).pushReplacement(MaterialPageRoute(
                  builder: (context) => const MyApp(),
                ))
            ),
          ),
          body : Container(
          // body: FutureBuilder<Customer>(
            // future: profilFuture,

            // builder: (context, snapshot) {
            //   if (snapshot.hasData && snapshot.data != null) {
                // return Container(
                  padding: const EdgeInsets.all(50),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    mainAxisSize: MainAxisSize.min,

                    children: <Widget>[
                      Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: <Widget>[
                              // ------ TODO: ADD BOX FOR USER INFORMATION DETAIL SECTION ------
                              Padding(
                                padding: const EdgeInsets.all(12),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: [
                                    Text(
                                      // '@${snapshot.data!.username}',
                                      'User Name: John Doe',
                                      style: TextStyle(
                                          fontSize: 14, color: Colors.grey[700]),
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
                                      'User Email: john.doe@gmail.com',
                                      style: TextStyle(
                                          fontSize: 14, color: Colors.grey[700]),
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
                                      'User Address: Skywalk Street II',
                                      style: TextStyle(
                                          fontSize: 14, color: Colors.grey[700]),
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
                                label: const Text("Update Profile Data",
                                ),
                                onPressed: () {
                                  Navigator.push(
                                    context,
                                    MaterialPageRoute(builder: (context) => const ProfileForm()),
                                  );
                                },
                                style: ElevatedButton.styleFrom(
                                  primary: Colors.blue,
                                  onPrimary: Colors.white,
                                  padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
                                ),
                              ),

                              // ------ TODO: ADD BOX FOR APAPAY BALANCE SECTION ------
                              const SizedBox(height: 24),
                              Divider(
                                thickness: 1,
                                color: Colors.grey,
                                height: 1,
                              ),
                              const SizedBox(height: 24),
                              Text('Apapay', textAlign: TextAlign.center, style: Theme.of(context).textTheme.headline6),
                              const SizedBox(height: 24),
                              Text('Balance: ' +
                                      // snapshot.data!.saldo
                                'Dummy 900'),
                              Padding(
                                  padding: const EdgeInsets.all(12),
                                  child: ElevatedButton(
                                    onPressed: () {
                                      Navigator.push(
                                        context,
                                        MaterialPageRoute(
                                            builder: (context) =>
                                                //TopUpBalance(customer: snapshot.data!)),
                                                TopUpBalance()
                                        ),
                                      );
                                    },
                                    child: const Text('+  '+ 'Top Up Balance'),
                                  )
                              ),
                              // ------ TODO: ADD BOX FOR SIGN OUT BUTTON AND DELETE ACCOUNT BUTTON SECTION ------
                              const SizedBox(height: 40),
                              Divider(
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
                                label: const Text("Sign Out", style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold),),
                                onPressed: () {
                                  // print('Console Message Using Print');
                                },
                                style: ElevatedButton.styleFrom(
                                  primary: Colors.red,
                                  onPrimary: Colors.white,
                                  padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
                                ),
                              ),

                              const SizedBox(height: 12),
                              ElevatedButton.icon(
                                icon: const Icon(
                                  Icons.delete,
                                  color: Colors.white,
                                  size: 16,
                                ),
                                label: const Text("Delete Account", style: TextStyle(fontSize: 14, fontWeight: FontWeight.bold),),
                                onPressed: () {
                                  // print('Console Message Using Print');
                                },
                                style: ElevatedButton.styleFrom(
                                  primary: Colors.pink,
                                  onPrimary: Colors.white,
                                  padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
                                ),
                              ),
                            ],
                          )
                      )
                    ],
                  // ),
                )
              // } else if (snapshot.hasError) {
              //   return Text('${snapshot.error}');
              // }
              // return Center(
              //   child: CircularProgressIndicator(),
              // );

          )
    );
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
