import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:frontend_mobile/page/profile/profile.dart';
import 'package:provider/provider.dart';

class TopUpBalancePage extends StatefulWidget {
  // final Customer customer;

  // const TopUpBalance({Key? key, required this.customer}) : super(key: key);
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
    builder: (context) =>
        AlertDialog(title: Text(title), content: Text(text)),
  );

  Future<String> topUpCustomerBalance(int amount) async {
    try {
      final String uriUpdateBalance = 'http://localhost:8081/api/user/d0b7fc03-7347-4104-a9a0-d9957e26553b/balance?amount=$amount';

      http.Response balanceResponse = await http.put(
        Uri.parse(uriUpdateBalance),
      );

      if (balanceResponse.statusCode == 200) {
        String responseBody = balanceResponse.body;
        return responseBody;
      } else {
        return 'error';
      }

    } catch (e) {
      print('Caught an exception: $e');
      return 'error';
    }
  }

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
              // )
            ],
          )),
    );
  }
}

