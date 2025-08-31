import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seijakulist.ui.screens.auth_screen.AuthResult
import com.example.seijakulist.ui.screens.auth_screen.AuthViewModel

@Composable
fun AuthScreen(
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val authResult by viewModel.authResult.collectAsState()

    LaunchedEffect(authResult) {
        if (authResult is AuthResult.Success) {
            onSignInSuccess()
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = { viewModel.signIn(email, password) },
                enabled = authResult !is AuthResult.Loading
            ) {
                Text("Iniciar sesión")
            }
            Button(
                onClick = { viewModel.signUp(email, password) },
                enabled = authResult !is AuthResult.Loading
            ) {
                Text("Registrarse")
            }
        }
        if (authResult is AuthResult.Loading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
        }
        if (authResult is AuthResult.Error) {
            Text(
                text = (authResult as AuthResult.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}