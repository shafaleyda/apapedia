import 'package:flutter/material.dart';

class LandingPage extends StatefulWidget {
  const LandingPage({
    Key? key,
  }) : super(key: key);

  @override
  State<LandingPage> createState() => _LandingPageState();
}

class _LandingPageState extends State<LandingPage> {
  int pageIndex = 0;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Stack(
          children: [
            Positioned(
              child: Image.asset(
                'images/navbar-icon.png',
                width: 100,
                height: 100,
              ),
              top: MediaQuery.of(context).size.height / 2 - 50,
              left: MediaQuery.of(context).size.width / 2 - 50,
            ),
          ],
        ),
      ),
    );
  }
}
