import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
// import 'package:provider/provider.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:frontend_mobile/page/profile/profile.dart';
import 'package:http/http.dart';
import 'package:http/http.dart';
import 'package:provider/provider.dart';

class TopUpBalance extends StatefulWidget {
  // final Customer customer;

  // const TopUpBalance({Key? key, required this.customer}) : super(key: key);
  const TopUpBalance({Key? key}) : super(key: key);

  @override
  State<TopUpBalance> createState() => _TopUpBalanceState();
}

class _TopUpBalanceState extends State<TopUpBalance> {
  final TextEditingController _saldoController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  void displayDialog(context, title, text) => showDialog(
    context: context,
    builder: (context) =>
        AlertDialog(title: Text(title), content: Text(text)),
  );

  @override
  Widget build(BuildContext context) {
    // var request = context.read<CookieRequest>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('TopUp Balance'),
        leading: BackButton(
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: Container(
          padding: const EdgeInsets.all(40.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              TextField(
                decoration: const InputDecoration(
                    labelText: "Amount of Balance"),
                keyboardType: TextInputType.number,
                controller: _saldoController,
                inputFormatters: <TextInputFormatter>[
                  FilteringTextInputFormatter.digitsOnly
                ], // Only numbers can be entered
              ),
              Padding(
                  padding: const EdgeInsets.only(top: 8),
                  child: ElevatedButton(
                    onPressed: () {
                            Navigator.of(context).pushReplacement(
                                MaterialPageRoute(
                              builder: (context) => const ProfilePage(),
                            )
                          );
                    },
                    // onPressed: () async {
                    //   var response = await topUp(
                    //       request, widget.customer, _saldoController.text);
                    //
                    //   if (response.statusCode == 200) {
                    //     displayDialog(
                    //         context, "Success", "Berhasil top up saldo sebesar " + _saldoController.text);
                    //     Navigator.of(context).pushReplacement(MaterialPageRoute(
                    //       builder: (context) => const ProfilePage(),
                    //     ));
                    //
                    //   } else if (response.statusCode == 400) {
                    //     displayDialog(context, "An Error Occurred",
                    //         "Request body has invalid type or missing field.");
                    //   } else if (response.statusCode == 403) {
                    //     displayDialog(context, "An Error Occurred", "Forbidden");
                    //   } else {
                    //     displayDialog(context, "An Error Occurred",
                    //         "");
                    //   }
                    // },
                      child: const Text('Top Up Balance')
                  )
              ),
              // )
            ],
          )),
    );
  }
}

// Future<http.Response> topUp(
//     CookieRequest request, Customer customer, String saldoBaru) async {
//   var body = jsonEncode({
//     "username": customer.username,
//     "email": customer.email,
//     "saldo": customer.saldo + int.parse(saldoBaru),
//   });
//   const host =
//   String.fromEnvironment('host', defaultValue: "http://localhost:8080");
//   final response = await request.post(
//       host + '/api/v1/customer/topUpBalance/' + request.username, body);
//   return response;
// }
