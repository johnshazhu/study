import 'dart:async';
import 'dart:convert';
import 'dart:developer';

import 'package:extended_nested_scroll_view/extended_nested_scroll_view.dart';
import 'package:flutter/material.dart';
import 'package:extended_nested_scroll_view/extended_nested_scroll_view.dart' as extended;
import 'package:flutter/services.dart';
import 'package:flutter_module/appbar.dart';
import 'package:video_player/video_player.dart';
import 'package:webview_flutter/webview_flutter.dart';

class DoctorVCourse extends StatefulWidget {
  final _title;

  DoctorVCourse(this._title);

  @override
  DoctorVCourseState createState() => DoctorVCourseState(this._title);
}

class DoctorVCourseState extends State<DoctorVCourse> {
  final _title;
  bool _isInit = false;
  double _expandedHeight = 450.0;
  double _webviewHeight = 320.0;
  bool _isExpanded = false;
  double width = 0.0;
  final GlobalKey globalKey = GlobalKey();
  final _tabs = [
    '课程列表',
    '本期互动',
  ] ;

  VideoPlayerController controller = VideoPlayerController.asset(
    'videos/butterfly.mp4',
    package: 'flutter_gallery_assets',
  );

  DoctorVCourseState(this._title);

  @override
  void initState() {
    super.initState();
    /*controller.initialize().then((value){
      ///调用setState方法重绘UI
      setState(() {
        ///判断是否初始化
        _isInit = controller.value.initialized;
      });
    });*/
  }

  @override
  Widget build(BuildContext context) {
    final size = MediaQuery.of(context).size;
    final width = size.width;
    final height = size.height;
    print('build $width, $height');
    this.width = size.width;
    return Scaffold(
      appBar: CustomAppBar(
          canPop: true,
          content: this._title,
          actionList: <Widget>[
            IconButton(
              icon: const Icon(Icons.share),
              color: Colors.black,
              onPressed: () {

              },
            )]
      ),
      body: _buildNestedScrollView()
    );
  }

  Future<Null> onRefresh1() {
    final Completer<Null> completer = new Completer<Null>();
    new Timer(const Duration(seconds: 1), () {
      completer.complete(null);
    });
    return completer.future.then((_) {});
  }

  Widget _buildInitWidget(){
    return AspectRatio(
      aspectRatio: 3 / 2,
      child: Stack(
        children: <Widget>[
          VideoPlayer(controller),
          const Center(child: CircularProgressIndicator()),
        ],
        fit: StackFit.expand,
      ),
    );
  }

  Widget _buildReadyWidget(){
    return AspectRatio(
      aspectRatio: 3 / 2,
      child: Stack(
        children: <Widget>[
          VideoPlayer(controller),
        ],
        fit: StackFit.expand,
      ),
    );
  }

  double _getExpandedHeight() {
    double height = Size.fromHeight(48).height + _webviewHeight + Size.fromHeight(56).height + Size.fromHeight(36).height;
    print('expanded h : $height, wh : $_webviewHeight');
    return height;
  }

  double _getWebViewHeight() {
    return _webviewHeight;
  }

  void updateHeight() {
    setState(() {
      print('updateHeight _isExpanded is $_isExpanded');
    });
  }

  Widget _buildLayoutBuilder(double h) {
    return Container(
      width: width,
      height: h,
      child: WebView(
        initialUrl: 'https://flutter-io.cn/',
        javascriptMode: JavascriptMode.unrestricted,
        onWebViewCreated: (webViewController) {
          //_loadHtmlFromAssets(webViewController, null);
        },
        onPageFinished: (url) {
          _webviewHeight = context.size.height;
          print('onPageFinished url $url, h : ${context.size.height}');
          //updateHeight();
        },
      ),
    );
  }

  _loadHtmlFromAssets(WebViewController _controller, String path) async {
    String fileText = await rootBundle.loadString(path);
    _loadHtmlText(_controller, fileText);
  }

  _loadHtmlText(WebViewController _controller, String content) async {
    _controller.loadUrl(Uri.dataFromString(
        content,
        mimeType: 'text/html',
        encoding: Encoding.getByName('utf-8')
    ).toString());
  }

