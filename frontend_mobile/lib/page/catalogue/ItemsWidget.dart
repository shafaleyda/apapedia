import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class ItemsWidget extends StatefulWidget {
  @override
  _ItemsWidgetState createState() => _ItemsWidgetState();
}

class _ItemsWidgetState extends State<ItemsWidget> {
  List<dynamic> products = [];

  @override
  void initState() {
    super.initState();
    fetchData(); 
  }

  void filterCatalogByProductName(String productName) {
    String urlCatalog = "http://localhost:8082";
    String urlFilterByProductName = '$urlCatalog/api/catalog/view-all-by-name?name=$productName';

    //Fetch API
    http.get(Uri.parse(urlFilterByProductName)).then((response) {
      if (response.statusCode == 200) {
        List<dynamic> responseData = json.decode(response.body);
        setState(() {
          products = responseData;
        });
      } else {
        print('Failed to load products: ${response.statusCode}');
      }
    }).catchError((error) {
      // Handle any error that might occur during the HTTP request
      print('Error loading products: $error');
    });
  }

  Future<void> fetchData() async {
    String urlCatalog = "http://localhost:8082";
    try {
      final response = await http.get(Uri.parse('$urlCatalog/api/catalog/all'));
      if (response.statusCode == 200) {
        setState(() {
          products = json.decode(response.body); // Store the fetched products
        });
      } else {
        // Handle errors if any
        print("Failed to fetch data");
      }
    } catch (error) {
      // Handle exceptions
      print("Error: $error");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scrollbar(
      child: SingleChildScrollView(
        child: GridView.count(
          childAspectRatio: 0.68,
          crossAxisCount: 2,
          shrinkWrap: true,
          children: [
            for (var product in products)
              Container(
                padding: EdgeInsets.only(left: 15, right: 15, top: 5),
                margin: EdgeInsets.symmetric(vertical: 3, horizontal: 10),
                decoration: BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.circular(20),
                ),
                child: Column(
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          padding: EdgeInsets.all(5),
                          decoration: BoxDecoration(
                            color: Color(0xFF4C53A5),
                            borderRadius: BorderRadius.circular(20),
                          ),
                          child: Text(
                            "buat diskon",
                            style: TextStyle(
                              fontSize: 14,
                              color: Colors.white,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                        Icon(
                          Icons.favorite_border,
                          color: Colors.red,
                        )
                      ],
                    ),
                    InkWell(
                      onTap: () {},
                      child: Container(
                        margin: EdgeInsets.all(10),
                        child: AspectRatio(
                          aspectRatio: 1, // Set the aspect ratio as needed
                          child: Image.memory(
                            base64Decode(product!['image']),
                            fit: BoxFit
                                .cover, // Adjust how the image fills the space
                          ),
                        ),
                      ),
                    ),
                    Container(
                      padding: EdgeInsets.only(bottom: 8),
                      alignment: Alignment.centerLeft,
                      child: Text(product['productName'],
                          style: TextStyle(
                            fontSize: 18,
                            color: Color(0xFF4C53A5),
                            fontWeight: FontWeight.bold,
                          )),
                    ),
                    Container(
                      alignment: Alignment.centerLeft,
                      child: Text(
                        product['productDescription'],
                        style: TextStyle(
                          fontSize: 15,
                          color: Color(0xFF4C53A5),
                        ),
                      ),
                    ),
                    Padding(
                      padding: EdgeInsets.symmetric(vertical: 10),
                      child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Text(
                              product['price'].toString(),
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
}
