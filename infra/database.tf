# Copyright (c) TIKI Inc.
# MIT license. See LICENSE file in root directory.

resource "digitalocean_database_cluster" "db-cluster-l0-index" {
  name                 = "l0-index-db-cluster-${local.region}"
  engine               = "pg"
  version              = "14.6"
  size                 = "db-s-1vcpu-1gb"
  region               = local.region
  node_count           = 1
  private_network_uuid = local.vpc_uuid
}

resource "digitalocean_database_db" "db-l0-index" {
  cluster_id = digitalocean_database_cluster.db-cluster-l0-index.id
  name       = "l0_index"
}

resource "digitalocean_database_firewall" "db-cluster-l0-index-fw" {
  cluster_id = digitalocean_database_cluster.db-cluster-l0-index.id

  rule {
    type  = "app"
    value = digitalocean_app.l0-index-app.id
  }
}

resource "digitalocean_database_user" "db-user-l0-index" {
  cluster_id = digitalocean_database_cluster.db-cluster-l0-index.id
  name       = "l0-index-service"
}
