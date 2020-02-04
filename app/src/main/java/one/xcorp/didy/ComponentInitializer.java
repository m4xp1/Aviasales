package one.xcorp.didy;

import androidx.annotation.NonNull;

// TODO: replace on Kotlin class when obtain SAM constructor support in Kotlin 1.4
public interface ComponentInitializer<Factory, Component> {

    @NonNull
    Component invoke(@NonNull Factory factory);
}
