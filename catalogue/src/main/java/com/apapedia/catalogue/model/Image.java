package com.apapedia.catalogue.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "image_data")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImage;
    private String name;
    private String type;

    @Column(name="image_data", length = 50000000)
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_catalog", referencedColumnName = "id_catalog")
    private Catalog catalogId;

    public Image(String originalFilename, String contentType, byte[] bytes) {
    }

    // constructor
//    public Image(String originalFilename, String contentType, byte[] bytes) {
//    }
}
