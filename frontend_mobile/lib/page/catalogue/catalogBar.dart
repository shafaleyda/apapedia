import 'package:flutter/material.dart';
import 'package:badges/badges.dart' as badges;

class CatalogAppBar extends StatelessWidget {
  TextEditingController productNameController = TextEditingController();
  TextEditingController minPriceController = TextEditingController();
  TextEditingController maxPriceController = TextEditingController();

  String? selectedSortValue;

  void _showFilterSearchDrawer(BuildContext context) {
    showModalBottomSheet(
      context: context,
      builder: (BuildContext context) {
        return Container(
          padding: EdgeInsets.all(20),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextFormField(
                controller: productNameController,
                decoration:
                    InputDecoration(labelText: 'Filter by Product Name'),
              ),
              TextFormField(
                controller: minPriceController,
                decoration: InputDecoration(
                    labelText: 'Filter by Range Price (Min Price)'),
              ),
              TextFormField(
                controller: maxPriceController,
                decoration: InputDecoration(
                    labelText: 'Filter by Range Price (Max Price)'),
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
              SizedBox(height: 20),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                children: [
                  ElevatedButton(
                    onPressed: () {
                      String productName = productNameController.text;
                      
                    },
                    child: Text('Search'),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      // Perform search by price range
                    },
                    child: Text('Search'),
                  ),
                  ElevatedButton(
                    onPressed: () {
                      // Perform sorting based on dropdown value
                    },
                    child: Text('Sort'),
                  ),
                ],
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
