create table if not exists adjustment_inventory_quantity_histories
(
    id         bigint auto_increment
        primary key,
    value      longtext  null,
    created_at timestamp null,
    updated_at timestamp null
)
    collate = utf8mb4_bin;

create table if not exists config_prices
(
    id                bigint auto_increment
        primary key,
    type              int            null comment '1 = Phụ tùng, 2 = Dịch vụ',
    customer_type_id  bigint         null,
    product_id        bigint         null,
    garage_service_id bigint         null,
    price             decimal(14, 2) null,
    garage_id         bigint         null,
    created_at        timestamp      null,
    updated_at        timestamp      null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists customer_types
(
    id                 bigint auto_increment
        primary key,
    customer_type_name varchar(255) null,
    description        text         null,
    garage_id          bigint       null,
    created_at         timestamp    null,
    updated_at         timestamp    null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists distributors
(
    id            bigint auto_increment
        primary key,
    code          varchar(255) null,
    name          varchar(500) null,
    province_id   bigint       null,
    district_id   bigint       null,
    ward_id       bigint       null,
    address       varchar(500) null,
    contact_name  varchar(255) null,
    contact_phone varchar(255) null,
    garage_id     bigint       null,
    is_delete     tinyint      null,
    created_at    timestamp    null,
    updated_at    timestamp    null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists employees
(
    id            bigint auto_increment
        primary key,
    full_name     varchar(255) null,
    gender        int          null,
    birthday      date         null,
    type          varchar(255) null,
    status        int          null comment '0 = Dang lam viec, 1 = Da nghi viec, 2 = Tam nghi',
    contract_type int          null comment '1 = Nhan su chinh thuc, 2 = Nhan su thue ngoai',
    start_date    date         null,
    end_date      date         null,
    garage_id     bigint       null,
    created_at    timestamp    null,
    updated_at    timestamp    null,
    phone_number  varchar(30)  null
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table if not exists garage_groups
(
    id                bigint auto_increment
        primary key,
    name              varchar(100) not null,
    code              varchar(40)  not null,
    phone             varchar(20)  not null,
    website           varchar(100) null,
    email             varchar(100) null,
    tax_code          varchar(40)  null,
    description       longtext     null,
    insurance_partner varchar(100) null,
    management_exp    longtext     null comment 'kinh nghiệm của quản lí thuộc về insurance partner',
    place_of_issue    varchar(255) null,
    date_of_issue     date         null,
    address           tinytext     null,
    status            int          not null,
    type              int          null,
    created_at        timestamp    null,
    updated_at        timestamp    null,
    constraint code
        unique (code)
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists garage_owners
(
    id              bigint auto_increment
        primary key,
    name            varchar(255) not null,
    code            varchar(40)  not null,
    user_name       varchar(255) not null,
    phone           varchar(20)  not null,
    email           varchar(100) null,
    identified_card varchar(255) null,
    date_of_issue   date         null,
    place_of_issue  varchar(255) null,
    gender          int          null,
    birthday        date         null,
    avatar          text         null,
    keycloak_id     varchar(500) null,
    status          int          null,
    created_at      timestamp    null,
    updated_at      timestamp    null,
    constraint code
        unique (code)
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists garage_service_types
(
    id          bigint auto_increment
        primary key,
    name        varchar(255) not null,
    description text         null,
    garage_id   bigint       null,
    created_at  timestamp    null,
    updated_at  timestamp    null
)
    collate = utf8mb4_unicode_ci;

create table if not exists garage_services
(
    id                     bigint auto_increment
        primary key,
    code                   varchar(255)         null,
    name                   varchar(255)         not null,
    garage_service_type_id bigint               null,
    description            text                 null,
    garage_id              bigint               null,
    is_active              tinyint(1) default 1 null,
    created_at             timestamp            null,
    updated_at             timestamp            null
)
    collate = utf8mb4_unicode_ci;

create table if not exists garages
(
    id                       bigint auto_increment
        primary key,
    name                     varchar(100)   not null,
    code                     varchar(40)    not null,
    contact_point_name       varchar(255)   null,
    contact_point_phone      varchar(40)    not null,
    contact_point_email      varchar(40)    null,
    avatar                   text           null,
    images                   text           null,
    contract_from_date       datetime       null,
    contract_to_date         datetime       null,
    contract_status          int            null comment '0 = OUTSIDE, 1 = SIGNED',
    latitude                 decimal(10, 8) not null,
    longitude                decimal(11, 8) not null,
    address                  varchar(200)   not null,
    ward_id                  bigint         not null,
    district_id              bigint         not null,
    province_id              bigint         not null,
    max_capacity             int            null,
    car_lift                 int            null,
    max_tonnage              int            null,
    number_gathering_point   int            null,
    garage_group_id          bigint         not null,
    open                     varchar(40)    null,
    close                    varchar(40)    null,
    support_sos              int            null comment '0  = NOT SUPPORT, 1 = SUPPORT',
    service_time             int            null comment '0 = NO, 1 = YES',
    garage_type              int            null comment '1 = PULLBACK, 2 = ROVING, 3 = ALL',
    description              text           null,
    average_rating           decimal(3, 1)  null,
    number_of_booked         int            null,
    number_of_rating         int            null,
    number_of_service_ticket int            null,
    garage_area              int            null,
    num_employee             int            null,
    equipment                varchar(255)   null,
    is_verified              tinyint(1)     null,
    status                   int            null,
    created_at               timestamp      null,
    updated_at               timestamp      null,
    logo                     text           null comment 'Dùng minio nhé ^^',
    additional_information   longtext       null,
    constraint code
        unique (code),
    constraint garages_garage_groups_id_fk
        foreign key (garage_group_id) references garage_groups (id)
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists customers
(
    id               bigint auto_increment
        primary key,
    customer_type_id bigint       null,
    full_name        varchar(250) null,
    phone_number     varchar(50)  null,
    address          tinytext     null,
    garage_id        bigint       null,
    created_at       timestamp    null,
    updated_at       timestamp    null,
    constraint unique_by_phone_and_garage_id
        unique (phone_number, garage_id),
    constraint customers_customer_types_id_fk
        foreign key (customer_type_id) references customer_types (id),
    constraint customers_garages_id_fk
        foreign key (garage_id) references garages (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table if not exists cars
(
    id             bigint auto_increment
        primary key,
    car_name       text        null,
    car_brand_id   bigint      null,
    car_model_id   bigint      null,
    car_year_id    bigint      null,
    car_version_id bigint      null,
    license_plate  varchar(50) null,
    vin_number     varchar(50) null,
    customer_id    bigint      null,
    garage_id      bigint      null,
    created_at     timestamp   null,
    updated_at     timestamp   null,
    constraint unique_license_plate_and_garage_id
        unique (license_plate, garage_id),
    constraint cars_customers_id_fk
        foreign key (customer_id) references customers (id),
    constraint cars_garages_id_fk
        foreign key (garage_id) references garages (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table if not exists garage_garage_owners
(
    garage_owner_id bigint not null,
    garage_id       bigint not null,
    primary key (garage_owner_id, garage_id),
    constraint fk_garagegarageowner_garage
        foreign key (garage_id) references garages (id),
    constraint fk_garagegarageowner_garageowner
        foreign key (garage_owner_id) references garage_owners (id)
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists generator_code_tickets
(
    id             bigint auto_increment
        primary key,
    code           varchar(45) not null,
    current_ticket int         not null,
    current_month  varchar(45) not null,
    garage_id      bigint      not null
)
    collate = utf8mb4_bin;

create table if not exists generators
(
    code       varchar(255) not null
        primary key,
    value      text         not null,
    created_at timestamp    null,
    updated_at timestamp    null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists inbound_products
(
    id                bigint auto_increment
        primary key,
    inbound_ticket_id bigint         null,
    product_id        bigint         null,
    distributor_id    bigint         null,
    unit              varchar(255)   null,
    request_quantity  decimal(10, 2) null,
    export_quantity   decimal(10, 2) null,
    note              varchar(255)   null,
    status            int            null comment '1 = Da nhap kho, 2 = Chua nhap kho',
    created_at        timestamp      null,
    updated_at        timestamp      null
)
    collate = utf8mb4_bin;

create table if not exists inbound_tickets
(
    id             bigint auto_increment
        primary key,
    code           varchar(255) null,
    type           int          null comment '1 = Mua hang NPP, 2 = KH doi tra (phieu DV), 3 = Hoan hang (phieu BH)',
    distributor_id bigint       null,
    ticket_id      bigint       null comment 'Id cua phieu dich vu/ ban hang',
    note           varchar(255) null,
    products       longtext     null,
    status         int          null comment '1 = Xac nhan, 2 = Chua xac nhan',
    garage_id      bigint       null,
    created_at     timestamp    null,
    updated_at     timestamp    null
)
    collate = utf8mb4_bin;

create table if not exists integration_configs
(
    id               bigint auto_increment
        primary key,
    integration_name varchar(55) null,
    value            text        null,
    garage_id        bigint      null,
    created_at       timestamp   null,
    updated_at       timestamp   null
)
    collate = utf8mb4_bin;

create table if not exists inventories
(
    id          bigint auto_increment
        primary key,
    name        varchar(500) null,
    description text         null,
    garage_id   bigint       null,
    created_at  timestamp    null,
    updated_at  timestamp    null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists inventory_histories
(
    id              bigint auto_increment
        primary key,
    counters_name   tinytext       null comment 'Người kiểm kê',
    approvers       tinytext       null,
    count_date      date           null comment 'Ngày kiểm kê',
    adjustment_date date           null,
    error_rate      decimal(10, 2) null,
    can_adjustment  tinyint(1)     null,
    inventory_id    bigint         null,
    created_at      timestamp      null,
    updated_at      timestamp      null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists inventory_histories_detail
(
    id                   bigint auto_increment
        primary key,
    product_id           bigint         null,
    system_unit_price    decimal(14, 2) null,
    reality_unit_price   decimal(14, 2) null,
    system_inventory     decimal(14, 2) null,
    reality_inventory    decimal(14, 2) null,
    inventory_history_id bigint         null,
    created_at           timestamp      null,
    updated_at           timestamp      null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists log_order_distributor
(
    id                   bigint auto_increment
        primary key,
    order_distributor_id bigint      null,
    log_version          int         null,
    products             longtext    null,
    action_user          varchar(45) null,
    created_at           timestamp   null,
    updated_at           timestamp   null
)
    collate = utf8mb4_bin;

create table if not exists log_sell_spare_part
(
    id                 bigint auto_increment
        primary key,
    sell_spare_part_id bigint      null,
    log_version        int         null,
    products           longtext    null,
    action_user        varchar(45) null,
    created_at         timestamp   null,
    updated_at         timestamp   null
)
    collate = utf8mb4_bin;

create table if not exists logs
(
    id                     bigint auto_increment
        primary key,
    params                 text      null,
    request                json      null,
    response               json      null,
    additional_information json      null,
    created_at             timestamp null,
    updated_at             timestamp null
)
    collate = utf8mb4_bin;

create table if not exists notification_configs
(
    id           bigint auto_increment
        primary key,
    notification text         null,
    title        varchar(500) null,
    body         text         null,
    image        text         null,
    created_at   timestamp    null,
    updated_at   timestamp    null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists order_distributor_products
(
    id                   bigint auto_increment
        primary key,
    order_distributor_id bigint         null,
    product_id           bigint         null,
    product_code         varchar(255)   null,
    product_name         tinytext       null,
    quantity             decimal(10, 2) null,
    unit                 varchar(255)   null,
    unit_price           decimal(10, 2) null,
    discount             decimal(10, 2) null,
    tax                  decimal(10, 2) null,
    price                decimal(10, 2) null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists orders_distributor
(
    id              bigint auto_increment
        primary key,
    order_code      varchar(255)   null,
    distributor_id  bigint         null,
    delivery_type   varchar(255)   null,
    delivery_status int default 2  null,
    payment_status  int default 1  null,
    discount        decimal(14, 2) null,
    tax             decimal(14, 2) null,
    total_price     decimal(14, 2) null,
    products        longtext       null,
    discount_type   int            null comment '1 = Trừ tiền, 2 = Theo %',
    garage_id       bigint         null,
    inventory_id    bigint         null,
    created_at      timestamp      null,
    updated_at      timestamp      null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists outbound_products
(
    id                 bigint auto_increment
        primary key,
    outbound_ticket_id bigint         null,
    product_id         bigint         null,
    unit               varchar(255)   null,
    distributor_id     bigint         null,
    request_quantity   decimal(10, 2) null,
    export_quantity    decimal(10, 2) null,
    note               longtext       null,
    status             int            null comment '1 = Da xuat, 2 = Chua xuat',
    created_at         timestamp      null,
    updated_at         timestamp      null
)
    collate = utf8mb4_bin;

create table if not exists outbound_tickets
(
    id             bigint auto_increment
        primary key,
    code           varchar(255) null,
    type           int          null comment '1 = Tra hang NPP, 2 = Dich vu, 3 = Ban hang',
    distributor_id bigint       null,
    ticket_id      bigint       null,
    products       longtext     null,
    note           longtext     null,
    status         int          null comment '1 = Da xac thuc, 2 = Chua xac thuc',
    garage_id      bigint       null,
    created_at     timestamp    null,
    updated_at     timestamp    null
)
    collate = utf8mb4_bin;

create table if not exists product_compatibilities
(
    id             bigint auto_increment
        primary key,
    product_id     bigint null,
    car_brand_id   bigint null,
    car_model_id   bigint null,
    car_year_id    bigint null,
    car_version_id bigint null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists product_logs
(
    id          bigint auto_increment
        primary key,
    product_id  bigint      not null,
    action_user varchar(45) null,
    old_value   text        null,
    new_value   text        null,
    created_at  timestamp   null,
    updated_at  timestamp   null
)
    collate = utf8mb4_bin;

create table if not exists products
(
    id                bigint auto_increment
        primary key,
    code              varchar(255)   null,
    name              varchar(255)   null,
    parent_product_id bigint         null,
    distributor_id    bigint         null,
    purchase_price    decimal(10, 2) null,
    quantity          decimal(10, 2) null,
    unit              varchar(255)   null,
    spare_part_type   int            null,
    inventory_id      bigint         null,
    garage_id         bigint         null,
    first_sell_at     date           null,
    created_at        timestamp      null,
    updated_at        timestamp      null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists project_configs
(
    id    bigint auto_increment
        primary key,
    code  varchar(45) not null,
    value text        not null
)
    collate = utf8mb4_bin;

create table if not exists quotation_infos
(
    id                  bigint auto_increment
        primary key,
    quotation_id        bigint         null,
    type                int            null comment '1 = Phụ tùng, 2 = Dịch vụ',
    product_id          bigint         null,
    garage_service_id   bigint         null,
    unit_price          decimal(14, 2) null,
    quantity            decimal(10, 2) null,
    discount            decimal(14, 2) null,
    tax                 decimal(14, 2) null,
    origin_price        decimal(14, 2) null,
    price               decimal(14, 2) null,
    employee_id         bigint         null,
    status              int            null,
    outbound_product_id bigint         null,
    created_at          timestamp      null,
    updated_at          timestamp      null,
    constraint quotation_infos_garage_services_id_fk
        foreign key (garage_service_id) references garage_services (id),
    constraint quotation_infos_products_id_fk
        foreign key (product_id) references products (id),
    constraint quotation_infos_quotation_infos_id_fk
        foreign key (quotation_id) references quotation_infos (id)
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists quotation_product_logs
(
    id           bigint auto_increment
        primary key,
    quotation_id bigint    null,
    log_version  int       null,
    content      longtext  not null,
    created_at   timestamp null,
    updated_at   timestamp null
)
    collate = utf8mb4_bin;

create table if not exists repair_orders
(
    id                     bigint auto_increment
        primary key,
    code                   varchar(255)  null,
    garage_id              bigint        null,
    customer_id            bigint        null,
    car_id                 bigint        null,
    services               text          null,
    appointment_date       date          null,
    time_frame             int           null,
    odo                    int           null,
    description            text          null,
    expected_handover_date datetime      null,
    status                 int default 1 null,
    payment_status         int default 1 null,
    canceled_at            timestamp     null,
    note                   text          null,
    created_at             timestamp     null,
    updated_at             timestamp     null,
    created_by             varchar(45)   null,
    constraint repair_orders___fk
        foreign key (garage_id) references garages (id),
    constraint repair_orders_cars_id_fk
        foreign key (car_id) references cars (id),
    constraint repair_orders_customers_id_fk
        foreign key (customer_id) references customers (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table if not exists diagnoses
(
    id              bigint auto_increment
        primary key,
    repair_order_id bigint    null,
    employee_id     bigint    null,
    status          int       null,
    description     longtext  null,
    images          longtext  null,
    created_at      timestamp null,
    updated_at      timestamp null,
    constraint diagnoses_repair_orders_id_fk
        foreign key (repair_order_id) references repair_orders (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

create table if not exists quotations
(
    id              bigint auto_increment
        primary key,
    repair_order_id bigint         null,
    diagnose_id     bigint         null,
    type            int            null comment '1 = Chọn dịch vụ, 2 = Hình ảnh',
    total_price     decimal(14, 2) null,
    images          longtext       null,
    status          int            null,
    discount_type   int            null comment '1 = Trừ tiền, 2 = Theo %',
    created_by      bigint         null,
    updated_by      bigint         null,
    created_at      timestamp      null,
    updated_at      timestamp      null,
    constraint quotations_repair_orders_id_fk
        foreign key (repair_order_id) references repair_orders (id)
)
    charset = utf8mb3
    row_format = DYNAMIC;

create index repair_orders_car_id_index
    on repair_orders (car_id);

create index repair_orders_customer_id_index
    on repair_orders (customer_id);

create table if not exists sell_spare_part_products
(
    id                 bigint auto_increment
        primary key,
    sell_spare_part_id bigint         null,
    product_id         bigint         null,
    unit               varchar(255)   null,
    quantity           decimal(10, 2) null,
    unit_price         decimal(10, 2) null,
    discount           decimal(10, 2) null,
    tax                decimal(10, 2) null,
    original_price     decimal(10, 2) null,
    price              decimal(10, 2) null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

create table if not exists sell_spare_parts
(
    id              bigint auto_increment
        primary key,
    sell_code       varchar(255)   null,
    customer_id     bigint         null,
    delivery_status int default 1  null,
    payment_status  int default 1  null,
    discount        decimal(14, 2) null,
    tax             decimal(14, 2) null,
    total_price     decimal(14, 2) null,
    products        longtext       null,
    discount_type   int            null comment '1 = Trừ tiền, 2 = Theo %',
    garage_id       bigint         null,
    inventory_id    bigint         null,
    created_at      timestamp      null,
    updated_at      timestamp      null
)
    collate = utf8mb4_unicode_ci
    row_format = DYNAMIC;

