package util.save

interface SaveableFactory<T : Saveable> {
    fun load(): T;
}
