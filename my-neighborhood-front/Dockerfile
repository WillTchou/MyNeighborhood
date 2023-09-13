FROM node:18.17.1-alpine

WORKDIR /my-neighborhood-front

COPY package.json .

RUN npm install -g npm@10.1.0

COPY . . 

EXPOSE 3000

CMD ["npm", "start"]