package main

import (
	pb "github.com/thai94/technology-research/go/grpc_2/routeguide"
)

type routeGuideServer struct {
	pb.UnimplementedRouteGuideServer
}
