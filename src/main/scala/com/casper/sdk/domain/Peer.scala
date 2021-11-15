package com.casper.sdk.domain

case class Peer(
                 node_id: String,
                 address: String
               )

case class Peers(
                  peers: List[Peer]
                )
