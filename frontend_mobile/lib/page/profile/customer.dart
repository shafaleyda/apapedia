class Customer {
  // final String nama;
  final String username;
  final String email;
  final int saldo;

  const Customer(
      {
        // required this.nama,
        required this.username,
        required this.email,
        required this.saldo,
      }
      );

  factory Customer.fromJson(Map<String, dynamic> json) {
    return Customer(
      // nama: json['nama'],
        username: json['username'],
        email: json['email'],
        saldo: json['saldo']
    );
  }
}
