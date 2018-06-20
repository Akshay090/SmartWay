package com.idealtechcontrivance.ashish.examcracker.Model;

public class Testimonials {
    private int id;
    private String name, testimonial, image;

    public Testimonials() {
    }

    public Testimonials(int id, String name, String testimonial, String image) {
        this.id = id;
        this.name = name;
        this.testimonial = testimonial;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTestimonial() {
        return testimonial;
    }

    public void setTestimonial(String testimonial) {
        this.testimonial = testimonial;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
