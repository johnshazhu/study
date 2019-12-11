import 'package:json_annotation/json_annotation.dart';
part 'app.g.dart';

@JsonSerializable()
class AppDevice {
  AppDevice(

  );

  int abType;

  String accountId;

  @JsonKey(name: 'android_id')
  String androidId;

  @JsonKey(name: 'appcode')
  String appCode = 'YUXY';

  String birthday;

  @JsonKey(name: 'build_serial')
  String buildSerial;

  String channel;

  @JsonKey(name: 'clientversion')
  String clientVersion;

  @JsonKey(name: 'deviceno')
  String deviceNo;

  @JsonKey(name: 'deviceuuid')
  String deviceUuid;

  String expectedDate;

  @JsonKey(name: 'hgestation')
  int gestation;

  String imei;

  int loginType;

  @JsonKey(name: 'ostype')
  int osType = 2;

  @JsonKey(name: 'osversion')
  String osVersion;

  String packageName;

  int prematureOpen;

  String sign;

  int t;

  String token;

  int userID;

  @JsonKey(name: 'yxyskin')
  int yxySkin;

  factory AppDevice.fromJson(Map<String, dynamic> json) => _$AppDeviceFromJson(json);
  Map<String, dynamic> toJson() => _$AppDeviceToJson(this);
}