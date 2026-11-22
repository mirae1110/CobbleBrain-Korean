package kr.cobblebrain.ko.mixin;

import kr.cobblebrain.ko.KoreanText;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Prototype: replace targeted incoming messages at chat rendering. */
@Mixin(ChatHud.class)
public abstract class ChatHudMixin {
    private boolean cobblebrainKo$reenter = false;

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void cobblebrainKo$translate(Text original, CallbackInfo ci) {
        if (cobblebrainKo$reenter) return;
        String input = original.getString();
        String output = KoreanText.translate(input);
        if (input.equals(output)) return;
        ci.cancel();
        cobblebrainKo$reenter = true;
        try { ((ChatHud)(Object)this).addMessage(Text.literal(output).setStyle(original.getStyle())); }
        finally { cobblebrainKo$reenter = false; }
    }
}
