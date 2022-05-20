package model.data.entity

import java.sql.Time
import javax.persistence.*

@Entity
@Table(name = "client")
open class Client {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "client_chat_id")
    open var clientChatId: Long? = null


}

@Entity
@Table(name = "platform")
open class Platform {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @Column(name = "name")
    open var name: String? = null


}

@Entity
@Table(name = "platform_property")
open class PlatformProperty {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @JoinColumn(name = "platform_id")
    @OneToOne
    open var platformId: Platform? = null

    @Column(name = "property_name")
    open var propertyName: String? = null
}

@Entity
@Table(name = "property")
open class Property {


    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @JoinColumn(name = "client_id")
    @OneToOne
    open var clientId: Client? = null

    @JoinColumn(name = "platform_prop_id")
    @OneToOne
    open var platformPropId: PlatformProperty? = null

    @Column(name = "value")
    open var value: String? = null


}

@Entity
@Table(name = "subscribe")
open class Subscribe {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @JoinColumn(name = "client_id")
    @OneToOne
    open var clientId: Client? = null

    @JoinColumn(name = "platform_id")
    @OneToOne
    open var platformId: Platform? = null

    @Column(name = "groupName")
    open var group: String = ""

    @Column(name = "time")
    open var time: Time? = null

    @Column(name = "get_update")
    open var getUpdate: Boolean = false

    override fun toString(): String =
        "Subscribe(\n" +
                "id: $id\n" +
                "cid: $clientId\n" +
                "pfid: $platformId\n" +
                "group: $group\n" +
                "time: $time\n" +
                "getupd: $getUpdate\n" +
                ")"
}

