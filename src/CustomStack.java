public class CustomStack<T> {
    private Node<T> top;  // Nodo en la parte superior de la pila
    private int size;     // Tamaño de la pila

    private static class Node<T> {
        private T data;    // Datos almacenados en el nodo
        private Node<T> next;  // Referencia al siguiente nodo

        public Node(T data) {
            this.data = data;
        }
    }

    // Constructor de la clase CustomStack
    public CustomStack() {
        top = null;  // Inicialmente, la pila está vacía
        size = 0;    // El tamaño se establece en cero
    }

    // Método para agregar un elemento a la pila
    public void push(T item) {
        Node<T> newNode = new Node<>(item); // Se crea un nuevo nodo con el elemento
        newNode.next = top; // El nuevo nodo apunta al nodo actual en la parte superior
        top = newNode;      // El nuevo nodo se convierte en la parte superior
        size++;            // Incrementa el tamaño de la pila
    }

    // Método para eliminar y devolver el elemento en la parte superior de la pila
    public T pop() {
        if (isEmpty()) {
            return null;  // Si la pila está vacía, no se puede desapilar, se devuelve null
        }
        T data = top.data;  // Se obtiene el dato del nodo en la parte superior
        top = top.next;     // Se actualiza la parte superior para apuntar al siguiente nodo
        size--;             // Se reduce el tamaño de la pila
        return data;        // Se devuelve el dato eliminado
    }

    // Método para obtener el elemento en la parte superior de la pila sin eliminarlo
    public T peek() {
        if (isEmpty()) {
            return null;  // Si la pila está vacía, no hay nada que ver, se devuelve null
        }
        return top.data;  // Se devuelve el dato en la parte superior
    }

    // Método para verificar si la pila está vacía
    public boolean isEmpty() {
        return top == null;  // La pila está vacía si no hay nodo en la parte superior
    }

    // Método para obtener el tamaño de la pila
    public int size() {
        return size;  // Devuelve el número de elementos en la pila
    }

    // Método para vaciar la pila
    public void clear() {
        top = null;  // Se establece la parte superior en null para vaciar la pila
        size = 0;    // Se restablece el tamaño a cero
    }
}
