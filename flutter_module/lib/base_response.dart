import 'package:json_annotation/json_annotation.dart';

part 'base_response.g.dart';

@JsonSerializable()
class APIBaseResponse {
  APIBaseResponse();

  int code;

  Map<String, dynamic> data;

  String msg;

  List<String> routers;

  factory APIBaseResponse.fromJson(Map<String, dynamic> json) => _$APIBaseResponseFromJson(json);
  Map<String, dynamic> toJson() => _$APIBaseResponseToJson(this);
}

@JsonSerializable()
class BaseResponseData {
  BaseResponseData();

  int yd;

  String showText;

  factory BaseResponseData.fromJson(Map<String, dynamic> json) => _$BaseResponseDataFromJson(json);
  Map<String, dynamic> toJson() => _$BaseResponseDataToJson(this);
}