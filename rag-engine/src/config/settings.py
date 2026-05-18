from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(env_file=".env", env_file_encoding="utf-8")

    # Database
    database_url: str = "postgresql+asyncpg://flowfix:flowfix@localhost:5432/flowfix"

    # Embedding
    embedding_model: str = "intfloat/multilingual-e5-large"
    embedding_device: str = "cpu"

    # LLM API Keys
    anthropic_api_key: str = ""
    openai_api_key: str = ""
    primary_llm: str = "claude"

    # Server
    server_port: int = 8003
    log_level: str = "INFO"

    # CORS
    backend_origin: str = "http://localhost:8083"


settings = Settings()
