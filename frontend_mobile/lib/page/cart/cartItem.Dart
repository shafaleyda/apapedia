// To parse this JSON data, do
//
//     final cartItem = cartItemFromJson(jsonString);

import 'dart:convert';

List<CartItem> cartItemFromJson(String str) => List<CartItem>.from(json.decode(str).map((x) => CartItem.fromJson(x)));

String cartItemToJson(List<CartItem> data) => json.encode(List<dynamic>.from(data.map((x) => x.toJson())));

class CartItem {
    String id;
    String productId;
    String seller;
    int price;
    String productName;
    String productDescription;
    int categoryId;
    String categoryName;
    int stock;
    String image;
    bool isDeleted;
    int quantity;

    CartItem({
        required this.id,
        required this.productId,
        required this.seller,
        required this.price,
        required this.productName,
        required this.productDescription,
        required this.categoryId,
        required this.categoryName,
        required this.stock,
        required this.image,
        required this.isDeleted,
        required this.quantity,
    });

    factory CartItem.fromJson(Map<String, dynamic> json) => CartItem(
        id: json["id"],
        productId: json["productId"],
        seller: json["seller"],
        price: json["price"],
        productName: json["productName"],
        productDescription: json["productDescription"],
        categoryId: json["categoryId"],
        categoryName: json["categoryName"],
        stock: json["stock"],
        image: json["image"],
        isDeleted: json["isDeleted"],
        quantity: json["quantity"],
    );

    Map<String, dynamic> toJson() => {
        "id": id,
        "productId": productId,
        "seller": seller,
        "price": price,
        "productName": productName,
        "productDescription": productDescription,
        "categoryId": categoryId,
        "categoryName": categoryName,
        "stock": stock,
        "image": image,
        "isDeleted": isDeleted,
        "quantity": quantity,
    };
}
