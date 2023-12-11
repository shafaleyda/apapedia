import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class CategoriesWidget extends StatefulWidget {
  @override
  _CategoriesWidgetState createState() => _CategoriesWidgetState(); 
}

class _CategoriesWidgetState extends State<CategoriesWidget> {
  List<dynamic> categories = [];
  

  @override
  void initState() {
    super.initState();
    fetchData(); // Fetch data when the widget initializes
  }

  Future<void> fetchData() async {
    String urlCatalog = "http://localhost:8082";
    try {
      final response = await http.get(Uri.parse('$urlCatalog/api/category/view-all'));
      if (response.statusCode == 200) {
        setState(() {
          categories = json.decode(response.body); // Store the fetched products
        });
        categories.asMap().forEach((index, value) {
          print("Index $index: $value['categoryName']");
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
    return SingleChildScrollView(
      scrollDirection: Axis.horizontal,
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          for (var category in categories)
          Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            padding: EdgeInsets.symmetric(vertical: 5, horizontal: 10),
            decoration: BoxDecoration(
              color: Colors.white,
              borderRadius: BorderRadius.circular(20),
            ),
            child: Row(
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text(
                 category['categoryName'],
                  style: TextStyle(
                    fontWeight: FontWeight.bold,
                    fontSize: 16,
                    color: Colors.black, 
                  ),
                )
              ],
            ),
          )
        ],
      ),
    );
  }
}

  