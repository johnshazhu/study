import 'package:flutter_module/app.dart';
import 'package:json_annotation/json_annotation.dart';
part 'base.g.dart';

@JsonSerializable()
class APIBaseRequest {
  APIBaseRequest();

  AppDevice appDevice;

  String childId;

  String memberId2;

  String traceLogId;

  int cts = -1;

  int uid = 0;

  factory APIBaseRequest.fromJson(Map<String, dynamic> json) => _$APIBaseRequestFromJson(json);
  Map<String, dynamic> toJson() => _$APIBaseRequestToJson(this);
}