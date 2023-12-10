import 'package:flutter/material.dart';

class ItemsWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scrollbar(
      child: SingleChildScrollView(
        child: GridView.count(
          childAspectRatio: 0.68,
          
          crossAxisCount: 2,
          shrinkWrap: true,
          children: [
            //Nanti ganti 
            for(int i=0; i<10; i++)
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
                      child: Image.asset(
                        "images/image1.png",
                        height: 120,
                        width: 120,
                      ),
                    ),
                  ),
                  Container(
                    padding: EdgeInsets.only(bottom: 8),
                    alignment: Alignment.centerLeft,
                    child: Text("Product Title",
                        style: TextStyle(
                          fontSize: 18,
                          color: Color(0xFF4C53A5),
                          fontWeight: FontWeight.bold,
                        )),
                  ),
                  Container(
                    alignment: Alignment.centerLeft,
                    child: Text(
                      "Product Description",
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
                            "Harga Produk",
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
