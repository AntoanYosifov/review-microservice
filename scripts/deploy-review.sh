#!/bin/bash
set -e

echo "Fetching secrets from SSM..."

DB_HOST=$(aws ssm get-parameter \
  --name "/hoc/review/db-host" \
  --query "Parameter.Value" \
  --output text \
  --region eu-central-1)

DB_NAME=$(aws ssm get-parameter \
  --name "/hoc/review/db-name" \
  --query "Parameter.Value" \
  --output text \
  --region eu-central-1)

DB_USERNAME=$(aws ssm get-parameter \
  --name "/hoc/review/db-username" \
  --with-decryption \
  --query "Parameter.Value" \
  --output text \
  --region eu-central-1)

DB_PASSWORD=$(aws ssm get-parameter \
  --name "/hoc/review/db-password" \
  --with-decryption \
  --query "Parameter.Value" \
  --output text \
  --region eu-central-1)

GHCR_PAT=$(aws ssm get-parameter \
  --name "/hoc/prod/ghcr-pat" \
  --with-decryption \
  --query "Parameter.Value" \
  --output text \
  --region eu-central-1)

echo "Logging in to GHCR..."
echo "$GHCR_PAT" | docker login ghcr.io -u AntoanYosifov --password-stdin

echo "Stopping existing container..."
docker stop hoc-review 2>/dev/null || true
docker rm hoc-review 2>/dev/null || true

echo "Pulling latest image..."
docker pull ghcr.io/antoanyosifov/review-microservice:latest

echo "Starting container..."
docker run -d \
  --name hoc-review \
  --restart unless-stopped \
  -p 8081:8081 \
  -e DB_HOST="$DB_HOST" \
  -e DB_NAME="$DB_NAME" \
  -e DB_USERNAME="$DB_USERNAME" \
  -e DB_PASSWORD="$DB_PASSWORD" \
  -e SPRING_PROFILES_ACTIVE=prod \
  ghcr.io/antoanyosifov/review-microservice:latest

echo "Waiting for application to become healthy..."
for i in $(seq 1 24); do
  if curl -sf http://localhost:8081/actuator/health > /dev/null 2>&1; then
    echo "Health check passed."
    exit 0
  fi
  echo "Attempt $i/24 - not ready yet, waiting 5s..."
  sleep 5
done

echo "Health check failed after 2 minutes."
exit 1
