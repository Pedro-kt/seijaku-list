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
import androidx.navigation.NavController
import com.example.seijakulist.ui.navigation.AppDestinations
import com.example.seijakulist.ui.screens.auth_screen.AuthResult
import com.example.seijakulist.ui.screens.auth_screen.AuthViewModel
import com.example.seijakulist.ui.theme.RobotoBold
import com.example.seijakulist.ui.theme.RobotoRegular

@Composable
fun AuthScreen(
    onSignInSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
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
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = {
                    navController.navigate(AppDestinations.LOGIN_ROUTE)
                },
                enabled = authResult !is AuthResult.Loading
            ) {
                Text("Iniciar sesión")
            }
            Button(
                onClick = { navController.navigate(AppDestinations.REGISTER_ROUTE) },
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