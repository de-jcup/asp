package de.jcup.asp.api;

public interface APIKey<T> extends Identifiable{

    /**
     * An immutable prototype - a necessary workaround to get type information with generics from the interface.
     * @return prototype, immutable, just for type detection
     */
    public T getPrototype();
}
