package com.example.more.touch;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.example.common.util.ToastUtilKt;

import java.util.List;

public class AutoAccessibilityService extends AccessibilityService {

    private final static String TAG = "AutoAccessibilityService";

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        // 用户在设置中启动了服务
        Log.d(TAG, "onAccessibilityEvent: " + "用户启动了服务");
//        ToastUtilKt.showToast(this,"onServiceConnected");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // 服务被结束
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //performGlobalAction(GLOBAL_ACTION_LOCK_SCREEN);

//        //任意事件触发都会调用该函数，因此可以在此实现辅助功能代码。
//        String viewId = "com.ss.android.ugc.aweme:id/dxk"; //该id为点赞view的id。
//        AccessibilityNodeInfo node = searchNodeInfoByViewId(event, viewId);
//        //Log.d(TAG, "onAccessibilityEvent: " + node.getViewIdResourceName());
//        Point p = getPointByNode(node);
//        clickByNode(this, p.x, p.y);
//        Log.d(TAG, "onAccessibilityEvent: ");
        //点击事件才响应
        if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED){
            Log.d(TAG, "onAccessibilityEvent:点击事件 ");
        }
    }

    @Override
    public void onInterrupt() {
        // feedback 被打断

    }

    //根据veiwID拿到对应的节点信息。
     AccessibilityNodeInfo searchNodeInfoByViewId(AccessibilityEvent event, String viewId){
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo == null) return null;
        List<AccessibilityNodeInfo> nodeList = nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        if(nodeList == null){
            for(int i=0; i < nodeInfo.getChildCount(); ++i){
                //AccessibilityNodeInfo node = searchNodeInfoByViewId(nodeInfo.getChild(i) , viewId);
                AccessibilityNodeInfo node = nodeInfo.getChild(i);
                if(node != null){
                    return node;
                }
            }
        }else if(nodeList != null && nodeList.size() > 0){
            for(AccessibilityNodeInfo node : nodeList){
                if(node.isVisibleToUser()){ //搜索到可见的view则返回。
                    return node;
                }
            }
        }
        return null;
    }

    //根据节点信息可获得对应的x，y坐标
     Point getPointByNode(AccessibilityNodeInfo node){
        if (node == null){
            return new Point(0, 0);
        }
        Rect rect = new Rect();
        node.getBoundsInScreen(rect);
        //printNodeInfo(node, "- ");
        Point point = new Point(rect.centerX(), rect.centerY());
        return point;
    }

    //实现对（x，y）坐标进行点击操作。
     boolean clickByNode(AccessibilityService service, int x, int y) {
        if (service == null) {
            //AutoLog.e("clickByNode, service is NULL.");
            return false;
        }
        Point point = new Point(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path path = new Path();
        path.moveTo(point.x, point.y);
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0L, 200L));
        GestureDescription gesture = builder.build();

        boolean isDispatched = service.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                //AutoLog.d("dispatchGesture click onCompleted.");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                //AutoLog.d("dispatchGesture click onCancelled.");
            }
        }, null);

        return isDispatched;
    }

    private boolean scrollUp(AccessibilityService service, int center_X, int center_Y) {
        if (service == null) {
            //AutoLog.e("ScrolUp, service is NULL.");
            return false;
        }
        //AutoLog.d("center position:" + "(" + center_X + "," + center_Y + ")");
        final android.graphics.Path path = new Path();
        path.moveTo((int) (center_X), (int) (center_Y * 1.5)); //起点坐标。
        path.lineTo((int) (center_X), (int) (center_Y * 0.5)); //终点坐标。
        GestureDescription.Builder builder = new GestureDescription.Builder();

        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 800)
        ).build();

        boolean isDispatched = service.dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                //AutoLog.d("dispatchGesture ScrollUp onCompleted.");
                path.close();
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                //AutoLog.d("dispatchGesture ScrollUp cancel.");
            }
        }, null);

        return isDispatched;
    }


}