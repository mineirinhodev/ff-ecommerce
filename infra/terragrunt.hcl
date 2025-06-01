remote_state {
  backend = "s3"
  config = {
    bucket         = "terraform-state-seu-bucket"
    key            = "${path_relative_to_include()}/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "terraform-lock"
  }
}