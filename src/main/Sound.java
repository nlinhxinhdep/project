package main;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {
    	soundURL[0] = getClass().getResource("/sound/StartingOff.mp3");
    	soundURL[1] = getClass().getResource("/sound/coin.wav");
    	soundURL[2] = getClass().getResource("/sound/powerup.wav");
    	soundURL[3] = getClass().getResource("/sound/unlock.wav");
    	soundURL[4] = getClass().getResource("/sound/fanfare.wav");
    	soundURL[5] = getClass().getResource("/sound/hitmonster.wav");
    	soundURL[6] = getClass().getResource("/sound/receivedamage.wav");
    	soundURL[7] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[8] = getClass().getResource("/sound/levelup.wav");
        soundURL[9] = getClass().getResource("/sound/cursor.wav");
        soundURL[10] = getClass().getResource("/sound/burning.wav");
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");
        soundURL[12] = getClass().getResource("/sound/gameover.wav");
        soundURL[13] = getClass().getResource("/sound/stairs.wav");
        soundURL[14] = getClass().getResource("/sound/sleep.wav");
        soundURL[15] = getClass().getResource("/sound/blocked.wav");
        soundURL[16] = getClass().getResource("/sound/parry.wav");
        soundURL[17] = getClass().getResource("/sound/speak.wav");
        soundURL[18] = getClass().getResource("/sound/DeepForest.mp3");
        soundURL[19] = getClass().getResource("/sound/Cavern.mp3");
        soundURL[20] = getClass().getResource("/sound/chipwall.wav");
        soundURL[21] = getClass().getResource("/sound/dooropen.wav");
        soundURL[22] = getClass().getResource("/sound/FinalAction.mp3");
    }

    public void setFile(int i) {
        try {
            // 1. Lấy input stream từ file gốc (MP3 hoặc WAV)
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            
            // 2. Lấy định dạng (format) của file gốc
            AudioFormat baseFormat = ais.getFormat();

            // 3. Tạo định dạng mới để giải mã (Decode) về PCM (âm thanh thô mà Clip hiểu được)
            AudioFormat decodeFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, // Định dạng chuẩn PCM
                baseFormat.getSampleRate(),      // Giữ nguyên tần số lấy mẫu
                16,                              // Chuyển về 16-bit (chuẩn)
                baseFormat.getChannels(),        // Giữ nguyên số kênh (Mono/Stereo)
                baseFormat.getChannels() * 2,    // Frame size = số kênh * 2 byte
                baseFormat.getSampleRate(),      // Frame rate = Sample rate
                false                            // Big Endian: false
            );

            // 4. Tạo luồng âm thanh mới đã được giải mã
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);

            // 5. Mở Clip bằng luồng đã giải mã (dais) thay vì luồng gốc (ais)
            clip = AudioSystem.getClip();
            clip.open(dais);
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                checkVolume();
            } else {
                // Nếu không hỗ trợ chỉnh volume thì bỏ qua để game không bị crash
                // Bạn có thể in ra console để debug nếu muốn
                // System.out.println("Volume control not supported for sound ID: " + i);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }
    public void checkVolume() {
        switch(volumeScale) {
        case 0: volume = -80f; break;
        case 1: volume = -20f; break;
        case 2: volume = -12f; break;
        case 3: volume = -5f; break;
        case 4: volume = 1f; break;
        case 5: volume = 6f; break;
        }
        fc.setValue(volume);
    }
}
