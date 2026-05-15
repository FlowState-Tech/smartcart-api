create table if not exists store_analytics_model (
    store_id bigint primary key,
    total_views bigint not null,
    abandoned_carts int not null,
    conversion_rate numeric(6, 4) not null
);

create table if not exists store_analytics_top_products (
    store_id bigint not null,
    position int not null,
    sku varchar(50) not null,
    primary key (store_id, position),
    constraint fk_store_analytics_top_products_store
        foreign key (store_id) references store_analytics_model(store_id)
);
