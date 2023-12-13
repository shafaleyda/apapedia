import 'package:flutter/material.dart';
import 'package:frontend_mobile/service/auth_service.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

// ignore: duplicate_import
import 'package:dot_navigation_bar/dot_navigation_bar.dart';

class CatalogHomeCustomer extends StatefulWidget {
  @override
  _CatalogHomeCustomerState createState() => _CatalogHomeCustomerState();
}

class _CatalogHomeCustomerState extends State<CatalogHomeCustomer> {
  List<dynamic> categories = [];
  List<dynamic> products = [];
  List<String> categoryNames = [];
  bool isWrappedVisible = false;
  TextEditingController productNameController = TextEditingController();
  TextEditingController minPriceController = TextEditingController();
  TextEditingController maxPriceController = TextEditingController();
  String? selectedSortValue;
  String urlCatalog = "http://localhost:8082";
  String urlOrder = "http://localhost:8080";
  String urlUser = "http://localhost:8081";

  bool productStockExist = false;

  @override
  void initState() {
    super.initState();
    _getToken();
    // fetchCategories();
    // fetchCatalog();
    isWrappedVisible = false;
  }

  Future<void> _getToken() async {
    AuthService authService = AuthService();
    String? token = await authService.getTokenFromStorage();

    if (token != null) {
      fetchCatalog();
      fetchCategories();
    } else {
      print("Token not found");
    }
  }

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
      enableDrag: true,
      builder: (BuildContext context) {
        return SingleChildScrollView(
          child: Container(
            padding: EdgeInsets.all(10),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Row(children: [
                  Text(
                    'Filter by Product Name',
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey,
                    ),
                  ),
                ]),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 4),
                  child: Row(
                    children: [
                      SizedBox(
                        width: MediaQuery.of(context).size.width * 0.7,
                        child: TextFormField(
                          controller: productNameController,
                          decoration: const InputDecoration(
                            border: OutlineInputBorder(),
                            labelText: "Product Name",
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 4),
                  child: Row(
                    children: [
                      ElevatedButton(
                        onPressed: () {
                          String productName = productNameController.text;
                          filterCatalogByProductName(productName);
                          Navigator.pop(context);
                          productNameController.clear();
                          //productNameController.dispose();
                        },
                        child: Text('Search'),
                      ),
                    ],
                  ),
                ),
                Divider(),
                Row(children: [
                  Text(
                    'Filter by Product Range Price',
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey,
                    ),
                  ),
                ]),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 4),
                  child: Row(
                    children: [
                      SizedBox(
                        width: MediaQuery.of(context).size.width * 0.7,
                        child: TextFormField(
                          controller: minPriceController,
                          decoration: const InputDecoration(
                            border: OutlineInputBorder(),
                            labelText: "Min Price",
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 4),
                  child: Row(
                    children: [
                      SizedBox(
                        width: MediaQuery.of(context).size.width * 0.7,
                        child: TextFormField(
                          controller: maxPriceController,
                          decoration: const InputDecoration(
                            border: OutlineInputBorder(),
                            labelText: "Max Price",
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  margin: EdgeInsets.symmetric(vertical: 4),
                  child: Row(
                    children: [
                      ElevatedButton(
                        onPressed: () {
                          String minPrice = minPriceController.text;
                          String maxPrice = maxPriceController.text;

                          int minPriceInt = int.parse(minPrice);
                          int maxPriceInt = int.parse(maxPrice);
                          filterCatalogByProductPrice(minPriceInt, maxPriceInt);
                          Navigator.pop(context);
                          minPriceController.clear();
                          maxPriceController.clear();
                        },
                        child: Text('Search'),
                      ),
                    ],
                  ),
                ),
                Divider(),
                Row(children: [
                  Text(
                    'Sort Product',
                    style: TextStyle(
                      fontSize: 15,
                      color: Colors.grey,
                    ),
                  ),
                ]),
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
                    } else if (newValue ==
                        'Sort by Product Price (Min - Max)') {
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
          ),
        );
      },
    );
  }

  void filterCatalogByProductName(String productName) {
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
    String urlFilterByProductPrice =
        '$urlCatalog/api/catalog/view-all-by-price?minPrice=$minPrice&maxPrice=$maxPrice';
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

  Future<String?> getUserId() async {
    final String apiUrl = '$urlUser/api/user/user-loggedin';

    try {
      final response = await http.get(Uri.parse(apiUrl));

      if (response.statusCode == 200) {
        final Map<String, dynamic> userData = json.decode(response.body);
        final String userId = userData['id'] as String;
        return userId;
      } else {
        // Handle other status codes
        print('Failed to fetch user data: ${response.statusCode}');
        return null;
      }
    } catch (error) {
      // Handle errors during the HTTP request
      print('Error fetching user data: $error');
      return null;
    }
  }

  // Future<bool> checkIfCartExists(String? userId) async {
  //   final String apiUrl =
  //       '$urlOrder/cart/customer/$userId'; //List<CartItemRest>
  //   print(apiUrl);
  //   try {
  //     final response = await http.get(Uri.parse(apiUrl));
  //     if (response.statusCode == 200) {
  //       if (idCart == null) {
  //         //Punya cart, tapi baru login
  //         final String urlGetCartId = '$urlOrder/cart/user/$userId';
  //         final cartIdFromApi = await http.get(Uri.parse(urlGetCartId));
  //         idCart = json.decode(cartIdFromApi.body) as String;
  //       }
  //       return true;
  //     } else if (response.statusCode == 404) {
  //       return false;
  //     } else {
  //       //Belum punya cart
  //       print('Failed to check cart existence: ${response.statusCode}');
  //       final String urlCreateCart = '$urlOrder/cart/create';
  //       final Map<String, dynamic> requestData = {
  //         'userId': userId,
  //       };

  //       try {
  //         final response = await http.post(
  //           Uri.parse(urlCreateCart),
  //           body: json.encode(requestData),
  //           headers: {
  //             'Content-Type': 'application/json',
  //           },
  //         );
  //         if (response.statusCode == 200) {
  //           print("BERHASIL CREATE CART");
  //           return true;
  //         } else {
  //           // Handle other status codes
  //           print('Failed to create cart: ${response.statusCode}');
  //           return false;
  //         }
  //       } catch (error) {
  //         print('Error create cart: $error');
  //         return false;
  //       }
  //     }
  //   } catch (error) {
  //     print('Error checking cart existence: $error');
  //     return false;
  //   }
  // }

  void addToCart(String idProduct) async {
    String? userId = await getUserId();
    print('User id plis $userId');
    final String urlGetCartId = '$urlOrder/cart/user/$userId';
    try {
      final cartIdFromApi = await http.get(Uri.parse(urlGetCartId));

      final Map<String, dynamic> cartData = json.decode(cartIdFromApi.body);
      final String idCart = cartData['id'] as String;

      //idCart = (json.decode(cartIdFromApi.body))['id'] as String;
      print('idCart:  $idCart');
      final String urlAddItemCart = '$urlOrder/cart/$idCart/add';
      final Map<String, dynamic> requestData = {
        'productId': idProduct,
        'quantity': 1,
      };

      try {
        final response = await http.post(
          Uri.parse(urlAddItemCart),
          body: json.encode(requestData),
          headers: {
            'Content-Type': 'application/json',
          },
        );

        if (response.statusCode == 200) {
          print('Added to cart successfully');
        } else {
          // Handle other status codes
          print('Failed to add to cart: ${response.statusCode}');
        }
      } catch (error) {
        // Handle errors during the HTTP request
        print('Error adding to cart: $error');
      }
      print('User ID: $userId');
    } catch (error) {
      print('disini $error');
    }
  }

  Future<bool> checkStock(String idProduct) async {
    String urlGetProduct = '$urlCatalog/api/catalog/$idProduct';
    print(urlGetProduct);

    try {
      final response = await http.get(Uri.parse(urlGetProduct));

      if (response.statusCode == 200) {
        Map<String, dynamic> productData = json.decode(response.body);
        int productStock = productData['stock'];

        print("Product stock $productStock");

        if (productStock <= 0) {
          return false; // Product out of stock
        } else {
          return true; // Product in stock
        }
      } else {
        print('Failed to load products: ${response.statusCode}');
        return false; // Return false for any other status code
      }
    } catch (error) {
      // Handle any error that might occur during the HTTP request
      print('Error loading products: $error');
      return false; // Return false if an error occurs
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
                  padding: EdgeInsets.only(left: 10),
                  child: Text(
                    "Catalog Page",
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
          SizedBox(height: 5),
          Container(
            //temp height
            //height: 500,
            padding: EdgeInsets.only(top: 15),
            decoration: BoxDecoration(
                color: Color.fromARGB(208, 255, 237, 210),
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
                      color: Colors.grey,
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
                      color: Colors.grey,
                    ),
                  ),
                ),

                //Items
                Scrollbar(
                  child: SingleChildScrollView(
                    child: GridView.count(
                      childAspectRatio: 0.68,
                      crossAxisCount: 2,
                      shrinkWrap: true,
                      children: [
                        for (var product in products)
                          GestureDetector(
                            onTap: () {
                              Navigator.pushNamed(context, '/productDetail',
                                  arguments: product);
                            },
                            child: Container(
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
                                      ElevatedButton(
                                        onPressed: () {
                                          Navigator.pushNamed(
                                              context, '/confirmOrder',
                                              arguments: product['idCatalog']
                                                  as String);
                                        },
                                        style: ElevatedButton.styleFrom(
                                          padding: EdgeInsets.all(5),
                                          primary: Color(0xFF4C53A5),
                                          shape: RoundedRectangleBorder(
                                            borderRadius:
                                                BorderRadius.circular(20),
                                          ),
                                        ),
                                        child: Text(
                                          "Order Now",
                                          style: TextStyle(
                                            fontSize: 14,
                                            color: Colors.white,
                                            fontWeight: FontWeight.bold,
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                  InkWell(
                                    onTap: () {},
                                    child: Container(
                                      margin: EdgeInsets.all(10),
                                      child: AspectRatio(
                                        aspectRatio:
                                            1, // Set the aspect ratio as needed
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
                                  Row(
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
                                      GestureDetector(
                                        onTap: () async {
                                          bool isStockAvailable =
                                              await checkStock(
                                                  product['idCatalog']);
                                          if (isStockAvailable) {
                                            addToCart(
                                                product['idCatalog'] as String);
                                            ScaffoldMessenger.of(context)
                                                .showSnackBar(
                                              SnackBar(
                                                content: Text('Added to cart'),
                                                duration: Duration(seconds: 2),
                                              ),
                                            );
                                          } else {
                                            ScaffoldMessenger.of(context)
                                                .showSnackBar(
                                              SnackBar(
                                                content: Text(
                                                    'Product stock is limited.'),
                                                duration: Duration(seconds: 2),
                                              ),
                                            );
                                          }
                                        },
                                        child: Icon(
                                          Icons.shopping_cart_checkout,
                                          color: Colors.red,
                                        ),
                                      ),
                                    ],
                                  )
                                ],
                              ),
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
