import 'package:flutter/material.dart';
import 'package:badges/badges.dart' as badges;

class CatalogAppBar extends StatelessWidget {
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
              onTap: () {},
              child: Icon(
                Icons.sort_outlined,
                size: 32,
                color: Color(0xFF4C53A5),
              ),
            ),
        ]));
  }
}
