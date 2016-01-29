package eu.modernmt.processing.tokenizer.jflex;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by davide on 29/01/16.
 */
public abstract class JFlexAnnotator {

    public static final int YYEOF = -1;
    public static final int PROTECT = 0;
    public static final int PROTECT_ALL = 1;
    public static final int PROTECT_RIGHT = 2;

    protected int zzStartReadOffset = 0;

    public final void annotate(boolean[] array, int tokenType) {
        int zzMarkedPos = getMarkedPosition();

        int zzStartRead = getStartRead() + zzStartReadOffset;
        zzStartReadOffset = 0;

        int yychar = yychar();
        int offset = 0;

        if (yychar > zzStartRead) {
            offset = yychar + zzStartRead;
        }

        switch (tokenType) {
            case PROTECT:
                for (int i = zzStartRead + 1; i < zzMarkedPos; i++) {
                    array[offset + i] = true;
                }
                break;
            case PROTECT_ALL:
                for (int i = zzStartRead; i < zzMarkedPos; i++)
                    array[offset + i] = true;
                break;
            case PROTECT_RIGHT:
                array[offset + zzMarkedPos] = true;
                break;
        }
    }

    public abstract void yyreset(Reader reader);

    public abstract int next() throws IOException;

    protected abstract int getStartRead();

    protected abstract int getMarkedPosition();

    protected abstract int yychar();

}