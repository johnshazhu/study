import 'package:flutter/material.dart';

class ClickableText extends State<CustomText> {
  final String _text;

  ClickableText(this._text);

  @override
  Widget build(BuildContext context) {
    return new Column(
      children: <Widget>[
        GestureDetector(
          onTap: _onTapped,
          child: Text(
            _text,
            textAlign: TextAlign.center,
            style: TextStyle(
                color: Color.fromARGB(0xFF, 0xFF, 0xFF, 0xFF),
                fontSize: 16.0
            ),
          ),
        ),
      ],
    );
  }

  void _onTapped() {

  }
}

class CustomText extends StatefulWidget {
  final String _text;

  CustomText(this._text);

  @override
  State<StatefulWidget> createState() {
    return ClickableText(_text);
  }
}