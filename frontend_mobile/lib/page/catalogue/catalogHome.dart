import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

// ignore: duplicate_import
import 'package:dot_navigation_bar/dot_navigation_bar.dart';

class CatalogHome extends StatefulWidget {
  @override
  _CatalogHomeState createState() => _CatalogHomeState();
}

class _CatalogHomeState extends State<CatalogHome> {
  List<dynamic> categories = [];
  List<dynamic> products = [];
  List<String> categoryNames = [];
  bool isWrappedVisible = false;

  @override
  void initState() {
    super.initState();
    fetchCategories();
    fetchCatalog();
    isWrappedVisible = false;
  }

  TextEditingController productNameController = TextEditingController();
  TextEditingController minPriceController = TextEditingController();
  TextEditingController maxPriceController = TextEditingController();
  String? selectedSortValue;
  String urlCatalog = "http://localhost:8082";

  Future<void> fetchCategories() async {
    try {
      final response =
          await http.get(Uri.parse('$urlCatalog/api/category/view-all'));
      if (response.statusCode == 200) {
        setState(() {
          categories = json.decode(response.body);
          categoryNames = categories
              .map((category) => category['categoryName'].toString())
              .toList();
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
                      filterCatalogByProductName(productName);
                      Navigator.pop(context);
                      //productNameController.dispose();
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
                  SizedBox(width: 10),
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

                      int minPriceInt = int.parse(minPrice);
                      int maxPriceInt = int.parse(maxPrice);
                      filterCatalogByProductPrice(minPriceInt, maxPriceInt);
                      Navigator.pop(context);
                      // minPriceController.dispose();
                      // maxPriceController.dispose();
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
                  if (newValue == 'Sort by Product Name (A - Z)') {
                    sortCatalog('productName', 'ASC');
                    Navigator.pop(context);
                  } else if (newValue == 'Sort by Product Name (Z - A)') {
                    sortCatalog('productName', 'DESC');
                    Navigator.pop(context);
                  } else if (newValue == 'Sort by Product Price (Min - Max)') {
                    sortCatalog('price', 'ASC');
                    Navigator.pop(context);
                  } else {
                    sortCatalog('price', 'DESC');
                    Navigator.pop(context);
                  }
                  // selectedSortValue = newValue;
                  // print(selectedSortValue);
                },
              ),
            ],
          ),
        );
      },
    );
  }

  void filterCatalogByProductName(String productName) {
    print("FILTER BY PRODUCT NAME");

    String urlCatalog = "http://localhost:8082";
    String urlFilterByProductName =
        '$urlCatalog/api/catalog/view-all-by-name?name=$productName';

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

  void filterCatalogByProductPrice(int minPrice, int maxPrice) {
    print("FILTER BY PRODUCT PRICE");

    String urlCatalog = "http://localhost:8082";
    String urlFilterByProductPrice =
        '$urlCatalog/api/catalog/view-all-by-price?minPrice=$minPrice&maxPrice=$maxPrice';
    print(urlFilterByProductPrice);
    //Fetch API
    http.get(Uri.parse(urlFilterByProductPrice)).then((response) {
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

  void sortCatalog(String sortField, String sortDirection) {
    print("FILTER BY PRODUCT PRICE");

    String urlCatalog = "http://localhost:8082";
    String urlSort =
        '$urlCatalog/api/catalog/view-all-sort-by?sortField=$sortField&sortDirection=$sortDirection';
    print(urlSort);
    //Fetch API
    http.get(Uri.parse(urlSort)).then((response) {
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

  Future<void> fetchCatalog() async {
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
    var category;
    return Scaffold(
      body: ListView(
        children: [
          //CatalogBar,
          Container(
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
              ])),
          Container(
            //temp height
            //height: 500,
            padding: EdgeInsets.only(top: 15),
            decoration: BoxDecoration(
                color: Colors.grey,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(35),
                  topRight: Radius.circular(35),
                )),
            child: Column(
              children: [
                //Categories
                Container(
                  alignment: Alignment.centerLeft,
                  margin: EdgeInsets.symmetric(
                    vertical: 20,
                    horizontal: 10,
                  ),
                  child: Text(
                    "Categories",
                    style: TextStyle(
                      fontSize: 25,
                      fontWeight: FontWeight.bold,
                      color: Colors.black,
                    ),
                  ),
                ),
                Visibility(
                  visible: isWrappedVisible,
                  child: Container(
                    margin: EdgeInsets.symmetric(horizontal: 15),
                    child: Wrap(
                      spacing: 8.0,
                      runSpacing: 8.0,
                      children: [
                        for (var category in categoryNames)
                          Container(
                            padding: EdgeInsets.symmetric(
                                vertical: 5, horizontal: 10),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(20),
                            ),
                            child: Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Text("$category"),
                              ],
                            ),
                          ),
                      ],
                    ),
                  ),
                ),
                Container(
                  margin: EdgeInsets.symmetric(
                      horizontal: 10,
                      vertical: 5), // Adjust the margin as needed
                  child: Align(
                    alignment: Alignment.centerLeft,
                    child: ElevatedButton(
                      onPressed: () {
                        setState(() {
                          isWrappedVisible =
                              !isWrappedVisible; // Toggle the visibility
                        });
                      },
                      child:
                          Text(isWrappedVisible ? 'Close' : 'Product Category'),
                    ),
                  ),
                ),

                //Items
                Container(
                  alignment: Alignment.centerLeft,
                  margin: EdgeInsets.symmetric(vertical: 20, horizontal: 10),
                  child: Text(
                    "Catalog List",
                    style: TextStyle(
                      fontSize: 25,
                      fontWeight: FontWeight.bold,
                      color: Colors.black,
                    ),
                  ),
                ),

                //Items
                //ItemsWidget(),
                Scrollbar(
                  child: SingleChildScrollView(
                    child: GridView.count(
                      childAspectRatio: 0.68,
                      crossAxisCount: 2,
                      shrinkWrap: true,
                      children: [
                        for (var product in products)
                          Container(
                            padding:
                                EdgeInsets.only(left: 15, right: 15, top: 5),
                            margin: EdgeInsets.symmetric(
                                vertical: 3, horizontal: 10),
                            decoration: BoxDecoration(
                              color: Colors.white,
                              borderRadius: BorderRadius.circular(20),
                            ),
                            child: Column(
                              children: [
                                Row(
                                  mainAxisAlignment:
                                      MainAxisAlignment.spaceBetween,
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
                                      aspectRatio:
                                          1, // Set the aspect ratio as needed
                                      // child: Image.memory(
                                      //   base64Decode(product!['image']),
                                      //   fit: BoxFit
                                      //       .cover, // Adjust how the image fills the space
                                      // ),
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
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceBetween,
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
                ),
              ],
            ),
          )
        ],
      ),
    );
  }
}

enum _selectedTab { catalog, profile }
