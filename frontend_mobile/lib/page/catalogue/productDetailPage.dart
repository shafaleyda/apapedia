// ignore_for_file: use_build_context_synchronously, sort_child_properties_last, prefer_const_constructors, avoid_print, use_super_parameters, unrelated_type_equality_checks

import 'package:flutter/material.dart';
import 'package:frontend_mobile/page/order/orderConfirm.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class ProductDetailPage extends StatefulWidget {
  const ProductDetailPage({Key? key}) : super(key: key);

  @override
  State<ProductDetailPage> createState() => _ProductDetailState();
}

class _ProductDetailState extends State<ProductDetailPage> {
  dynamic product;

  @override
  void initState() {
    super.initState();
  }

  Future<bool> _checkToken() async {
    AuthService authService = AuthService();
    String? token = await authService.getTokenFromStorage();

    if (token != null) {
      return true;
    } else {
      return false;
    }
  }

  void addToCart() async {
    try {
      var url = Uri.parse('http://localhost:8081/api/user/user-loggedin');

      http.Response response = await http.get(url);

      if (response.statusCode == 200) {
        Map<String, dynamic> userLoggedIn = json.decode(response.body);

        var urlGetCard =
            Uri.parse('http://localhost:8080/cart/user/${userLoggedIn['id']}');

        http.Response responseCart = await http.get(urlGetCard);

        if (responseCart.statusCode == 200) {
          var urlGetCartItems = Uri.parse(
              'http://localhost:8080/cart/customer/${userLoggedIn['id']}');

          http.Response responseCartItems = await http.get(urlGetCartItems);
          if (responseCartItems.statusCode == 200) {
            List<Map<String, dynamic>> cartItems =
                List<Map<String, dynamic>>.from(
                    json.decode(responseCartItems.body));

            for (var item in cartItems) {
              if (item['productId'] == product['idCatalog']) {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Product already exist in cart'),
                    backgroundColor: Color.fromARGB(255, 196, 96, 89),
                  ),
                );
                return;
              }
            }
          }

          Map<String, dynamic> cart = json.decode(responseCart.body);

          String cartId = cart['id'];

          var urlAddItem = Uri.parse('http://localhost:8080/cart/$cartId/add');

          http.Response responseAddItem = await http.post(urlAddItem,
              headers: <String, String>{
                'Content-Type': 'application/json',
              },
              body: jsonEncode(<String, dynamic>{
                'productId': product['idCatalog'],
                'quantity': 1,
              }));

          if (responseAddItem.statusCode == 200) {
            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text('Product successfully added to cart'),
                backgroundColor: Color.fromARGB(255, 89, 196, 119),
              ),
            );
            return;
          } else {
            print(
                'Failed to add item to cart. Status code: ${responseAddItem.statusCode}');
            return;
          }
        } else {
          print(
              'Failed to fetch cart. Status code: ${responseCart.statusCode}');
          return;
        }
      } else {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Please login to add product to cart'),
            backgroundColor: Color.fromARGB(255, 196, 96, 89),
          ),
        );
        return;
      }
    } catch (e) {
      print('Caught an exception: $e');
      return;
    }
  }

  void orderItem() {
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => const OrderConfirmPage(),
        settings: RouteSettings(
          arguments: product,
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    // Retrieve product arguments
    final arguments = ModalRoute.of(context)!.settings.arguments;
    if (arguments != null) {
      product = arguments;
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text('Product Detail'),
      ),
      body: Scrollbar(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Center(
                child: Image.memory(
                  base64Decode(product!['image']),
                  height: 300,
                  width: 300,
                ),
              ),
              const SizedBox(height: 16.0),
              Text(
                product['productName'],
                style: const TextStyle(
                  fontSize: 20.0,
                  fontWeight: FontWeight.bold,
                ),
              ),
              const SizedBox(height: 16.0),
              Text(
                product['productDescription'],
                style: const TextStyle(
                  fontSize: 16.0,
                ),
              ),
              const SizedBox(height: 16.0),
              Text(
                'Price: ${product['price']}',
                style: const TextStyle(
                  fontSize: 16.0,
                ),
              ),
              const SizedBox(height: 16.0),
              Text(
                'Stock: ${product['stock']}',
                style: const TextStyle(
                  fontSize: 16.0,
                ),
              ),
              const SizedBox(height: 16.0),
              Text(
                'Category: ${product['categoryName']}',
                style: const TextStyle(
                  fontSize: 16.0,
                ),
              ),
            ],
          ),
        ),
      ),
      floatingActionButton: Row(
        mainAxisAlignment: MainAxisAlignment.end,
        children: [
          ElevatedButton(
            onPressed: () {
              if (_checkToken() == true) {
                addToCart();
              } else {
                ScaffoldMessenger.of(context).showSnackBar(
                  const SnackBar(
                    content: Text('Please login to add product to cart'),
                    backgroundColor: Color.fromARGB(255, 196, 96, 89),
                  ),
                );
              }
            },
            child: const Text('Add to Chart'),
          ),
          const SizedBox(width: 16.0),
          ElevatedButton(
              onPressed: () {
                if (_checkToken() == true) {
                  orderItem();
                } else {
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(
                      content: Text('Please login to order product'),
                      backgroundColor: Color.fromARGB(255, 196, 96, 89),
                    ),
                  );
                }
              },
              child: const Text('Order Now',
                  style: TextStyle(color: Colors.white)),
              style: ElevatedButton.styleFrom(
                  backgroundColor: Color.fromARGB(255, 7, 177, 255))),
        ],
      ),
    );
  }
}
