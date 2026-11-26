package bw;

import arc.Core;
import arc.Events;
import arc.graphics.gl.Shader;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.mod.Mod;
import mindustry.graphics.Shaders;

public class BlackWhiteMod extends Mod {

    @Override
    public void init() {
        if (Vars.headless) return;

        Events.on(ClientLoadEvent.class, e -> {
            Log.info("开始加载黑白着色器...");
            
            // 替换原版的核心着色器
            replaceShader("sprite"); 
            replaceShader("water");
            replaceShader("shield");
            replaceShader("buildBeam");
            replaceShader("blockbuild");
        });
    }

    private void replaceShader(String name) {
        try {
            // 注意：这里的路径去掉了 assets/，因为你的 jar 包结构里就是直接在根目录的 shaders 下
            String vert = Core.files.internal("shaders/bw.vert").readString();
            String frag = Core.files.internal("shaders/bw.frag").readString();
            Shader bwShader = new Shader(vert, frag);

            java.lang.reflect.Field field = Shaders.class.getDeclaredField(name);
            field.setAccessible(true);
            field.set(null, bwShader);
            
            Log.info("成功替换 Shader: " + name);
        } catch (Exception ex) {
            Log.err("替换 Shader 失败: " + name + " - " + ex.getMessage());
        }
    }
}
