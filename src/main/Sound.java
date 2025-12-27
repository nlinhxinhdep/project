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
        soundURL[23] = getClass().getResource("/sound/BIYTheme.mp3");
    }

    public void setFile(int i) {
        try {
            // Lấy input stream từ file gốc (MP3 hoặc WAV)
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            
            // Lấy tên file để kiểm tra
            String fileName = soundURL[i].toString();

            // CHỈ GIẢI MÃ NẾU LÀ FILE MP3 (Nhạc nền)
            if (fileName.endsWith(".mp3")) {
                AudioFormat baseFormat = ais.getFormat();
                AudioFormat decodeFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false
                );
                AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
                clip = AudioSystem.getClip();
                clip.open(dais);
            } 
            // NẾU LÀ WAV (Sound Effect) -> LOAD NHANH NHƯ CŨ
            else {
                clip = AudioSystem.getClip();
                clip.open(ais);
                // Thêm một "người giám sát". Khi tiếng động phát xong (STOP), tự động xóa nó khỏi RAM.
                clip.addLineListener(new javax.sound.sampled.LineListener() {
                    @Override
                    public void update(javax.sound.sampled.LineEvent event) {
                        if (event.getType() == javax.sound.sampled.LineEvent.Type.STOP) {
                            event.getLine().close(); // Hát xong thì tự đóng -> Giải phóng RAM
                        }
                    }
                });
            }
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
        if (clip == null) return; // Nếu lỗi nhạc thì im lặng, đừng crash game
        clip.start();
    }

    public void loop() {
        if (clip == null) return;
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
