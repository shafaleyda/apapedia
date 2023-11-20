import 'package:intl/intl.dart';

class CustomDateFormat {
  static DateFormat dateFormat = DateFormat("dd-MM-yyyy HH:mm", 'en');

  static String format(DateTime dateTime) {
    return dateFormat.format(dateTime).toString();
  }
}
