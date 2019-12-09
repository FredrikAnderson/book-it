package com.fredrik.bookit;

import java.util.HashSet;
import java.util.Set;

import com.fredrik.bookit.web.controller.ProjectsResource;
import com.fredrik.bookit.web.controller.ResourcesResource;

public class RestApplication { // extends Application {

//  public RestApplication() {
//    BeanConfig beanConfig = new BeanConfig();
//    beanConfig.setBasePath("/api/");
//    beanConfig.setVersion("1.0.0");
//    beanConfig.setResourcePackage("com.fredrik.bookit.web.rest");
//    beanConfig.setScan(true);
//  }

//  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();

    // Resources classes
//    resources.add(EnvironmentResource.class);
    resources.add(ResourcesResource.class);
    resources.add(ProjectsResource.class);

    // Exception mappers
//    resources.add(RestExceptionMapper.class);

    // Swagger configuration
//    resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
//    resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

//    resources.add(AuthenticationHeaderInterceptor.class);
//    resources.add(RestMetricsInterceptor.class);

    return resources;
  }
}
