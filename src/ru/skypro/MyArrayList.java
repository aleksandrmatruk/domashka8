package ru.skypro;
import java.util.*;
import java.util.function.Consumer;
import jdk.internal.access.SharedSecrets;
import jdk.internal.util.ArraysSupport;


public class MyArrayList<E> extends AbstractList<E>
        implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
    @java.io.Serial
    private static final long serialVersionUID = 8683452581122892189L;
    /**
     Дефолтный размер списка при объявлении
     */
    private static final int DEFAULT_CAPACITY = 10;
    /**
    Общий пустой экземпляр массива, используемый для пустых экземпляров.
     */
    private static final Object[] EMPTY_ELEMENTDATA = {};

    /**
     Общий пустой экземпляр массива, используемый для пустых экземпляров размера по умолчанию.
     Отличается от EMPTY_ELEMENTDATA, чтобы знать, насколько он будет увеличиваться при добавлении первого элемента.
     */
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
    /**
     Буфер массива, в котором хранятся элементы MyArrayList. Емкость MyArrayList равна длине этого буфера массива.
     любой пустой MyArrayList с elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA будет расширен до DEFAULT_CAPACITY
     при добавлении первого элемента
     */


    transient Object[] elementData; //Неприватный для упрощения доступа к вложенным классам
    /**
    Размер MyArrayList (кол-во элементов в списке)
    */
    private int size;

    /**
     * Создает пустой список с указанной начальной емкостью
     *
     * @param  initialCapacity  начальная емкость списка
     * @throws IllegalArgumentException бросает ошибку, если передаваемая емкость < 0 (отрицательная)
     */
    public MyArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new IllegalArgumentException("Некорректное значение: "+
                    initialCapacity);
        }
    }

    /**
     * Создает пустой список с начальной емкостью 10.
     */
    public MyArrayList() {
        this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

    /**
     * Создает список, содержащий элементы указанной коллекции
     * в том порядке, в котором они были возвращены итератором
     *
     * @param c коллекция, элементы которой должны быть помещены в список
     * @throws NullPointerException если указанная коллекция пуста (null)
     */
    public MyArrayList(Collection<? extends E> c) {
        Object[] a = c.toArray();
        if ((size = a.length) != 0) {
            if (c.getClass() == MyArrayList.class) {
                elementData = a;
            } else {
                elementData = Arrays.copyOf(a, size, Object[].class);
            }
        } else {
            // заменить пустым массивом
            elementData = EMPTY_ELEMENTDATA;
        }
    }

    /**
     * Обрезает емкость текущего экземпляра ArrayList
     * до текущего размера (size).  Приложение может использовать эту операцию для уменьшения
     * памяти для хранения экземпляра ArrayList
     */
    public void trimToSize() {
        modCount++;
        if (size < elementData.length) {
            elementData = (size == 0)
                    ? EMPTY_ELEMENTDATA
                    : Arrays.copyOf(elementData, size);
        }
    }

    /**
     * Увеличивает емкость экземпляра ArrayList при необходимости,
     * чтобы гарантированно иметь minCapacity элементов
     *
     * @param minCapacity желаемая минимальная емкость
     */
    public void ensureCapacity(int minCapacity) {
        if (minCapacity > elementData.length
                && !(elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA
                && minCapacity <= DEFAULT_CAPACITY)) {
            modCount++;
            grow(minCapacity);
        }
    }

    /**
     * Увеличивает емкость экземпляра ArrayList при необходимости,
     * чтобы гарантированно иметь minCapacity элементов
     *
     * Если массив не пуст, то мы увеличиваем наш исходный массив на длину, равную половине(округление вниз) старой длины,
     * если получившаяся длина не больше максимальной длины массива(Integer.MAX_VALUE - 8),
     *
     * иначе равную максимальной длине массива,
     * если oldLength + minGrowth меньше максимальной длины массива,
     *
     * иначе равную Integer.MAX_VALUE.
     *
     * Иначе мы заново инициализируем массив, передавая в него ссылку на массив объектов длиной 10
     * или minCapacity, если minCapacity > 10.
     *
     * @param minCapacity желаемая минимальная емкость
     * @throws OutOfMemoryError если minCapacity < 0 (отрицательное)
     */
    private Object[] grow(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {

            int newCapacity = ArraysSupport.newLength(oldCapacity,
                    minCapacity - oldCapacity, /* минимальный рост */
                    oldCapacity >> 1           /* предпочтительный рост */);
            return elementData = Arrays.copyOf(elementData, newCapacity);
        } else {
            return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
        }
    }

    private Object[] grow() { return grow(size + 1); }

    // Возвращает размер массива
    public int size() { return size; }

    // Проверить массив на наличие в нем элементов. Возвращает true или false
    public boolean isEmpty() {
        return size == 0;
    }

    // Проверяет, содержится ли элемент o в массиве. Возвращает true или false
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    /**
     * Возвращает индекс первого вхождения элемента в списке
     * или -1, если список не содержит данный элемент
     */
    public int indexOf(Object o) {
        return indexOfRange(o, 0, size);
    }

    int indexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = start; i < end; i++) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = start; i < end; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Возвращает индекс последнего вхождения элемента в списке
     * или -1, если список не содержит данный элемент
     */
    public int lastIndexOf(Object o) {
        return lastIndexOfRange(o, 0, size);
    }

    int lastIndexOfRange(Object o, int start, int end) {
        Object[] es = elementData;
        if (o == null) {
            for (int i = end - 1; i >= start; i--) {
                if (es[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = end - 1; i >= start; i--) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    // Возвращает созданную копию этого ArrayList (Сами элементы не копируются)
    public Object clone() {
        try {
            MyArrayList<?> v = (MyArrayList<?>) super.clone();
            v.elementData = Arrays.copyOf(elementData, size);
            v.modCount = 0;
            return v;
        } catch (CloneNotSupportedException e) {
            // этого не должно происходить, так как мы Cloneable
            throw new InternalError(e);
        }
    }

    /**
     * Возвращает массив, содержащий все элементы передаваемого списка
     * в правильной (должной) последовательности (от первого до последнего элемента).
     * Возвращенный массив будет "безопасным" в том смысле,
     * что этот список не поддерживает никаких ссылок на него.
     */
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    /**
     * Возвращает массив, содержащий все элементы передаваемого списка
     * в правильной (должной) последовательности (от первого до последнего элемента).
     * тип возвращаемого массива во время выполнения совпадает с типом указанного массива.
     * Если список помещается в указанный массив, он возвращается в нем.
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    // Операции позиционного доступа

    @SuppressWarnings("unchecked")
    E elementData(int index) {
        return (E) elementData[index];
    }

    @SuppressWarnings("unchecked")
    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

    // Возвращает элемент по указанному индексу в списке
    public E get(int index) {
        Objects.checkIndex(index, size);
        return elementData(index);
    }

    // Заменяет элемент списка по указанному индексу на указанный элемент
    public E set(int index, E element) {
        Objects.checkIndex(index, size);
        E oldValue = elementData(index);
        elementData[index] = element;
        return oldValue;
    }

    // Этот вспомогательный метод отделен от add(E), чтобы размер байт-кода метода не превышал 35.
    private void add(E e, Object[] elementData, int s) {
        if (s == elementData.length)
            elementData = grow();
        elementData[s] = e;
        size = s + 1;
    }

    // Добавляет указанный элемент в конец списка
    public boolean add(E e) {
        modCount++;
        add(e, elementData, size);
        return true;
    }

    /**
     * Вставляет указанный элемент element в указанную позицию index в этом списке.
     * Сдвигает элементы списка, начиная с текущей указанной позиции index,
     * и элементы, следующие за ним (если таковые имеются) вправо (добавляя единицу к их индексу).
     */
    public void add(int index, E element) {
        rangeCheckForAdd(index);
        modCount++;
        final int s;
        Object[] elementData;
        if ((s = size) == (elementData = this.elementData).length)
            elementData = grow();
        System.arraycopy(elementData, index,
                elementData, index + 1,
                s - index);
        elementData[index] = element;
        size = s + 1;
    }

    /**
     * Удаляет элемент в указанной позиции index в списке.
     * Сдвигает все последующие элементы влево (уменьшая их индекс на единицу).
     */
    public E remove(int index) {
        Objects.checkIndex(index, size);
        final Object[] es = elementData;

        @SuppressWarnings("unchecked") E oldValue = (E) es[index];
        fastRemove(es, index);

        return oldValue;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof List)) {
            return false;
        }

        final int expectedModCount = modCount;
        boolean equal = (o.getClass() == MyArrayList.class)
                ? equalsArrayList((MyArrayList<?>) o)
                : equalsRange((List<?>) o, 0, size);

        checkForComodification(expectedModCount);
        return equal;
    }

    boolean equalsRange(List<?> other, int from, int to) {
        final Object[] es = elementData;
        if (to > es.length) {
            throw new ConcurrentModificationException();
        }
        var oit = other.iterator();
        for (; from < to; from++) {
            if (!oit.hasNext() || !Objects.equals(es[from], oit.next())) {
                return false;
            }
        }
        return !oit.hasNext();
    }

    private boolean equalsArrayList(MyArrayList<?> other) {
        final int otherModCount = other.modCount;
        final int s = size;
        boolean equal;
        if (equal = (s == other.size)) {
            final Object[] otherEs = other.elementData;
            final Object[] es = elementData;
            if (s > es.length || s > otherEs.length) {
                throw new ConcurrentModificationException();
            }
            for (int i = 0; i < s; i++) {
                if (!Objects.equals(es[i], otherEs[i])) {
                    equal = false;
                    break;
                }
            }
        }
        other.checkForComodification(otherModCount);
        return equal;
    }

    private void checkForComodification(final int expectedModCount) {
        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    public int hashCode() {
        int expectedModCount = modCount;
        int hash = hashCodeRange(0, size);
        checkForComodification(expectedModCount);
        return hash;
    }

    int hashCodeRange(int from, int to) {
        final Object[] es = elementData;
        if (to > es.length) {
            throw new ConcurrentModificationException();
        }
        int hashCode = 1;
        for (int i = from; i < to; i++) {
            Object e = es[i];
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    /**
     * Удаляет первое вхождение указанного элемента из списка (если таковой присутствует в списке)
     * Если в списке отсутствует указанный элемент, список не меняется.
     */
    public boolean remove(Object o) {
        final Object[] es = elementData;
        final int size = this.size;
        int i = 0;
        found: {
            if (o == null) {
                for (; i < size; i++)
                    if (es[i] == null)
                        break found;
            } else {
                for (; i < size; i++)
                    if (o.equals(es[i]))
                        break found;
            }
            return false;
        }
        fastRemove(es, i);
        return true;
    }

    /**
     * Private метод remove который не производит проверку границ
     * и не возвращает true или false
     */
    private void fastRemove(Object[] es, int i) {
        modCount++;
        final int newSize;
        if ((newSize = size - 1) > i)
            System.arraycopy(es, i + 1, es, i, newSize - i);
        es[size = newSize] = null;
    }

    /**
     * Удаляет все элементы в списке.
     * Список будет пуст по итогу.
     */
    public void clear() {
        modCount++;
        final Object[] es = elementData;
        for (int to = size, i = size = 0; i < to; i++)
            es[i] = null;
    }

    /**
     * Добавляет все элементы передаваемого списка в конец списка, метод для которого был вызван,
     * в том порядке, в котором элементы списка были возвращены итератором.
     * Поведение этой операции не определено в случае, если передаваемая коллекция модифицируется (изменяется)
     * в момент работы метода addAll.
     */
    public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        modCount++;
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + numNew);
        System.arraycopy(a, 0, elementData, s, numNew);
        size = s + numNew;
        return true;
    }

    /**
     * Добавляет все элементы передаваемого списка в список, метод для которого был вызван,
     * начиная с указанной позиции index.
     * Смещает элементы списка от позиции index и все последующие вправо, увеличивая их индекс
     * Новые элементы появятся в списке в порядке, в котором они были возвращены итератором
     */
    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheckForAdd(index);

        Object[] a = c.toArray();
        modCount++;
        int numNew = a.length;
        if (numNew == 0)
            return false;
        Object[] elementData;
        final int s;
        if (numNew > (elementData = this.elementData).length - (s = size))
            elementData = grow(s + numNew);

        int numMoved = s - index;
        if (numMoved > 0)
            System.arraycopy(elementData, index,
                    elementData, index + numNew,
                    numMoved);
        System.arraycopy(a, 0, elementData, index, numNew);
        size = s + numNew;
        return true;
    }

    /**
     * Версия rangeCheck, используемая add и addAll.
     */
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }

    /**
     * Создает подробное сообщение IndexOutOfBoundsException.
     */
    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Size: "+size;
    }

    /**
     * Сохраняет состояние экземпляра {@code ArrayList} в поток.
     */
    @java.io.Serial
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {
        int expectedModCount = modCount;
        s.defaultWriteObject();

        s.writeInt(size);

        for (int i=0; i<size; i++) {
            s.writeObject(elementData[i]);
        }

        if (modCount != expectedModCount) {
            throw new ConcurrentModificationException();
        }
    }

    /**
     * Восстанавливает экземпляр {@code ArrayList} из потока.
     */
    @java.io.Serial
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {

        s.defaultReadObject();

        s.readInt();

        if (size > 0) {

            SharedSecrets.getJavaObjectInputStreamAccess().checkArray(s, Object[].class, size);
            Object[] elements = new Object[size];

            for (int i = 0; i < size; i++) {
                elements[i] = s.readObject();
            }

            elementData = elements;
        } else if (size == 0) {
            elementData = EMPTY_ELEMENTDATA;
        } else {
            throw new java.io.InvalidObjectException("Некорректный размер: " + size);
        }
    }

    /**
     * Возвращает итератор списка по элементам в этом списке (в правильной последовательности),
     * начиная с указанной позиции в списке. Указанный индекс index указывает первый элемент,
     * который будет возвращен первоначальным вызовом next.
     * Первоначальный вызов предыдущего вернет элемент с указанным индексом index минус один.
     */
    public ListIterator<E> listIterator(int index) {
        rangeCheckForAdd(index);
        return new ListItr(index);
    }

    /**
     * Возвращает итератор списка по элементам в списке (в правильной последовательности).
     */
    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    // Оптимизированная версия AbstractList.Itr
    private class Itr implements Iterator<E> {
        int cursor;
        int lastRet = -1;
        int expectedModCount = modCount;

        Itr() {}

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            checkForComodification();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) elementData[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                MyArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = MyArrayList.this.size;
            int i = cursor;
            if (i < size) {
                final Object[] es = elementData;
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept(elementAt(es, i));
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    // Оптимизированная версия AbstractList.ListItr
    private class ListItr extends Itr implements ListIterator<E> {
        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() {
            return cursor != 0;
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor - 1;
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            checkForComodification();
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = MyArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) elementData[lastRet = i];
        }

        public void set(E e) {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                MyArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            checkForComodification();

            try {
                int i = cursor;
                MyArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }
}