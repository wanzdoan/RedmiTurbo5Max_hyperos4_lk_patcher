
void FUN_ffff0000b8009120(code *param_1,undefined8 param_2,undefined4 param_3)

{
  FUN_ffff0000b8003aa0();
  (*param_1)(param_2,param_3,0,0);
  CallSecureMonitor(0);
  return;
}


