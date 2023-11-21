package com.jpa.sample.repository;

import com.jpa.sample.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
// 기본적인 CRUD는 JpaRepository에 이미 정의되어 있다.
public interface ItemRepository extends JpaRepository<Item, Long> {
    // Q : 왜 interface로 정의 되는가?
    // A : 조금 더 명확하게 표현하자면 다음과 같이 요약할 수 있습니다:
    //
    //"레포지토리는 인터페이스로 정의되는데, 이는 스프링 데이터 JPA가 이 인터페이스를 상속받아 구현체를 생성해주기 때문입니다. 생성된 구현체는 내부적으로 JPA 구현체(예: 하이버네이트)를 사용하여 데이터베이스 작업을 수행합니다."
    //
    //즉, 레포지토리 인터페이스를 직접 상속받는 주체는 스프링 데이터 JPA이고, 이로 인해 개발자는 SQL 쿼리 없이도 데이터베이스 작업을 수행할 수 있습니다. 그리고 이 작업은 내부적으로 JPA 구현체를 통해 이루어집니다.
    //스프링 데이터 JPA는 개발자가 정의한 레포지토리 인터페이스를 바탕으로 그 인터페이스의 구현체를 자동으로 생성합니다. 이때, 이 생성된 구현체가 실제로 데이터베이스와 통신하는 작업은 JPA 구현체(예: 하이버네이트)를 활용하여 수행합니다.
    //
    //즉, 스프링 데이터 JPA의 구현체는 내부적으로 하이버네이트와 같은 JPA 구현체를 사용하여 데이터베이스 작업을 수행하는 것을 말합니다.
    //
    //예를 들어, findAll()이라는 메서드가 레포지토리 인터페이스에 정의되어 있다면, 스프링 데이터 JPA는 이 메서드의 구현체를 생성합니다. 이 findAll() 메서드가 호출될 때, 스프링 데이터 JPA는 내부적으로 하이버네이트를 사용해서 모든 데이터를 조회하는 쿼리를 실행하게 됩니다.
    //
    //이렇게 스프링 데이터 JPA는 개발자가 직접 복잡한 쿼리를 작성하지 않아도 되도록, 인터페이스의 메서드를 호출하면 내부적으로 필요한 데이터베이스 작업을 수행하는 구현체를 생성해주는 역할을 합니다. 이 과정에서 JPA 구현체인 하이버네이트가 실제로 데이터베이스 작업을 수행하는 역할을 하게 됩니다.


    // update는 어떻게 할까?

    List<Item> findByItemName(String itemName);

    // OR 조건 처리
    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    // LessThan 조건 처리 : price 변수보다 작은 상품 데이터 조회
    List<Item> findByPriceLessThan(Integer price);
    // 왜 int형이 아닌 Integer형을 사용할까?
    // Int : NULL값을 가질 수 없다. Integer : NULL 값을 가질 수 있기 때문이다.

    // OrderBy로 정렬하기
    List<Item> findAllByOrderByPriceDesc();

    // JPQL 쿼리 작성하기 : SQL과 유사한 객체 지향 쿼리 언어
    // 실제 쿼리문이 아닌, (Item) 엔티티에게 쿼리를 날린다.
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> priceSorting(@Param("itemDetail") String itemDetail);

    // nativeQuery 사용
    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> priceSortingNative(@Param("itemDetail") String itemDetail);

}
