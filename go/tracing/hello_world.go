package main

import (
	"github.com/opentracing/opentracing-go"
	"github.com/uber/jaeger-client-go"
	jaegercfg "github.com/uber/jaeger-client-go/config"
	jaegerlog "github.com/uber/jaeger-client-go/log"
)

func main() {

	cfg := jaegercfg.Configuration{
		ServiceName: "hello go tracing",
		Sampler: &jaegercfg.SamplerConfig{
			Type:  jaeger.SamplerTypeConst,
			Param: 1,
		},
		Reporter: &jaegercfg.ReporterConfig{
			LogSpans: true,
		},
	}
	jLogger := jaegerlog.StdLogger

	tracer, closer, err := cfg.NewTracer(
		jaegercfg.Logger(jLogger),
	)

	if err != nil {
		return
	}

	opentracing.SetGlobalTracer(tracer)

	method1()

	defer closer.Close()
}

func method1() {

	tracer := opentracing.GlobalTracer()
	span := tracer.StartSpan("method1")
	println("method1")
	method2(span)
	defer span.Finish()
}

func method2(parentSpan opentracing.Span) {

	tracer := opentracing.GlobalTracer()
	span := tracer.StartSpan("method2",
		opentracing.ChildOf(parentSpan.Context()))

	println("method2")

	defer span.Finish()
}
