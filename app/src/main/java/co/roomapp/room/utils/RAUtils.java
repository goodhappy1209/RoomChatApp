package co.roomapp.room.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import org.apache.commons.validator.routines.UrlValidator;
import org.xmlpull.v1.XmlPullParser;
import org.xmpp.packet.IQ;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import co.roomapp.room.application.RoomApplication;
import co.roomapp.room.model.RAAttachment;

import android.graphics.Path;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;
import android.opengl.Matrix;
/**
 * Created by drottemberg on 9/16/14.
 */
public class RAUtils {

    public static String encrypt(String string) throws UnsupportedEncodingException, Exception {
        Cipher c = new Cipher("Prout2014Prout2014");
        //return new String(c.encrypt(string.getBytes("UTF-8")),"UTF-8");
        return Base64.encodeBytes(c.encrypt(string.getBytes("UTF-8")));
    }

    public static String decrypt(String string) throws UnsupportedEncodingException, Exception {
        Cipher c = new Cipher("Prout2014Prout2014");
        byte[] b = Base64.decode(string);
        byte[] r  = c.decrypt(b);
        String ns = new String(r);
        return ns;
    }

    public static String encryptRemote(String string) throws UnsupportedEncodingException, Exception {
        Cipher c = new Cipher("1GI4*h.kKH5H+m@'E1*uc3`N}!Pq{,W{O20<C#i'ddfBo5hL}3RsW$%TZAr<R+X");
        //return new String(c.encrypt(string.getBytes("UTF-8")),"UTF-8");
        return Base64.encodeBytes(c.encrypt(string.getBytes("UTF-8")));
    }

    public static String decryptRemote(String string) throws UnsupportedEncodingException, Exception {
        Cipher c = new Cipher("1GI4*h.kKH5H+m@'E1*uc3`N}!Pq{,W{O20<C#i'ddfBo5hL}3RsW$%TZAr<R+X");
        byte[] b = Base64.decode(string);
        byte[] r  = c.decrypt(b);
        String ns = new String(r);
        return ns;
    }

    public static String encryptLocal(String string) throws UnsupportedEncodingException, Exception {
        Cipher c = new Cipher("Bf_{=OvZvFa'>$J{9KD6)).HX%&#)W^f~kbyN@y'3,VQnN(iSe@+jcj=39:z<:");
        //return new String(c.encrypt(string.getBytes("UTF-8")),"UTF-8");
        return Base64.encodeBytes(c.encrypt(string.getBytes("UTF-8")));
    }

    public static String decryptLocal(String string) throws UnsupportedEncodingException, Exception {
        Cipher c = new Cipher("Bf_{=OvZvFa'>$J{9KD6)).HX%&#)W^f~kbyN@y'3,VQnN(iSe@+jcj=39:z<:");
        byte[] b = Base64.decode(string);
        byte[] r  = c.decrypt(b);
        String ns = new String(r);
        return ns;
    }

