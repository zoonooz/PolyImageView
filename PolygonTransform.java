import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

import com.squareup.picasso.Transformation;

/**
 * Created by zoonooz on 2/9/16 AD.
 * Polygon Picasso Transform
 */
public class PolygonTransform implements Transformation {

    private int side = 6;
    private int padding = 0;

    public PolygonTransform(int side) {
        this(side, 0);
    }

    public PolygonTransform(int side, int padding) {
        this.side = side;
        this.padding = padding;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        if (source != null) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            // begin

            float centerX = size / 2f;
            float centerY = size / 2f;
            float radius = size / 2f - padding;
            float subRadius = radius * 0.2f;
            double radianPerSide = Math.PI * 2 / side;
            float degreePerSide = 360f / side;

            Path path = new Path();
            path.moveTo(centerX, padding);

            // first curve
            RectF rect = new RectF(centerX - subRadius, padding, centerX + subRadius, padding + subRadius * 2);
            path.arcTo(rect, 270, degreePerSide / 2f);

            for (int i = 1; i <= side; i++) {
                double zeta = (3 * Math.PI / 2) + (radianPerSide * i);
                double pointX = centerX + radius * Math.cos(zeta);
                double pointY = centerY + radius * Math.sin(zeta);
                double controlX = centerX + (radius * 0.8) * Math.cos(zeta);
                double controlY = centerY + (radius * 0.8) * Math.sin(zeta);
                double radianDelta = Math.atan2(pointY - controlY, pointX - controlX);

                RectF rectf = new RectF(
                        (float) controlX - subRadius,
                        (float) controlY - subRadius,
                        (float) controlX + subRadius,
                        (float) controlY + subRadius
                );

                float startAngle = (float) Math.toDegrees(radianDelta - (radianPerSide / 2));
                float endAngle = (float) Math.toDegrees(radianDelta + (radianPerSide / 2));
                path.arcTo(rectf, startAngle, endAngle - startAngle);
            }

            canvas.drawPath(path, paint);

            squaredBitmap.recycle();
            return bitmap;
        }
        return null;
    }

    @Override
    public String key() {
        return "polygon:" + side + ":" + padding ;
    }
}
