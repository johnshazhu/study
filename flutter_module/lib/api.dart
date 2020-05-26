import 'dart:convert';
import 'dart:io';

import 'package:device_info/device_info.dart';
import 'package:dio/adapter.dart';
import 'package:dio/dio.dart';
import 'package:flutter/material.dart';
import 'package:flutter_module/app.dart';
import 'package:flutter_module/base.dart';
import 'package:flutter_module/base_response.dart';
import 'package:json_annotation/json_annotation.dart';
import 'package:uuid/uuid.dart';

part 'api.g.dart';

@JsonSerializable()
class GetCourseActivityInfo extends APIBaseRequest {
  GetCourseActivityInfo();

  String courseActivityId = "401018663982297088";
  int sourceType = 1;
  int autoUnlock = 1;//是否需要自动解锁 0 否 1 是

  factory GetCourseActivityInfo.fromJson(Map<String, dynamic> json) => _$GetCourseActivityInfoFromJson(json);
  Map<String, dynamic> toJson() => _$GetCourseActivityInfoToJson(this);
}

@JsonSerializable()
class GetCourseActivityInfoRsp extends APIBaseResponse {
  GetCourseActivityInfoRsp();

  factory GetCourseActivityInfoRsp.fromJson(Map<String, dynamic> json) => _$GetCourseActivityInfoRspFromJson(json);
  Map<String, dynamic> toJson() => _$GetCourseActivityInfoRspToJson(this);
}

@JsonSerializable()
class GetCourseActivityInfoData extends BaseResponseData {
  GetCourseActivityInfoData();

  CourseActivityInfo courseActivityInfo;

  factory GetCourseActivityInfoData.fromJson(Map<String, dynamic> json) => _$GetCourseActivityInfoDataFromJson(json);
  Map<String, dynamic> toJson() => _$GetCourseActivityInfoDataToJson(this);
}

@JsonSerializable()
class CourseActivityInfo {
  CourseActivityInfo();

  String courseActivityId;

  String activityName;

  String coverImg;

  int courseType;

  String courseId;

  String courseCode;

  String courseName;

  int regDay;

  String receivedText;

  String unccalimedText;

  String lockCondition;

  String disqualificationText;

  String unlockBackTip;

  String unlockBackTip1;

  String unlockBackTip2;

  String backTip;

  String shareTitle;

  String shareText;

  String vipChildChannel;

  String vipBuyText;

  String title;

  String content;

  String vipBuySkipModel;

  String shareLinkUrl;

  String courseRemark;

  String unaccalimedBackTip;

  String unaccalimedBackText;

  String receivedBackTip;

  String receivedBackText;

  String firstDayText;

  String nonFirstDayText;

  factory CourseActivityInfo.fromJson(Map<String, dynamic> json) => _$CourseActivityInfoFromJson(json);
  Map<String, dynamic> toJson() => _$CourseActivityInfoToJson(this);
}

Future request(BuildContext context, String url, String deviceJson, Function successCallback) async {
  //"https://yxyapi2.drcuiyutao.com/yxy-vip-gateway/api/json/courseActivity/getCourseActivityInfo"
  try {
    if (url == null) {
      url = "https://yxyapi2.com.study.com/yxy-vip-gateway/api/json/courseActivity/getCourseActivityInfo";
    }

    print(deviceJson);
    Map<String, dynamic> map = json.decode(deviceJson);
    AppDevice appDevice = AppDevice.fromJson(map);
    if (Platform.isAndroid) {
      //DeviceInfoPlugin().androidInfo.then((deviceInfo) => outputDevice(deviceInfo));
    }
    GetCourseActivityInfo req = GetCourseActivityInfo();
    req.appDevice = appDevice;
    req.childId = "408203771897786368";
    req.traceLogId = Uuid().v1();
    print("xdebug uuid : " + req.traceLogId);
    Dio dio = Dio();
    (dio.httpClientAdapter as DefaultHttpClientAdapter).onHttpClientCreate =
        (HttpClient client) {
      client.findProxy = (uri) {
        //proxy all request to localhost:8888
        /*return HttpClient.findProxyFromEnvironment(uri, environment: {
          "http_proxy": "192.168.211.36:8888",
          "https_proxy": "192.168.211.36:8888",
        });*/
        return "PROXY 192.168.211.36:8888";
      };
      client.badCertificateCallback =
          (X509Certificate cert, String host, int port) => true;
    };
    dio.options
      ..baseUrl = "https://yxyapi2.com.study.com/yxy-vip-gateway/api/json/"
      ..connectTimeout = 5000
      ..receiveTimeout = 5000;

    String body = json.encode(req);
    print("xdebug request star");
    Response response = await dio.post("courseActivity/getCourseActivityInfo",
        data: {
          "body": body
        },
        options: Options(
          contentType: Headers.formUrlEncodedContentType,
        ));
    Map<String, dynamic> m = json.decode(response.data);
    if (successCallback != null) {
      successCallback(m);
    }
  } catch (e) {
    print("xdebug request exception");
    print(e);
  }
}

void outputDevice(AndroidDeviceInfo deviceInfo) {
  print(deviceInfo.version.sdkInt);
  print(deviceInfo.version.codename);
  print(deviceInfo.version.baseOS);
  print(deviceInfo.version.release);

  print(deviceInfo.device);
  print(deviceInfo.board);
  print(deviceInfo.brand);
  print(deviceInfo.fingerprint);
  print(deviceInfo.display);
  print(deviceInfo.id);
  print(deviceInfo.manufacturer);
  print(deviceInfo.model);
}