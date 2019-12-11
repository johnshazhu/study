import 'package:flutter/material.dart';

class CustomAppBar extends AppBar {
  CustomAppBar({Key key, canPop = true, content, right = '', List<Widget> actionList}):
        super(key : key,
          automaticallyImplyLeading: canPop,
          backgroundColor: Color.fromARGB(0xFF, 0xFF, 0xFF, 0xFF),
          leading: IconButton(
            icon: const Icon(Icons.arrow_back_ios),
            color: Colors.black,
            onPressed: () {

            },
          ),
          centerTitle: true,
          title: Text(content,
              style: TextStyle(
                color: Color.fromARGB(0xFF, 0x0, 0x0, 0x0),
                fontSize: 16.0,
              ),
              textAlign: TextAlign.center),
          actions: actionList
      );
}