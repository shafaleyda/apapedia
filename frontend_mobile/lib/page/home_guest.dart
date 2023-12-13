import 'package:flutter/material.dart';
import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:frontend_mobile/common/cookie_request.dart';
import 'package:frontend_mobile/main.dart';
import 'package:frontend_mobile/page/catalogue/catalogHome.dart';
import 'package:frontend_mobile/page/home.dart';
import 'package:frontend_mobile/page/landingPage.dart';
import 'package:frontend_mobile/page/order-history/order-history.dart';
import 'package:frontend_mobile/page/profile/customer.dart';
import 'package:frontend_mobile/page/profile/profile.dart';

class GuestHomePage extends StatefulWidget {
  const GuestHomePage({
    Key? key,
  }) : super(key: key);

  @override
  State<GuestHomePage> createState() => _GuestHomePageState();
}

class _GuestHomePageState extends State<GuestHomePage> {
  int pageIndex = 0;
  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    final page = [
      CatalogHome(),
    ];
    return Scaffold(
      body: page[pageIndex],
      extendBody: true,
      bottomNavigationBar: CurvedNavigationBar(
        backgroundColor: Colors.transparent,
        color: Color.fromARGB(208, 255, 237, 210),
        items: <Widget>[
          Image.asset(
            'images/navbar-icon.png',
            width: 30,
            height: 30,
          ),
        ],
        animationCurve: Curves.easeInOut,
        animationDuration: Duration(milliseconds: 500),
        onTap: (index) {
          setState(() {
            pageIndex = index;
          });
        },
      ),
    );
  }
}