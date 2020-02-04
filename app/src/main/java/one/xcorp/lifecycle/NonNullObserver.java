package one.xcorp.lifecycle;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

// TODO: replace on Kotlin class when obtain SAM constructor support in Kotlin 1.4
public interface NonNullObserver<T> extends Observer<T> {

    @Override
    void onChanged(@NonNull T type);
}
