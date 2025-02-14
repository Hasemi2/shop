package com.practice.shop.item.service;

import com.practice.shop.item.domain.Item;
import com.practice.shop.item.domain.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemService {

  private final ItemRepository itemRepository;

  @Transactional
  public void saveItem(Item item) {
    itemRepository.save(item);
  }

  public Item findById(Long itemId) {
    return itemRepository.findById(itemId)
        .orElseThrow(() -> new IllegalStateException("존재하지 않는 상품입니다."));
  }
}
