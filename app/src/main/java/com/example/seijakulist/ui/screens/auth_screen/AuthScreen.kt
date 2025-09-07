import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seijakulist.ui.screens.auth_screen.AuthResult
import com.example.seijakulist.ui.screens.auth_screen.AuthViewModel
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

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

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Hola!... Bienvenido a Seijaku List",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 55.sp,
                fontFamily = RobotoBold,
                lineHeight = 60.sp,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Lleva un registro de tus animes, mangas o personajes favoritos",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontFamily = RobotoRegular,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Para continuar inicie sesión con su cuenta o regístrese",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontFamily = RobotoRegular,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
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
}