FROM node:18.17.1-alpine as build
WORKDIR /my-neighborhood-front
ARG REACT_APP_GOOGLE_MAPS_API_KEY
ENV REACT_APP_GOOGLE_MAPS_API_KEY=$REACT_APP_GOOGLE_MAPS_API_KEY
ARG REACT_APP_API_HOST
ENV REACT_APP_API_HOST=$REACT_APP_API_HOST
COPY package.json .
COPY package-lock.json .
RUN npm install --legacy-peer-deps
COPY . . 
EXPOSE 3000
CMD ["npm", "start"]