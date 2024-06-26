package com.kuku9.goods.domain.event.controller;

import com.kuku9.goods.domain.event.dto.AllEventResponse;
import com.kuku9.goods.domain.event.dto.EventRequest;
import com.kuku9.goods.domain.event.dto.EventUpdateRequest;
import com.kuku9.goods.domain.event.service.EventService;
import com.kuku9.goods.global.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "이벤트 등록")
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<String> createEvent(
        @Valid @RequestBody EventRequest request,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long eventId = eventService.createEvent(request, customUserDetails.getUser());

        return ResponseEntity.created(URI.create("/api/v1/events/" + eventId)).build();
    }

    @Operation(summary = "이벤트 수정")
    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<String> updateEvent(
        @Valid @RequestBody EventUpdateRequest request,
        @PathVariable Long eventId, @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        Long updatedEventId = eventService.updateEvent(eventId, request,
            customUserDetails.getUser());

        return ResponseEntity.created(URI.create("/api/v1/events/" + updatedEventId)).build();
    }

    @Operation(summary = "이벤트 단건 조회")
    @GetMapping("/{eventId}")
    public ResponseEntity<AllEventResponse> getEvent(@PathVariable Long eventId) {

        AllEventResponse allEventResponse = eventService.getEvent(eventId);

        return ResponseEntity.ok().body(allEventResponse);
    }

    @Operation(summary = "이벤트 전체 조회")
    @GetMapping
    public ResponseEntity<Page<AllEventResponse>> getAllEvents(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "createdAt"));
        Page<AllEventResponse> eventResponses = eventService.getAllEvents(pageable);

        return ResponseEntity.ok().body(eventResponses);
    }

    @Operation(summary = "이벤트 삭제")
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<Void> deleteEvent(
        @PathVariable Long eventId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        eventService.deleteEvent(eventId, customUserDetails.getUser());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "이벤트 상품 삭제")
    @DeleteMapping("/event-products/{eventProductId}")
    @PreAuthorize("hasAnyRole('ROLE_SELLER')")
    public ResponseEntity<Void> deleteEventProduct(
        @PathVariable Long eventProductId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails) {

        eventService.deleteEventProduct(eventProductId, customUserDetails.getUser());

        return ResponseEntity.ok().build();
    }
}
