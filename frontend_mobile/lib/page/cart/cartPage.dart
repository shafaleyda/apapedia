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
            print('Cart: $cart');
            print('Cart items: $cartItems');

            return Scrollbar(
              child: SingleChildScrollView(
                child: GridView.count(
                  childAspectRatio: 0.68,
                  crossAxisCount: 2,
                  shrinkWrap: true,
                  children: [
                    for (var cartItem in cartItems)
                      Container(
                        padding: EdgeInsets.only(left: 15, right: 15, top: 5),
                        margin:
                            EdgeInsets.symmetric(vertical: 3, horizontal: 10),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(20),
                        ),
                        child: Column(
                          children: [
                            InkWell(
                              onTap: () {},
                              child: Container(
                                margin: EdgeInsets.all(10),
                                child: AspectRatio(
                                  aspectRatio:
                                      1, // Set the aspect ratio as needed
                                  child: Image.memory(
                                    base64Decode(cartItem!['image']),
                                    fit: BoxFit
                                        .cover, // Adjust how the image fills the space
                                  ),
                                ),
                              ),
                            ),
                            Container(
                              padding: EdgeInsets.only(bottom: 8),
                              alignment: Alignment.centerLeft,
                              child: Text(cartItem['productName'],
                                  style: TextStyle(
                                    fontSize: 18,
                                    color: Color(0xFF4C53A5),
                                    fontWeight: FontWeight.bold,
                                  )),
                            ),
                            Container(
                              alignment: Alignment.centerLeft,
                              child: Text(
                                cartItem['productDescription'],
                                style: TextStyle(
                                  fontSize: 15,
                                  color: Color(0xFF4C53A5),
                                ),
                              ),
                            ),
                            Padding(
                              padding: EdgeInsets.symmetric(vertical: 10),
                              child: Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
                                  children: [
                                    Text(
                                      cartItem['price'].toString(),
                                      style: TextStyle(
                                        fontSize: 15,
                                        fontWeight: FontWeight.bold,
                                        color: Color(0xFF4C53A5),
                                      ),
                                    ),
                                    Icon(
                                      Icons.shopping_cart_checkout,
                                      color: Colors.red,
                                    )
                                  ]),
                            )
                          ],
                        ),
                      )
                  ],
                ),
              ),
            );
          }
        },
      ),
    );
  }
}
