import 'dart:io';
import 'dart:convert';
import 'package:convert/convert.dart';
import 'package:crypto/crypto.dart';

import 'package:flutter_module/base.dart';

String genSign(APIBaseRequest request) {
  if (request != null && request.appDevice != null) {
    StringBuffer buffer = StringBuffer();
    buffer.write("deviceno=");
    buffer.write(systemEncoding.encode(request.appDevice.deviceNo));
    buffer.write("&t=");
    buffer.write(request.appDevice.t.toString());
    buffer.write("|nauyeuxvy");

    String data = buffer.toString();
    print(data);
    String md5 = generateMd5(data);
    print("md5 : $md5");
    return md5;
  }

  return null;
}

String generateMd5(String data) {
  var content = new Utf8Encoder().convert(data);
  var digest = md5.convert(content);
  // 这里其实就是 digest.toString()
  return hex.encode(digest.bytes);
}