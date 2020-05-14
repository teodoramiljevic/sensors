# NGINX instructions (Docker)

In this project NGINX server is used as load balancer towards the sesnor REST API.

## Configuration

This is the basic configuration to setup load balancing using round robin method by default:
```
upstream api{
    server ${API_HOST1};
    server ${API_HOST2};
}

server{
    listen ${NGINX_PORT};

    location / {
        proxy_pass http://api;
    }
}
```
In this template enviromental variables are used, which are later replaced with real values when the NGINX container is started.

## Docker compose

Environment variables are not automatically resolved by specifying them under the environment section. As stated in [official documentation](https://github.com/docker-library/docs/tree/master/nginx#using-environment-variables-in-nginx-configuration) `envsubst` is used to resolve the values.
This is done by replacing the default nginx image command:
```
/bin/bash -c "envsubst < /etc/nginx/conf.d/nginx.template > /etc/nginx/conf.d/default.conf && exec nginx -g 'daemon off;'"f
```