  Widget _buildTestNestedScrollView() {
    return DefaultTabController(
      length: _tabs.length,

      child: NestedScrollViewRefreshIndicator(
        onRefresh: onRefresh1,
        child: extended.NestedScrollView(
          physics: const AlwaysScrollableScrollPhysics(),
          headerSliverBuilder: (context, innerBoxIsScrolled) {
            return <Widget>[
              SliverOverlapAbsorber(
                // This widget takes the overlapping behavior of the SliverAppBar,
                // and redirects it to the SliverOverlapInjector below. If it is
                // missing, then it is possible for the nested "inner" scroll view
                // below to end up under the SliverAppBar even when the inner
                // scroll view thinks it has not been scrolled.
                // This is not necessary if the "headerSliverBuilder" only builds
                // widgets that do not overlap the next sliver.
                handle: extended.NestedScrollView.sliverOverlapAbsorberHandleFor(context),
                child: SliverAppBar(
                  //title: const Text('Books'), // This is the title in the app bar.
                    backgroundColor: Color.fromARGB(0xFF, 0xFF, 0xFF, 0xFF),
                    flexibleSpace: Column(
                      children: <Widget>[
                        Padding(
                          padding: const EdgeInsets.fromLTRB(0, 20, 0, 0),
                          child: Text(
                            '新用户注册7天内，天天免费打开1个育儿锦囊',
                            textAlign: TextAlign.center,
                            style: TextStyle(
                                fontSize: 14.0,
                                color: Color.fromARGB(0xFF, 0x64, 0x64, 0x64)
                            ),
                          ),
                        ),
/*                        Padding(
                          padding: const EdgeInsets.fromLTRB(0, 20, 0, 0),
                          child: GestureDetector(
                            onTap: () {
                              if (controller.value.isPlaying) {
                                controller.pause();
                              } else {
                                controller.play();
                              }
                            },
                            child: _isInit ? _buildReadyWidget() : _buildInitWidget()),
                        ),*/
                        LayoutBuilder(
                          builder: (context, constraints) {
                            print('LayoutBuilder maxw : ${constraints.maxWidth}, maxh : ${constraints.maxHeight}');
                            return _buildLayoutBuilder(_getWebViewHeight());
                          },
                        ),
                        GestureDetector(
                          onTap: () {
                            _isExpanded = !_isExpanded;
                            updateHeight();
                          },
                          child: Padding(
                            padding: const EdgeInsets.fromLTRB(0, 20, 20, 0),
                            child: Text(
                              _isExpanded ? '收起' : '展开',
                              textAlign: TextAlign.right,
                              style: TextStyle(
                                  fontSize: 14.0,
                                  color: Color.fromARGB(0xFF, 0x64, 0x64, 0x64)
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                    pinned: true,
                    expandedHeight: _getExpandedHeight(),
                    // The "forceElevated" property causes the SliverAppBar to show
                    // a shadow. The "innerBoxIsScrolled" parameter is true when the
                    // inner scroll view is scrolled beyond its "zero" point, i.e.
                    // when it appears to be scrolled below the SliverAppBar.
                    // Without this, there are cases where the shadow would appear
                    // or not appear inappropriately, because the SliverAppBar is
                    // not actually aware of the precise position of the inner
                    // scroll views.
                    forceElevated: innerBoxIsScrolled,
                    bottom: PreferredSize(
                      preferredSize: Size.fromHeight(48),
                      child: Material(
                        color: Colors.teal,
                        child: TabBar(
                          // These are the widgets to put in each tab in the tab bar.
                          tabs: _tabs.map((String name) => Tab(text: name)).toList(),
                        ),
                      ),
                    )
                ),
              ),
            ];
          },
          body: TabBarView(
            // These are the contents of the tab views, below the tabs.
            children: _tabs.map((String name) {
              return SafeArea(
                top: false,
                bottom: false,
                child: Builder(
                  // This Builder is needed to provide a BuildContext that is "inside"
                  // the NestedScrollView, so that sliverOverlapAbsorberHandleFor() can
                  // find the NestedScrollView.
                  builder: (BuildContext context) {
                    return CustomScrollView(
                      // The "controller" and "primary" members should be left
                      // unset, so that the NestedScrollView can control this
                      // inner scroll view.
                      // If the "controller" property is set, then this scroll
                      // view will not be associated with the NestedScrollView.
                      // The PageStorageKey should be unique to this ScrollView;
                      // it allows the list to remember its scroll position when
                      // the tab view is not on the screen.
                      key: PageStorageKey<String>(name),
                      slivers: <Widget>[
                        SliverOverlapInjector(
                          // This is the flip side of the SliverOverlapAbsorber above.
                          handle: extended.NestedScrollView.sliverOverlapAbsorberHandleFor(context),
                        ),
                        SliverPadding(
                          padding: const EdgeInsets.all(8.0),
                          // In this example, the inner scroll view has
                          // fixed-height list items, hence the use of
                          // SliverFixedExtentList. However, one could use any
                          // sliver widget here, e.g. SliverList or SliverGrid.
                          sliver: SliverFixedExtentList(
                            // The items in this example are fixed to 48 pixels
                            // high. This matches the Material Design spec for
                            // ListTile widgets.
                            itemExtent: 48.0,
                            delegate: SliverChildBuilderDelegate(
                                  (BuildContext context, int index) {
                                // This builder is called for each child.
                                // In this example, we just number each list item.
                                return ListTile(
                                  title: Text('Item $index'),
                                );
                              },
                              // The childCount of the SliverChildBuilderDelegate
                              // specifies how many children this inner list
                              // has. In this example, each tab has a list of
                              // exactly 30 items, but this is arbitrary.
                              childCount: 30,
                            ),
                          ),
                        ),
                      ],
                    );
                  },
                ),
              );
            }).toList(),
          ),
        ),
      ),
    );
  }

  Widget _buildNestedScrollView() {
    return DefaultTabController(
      length: _tabs.length, // This is the number of tabs.
      child: extended.NestedScrollView(
        headerSliverBuilder: (BuildContext context, bool innerBoxIsScrolled) {
          // These are the slivers that show up in the "outer" scroll view.
          return <Widget>[
            SliverOverlapAbsorber(
              // This widget takes the overlapping behavior of the SliverAppBar,
              // and redirects it to the SliverOverlapInjector below. If it is
              // missing, then it is possible for the nested "inner" scroll view
              // below to end up under the SliverAppBar even when the inner
              // scroll view thinks it has not been scrolled.
              // This is not necessary if the "headerSliverBuilder" only builds
              // widgets that do not overlap the next sliver.
              handle: extended.NestedScrollView.sliverOverlapAbsorberHandleFor(context),
              child: SliverAppBar(
                title: const Text('Books'), // This is the title in the app bar.
                flexibleSpace: Container(
                  child: Padding(
                    padding: const EdgeInsets.fromLTRB(0, 220, 0, 0),
                    child: Text(
                      '新用户注册7天内，天天免费打开1个育儿锦囊',
                      textAlign: TextAlign.center,
                      style: TextStyle(
                          fontSize: 14.0,
                          color: Color.fromARGB(0xFF, 0x64, 0x64, 0x64)
                      ),
                    ),
                  ),
                ),
                pinned: true,
                expandedHeight: 450.0,
                // The "forceElevated" property causes the SliverAppBar to show
                // a shadow. The "innerBoxIsScrolled" parameter is true when the
                // inner scroll view is scrolled beyond its "zero" point, i.e.
                // when it appears to be scrolled below the SliverAppBar.
                // Without this, there are cases where the shadow would appear
                // or not appear inappropriately, because the SliverAppBar is
                // not actually aware of the precise position of the inner
                // scroll views.
                forceElevated: innerBoxIsScrolled,
                bottom: TabBar(
                  // These are the widgets to put in each tab in the tab bar.
                  tabs: _tabs.map((String name) => Tab(text: name)).toList(),
                ),
              ),
            ),
          ];
        },
        body: TabBarView(
          // These are the contents of the tab views, below the tabs.
          children: _tabs.map((String name) {
            return SafeArea(
              top: false,
              bottom: false,
              child: Builder(
                // This Builder is needed to provide a BuildContext that is "inside"
                // the NestedScrollView, so that sliverOverlapAbsorberHandleFor() can
                // find the NestedScrollView.
                builder: (BuildContext context) {
                  return CustomScrollView(
                    // The "controller" and "primary" members should be left
                    // unset, so that the NestedScrollView can control this
                    // inner scroll view.
                    // If the "controller" property is set, then this scroll
                    // view will not be associated with the NestedScrollView.
                    // The PageStorageKey should be unique to this ScrollView;
                    // it allows the list to remember its scroll position when
                    // the tab view is not on the screen.
                    key: PageStorageKey<String>(name),
                    slivers: <Widget>[
                      SliverOverlapInjector(
                        // This is the flip side of the SliverOverlapAbsorber above.
                        handle: extended.NestedScrollView.sliverOverlapAbsorberHandleFor(context),
                      ),
                      SliverPadding(
                        padding: const EdgeInsets.all(8.0),
                        // In this example, the inner scroll view has
                        // fixed-height list items, hence the use of
                        // SliverFixedExtentList. However, one could use any
                        // sliver widget here, e.g. SliverList or SliverGrid.
                        sliver: SliverFixedExtentList(
                          // The items in this example are fixed to 48 pixels
                          // high. This matches the Material Design spec for
                          // ListTile widgets.
                          itemExtent: 48.0,
                          delegate: SliverChildBuilderDelegate(
                                (BuildContext context, int index) {
                              // This builder is called for each child.
                              // In this example, we just number each list item.
                              return ListTile(
                                title: Text('Item $index'),
                              );
                            },
                            // The childCount of the SliverChildBuilderDelegate
                            // specifies how many children this inner list
                            // has. In this example, each tab has a list of
                            // exactly 30 items, but this is arbitrary.
                            childCount: name == '课程列表' ? 30 : 10,
                          ),
                        ),
                      ),
                    ],
                  );
                },
              ),
            );
          }).toList(),
        ),
      ),
    );
  }
}