FROM nginx:alpine

COPY nginx/app/nginx.conf /etc/nginx/templates/default.conf.template

COPY nginx/html/static/css /usr/share/nginx/html/
COPY nginx/html/static/js /usr/share/nginx/html/
COPY nginx/html/ /usr/share/nginx/html/

EXPOSE 80