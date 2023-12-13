import 'package:flutter/material.dart';
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
        Map<String, dynamic> cart = json.decode(response.body);
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
          title: Text('Cart Page'),
        ),
        body: FutureBuilder(
          future: Future.wait([_cartFuture, _cartItemsFuture]),
          builder: (context, AsyncSnapshot<List<dynamic>> snapshot) {
            if (snapshot.connectionState == ConnectionState.waiting) {
              return CircularProgressIndicator();
            } else if (snapshot.hasError) {
              return Text('Error: ${snapshot.error}');
            } else {
              Map<String, dynamic> cart = snapshot.data![0];
              List<Map<String, dynamic>> cartItems = snapshot.data![1];

              return ListView(
                children: [
                  Container(
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Cart',
                      style:
                          TextStyle(fontSize: 23, fontWeight: FontWeight.bold),
                    ),
                  ),
                  Container(
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Total price: ${cart['totalPrice']}',
                      style: TextStyle(fontSize: 18),
                    ),
                  ),
                  Container(
                    padding: EdgeInsets.all(10),
                    child: Text(
                      'Cart items',
                      style:
                          TextStyle(fontSize: 23, fontWeight: FontWeight.bold),
                    ),
                  ),
                  Container(
                    padding: EdgeInsets.all(10),
                    child: ListView.builder(
                      shrinkWrap: true,
                      itemCount: cartItems.length,
                      itemBuilder: (context, index) {
                        Map<String, dynamic> cartItem = cartItems[index];
                        return Container(
                          padding: EdgeInsets.only(left: 15, right: 15, top: 5),
                          margin:
                              EdgeInsets.symmetric(vertical: 3, horizontal: 10),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Image.memory(
                                base64Decode(cartItem['image']),
                                height: 200,
                                width: 200,
                              ),
                              Text(
                                '${cartItem['productName']}',
                                style: TextStyle(
                                    fontSize: 18, fontWeight: FontWeight.bold),
                              ),
                              Text(
                                'Harga: ${cartItem['price']}',
                                style: TextStyle(fontSize: 18),
                              ),
                              Text(
                                'Jumlah: ${cartItem['quantity']}',
                                style: TextStyle(fontSize: 18),
                              ),
                              Text(
                                'Total: ${cartItem['price'] * cartItem['quantity']}',
                                style: TextStyle(fontSize: 18),
                              ),
                            ],
                          ),
                        );
                      },
                    ),
                  ),
                ],
              );
            }
          },
        ));
  }
}
