import 'package:flutter/material.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/main.dart';
import 'package:frontend_mobile/page/home.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class OrderHistoryPage extends StatefulWidget {
  const OrderHistoryPage({Key? key}) : super(key: key);

  @override
  State<OrderHistoryPage> createState() => _OrderHistoryPageState();
}

class _OrderHistoryPageState extends State<OrderHistoryPage> {
  Future<List<Map<String, dynamic>>> _checkTokenAndFetchData() async {
    AuthService authService = AuthService();
    String? token = await authService.getTokenFromStorage();

    if (token != null) {
      return fetchCustomerOrderHistory();
    } else {
      return [];
    }
  }

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
        return {'error': 'Failed to fetch logged-in user'};
      }
    } catch (e) {
      print('Caught an exception: $e');
      return {'error': 'Caught an exception'};
    }
  }

  Future<List<Map<String, dynamic>>> fetchCustomerOrderHistory() async {
    try {
      Map<String, dynamic> userLoggedIn = await fetchLoggedInUser();
      String customerId = userLoggedIn['id'];

      var url = Uri.parse('http://localhost:8080/order/customer/$customerId');

      var response = await http.get(url);

      if (response.statusCode == 200) {
        Map<String, dynamic> myResponse = jsonDecode(response.body);
        List<Map<String, dynamic>> ordersRaw =
            List<Map<String, dynamic>>.from(myResponse['data']);

        if (ordersRaw.isNotEmpty) {
          List<Map<String, dynamic>> orderList = [];

          for (var orderDetails in ordersRaw) {
            Map<String, dynamic> orderMap = orderDetails['order'];
            List<Map<String, dynamic>> listOrderItem =
                List<Map<String, dynamic>>.from(orderDetails['listOrderItem']);

            Map<String, dynamic> orderDetailsMap = {
              'order': orderMap,
              'listOrderItem': listOrderItem,
            };

            orderList.add(orderDetailsMap);
          }

          return orderList;
        }
      } else {
        print('Failed to load data. Status code: ${response.statusCode}');
      }
    } catch (error) {
      print('Error: $error');
    }

    return [];
  }

  Future<String> updateStatus(String orderId, int statusCase) async {
    try {
      var status = statusCase + 4;
      final String uri = 'http://localhost:8080/order/update/$orderId';

      Map<String, int> requestBody = {'status': status};

      http.Response response = await http.put(
        Uri.parse(uri),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(requestBody),
      );

      if (response.statusCode == 200) {
        Map<String, dynamic> responseBody = json.decode(response.body);
        int updatedStatus = responseBody['status'];
        String idSeller = responseBody['seller'];
        int totalPrice = responseBody['totalPrice'];

        if (updatedStatus == 5) {
          final String uriUpdateBalance =
              'http://localhost:8081/api/user/$idSeller/balance?amount=$totalPrice';

          http.Response balanceResponse = await http.put(
            Uri.parse(uriUpdateBalance),
          );
        }
        return 'successful';
      } else {
        print('Failed to update status. Status code: ${response.statusCode}');
        return 'error';
      }
    } catch (e) {
      print('Caught an exception: $e');
      return 'error';
    }
  }

  List<int> selectedStatusList = [];

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: const Text(
            'Order History',
            style: TextStyle(fontWeight: FontWeight.bold),
          ),
          automaticallyImplyLeading: false),
      body: Container(
        child: FutureBuilder<List<Map<String, dynamic>>>(
          future: _checkTokenAndFetchData(),
          builder: (context, snapshot) {
            if (snapshot.connectionState == ConnectionState.none ||
                snapshot.connectionState == ConnectionState.waiting) {
              return Center(
                child: CircularProgressIndicator(),
              );
            } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(top: 230.0),
                      child: GestureDetector(
                        child: Container(
                          decoration: BoxDecoration(
                            color: Color.fromARGB(208, 255, 237, 210),
                            borderRadius: BorderRadius.all(Radius.circular(10.0)),
                          ),
                          padding: EdgeInsets.all(8.0),
                          child: Center(
                            child: Text(
                              "Make your first order!",
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: const Color.fromARGB(255, 0, 0, 0),
                              ),
                            ),
                          ),
                        ),
                      ),
                    ),
                  ],
                ),
              );
            } else {
              return Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.fromLTRB(16.0, 32.0, 16.0, 12.0),
                    child: Text(
                      "Order History",
                      style: TextStyle(
                        fontSize: 30,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                      itemCount: snapshot.data!.length,
                      itemBuilder: (context, index) {
                        Map<String, dynamic> orderDetails =
                            snapshot.data![index];
                        while (selectedStatusList.length <= index) {
                          selectedStatusList.add(0);
                        }
                        return Padding(
                          padding: const EdgeInsets.all(16.0),
                          child: Align(
                            alignment: Alignment.topCenter,
                            child: SizedBox(
                              width: 600,
                              child: Card(
                                shape: RoundedRectangleBorder(
                                  borderRadius: BorderRadius.circular(20),
                                ),
                                child: Container(
                                  padding: const EdgeInsets.all(16.0),
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      for (var orderItem
                                          in orderDetails['listOrderItem'])
                                        Column(
                                          crossAxisAlignment:
                                              CrossAxisAlignment.start,
                                          children: [
                                            Text(
                                                'Item ${orderDetails['listOrderItem'].indexOf(orderItem) + 1}'),
                                            Text(
                                                'Product name: ${orderItem['productName']}'),
                                            Text(
                                                'Quantity: ${orderItem['quantity']}'),
                                            SizedBox(height: 8.0),
                                          ],
                                        ),
                                      Text(
                                          'Product total price: ${orderDetails['order']['totalPrice']}'),
                                      SizedBox(height: 8.0),
                                      Text(
                                        'Order status: ${getStatusText(orderDetails['order']['status'])}',
                                        style: TextStyle(color: Colors.blue),
                                      ),
                                      SizedBox(height: 8.0),
                                      Row(
                                        children: [
                                          Text('Update status:'),
                                          SizedBox(width: 6.0),
                                          DropdownButton<int>(
                                            value: selectedStatusList[index],
                                            items: List.generate(
                                              2,
                                              (dropIndex) =>
                                                  DropdownMenuItem<int>(
                                                value: dropIndex,
                                                child: Text(getStatusText(
                                                    dropIndex,
                                                    forDropdown: true)),
                                              ),
                                            ),
                                            onChanged: (value) {
                                              setState(() {
                                                selectedStatusList[index] =
                                                    value!;
                                              });
                                            },
                                          ),
                                        ],
                                      ),
                                      SizedBox(height: 8.0),
                                      Center(
                                        child: ElevatedButton(
                                          onPressed: orderDetails['order']
                                                      ['status'] ==
                                                  5
                                              ? null
                                              : () async {
                                                  String result =
                                                      await updateStatus(
                                                          orderDetails['order']
                                                              ['id'],
                                                          selectedStatusList[
                                                              index]);
                                                  setState(() {});
                                                },
                                          child: orderDetails['order']
                                                      ['status'] ==
                                                  5
                                              ? Text('Selesai',
                                                  style: TextStyle(
                                                      color: Colors.red))
                                              : Text('Update Order Status'),
                                        ),
                                      )
                                    ],
                                  ),
                                ),
                              ),
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                ],
              );
            }
          },
        ),
      ),
    );
  }
}

String getStatusText(int statusCode, {bool forDropdown = false}) {
  if (forDropdown) {
    switch (statusCode) {
      case 0:
        return 'Barang diterima';
      case 1:
        return 'Selesai';
      default:
        return 'Unknown';
    }
  } else {
    switch (statusCode) {
      case 0:
        return 'Menunggu konfirmasi';
      case 1:
        return 'Dikonfirmasi penjual';
      case 2:
        return 'Menunggu kurir';
      case 3:
        return 'Dalam perjalanan';
      case 4:
        return 'Barang diterima';
      case 5:
        return 'Selesai';
      default:
        return 'Unknown';
    }
  }
}
