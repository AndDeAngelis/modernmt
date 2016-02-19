package eu.modernmt.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tag extends Token implements Comparable<Tag> {

    public static final Pattern TagRegex = Pattern.compile(
            "(<((\\p{Alpha}|_|:)(\\p{Alpha}|\\p{Digit}|\\.|-|_|:|)*)[^>]*/?>)|" +
                    "(</((\\p{Alpha}|_|:)(\\p{Alpha}|\\p{Digit}|\\.|-|_|:|)*)[^>]*>)");
    public static final Pattern TagNameRegex = Pattern.compile("(\\p{Alpha}|_|:)(\\p{Alpha}|\\p{Digit}|\\.|-|_|:|)*");

    protected enum Type {
        OPENING_TAG,
        CLOSING_TAG,
        EMPTY_TAG,
    }

    public static Tag fromText(String text) {
        int length = text.length();

        if (length < 3)
            throw new IllegalArgumentException("Invalid tag: " + text);

        String name;
        Type type;
        int nameStartPosition = 1;

        if (text.charAt(1) == '/') {
            type = Type.CLOSING_TAG;
            nameStartPosition = 2;
        } else if (text.charAt(length - 2) == '/') {
            type = Type.EMPTY_TAG;
        } else {
            type = Type.OPENING_TAG;
        }

        Matcher matcher = TagNameRegex.matcher(text);
        if (!matcher.find(nameStartPosition))
            throw new IllegalArgumentException("Invalid tag: " + text);
        name = matcher.group();

        return new Tag(name, text, type);
    }

    protected final Type type;
    protected final String name; // tag name
    protected boolean leftSpace;
    //position of the word after which the tag is placed; indexes of words start from 0
    // e.g. a tag at the beginning of the sentence has position=0
    // e.g. a tag at the end of the sentence (of Length words) has position=Length
    protected int position;

    public Tag(String name, String text, Type type) {
        this(name, text, true, true, -1, type);
    }

    public Tag(String name, String text, boolean leftSpace, boolean rightSpace, int position, Type type) {
        super(text, rightSpace);
        this.leftSpace = leftSpace;
        this.position = position;
        this.type = type;
        this.name = name;
    }

    public boolean hasLeftSpace() {
        return leftSpace;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setLeftSpace(boolean leftSpace) {
        this.leftSpace = leftSpace;
    }

    public int compareTo(Tag other) {
        return Integer.compare(this.position, other.getPosition());
    }

}
