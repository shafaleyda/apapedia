class UserRequest {
  final String name;
  final String username;
  final String email;
  final String address;

  const UserRequest(
      {
        required this.name,
        required this.username,
        required this.email,
        required this.address,
      }
      );

  factory UserRequest.fromJson(Map<String, dynamic> json) {
    return UserRequest(
      name: json['name'],
      username: json['username'],
      email: json['email'],
      address: json['address'],
    );
  }
}
