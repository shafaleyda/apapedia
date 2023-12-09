import 'package:flutter/material.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/main.dart';
import 'package:frontend_mobile/home.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class OrderHistoryPage extends StatefulWidget {
  const OrderHistoryPage({Key? key}) : super(key: key);

  @override
  State<OrderHistoryPage> createState() => _OrderHistoryPageState();
}

class _OrderHistoryPageState extends State<OrderHistoryPage> {
  Future<List<Map<String, dynamic>>> fetchCustomerOrderHistory() async {
    var url = Uri.parse(
        'http://localhost:8080/order/customer/bd6513b7-8104-4f7e-88fa-7403ef88a742');

    try {
      var response = await http.get(url);

      if (response.statusCode == 200) {
        Map<String, dynamic> myResponse = jsonDecode(response.body);
        List<Map<String, dynamic>> ordersRaw =
            List<Map<String, dynamic>>.from(myResponse['data']);

        if (ordersRaw.isNotEmpty) {
          List<Map<String, dynamic>> orderList = [];

          for (var orderDetails in ordersRaw) {
            // Access the order details directly, no 'order' key
            
            Map<String, dynamic> orderMap = orderDetails['order'];
            List<Map<String, dynamic>> listOrderItem = List<Map<String, dynamic>>.from(orderDetails['listOrderItem']);

            Map<String, dynamic> orderDetailsMap = {
              'order': orderMap,
              'listOrderItem': listOrderItem,
            };

            orderList.add(orderDetailsMap);
          }

          print(orderList);
          return orderList;
        }
      } else {
        print('Failed to load data. Status code: ${response.statusCode}');
      }
    } catch (error) {
      print('Error: $error');
    }

    // Return an empty list if there's an error or no data
    return [];
  }

  Future<String> updateStatus(String orderId, int status) async {
    try {
      final String uri = 'http://localhost:8080/order/update/$orderId';

      Map<String, int> requestBody = {'status': status};

      http.Response response = await http.put(
        Uri.parse(uri),
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode(requestBody),
      );

      if (response.statusCode == 200) {
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

  int selectedStatus = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Order History'),
      ),
      body: Container(
        margin: EdgeInsets.only(top: 50),
        child: FutureBuilder<List<Map<String, dynamic>>>(
          future: fetchCustomerOrderHistory(),
          builder: (context, snapshot) {
            if (!snapshot.hasData || snapshot.data!.isEmpty) {
              return Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: const [
                  Text(
                    "Belum ada order :(",
                    style: TextStyle(
                      color: Color(0xFF59A5D8),
                      fontSize: 20,
                    ),
                  ),
                  SizedBox(height: 8),
                ],
              );
            } else {
              return ListView.builder(
                itemCount: snapshot.data!.length,
                itemBuilder: (context, index) {
                  Map<String, dynamic> orderDetails = snapshot.data![index];
                  return Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Card(
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10),
                        side: BorderSide(color: Colors.blue),
                      ),
                      child: Container(
                        padding: const EdgeInsets.all(16.0),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            for (var orderItem in orderDetails['listOrderItem'])
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text('Item ${orderDetails['listOrderItem'].indexOf(orderItem) + 1}'),
                                  Text('Product name: ${orderItem['productName']}'),
                                  Text('Quantity: ${orderItem['quantity']}'),
                                  SizedBox(height: 8.0),
                                ],
                              ),
                            Text('Product total price: ${orderDetails['order']['totalPrice']}'),
                            SizedBox(height: 8.0),
                            Text(
                              'Order status: ${getStatusText(orderDetails['order']['status'])}',
                              style: TextStyle(color: Colors.blue),
                            ),
                            SizedBox(height: 8.0),
                            Row(
                              children: [
                                Text('Select status:'),
                                SizedBox(width: 8.0),
                                DropdownButton<int>(
                                  value: selectedStatus,
                                  items: List.generate(
                                    6,
                                    (index) => DropdownMenuItem<int>(
                                      value: index,
                                      child: Text(getStatusText(index)),
                                    ),
                                  ),
                                  onChanged: (value) {
                                    setState(() {
                                      selectedStatus = value!;
                                    });
                                  },
                                ),
                              ],
                            ),
                            SizedBox(height: 8.0),
                            ElevatedButton(
                              onPressed: () async {
                                String result = await updateStatus(orderDetails['order']['id'], selectedStatus);
                                print(result);
                                setState(() {
                                });
                              },
                              
                              child: Text('Update Order Status'),
                            ),
                          ],
                        ),
                      ),
                    ),
                  );
                },
              );
            }
          },
        ),
      ),
    );
  }
}

String getStatusText(int statusCode) {
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



