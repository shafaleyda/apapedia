class ProfileResponse {
  final String id;
  final String name;
  final String username;
  final String email;
  final String address;
  final int balance;

  const ProfileResponse(
      {
        required this.id,
        required this.name,
        required this.username,
        required this.email,
        required this.address,
        required this.balance,
      }
      );

  factory ProfileResponse.fromJson(Map<String, dynamic> json) {
    return ProfileResponse(
        id: json['id'],
        name: json['name'],
        username: json['username'],
        email: json['email'],
        address: json['address'],
        balance: json['balance']
    );
  }
}
