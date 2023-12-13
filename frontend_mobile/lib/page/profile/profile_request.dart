class UserRequest {
  final String name;
  final String username;
  final String email;
  final String address;
  final String password;


  const UserRequest(
      {
        required this.name,
        required this.username,
        required this.email,
        required this.address,
        // TODO
        required this.password,

      }
      );

  factory UserRequest.fromJson(Map<String, dynamic> json) {
    return UserRequest(
      name: json['name'],
      username: json['username'],
      email: json['email'],
      address: json['address'],
      password: json['password'],

    );
  }
}
