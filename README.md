Speech to Text Android App
Um aplicativo Android moderno desenvolvido com Kotlin e Jetpack Compose que permite transcrever áudio em texto usando a API do Google Speech-to-Text.

✨ Funcionalidades
🎤 Gravação de áudio ao vivo - Grave diretamente pelo microfone
📁 Transcrição de arquivos - Selecione arquivos de áudio já existentes no dispositivo
🌍 Suporte a múltiplos idiomas - Configurado para português brasileiro (pt-BR)
📱 Interface moderna - Desenvolvido com Jetpack Compose
🔊 Múltiplos formatos - Suporte para MP3, WAV, FLAC, OGG, M4A, 3GP
⚡ Transcrição em tempo real - Processamento rápido e eficiente
🚀 Tecnologias Utilizadas
Kotlin - Linguagem principal
Jetpack Compose - Interface de usuário moderna
Google Speech-to-Text API - Serviço de transcrição
Retrofit - Cliente HTTP para comunicação com a API
MediaRecorder - Gravação de áudio
Material Design 3 - Sistema de design moderno
📋 Pré-requisitos
Android Studio Arctic Fox ou superior
Android SDK 21 ou superior
Conta do Google Cloud Platform
Chave de API do Google Speech-to-Text
⚙️ Configuração
1. Configuração do Google Cloud
   Acesse o Google Cloud Console
   Crie um novo projeto ou selecione um existente
   Ative a Cloud Speech-to-Text API:
   Navegue até "APIs & Services" > "Library"
   Procure por "Cloud Speech-to-Text API"
   Clique em "Enable"
   Crie uma chave de API:
   Vá para "APIs & Services" > "Credentials"
   Clique em "Create Credentials" > "API Key"
   Copie a chave gerada
2. Configuração do Projeto
   Clone o repositório:
   bash
   git clone https://github.com/seu-usuario/speech-to-text-android.git
   cd speech-to-text-android
   Abra o projeto no Android Studio
   Substitua a chave da API no arquivo SpeechToTextViewModel.kt:
   kotlin
   private val apiKey = "SUA_CHAVE_DA_API_AQUI"
   Sincronize o projeto com Gradle
   🔧 Instalação
   Conecte seu dispositivo Android ou inicie um emulador
   Execute o projeto através do Android Studio
   Conceda as permissões necessárias quando solicitado
   📱 Como Usar
   Gravação ao Vivo
   Toque no ícone do microfone (🎤)
   Fale claramente próximo ao dispositivo
   Toque no ícone de parar (⏹️) para finalizar
   Aguarde a transcrição aparecer
   Arquivo Pré-gravado
   Toque no ícone da pasta (📁)
   Selecione um arquivo de áudio do seu dispositivo
   Aguarde o processamento e transcrição
   Formatos Suportados
   MP3 - Formato mais comum
   WAV - Alta qualidade
   FLAC - Compressão sem perdas
   OGG - Formato aberto
   M4A/AAC - Formato Apple/moderno
   3GP - Gravações móveis
   🛡️ Permissões
   O aplicativo solicita as seguintes permissões:

xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_MEDIA_AUDIO" android:minSdkVersion="33" />
🏗️ Estrutura do Projeto
app/
├── src/main/java/com/example/speechtotext/
│   ├── MainActivity.kt                 # Atividade principal
│   ├── SpeechToTextViewModel.kt        # Lógica de negócio
│   ├── SpeechToTextScreen.kt           # Interface do usuário
│   └── models/
│       ├── GoogleSpeechRequest.kt      # Modelos de requisição
│       ├── GoogleSpeechResponse.kt     # Modelos de resposta
│       └── SpeechToTextState.kt        # Estado da aplicação
└── src/main/res/
└── AndroidManifest.xml             # Configurações e permissões
🔧 Dependências
kotlin
dependencies {
implementation("androidx.compose.ui:ui:1.5.4")
implementation("androidx.compose.material3:material3:1.1.2")
implementation("androidx.activity:activity-compose:1.8.0")
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.google.accompanist:accompanist-permissions:0.32.0")
}
🌐 Idiomas Suportados
Português (Brasil) - pt-BR (padrão)
Inglês (EUA) - en-US
Espanhol - es-ES
E muitos outros...
Para alterar o idioma, modifique o languageCode no SpeechToTextViewModel.kt:

kotlin
languageCode = "pt-BR" // ou "en-US", "es-ES", etc.
🔐 Segurança
⚠️ Importante para Produção
Nunca deixe a chave da API hardcoded no código
Use variáveis de ambiente ou BuildConfig
Implemente restrições de API no Google Cloud
Considere usar OAuth 2.0 para maior segurança
Exemplo de Configuração Segura:
kotlin
// build.gradle (app)
android {
buildTypes {
release {
buildConfigField("String", "GOOGLE_API_KEY", "\"${project.findProperty("GOOGLE_API_KEY")}\"")
}
}
}

// No ViewModel
private val apiKey = BuildConfig.GOOGLE_API_KEY
🐛 Solução de Problemas
Erro: "API Key inválida"
Verifique se a chave está correta
Confirme se a API está ativada no Google Cloud
Verifique se há restrições na chave
Erro: "Permissão negada"
Verifique se as permissões foram concedidas
Reinstale o app se necessário
Áudio não é transcrito
Verifique a qualidade do áudio
Teste com diferentes formatos
Verifique a conexão com a internet
📄 Licença
Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para detalhes.

🤝 Contribuindo
Faça um Fork do projeto
Crie uma branch para sua feature (git checkout -b feature/nova-feature)
Commit suas mudanças (git commit -m 'Adiciona nova feature')
Push para a branch (git push origin feature/nova-feature)
Abra um Pull Request
📞 Suporte
Se você encontrar problemas ou tiver dúvidas:

Verifique a seção de Solução de Problemas
Abra uma Issue
Consulte a documentação oficial do Google Speech-to-Text
📊 Roadmap
Suporte a transcrição offline
Múltiplos idiomas em uma única sessão
Exportação de transcrições
Histórico de transcrições
Configurações de qualidade de áudio
Modo escuro/claro
Suporte a arquivos longos (API assíncrona)
Desenvolvido com ❤️ em Kotlin e Jetpack Compose