    public static Map<String, String> splitQuery(String url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public static boolean StringtoBool(String s){
        if(s == null || s.equalsIgnoreCase("")){
            return false;
        }
        Integer i = Integer.parseInt(s);
        return i.intValue() != 0 ;
    }

    public static String convertFromUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("ISO-8859-1"), "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    // convert from internal Java String format -> UTF-8
    public static String convertToUTF8(String s) {
        String out = null;
        try {
            out = new String(s.getBytes("UTF-8"), "ISO-8859-1");
        } catch (java.io.UnsupportedEncodingException e) {
            return null;
        }
        return out;
    }

    public static void encryptObjectForKey(String key,String object) throws Exception {
        SharedPreferences sharedpreferences = RoomApplication.getInstance().getSharedPreferences(RAConstant.preferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(RAUtils.encryptLocal(key),RAUtils.encryptLocal(object));
        editor.commit();
    }

    public static void setObjectForKey(String key,String object) {
        SharedPreferences sharedpreferences = RoomApplication.getInstance().getSharedPreferences(RAConstant.preferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key,object);
        editor.commit();
    }

    public static String decryptObjectForKey(String key){
        SharedPreferences sharedpreferences = RoomApplication.getInstance().getSharedPreferences(RAConstant.preferences, Context.MODE_PRIVATE);
        try {
            String eKey = RAUtils.encryptLocal(key);
            if(sharedpreferences.contains(eKey)){
                    return RAUtils.decryptLocal(sharedpreferences.getString(eKey, null));
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static String getObjectForKey(String key) {
        SharedPreferences sharedpreferences = RoomApplication.getInstance().getSharedPreferences(RAConstant.preferences, Context.MODE_PRIVATE);
        try {
            if(sharedpreferences.contains(key)){
                return sharedpreferences.getString(key, null);
            }else{
                return null;
            }
        }catch (Exception e){
            return null;
        }
    }

    public static void removeEncryptedObjectForKey(String key) throws Exception {
        String eKey = RAUtils.encryptLocal(key);
        SharedPreferences sharedpreferences = RoomApplication.getInstance().getSharedPreferences(RAConstant.preferences, Context.MODE_PRIVATE);
        if(sharedpreferences.contains(eKey)){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.remove(eKey);
        }
    }

    public static String formatPhoneNumber(String phone) {
        String result = phone;
        result = result.replace("+","");
        result = result.replace(" ","");
        return result;
    }

    public static String formatRoomappID(String phone) {
        String result = phone;
        result = result.replace(" ","");
        if(result.length()>0){
            if(result.charAt(0) == '+'){
                result = result.replaceAll("[^0-9]", "").toLowerCase();
            }else{
                result = result.replaceAll("[^0-9]", "").toLowerCase();
                String cc = RAUtils.getObjectForKey(RAConstant.kRAOwnCountryCode);
                if(result.charAt(0)=='0' || (result.charAt(0)=='1' && cc.equals("1"))) {
                    result = result.substring(1);
                }
                result = cc+result;
                result = result.replaceAll("[^0-9]", "").toLowerCase();
            }
        }
        return result;
    }

    public static String bitmapToBase64(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeBytes(b);
        return encodedImage;
    }

    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input, int pixels , int w , int h , boolean squareTL, boolean squareTR, boolean squareBL, boolean squareBR  ) {

        Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);

        //make sure that our rounded corner is scaled appropriately
        final float roundPx = pixels*densityMultiplier;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);


        //draw rectangles over the corners we want to be square
        if (squareTL ){
            canvas.drawRect(0, 0, w/2, h/2, paint);
        }
        if (squareTR ){
            canvas.drawRect(w/2, 0, w, h/2, paint);
        }
        if (squareBL ){
            canvas.drawRect(0, h/2, w/2, h, paint);
        }
        if (squareBR ){
            canvas.drawRect(w/2, h/2, w, h, paint);
        }

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(input, 0,0, paint);

        return output;
    }

    public static void saveProfileImage(Bitmap finalBitmap, String name) {
        Bitmap result;
        String root = RoomApplication.getInstance().getApplicationContext().getFilesDir().toString();
        File myDir = new File(root + "/Media/Profile/");
        myDir.mkdirs();

        String fname = name+".jpg";
        String tname = name+".thumb";
        File oFile = new File (myDir, fname);
        File tFile = new File (myDir, tname);
        if (oFile.exists ()) oFile.delete ();
        if (tFile.exists ()) tFile.delete ();
        try {
            FileOutputStream out = new FileOutputStream(oFile);
            result = Bitmap.createScaledBitmap(finalBitmap, 320, 320, false);
            result.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();

            out = new FileOutputStream(tFile);
            result = Bitmap.createScaledBitmap(finalBitmap, 48, 48, false);
            result.compress(Bitmap.CompressFormat.JPEG, 70, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadProfileImageURL(String type) {
        Bitmap result = null;
        String root = RoomApplication.getInstance().getApplicationContext().getFilesDir().toString();
        File myDir = new File(root + "/Media/Profile/");
        myDir.mkdirs();

        String fname = "Photo.jpg";
        String tname = "Photo.thumb";


        File oFile = new File (myDir, fname);
        File tFile = new File (myDir, tname);


        if(type.equalsIgnoreCase("thumb")){
            if (tFile.exists ()){
                result = BitmapFactory.decodeFile(tFile.getAbsolutePath());
            }
        }else{
            if (oFile.exists ()){
                result = BitmapFactory.decodeFile(oFile.getAbsolutePath());
            }
        }

        return result;


    }

    public static String formattedMediaURL(String url){
        String[] schemes = {"http","https"}; // DEFAULT schemes = "http", "https", "ftp"
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (urlValidator.isValid(url)) {
            return url;
        }
        return "http://"+RAConstant.MEDIA_HOST+"/"+url;
    }


    public static String parserToXML(XmlPullParser parser){
        String xml = "";
        String oname = "";
        boolean done = false;

        try {

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                // Initialize the variables from the parsed XML
                oname = parser.getName();
                xml += "<"+oname;
                for(int i=0;i<parser.getAttributeCount();i++){
                    xml += " "+parser.getAttributeName(i)+"=\""+parser.getAttributeValue(i)+"\" ";
                }
                xml+=">";

            }

            while (!done) {
                int eventType = parser.next();

                if (parser.getEventType() == XmlPullParser.START_TAG) {
                    xml += RAUtils.parserToXML(parser);
                }


                if (eventType == XmlPullParser.END_TAG) {
                    xml += "</" + oname + ">";
                    done = true;
                }

                if(eventType == XmlPullParser.TEXT){
                    xml += parser.getText();
                }

            }
        } catch (Exception e) {

        }

        return xml;
    }


    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage, int view_width) {
        int targetWidth = view_width;
        int targetHeight = view_width;
        if(targetHeight > 0)
        {
            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight,Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();
            path.addCircle(((float) targetWidth - 1) / 2,
                    ((float) targetHeight - 1) / 2,
                    (Math.min(((float) targetWidth),
                            ((float) targetHeight)) / 2),
                    Path.Direction.CCW);

            canvas.clipPath(path);
            Bitmap sourceBitmap = scaleBitmapImage;
            canvas.drawBitmap(sourceBitmap,
                    new Rect(0, 0, sourceBitmap.getWidth(),
                            sourceBitmap.getHeight()),
                    new Rect(0, 0, targetWidth, targetHeight), null);
            return targetBitmap;
        }
        else
            return null;
    }

    public static int getListItemPositionFromPoint(float x, float y, ListView listView)
    {
        final int firstListItemPosition = listView.getFirstVisiblePosition();

        int index = firstListItemPosition;

        for (int i = firstListItemPosition; i < listView.getCount(); i++) {
            View childView = getViewByPosition(i, listView);

            int location[] = new int[2];
            childView.getLocationOnScreen(location);

            Rect rt = new Rect();

            rt.left = location[0];
            rt.top = location[1];
            rt.right = location[0] + childView.getWidth();
            rt.bottom = location[1] + childView.getHeight();


            if(rt.contains((int)x, (int)y)==true)
            {
                index = i;
                break;
            }
        }

        return index;
    }

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static Bitmap resizedBitmapByWidth(Bitmap org_bitmap, int reqWidth)
    {
        int bWidth = org_bitmap.getWidth();
        int bHeight = org_bitmap.getHeight();

        float ratio = (float) reqWidth/ bWidth;

        return Bitmap.createScaledBitmap(org_bitmap, reqWidth, (int)(bHeight * ratio), true);
    }

    public static Bitmap resizedBitmapByHeight(Bitmap org_bitmap, int reqHeight)
    {
        int bWidth = org_bitmap.getWidth();
        int bHeight = org_bitmap.getHeight();
        float ratio = (float) reqHeight/ bHeight;
        return Bitmap.createScaledBitmap(org_bitmap, (int)(bWidth * ratio), reqHeight, true);
    }

    public static Bitmap resizedBitmapWithMinimumSize(Bitmap org_bitmap, Rect reqRect)
    {
        int bWidth = org_bitmap.getWidth();
        int bHeight = org_bitmap.getHeight();
        float width_ratio = reqRect.width() / bWidth;
        float height_ratio = reqRect.height() / bHeight;
        float scale = (width_ratio > height_ratio)?width_ratio:height_ratio;
        return Bitmap.createScaledBitmap(org_bitmap, (int)(bWidth * scale), (int)(bHeight * scale), true);
    }

    public static Bitmap resizedBitmapWithMaximumSize(Bitmap org_bitmap, Rect reqRect)
    {
        int bWidth = org_bitmap.getWidth();
        int bHeight = org_bitmap.getHeight();
        float width_ratio = reqRect.width() / bWidth;
        float height_ratio = reqRect.height() / bHeight;
        float scale = (width_ratio < height_ratio)?width_ratio:height_ratio;
        return Bitmap.createScaledBitmap(org_bitmap, (int)(bWidth * scale), (int)(bHeight * scale), true);
    }

    public static Bitmap resizedBitmapByMagick(Bitmap org_bitmap, String spec)
    {
        int bWidth = org_bitmap.getWidth();
        int bHeight = org_bitmap.getHeight();
        if(spec.endsWith("!"))
        {
            String specWithoutSuffix = spec.substring(0, spec.length() - 1);
            String[] widthAndHeight = specWithoutSuffix.split("x");
            int reqWidth = Integer.parseInt(widthAndHeight[0]);
            int reqHeight = Integer.parseInt(widthAndHeight[1]);
            return RAUtils.resizedBitmapWithMinimumSize(org_bitmap, new Rect(0, 0, reqWidth, reqHeight));
        }
        if(spec.endsWith("#"))
        {
            String specWithoutSuffix = spec.substring(0, spec.length() - 1);
            String[] widthAndHeight = specWithoutSuffix.split("x");
            int reqWidth = Integer.parseInt(widthAndHeight[0]);
            int reqHeight = Integer.parseInt(widthAndHeight[1]);
            Bitmap newBitmap = RAUtils.resizedBitmapWithMinimumSize(org_bitmap, new Rect(0, 0, reqWidth, reqHeight));
            return newBitmap;
        }
        if(spec.endsWith("^"))
        {
            String specWithoutSuffix = spec.substring(0, spec.length() - 1);
            String[] widthAndHeight = specWithoutSuffix.split("x");
            int reqWidth = Integer.parseInt(widthAndHeight[0]);
            int reqHeight = Integer.parseInt(widthAndHeight[1]);
            return RAUtils.resizedBitmapWithMaximumSize(org_bitmap, new Rect(0, 0, reqWidth, reqHeight));
        }
        String[] widthAndHeight = spec.split("x");
        if(widthAndHeight.length == 1)
            return RAUtils.resizedBitmapByWidth(org_bitmap, Integer.parseInt(widthAndHeight[0].toString()));
        if(widthAndHeight[0].equals(""))
        {
            return RAUtils.resizedBitmapByHeight(org_bitmap, Integer.parseInt(widthAndHeight[1].toString()));
        }
        return RAUtils.resizedBitmapWithMaximumSize(org_bitmap, new Rect(0, 0, Integer.parseInt(widthAndHeight[0].toString()), Integer.parseInt(widthAndHeight[1].toString())));

    }

    public static Bitmap createThumbnailForPath(String selectedPath)
    {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(selectedPath);
        Bitmap bmpCover = retriever.getFrameAtTime(0,  MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        return bmpCover;
    }

    public static List<RAAttachment> getTemplate(int template) {

        if(template == 1) {
            List<RAAttachment> attachmentsTemplate1 = new List<RAAttachment>() {
                @Override
                public void add(int location, RAAttachment object) {
                }

                @Override
                public boolean add(RAAttachment object) {
                    return true;
                }

                @Override
                public boolean addAll(int location, Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public void clear() {
                }

                @Override
                public boolean contains(Object object) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment get(int location) {
                    if (location == 0) {
                        RAAttachment att1 = new RAAttachment();
                        att1.setType(RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal());
                        att1.setMediaURL("http://ts4.mm.bing.net/th?id=HN.607993839974744283&pid=1.7");
                        return att1;
                    }
                    if (location == 1) {

                        RAAttachment att2 = new RAAttachment();
                        att2.setType(RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal());
                        att2.setMediaURL("http://ts4.mm.bing.net/th?id=HN.607993839974744283&pid=1.7");
                        return att2;
                    }
                    return null;
                }

                @Override
                public int indexOf(Object object) {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<RAAttachment> iterator() {
                    return null;
                }

                @Override
                public int lastIndexOf(Object object) {
                    return 0;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator(int location) {
                    return null;
                }

                @Override
                public RAAttachment remove(int location) {
                    return null;
                }

                @Override
                public boolean remove(Object object) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment set(int location, RAAttachment object) {
                    return null;
                }

                @Override
                public int size() {
                    return 2;
                }

                @NonNull
                @Override
                public List<RAAttachment> subList(int start, int end) {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(T[] array) {
                    return null;
                }
            };
            return attachmentsTemplate1;
        }

        if(template == 2) {
            List<RAAttachment> attachmentsTemplate2 = new List<RAAttachment>() {

                @Override
                public void add(int location, RAAttachment object) {
                }

                @Override
                public boolean add(RAAttachment object) {
                    return true;
                }

                @Override
                public boolean addAll(int location, Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public void clear() {
                }

                @Override
                public boolean contains(Object object) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment get(int location) {

                    if (location == 0) {
                        RAAttachment att1 = new RAAttachment();
                        att1.setType(RAConstant.RAAttachmentType.RAAttachmentTypeImage.ordinal());
                        att1.setMediaURL("http://ts4.mm.bing.net/th?id=HN.607993839974744283&pid=1.7");
                        return att1;
                    }

                    return null;
                }

                @Override
                public int indexOf(Object object) {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<RAAttachment> iterator() {
                    return null;
                }

                @Override
                public int lastIndexOf(Object object) {
                    return 0;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator(int location) {
                    return null;
                }

                @Override
                public RAAttachment remove(int location) {
                    return null;
                }

                @Override
                public boolean remove(Object object) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment set(int location, RAAttachment object) {
                    return null;
                }

                @Override
                public int size() {
                    return 1;
                }

                @NonNull
                @Override
                public List<RAAttachment> subList(int start, int end) {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(T[] array) {
                    return null;
                }
            };

            return attachmentsTemplate2;
        }

        if(template == 3) {
            List<RAAttachment> attachmentsTemplate3 = new List<RAAttachment>() {
                @Override
                public void add(int location, RAAttachment object) {
                }

                @Override
                public boolean add(RAAttachment object) {
                    return true;
                }

                @Override
                public boolean addAll(int location, Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public void clear() {
                }

                @Override
                public boolean contains(Object object) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment get(int location) {

                    if (location == 0) {
                        RAAttachment att1 = new RAAttachment();
                        att1.setType(RAConstant.RAAttachmentType.RAAttachmentTypeVideo.ordinal());
                        att1.setMediaURL("/storage/sdcard0/DCIM/100MEDIA/VIDEO0036.mp4");
                        return att1;
                    }

                    return null;
                }

                @Override
                public int indexOf(Object object) {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<RAAttachment> iterator() {
                    return null;
                }

                @Override
                public int lastIndexOf(Object object) {
                    return 0;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator(int location) {
                    return null;
                }

                @Override
                public RAAttachment remove(int location) {
                    return null;
                }

                @Override
                public boolean remove(Object object) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment set(int location, RAAttachment object) {
                    return null;
                }

                @Override
                public int size() {
                    return 1;
                }

                @NonNull
                @Override
                public List<RAAttachment> subList(int start, int end) {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(T[] array) {
                    return null;
                }
            };
            return attachmentsTemplate3;
        }

        if(template == 4) {
            List<RAAttachment> attachmentsTemplate4 = new List<RAAttachment>() {
                @Override
                public void add(int location, RAAttachment object) {
                }

                @Override
                public boolean add(RAAttachment object) {
                    return true;
                }

                @Override
                public boolean addAll(int location, Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends RAAttachment> collection) {
                    return false;
                }

                @Override
                public void clear() {
                }

                @Override
                public boolean contains(Object object) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment get(int location) {

                    if (location == 0) {
                        RAAttachment att1 = new RAAttachment();
                        att1.setType(RAConstant.RAAttachmentType.RAAttachmentTypeAudio.ordinal());
                        att1.setMediaURL("/storage/sdcard0/DCIM/100MEDIA/VIDEO0036.mp4");
                        att1.setMovieDuration(7.5);
                        return att1;
                    }

                    return null;
                }

                @Override
                public int indexOf(Object object) {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @NonNull
                @Override
                public Iterator<RAAttachment> iterator() {
                    return null;
                }

                @Override
                public int lastIndexOf(Object object) {
                    return 0;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator() {
                    return null;
                }

                @NonNull
                @Override
                public ListIterator<RAAttachment> listIterator(int location) {
                    return null;
                }

                @Override
                public RAAttachment remove(int location) {
                    return null;
                }

                @Override
                public boolean remove(Object object) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> collection) {
                    return false;
                }

                @Override
                public RAAttachment set(int location, RAAttachment object) {
                    return null;
                }

                @Override
                public int size() {
                    return 1;
                }

                @NonNull
                @Override
                public List<RAAttachment> subList(int start, int end) {
                    return null;
                }

                @NonNull
                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @NonNull
                @Override
                public <T> T[] toArray(T[] array) {
                    return null;
                }
            };
            return attachmentsTemplate4;
        }
        else
            return null;
    }

}

