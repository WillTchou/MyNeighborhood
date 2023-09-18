FROM node:18.17.1-alpine
WORKDIR /my-neighborhood-front
ARG REACT_APP_GOOGLE_MAPS_API_KEY
ENV REACT_APP_GOOGLE_MAPS_API_KEY=$REACT_APP_GOOGLE_MAPS_API_KEY
COPY package.json .
COPY package-lock.json .
RUN npm install --legacy-peer-deps
COPY . . 
EXPOSE 3000
CMD ["npm", "start"]