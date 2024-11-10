### STAGE 1: Build ###
FROM node:16 AS build
WORKDIR /usr/src/app

# Install dependencies
COPY package.json package-lock.json ./
RUN npm install

# Copy the rest of the application code
COPY . .

# Expose the default Angular port
EXPOSE 4200

# Run Angular app using ng serve
CMD ["npm", "start"]
