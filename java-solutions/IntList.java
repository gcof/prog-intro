import java.util.Arrays;

public class IntList {

    private int[] data;
    private int size;

    IntList() {
        data = new int[1];
        size = 0;
    }

    IntList(int []data) {
        this.data = data;
        size = data.length;
    }

    public void add(int value) {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
        data[size++] = value;
    }

    public void add(IntList value) {
        for (int i = 0; i < value.size; i++) {
            add(value.get(i));
        }
    }

    public boolean contains(int value) {
        for (int i = 0; i < size; i++) {
            if (data[i] == value) {
                return true;
            }
        }
        return false;
    }

    public void delete() {
        if (size < 0) {
            throw new IndexOutOfBoundsException();
        }
        size--;
        if(size < data.length / 4) {
            data = Arrays.copyOf(data, data.length / 2);
        }
    }

    public int get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return data[index];
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i != size - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}
