
class APIBaseResponse<T extends BaseResponseData> {
  APIBaseResponse();

  int code;

  T data;

  String msg;

  List routers;
}

class BaseResponseData {
  BaseResponseData();

  int yd;

  String showText;
}