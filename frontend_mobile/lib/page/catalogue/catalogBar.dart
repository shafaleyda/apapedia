import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:badges/badges.dart' as badges;
import 'package:frontend_mobile/page/catalogue/ItemsWidget.dart';
import 'package:http/http.dart' as http;
import 'ItemsWidget.dart';


// class CatalogAppBar extends StatefulWidget {
//   @override
//   _CatalogAppBarState createState() => _CatalogAppBarState();
// }
class CatalogAppBar extends StatelessWidget {

  final ItemsWidget itemsWidget = new ItemsWidget();
  List<dynamic> products = [];

  TextEditingController productNameController = TextEditingController();
  TextEditingController minPriceController = TextEditingController();
  TextEditingController maxPriceController = TextEditingController();
  String? selectedSortValue;
  String urlCatalog = "http://localhost:8082";

  void _showFilterSearchDrawer(BuildContext context) {
    showModalBottomSheet(
      context: context,
      builder: (BuildContext context) {
        return Container(
          padding: EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: productNameController,
                      decoration:
                          InputDecoration(labelText: 'Filter by Product Name'),
                    ),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      String productName = productNameController.text;
                      
                    },
                    child: Text('Search'),
                  ),
                ],
              ),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: minPriceController,
                      decoration: InputDecoration(
                          labelText: 'Filter by Range Price (Min Price)'),
                    ),
                  ),
                ],
              ),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: maxPriceController,
                      decoration: InputDecoration(
                          labelText: 'Filter by Range Price (Max Price)'),
                    ),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      String minPrice = minPriceController.text;
                      String maxPrice = maxPriceController.text;
                      // Perform search by max price
                    },
                    child: Text('Search'),
                  ),
                ],
              ),
              SizedBox(height: 20),
              DropdownButton<String>(
                hint: Text('Sort Catalog'),
                items: [
                  'Sort by Product Name (A - Z)',
                  'Sort by Product Name (Z - A)',
                  'Sort by Product Price (Min - Max)',
                  'Sort by Product Price (Max - Min)'
                ].map((String value) {
                  return DropdownMenuItem<String>(
                    value: value,
                    child: Text(value),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  selectedSortValue = newValue;
                },
              ),
            ],
          ),
        );
      },
    );
  }

  // @override
  // void dispose() {
  //   productNameController.dispose();
  //   minPriceController.dispose();
  //   maxPriceController.dispose();
  //   super.dispose();
  // }

  @override
  Widget build(BuildContext context) {
    return Container(
        color: Colors.white,
        padding: EdgeInsets.all(25),
        child: Row(children: [
          Padding(
            padding: EdgeInsets.only(left: 20),
            child: Text(
              "APAPEDIA",
              style: TextStyle(
                fontSize: 23,
                fontWeight: FontWeight.bold,
                color: Color(0xFF4C53A5),
              ),
            ),
          ),
          Spacer(),
          InkWell(
            onTap: () {
              _showFilterSearchDrawer(context);
            },
            child: Icon(
              Icons.sort_outlined,
              size: 32,
              color: Color(0xFF4C53A5),
            ),
          ),
        ]));
  }
}
