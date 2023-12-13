import 'package:flutter/material.dart';

class catalogDetail extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final String? idCatalog = ModalRoute.of(context)!.settings.arguments as String?;

    // Use the 'idCatalog' (UUID) to perform actions or display details related to the catalog ID
    // Example: fetchCatalogDetails(idCatalog);

    return Scaffold(
      appBar: AppBar(
        title: Text('Catalog Detail'),
      ),
      body: Center(
        child: Text('Catalog ID: $idCatalog'), // Display the catalog ID (UUID)
      ),
    );
  }
}
