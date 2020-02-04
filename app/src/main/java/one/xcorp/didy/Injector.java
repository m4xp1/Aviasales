package one.xcorp.didy;

import androidx.annotation.NonNull;

// TODO: replace on Kotlin class when obtain SAM constructor support in Kotlin 1.4
public interface Injector<Component> {

    void invoke(@NonNull Component component);
}
