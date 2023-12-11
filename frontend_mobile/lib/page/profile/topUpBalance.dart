import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:frontend_mobile/page/profile/profile.dart';
import 'package:provider/provider.dart';

class TopUpBalancePage extends StatefulWidget {
  const TopUpBalancePage({Key? key}) : super(key: key);

  @override
  State<TopUpBalancePage> createState() => _TopUpBalancePageState();
}

class _TopUpBalancePageState extends State<TopUpBalancePage> {
  final TextEditingController _saldoController = TextEditingController();

  @override
  void initState() {
    super.initState();
  }

  void displayDialog(context, title, text) => showDialog(
    context: context,
        builder: (context) => AlertDialog(
          title: Text(title),
          content: Text(text),
          actions: <Widget>[
            ElevatedButton(
              onPressed: () {
                Navigator.of(context).pop(); // Close the dialog
              },
              child: Text('OK'),
            ),
          ],
        ),
      );

  Future<Map<String, dynamic>> fetchLoggedInUser() async {
    try {
      var url = Uri.parse('http://localhost:8081/api/user/user-loggedin');

      http.Response response = await http.get(url);

      if (response.statusCode == 200) {
        Map<String, dynamic> userLoggedIn = json.decode(response.body);
        return userLoggedIn;
      } else {
        print(
            'Failed to fetch logged-in user. Status code: ${response.statusCode}');
        return {'error': 'error'};
      }
    } catch (e) {
      print('Caught an exception: $e');
      return {'error': 'error'};
    }
  }

  Future<String> topUpCustomerBalance(int amount) async {
    try {
      Map<String, dynamic> userLoggedIn = await fetchLoggedInUser();
      String customerId = userLoggedIn['id'];

      final String uriUpdateBalance = 'http://localhost:8081/api/user/$customerId/balance?amount=$amount';

      http.Response balanceResponse = await http.put(
        Uri.parse(uriUpdateBalance),
      );

      if (balanceResponse.statusCode == 200) {
        String responseBody = balanceResponse.body;
        displayDialog(context, 'Success', 'Balance topped up successfully!');
        return responseBody;
      } else {
        displayDialog(context, 'Error', 'Failed to top up balance!');
        return 'error';
      }

    } catch (e) {
      print('Caught an exception: $e');
      displayDialog(context, 'Error', 'Failed to top up balance!');
      return 'error';
    }
  }

  @override
  Widget build(BuildContext context) {
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
                ], 
              ),
              Padding(
                  padding: const EdgeInsets.only(top: 8),
                  child: ElevatedButton(
                    onPressed: () async {
                      int amount = int.tryParse(_saldoController.text) ?? 0;
                      var response = await topUpCustomerBalance(amount);
                      print(response);
                    
                    },
                      child: const Text('Top Up Balance')
                  )
              ),
            ],
          )),
    );
  }
}

