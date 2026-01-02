package main;

import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
// import javax.sound.sampled.LineUnavailableException;
// import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    Clip musicClip; // Đổi tên biến này để chỉ dùng riêng cho Music
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

    // --- DÀNH RIÊNG CHO NHẠC NỀN (MUSIC) ---
    public void setFile(int i) {
        try {
            // 1. Dừng và giải phóng nhạc cũ NGAY LẬP TỨC để cứu RAM
            if (musicClip != null) {
                musicClip.stop();
                musicClip.close();
            }
            // Gọi dọn rác hệ thống (Chỉ nên dùng khi chuyển cảnh/load nhạc nặng)
            System.gc();

            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            AudioFormat baseFormat = ais.getFormat();
            
            // Decode MP3 to PCM
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
            
            musicClip = AudioSystem.getClip();
            musicClip.open(dais);
            
            // Setup Volume cho Music
            checkVolume();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (musicClip != null) {
            musicClip.start();
        }
    }

    public void loop() {
        if (musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (musicClip != null) {
            musicClip.stop();
        }
    }

    // --- DÀNH RIÊNG CHO HIỆU ỨNG (SE) ---
    // Hàm này sẽ tự tạo clip, tự chơi, và tự đóng. Không đụng đến biến musicClip.
    public void playSE(int i) {
        
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            Clip clip = AudioSystem.getClip(); // Tạo biến cục bộ, không dùng biến toàn cục
            clip.open(ais);
            
            // Chỉnh volume riêng cho Clip này
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                // Set volume theo logic cũ
                float vol = 0;
                switch(volumeScale) {
                    case 0: vol = -80f; break;
                    case 1: vol = -20f; break;
                    case 2: vol = -12f; break;
                    case 3: vol = -5f; break;
                    case 4: vol = 1f; break;
                    case 5: vol = 6f; break;
                }
                fc.setValue(vol);
            }

            // Tự động đóng khi phát xong (Quan trọng để không tràn RAM)
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                }
            });

            clip.start(); // Phát ngay lập tức

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkVolume() {
        if (musicClip != null && musicClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            fc = (FloatControl)musicClip.getControl(FloatControl.Type.MASTER_GAIN);
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
}