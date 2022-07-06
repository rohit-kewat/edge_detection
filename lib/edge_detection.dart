import 'dart:async';
import 'dart:io';

import 'package:flutter/services.dart';

class EdgeDetection {
  static const MethodChannel _channel = const MethodChannel('edge_detection');

  static Future<String?> detectEdge(String imageFile) async {
    return await _channel
        .invokeMethod('edge_detect', <String, dynamic>{"file": imageFile});
  }
}
