package lesson4;

import java.util.*;
import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        SortedMap<Character, Node> children = new TreeMap<>();
    }

    private final Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     *
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     *
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new TrieIterator();
    }

    public class TrieIterator implements Iterator<String> {
        private final List<String> listWord = new ArrayList<>();
        private int count;
        private String current;

        // Ресурсоёмкость: O(n)
        // Трудоёмксость: O(n)

        private TrieIterator() {
            count = 0;
            addToList(root, "");
        }

        private void addToList(Node node, String word) {
            for (char child : node.children.keySet()) {
                if (child == (char) 0)
                    listWord.add(word);
                else
                    addToList(node.children.get(child), word + child);
            }
        }

        // Ресурсоёмкость: O(1)
        // Трудоёмксость: O(1)

        @Override
        public boolean hasNext() {
            return count < listWord.size();
        }

        // Ресурсоёмкость: O(1)
        // Трудоёмксость: O(1)

        @Override
        public String next() {
            if (count >= listWord.size())
                throw new NoSuchElementException();
            current = listWord.get(count);
            count++;
            return current;
        }

        // Ресурсоёмкость: O(1)
        // Трудоёмксость: O(1)

        @Override
        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            Trie.this.remove(current);
            current = null;
        }
    }

}