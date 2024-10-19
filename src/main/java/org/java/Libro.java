package org.java;

public class Libro {
    private String isbn;
    private String name;
    private String author;
    private String category;
    private String price;
    private String quantity;

    public Libro(String isbn, String name, String author, String category, String price, String quantity) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    // Metodo de impresion de los datos del libro
    @Override
    public String toString() {
        StringBuilder jsonBuilder = new StringBuilder("{");

        if (isbn != null && !isbn.isEmpty()) {
            jsonBuilder.append(String.format("\"isbn\":\"%s\",", isbn));
        }
        if (name != null && !name.isEmpty()) {
            jsonBuilder.append(String.format("\"name\":\"%s\",", name));
        }
        if (author != null && !author.isEmpty()) {
            jsonBuilder.append(String.format("\"author\":\"%s\",", author));
        }
        if (category != null && !category.isEmpty()) {
            jsonBuilder.append(String.format("\"category\":\"%s\",", category));
        }
        if (price != null && !price.isEmpty()) {
            jsonBuilder.append(String.format("\"price\":\"%s\",", price));
        }
        if (quantity != null && !quantity.isEmpty()) {
            jsonBuilder.append(String.format("\"quantity\":\"%s\",", quantity));
        }

        jsonBuilder.append("}");

        // Eliminar la coma final si existe antes de los campos numéricos
        int lastCommaIndex = jsonBuilder.lastIndexOf(",");
        if (lastCommaIndex != -1 && jsonBuilder.charAt(lastCommaIndex + 1) != '"') {
            jsonBuilder.deleteCharAt(lastCommaIndex);
        }

        return jsonBuilder.toString();
    }


}
