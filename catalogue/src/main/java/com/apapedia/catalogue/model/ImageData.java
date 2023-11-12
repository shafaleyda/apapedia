package com.apapedia.catalogue.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "image_data")
public class ImageData {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String idImage;

    private String name;
    private String type;

    @Lob
    private byte[] data;


}

//    @Column(name="image_data", length = 50000000)
//    private byte[] imageData;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "id_catalog", referencedColumnName = "id_catalog")
//    private Catalog catalogId;

//    public ImageData(String originalFilename, String contentType, byte[] bytes) {
//    }

// constructor
//    public ImageData(String originalFilename, String contentType, byte[] bytes) {
//    }
