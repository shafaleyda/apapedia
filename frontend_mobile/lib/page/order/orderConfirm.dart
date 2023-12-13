// ignore_for_file: use_build_context_synchronously

import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:frontend_mobile/page/home.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class OrderConfirmPage extends StatefulWidget {
  const OrderConfirmPage({Key? key}) : super(key: key);

  @override
  State<OrderConfirmPage> createState() => _OrderConfirmState();
}

class _OrderConfirmState extends State<OrderConfirmPage> {
  late Future<Map<String, dynamic>> _userFuture;
  dynamic product;
  bool orderNow = false;
  Map<String, dynamic> cart = {};

  @override
  void initState() {
    super.initState();
    _userFuture = fetchLoggedInUser();
  }

  Future<Map<String, dynamic>> fetchCustomerCart() async {
    try {
      Map<String, dynamic> userLoggedIn = await fetchLoggedInUser();
      String customerId = userLoggedIn['id'];

      var url = Uri.parse('http://localhost:8080/cart/user/$customerId');

      var response = await http.get(url);

      if (response.statusCode == 200) {
        cart = json.decode(response.body);
        return cart;
      }
    } catch (error) {
      print('Error: $error');
    }

    return {'error': 'Failed to fetch cart'};
  }

  Future<List<Map<String, dynamic>>> fetchCustomerCartItem() async {
    try {
      Map<String, dynamic> userLoggedIn = await fetchLoggedInUser();
      String customerId = userLoggedIn['id'];

      var url = Uri.parse('http://localhost:8080/cart/customer/$customerId');

      var response = await http.get(url);

      if (response.statusCode == 200) {
        List<Map<String, dynamic>> cartItems =
            List<Map<String, dynamic>>.from(json.decode(response.body));
        return cartItems;
      }
    } catch (error) {
      print('Error: $error');
    }

    return [];
  }

