// ignore_for_file: curly_braces_in_flow_control_structures, avoid_print, use_super_parameters

import 'package:flutter/material.dart';
import 'package:frontend_mobile/page/order/orderConfirm.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class CartPage extends StatefulWidget {
  const CartPage({Key? key}) : super(key: key);

  @override
  State<CartPage> createState() => _CartPageState();
}

class _CartPageState extends State<CartPage> {
  late Future<Map<String, dynamic>> _cartFuture;
  late Future<List<Map<String, dynamic>>> _cartItemsFuture;
  List<Map<String, dynamic>> cartItems = [];
  List<bool> selectedItems = [];
  Map<String, dynamic> cart = {};

  @override
  void initState() {
    super.initState();
    _cartFuture = _checkTokenAndFetchCart();
    _cartItemsFuture = _checkTokenAndFetchCartItems();
  }

  Future<Map<String, dynamic>> _checkTokenAndFetchCart() async {
    AuthService authService = AuthService();
    String? token = await authService.getTokenFromStorage();

    if (token != null) {
      return fetchCustomerCart();
    } else {
      return {'error': 'Token not available'};
    }
  }

  Future<List<Map<String, dynamic>>> _checkTokenAndFetchCartItems() async {
    AuthService authService = AuthService();
    String? token = await authService.getTokenFromStorage();

    if (token != null) {
      return fetchCustomerCartItem();
    } else {
      return [];
    }
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

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'Cart',
          style: TextStyle(fontWeight: FontWeight.bold),
        ),
        automaticallyImplyLeading: false,
      ),
      body: Column(
        children: [
          Expanded(
            child: FutureBuilder(
              future: Future.wait([_cartFuture, _cartItemsFuture]),
              builder: (context, AsyncSnapshot<List<dynamic>> snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return const CircularProgressIndicator();
                } else if (snapshot.hasError) {
                  return Text('Error: ${snapshot.error}');
                } else {
                  cart = snapshot.data![0];
                  cartItems = snapshot.data![1];

                  if (cartItems.isEmpty) {
                    return Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          GestureDetector(
                            child: Container(
                              decoration: const BoxDecoration(
                                color: Color.fromARGB(208, 255, 237, 210),
                                borderRadius:
                                    BorderRadius.all(Radius.circular(10.0)),
                              ),
                              padding: const EdgeInsets.all(8.0),
                              child: const Center(
                                child: Text(
                                  "Your cart is empty",
                                  style: TextStyle(
                                    fontSize: 18,
                                    fontWeight: FontWeight.bold,
                                    color: Color.fromARGB(255, 0, 0, 0),
                                  ),
                                ),
                              ),
                            ),
                          ),
                        ],
                      ),
                    );
                  }

                  if (selectedItems.length != cartItems.length) {
                    selectedItems =
                        List.generate(cartItems.length, (index) => false);
                  }

                  return ListView.builder(
                    itemCount: cartItems.length,
                    itemBuilder: (context, index) {
                      Map<String, dynamic> cartItem = cartItems[index];
                      return Container(
                          padding: const EdgeInsets.only(
                              left: 15, right: 15, top: 5),
                          margin: const EdgeInsets.symmetric(
                              vertical: 3, horizontal: 10),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Row(
                            children: [
                              Checkbox(
                                value: selectedItems[index],
                                onChanged: (bool? value) {
                                  setState(() {
                                    selectedItems[index] = value!;
                                  });
                                },
                              ),
                              Padding(
                                  padding: const EdgeInsets.all(8.0),
                                  child: InkWell(
                                    onTap: () {
                                      Navigator.pushNamed(
                                          context, '/productDetail',
                                          arguments: cartItem);
                                    },
                                    child: Image.memory(
                                      base64Decode(cartItem['image']),
                                      height: 100,
                                      width: 100,
                                    ),
                                  )),
                              const SizedBox(width: 8.0),
                              Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  Text(
                                    '${cartItem['productName']}',
                                    style: const TextStyle(
                                      fontSize: 18,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                  Text(
                                    'Harga: ${cartItem['price']}',
                                    style: const TextStyle(fontSize: 12),
                                  ),
                                ],
                              ),
                              const Spacer(
                                flex: 1,
                              ),
                              Column(
                                children: [
                                  Row(
                                    children: [
                                      IconButton(
                                        onPressed: () async {
                                          try {
                                            if (cartItem['quantity'] == 1) {
                                              return;
                                            }
                                            String cartId = cart['id'];
                                            String cartItemId = cartItem['id'];

                                            var url = Uri.parse(
                                                'http://localhost:8080/cart/$cartId/update');

                                            var response = await http.put(
                                              url,
                                              headers: <String, String>{
                                                'Content-Type':
                                                    'application/json',
                                              },
                                              body: jsonEncode(<String, Object>{
                                                'id': cartItemId,
                                                'quantity':
                                                    cartItem['quantity'] - 1,
                                              }),
                                            );

                                            if (response.statusCode == 200) {
                                              setState(() {
                                                _cartItemsFuture =
                                                    _checkTokenAndFetchCartItems();

                                                _cartFuture =
                                                    _checkTokenAndFetchCart();
                                              });
                                            }
                                          } catch (error) {
                                            print('Error: $error');
                                          }
                                        },
                                        icon: const Icon(Icons.remove),
                                      ),
                                      Text(
                                        'Jumlah: ${cartItem['quantity']}',
                                        style: const TextStyle(fontSize: 12),
                                      ),
                                      IconButton(
                                        onPressed: () async {
                                          try {
                                            if (cartItem['quantity'] ==
                                                cartItem['stock']) {
                                              ScaffoldMessenger.of(context)
                                                  .showSnackBar(
                                                const SnackBar(
                                                  content: Text(
                                                      'Stock tidak mencukupi'),
                                                  backgroundColor:
                                                      Color.fromARGB(
                                                          255, 196, 96, 89),
                                                ),
                                              );
                                              return;
                                            }
                                            String cartId = cart['id'];
                                            String cartItemId = cartItem['id'];

                                            var url = Uri.parse(
                                                'http://localhost:8080/cart/$cartId/update');

                                            var response = await http.put(
                                              url,
                                              headers: <String, String>{
                                                'Content-Type':
                                                    'application/json',
                                              },
                                              body: jsonEncode(<String, Object>{
                                                'id': cartItemId,
                                                'quantity':
                                                    cartItem['quantity'] + 1,
                                              }),
                                            );

                                            if (response.statusCode == 200) {
                                              setState(() {
                                                _cartItemsFuture =
                                                    _checkTokenAndFetchCartItems();

                                                _cartFuture =
                                                    _checkTokenAndFetchCart();
                                              });
                                            }
                                          } catch (error) {
                                            print('Error: $error');
                                          }
                                        },
                                        icon: const Icon(Icons.add),
                                      ),
                                      IconButton(
                                        onPressed: () async {
                                          try {
                                            String cartId = cart['id'];
                                            String cartItemId = cartItem['id'];

                                            var url = Uri.parse(
                                                'http://localhost:8080/cart/$cartId/delete');

                                            var response = await http.delete(
                                                url,
                                                headers: <String, String>{
                                                  'Content-Type':
                                                      'application/json',
                                                },
                                                body:
                                                    jsonEncode(<String, Object>{
                                                  'cartItemId': cartItemId,
                                                }));

                                            if (response.statusCode == 200) {
                                              setState(() {
                                                _cartItemsFuture =
                                                    _checkTokenAndFetchCartItems();
                                                _cartFuture =
                                                    _checkTokenAndFetchCart();
                                              });
                                            }
                                          } catch (error) {
                                            print('Error: $error');
                                          }
                                        },
                                        icon: const Icon(Icons.delete),
                                      ),
                                    ],
                                  ),
                                  Text(
                                      'Total Price: ${cartItem['price'] * cartItem['quantity']}',
                                      style: const TextStyle(
                                          fontSize: 12,
                                          fontWeight: FontWeight.bold)),
                                ],
                              ),
                            ],
                          ));
                    },
                  );
                }
              },
            ),
          ),
          Container(
            padding: const EdgeInsets.fromLTRB(15, 10, 15, 100),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                ElevatedButton(
                  onPressed: () {
                    if (selectedItems.isEmpty) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(
                          content: Text('There is no item selected'),
                          backgroundColor: Color.fromARGB(255, 196, 96, 89),
                        ),
                      );
                      return;
                    }
                    List<Map<String, dynamic>> selectedCartItems = [];
                    for (int i = 0; i < selectedItems.length; i++) {
                      if (selectedItems[i]) {
                        selectedCartItems.add(cartItems[i]);
                      }
                    }
                    Navigator.pushNamed(
                      context,
                      '/orderConfirm',
                      arguments: selectedCartItems,
                    );
                  },
                  child: const Text('Checkout'),
                ),
                FutureBuilder<Map<String, dynamic>>(
                  future: _cartFuture,
                  builder: (context, snapshot) {
                    if (snapshot.connectionState == ConnectionState.waiting) {
                      return const Text(
                        'Total price: Loading...',
                        style: TextStyle(
                            fontSize: 16, fontWeight: FontWeight.bold),
                      );
                    } else if (snapshot.hasError) {
                      return const Text(
                        'Error loading total price',
                        style: TextStyle(
                            fontSize: 16, fontWeight: FontWeight.bold),
                      );
                    } else {
                      num totalPrice = snapshot.data?['totalPrice'] ?? 0.0;
                      return Text(
                        'Total price: $totalPrice',
                        style: const TextStyle(
                            fontSize: 16, fontWeight: FontWeight.bold),
                      );
                    }
                  },
                )
              ],
            ),
          ),
        ],
      ),
    );
  }
}
