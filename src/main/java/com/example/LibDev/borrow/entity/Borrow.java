package com.example.LibDev.borrow.entity;

import com.example.LibDev.borrow.entity.type.Status;
import com.example.LibDev.global.entity.BaseEntity;
import com.example.LibDev.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
public class Borrow extends BaseEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dueDate; // 반납 예정일

    private LocalDateTime returnDate; // 실제 반납일

    private boolean extended; // 연장 여부

    private boolean overdue; // 연체 여부

    @Builder.Default
    private int overdueDays = 0; // 연체일 수(초기값 0)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Status status = Status.BORROWED; // 대출 상태

    @Column(name = "book_id", nullable = false)
    private Long bookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}