  void addOrder(Map<dynamic, int> productsToOrder) async {
    try {
      Map<String, dynamic> userLoggedIn = await fetchLoggedInUser();

      String userId = userLoggedIn['id'];

      var addOrder = Uri.parse('http://localhost:8080/order/create');

      http.Response response = await http.post(addOrder,
          headers: <String, String>{
            'Content-Type': 'application/json; charset=UTF-8',
          },
          body: jsonEncode(<dynamic, dynamic>{
            'items': productsToOrder,
            'customer': userId,
            'status': 0,
          }));

      if (response.statusCode == 200) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Order successfully added'),
            backgroundColor: Color.fromARGB(255, 89, 196, 119),
          ),
        );
        if (!orderNow) {
          cart = await fetchCustomerCart();
          List<Map<String, dynamic>> cartItems = await fetchCustomerCartItem();
          String cartId = cart['id'];

          for (var productToDelete in productsToOrder.keys) {
            var urlDeleteCart =
                Uri.parse('http://localhost:8080/cart/$cartId/delete');

            String cartItemId = '';

            for (var cartItem in cartItems) {
              if (cartItem['productId'] == productToDelete) {
                cartItemId = cartItem['id'];
                break;
              }
            }

            var response = await http.delete(urlDeleteCart,
                headers: <String, String>{
                  'Content-Type': 'application/json',
                },
                body: jsonEncode(<String, dynamic>{
                  'cartItemId': cartItemId,
                }));

            if (response.statusCode == 200) {
              print('Successfully deleted item from cart');
            } else {
              print('Failed to delete item from cart');
            }
          }
        }
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => const HomePage()),
        );
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Failed to add order, please try again'),
            backgroundColor: Color.fromARGB(255, 196, 96, 89),
          ),
        );
      }
    } catch (e) {
      print('Caught an exception: $e');
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

  num calculateTotalPrice(List<dynamic> products) {
    num totalPrice = 0;
    for (var product in products) {
      totalPrice += (product['price'].toInt() * product['quantity']).toInt();
    }
    return totalPrice;
  }

  @override
  Widget build(BuildContext context) {
    final arguments = ModalRoute.of(context)!.settings.arguments;

    return Scaffold(
      appBar: AppBar(
        title: const Text('Order Confirmation'),
        automaticallyImplyLeading: true,
      ),
      body: FutureBuilder<Map<String, dynamic>>(
        future: _userFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const CircularProgressIndicator();
          } else if (snapshot.hasError) {
            return Text('Error: ${snapshot.error}');
          } else {
            Map<String, dynamic> userLoggedIn = snapshot.data!;
            if (arguments != null) {
              if (arguments is Map) {
                product = arguments;
                orderNow = true;
                return buildOrderConfirmation(userLoggedIn);
              } else {
                product = arguments;
                orderNow = false;
                return buildOrderConfirmationList(userLoggedIn);
              }
            }

            return const Text('No product found');
          }
        },
      ),
    );
  }

  Widget buildOrderConfirmation(Map<String, dynamic> userLoggedIn) {
    return SingleChildScrollView(
        child: Column(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      children: [
        Container(
          padding: const EdgeInsets.only(left: 15, right: 15, top: 5),
          margin: const EdgeInsets.symmetric(vertical: 3, horizontal: 10),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                'Product Name: ${product['productName']}',
                style: const TextStyle(
                  fontSize: 20.0,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16.0),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    'Price: ${product['price']}',
                    style: const TextStyle(
                      fontSize: 16.0,
                    ),
                  ),
                  Text(
                    'Total Product Price: ${product['price']}',
                    style: const TextStyle(
                      fontSize: 16.0,
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 16.0),
              const Text(
                'Quantity: 1',
                style: TextStyle(
                  fontSize: 16.0,
                ),
              ),
            ],
          ),
        ),
        Container(
          padding: const EdgeInsets.only(left: 15, right: 15, top: 5),
          margin: const EdgeInsets.symmetric(
            vertical: 3,
            horizontal: 10,
          ),
          decoration: BoxDecoration(
            color: Colors.white,
            borderRadius: BorderRadius.circular(20),
          ),
          child: Column(children: [
            Text('Receiver Name: ${userLoggedIn['name']}',
                style: const TextStyle(
                    fontSize: 20.0, fontWeight: FontWeight.bold)),
            Text('Shipping Address: ${userLoggedIn['address']}',
                style: const TextStyle(
                    fontSize: 20.0, fontWeight: FontWeight.bold)),
          ]),
        ),
        Container(
          padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
          child: Column(
            children: [
              Text(
                'Total Price: ${product['price']}',
                style: const TextStyle(
                  fontSize: 18,
                  fontWeight: FontWeight.bold,
                ),
              ),
              Container(
                padding: const EdgeInsets.fromLTRB(15, 10, 15, 15),
                child: ElevatedButton(
                  onPressed: () {
                    // Handle the confirm order action
                    if (userLoggedIn['balance'] < product['price']) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('Insufficient balance'),
                          backgroundColor: Color.fromARGB(255, 196, 96, 89),
                        ),
                      );
                    } else {
                      Map<String, int> productsToOrder = HashMap();
                      productsToOrder[product['productId']] = 1;
                      addOrder(productsToOrder);
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(
                        vertical: 16.0, horizontal: 32.0),
                    textStyle: const TextStyle(fontSize: 18.0),
                  ),
                  child: const Text('Confirm Order'),
                ),
              )
            ],
          ),
        ),
      ],
    ));
  }

  Widget buildOrderConfirmationList(Map<String, dynamic> userLoggedIn) {
    return SingleChildScrollView(
      child: Column(
        children: [
          Container(
            padding: const EdgeInsets.all(16.0),
            child: ListView.builder(
              shrinkWrap: true,
              itemCount: product.length,
              itemBuilder: (context, index) {
                return Container(
                  padding: const EdgeInsets.only(left: 15, right: 15, top: 5),
                  margin: const EdgeInsets.symmetric(
                    vertical: 3,
                    horizontal: 10,
                  ),
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(20),
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '${product[index]['productName']}',
                        style: const TextStyle(
                            fontSize: 20.0, fontWeight: FontWeight.bold),
                      ),
                      const SizedBox(height: 16.0),
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Text(
                            'Price: ${product[index]['price']}',
                            style: const TextStyle(
                              fontSize: 16.0,
                            ),
                          ),
                          Text(
                            'Total Product Price: ${product[index]['price'] * product[index]['quantity']}',
                            style: const TextStyle(
                              fontSize: 16.0,
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 16.0),
                      Text(
                        'Quantity: ${product[index]['quantity']}',
                        style: const TextStyle(
                          fontSize: 16.0,
                        ),
                      ),
                      const SizedBox(height: 16.0),
                    ],
                  ),
                );
              },
            ),
          ),
          Container(
            padding: const EdgeInsets.only(left: 15, right: 15, top: 5),
            margin: const EdgeInsets.symmetric(
              vertical: 3,
              horizontal: 10,
            ),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Column(children: [
              Text('Receiver Name: ${userLoggedIn['name']}',
                  style: const TextStyle(
                      fontSize: 20.0, fontWeight: FontWeight.bold)),
              Text('Shipping Address: ${userLoggedIn['address']}',
                  style: const TextStyle(
                      fontSize: 20.0, fontWeight: FontWeight.bold)),
            ]),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(15, 10, 15, 10),
            child: Column(
              children: [
                Text(
                  'Total Price: ${calculateTotalPrice(product)}',
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                Container(
                  padding: const EdgeInsets.fromLTRB(15, 10, 15, 15),
                  child: ElevatedButton(
                    onPressed: () {
                      if (userLoggedIn['balance'] <
                          calculateTotalPrice(product)) {
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                            content: Text('Insufficient balance'),
                            backgroundColor: Color.fromARGB(255, 196, 96, 89),
                          ),
                        );
                      } else {
                        Map<String, int> productsToOrder = HashMap();
                        for (var product in product) {
                          productsToOrder[product['id']] = product['quantity'];
                        }
                        addOrder(productsToOrder);
                      }
                    },
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(
                        vertical: 16.0,
                        horizontal: 32.0,
                      ),
                      textStyle: const TextStyle(fontSize: 18.0),
                    ),
                    child: const Text('Confirm Order'),
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
