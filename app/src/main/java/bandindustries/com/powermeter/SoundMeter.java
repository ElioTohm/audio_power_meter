package bandindustries.com.powermeter;

/**
 * Created by Elio on 10/8/2016.
 */

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

class SoundMeter {

    private AudioRecord ar = null;
    private int minSize;

    public void start() {
        minSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);
        ar.startRecording();
    }


    public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer)
        {
            if (Math.abs(s) > max)
            {
                max = Math.abs(s);
            }
        }
        return max;
    }

    public int getRawAmplitude() {
        short[] buffer = new short[minSize];
        final int bufferReadSize = ar.read(buffer, 0, minSize);

        if (bufferReadSize < 0) {
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < bufferReadSize; i++) {
            sum += Math.abs(buffer[i]);
        }

        if (bufferReadSize > 0) {
            return sum / bufferReadSize;
        }

        return 0;
    }
}