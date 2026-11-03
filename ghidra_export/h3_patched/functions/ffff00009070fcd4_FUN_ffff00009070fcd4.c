// ffff00009070fcd4  size=44  FUN_ffff00009070fcd4

char * FUN_ffff00009070fcd4(int param_1)

{
  char *pcVar1;
  char *pcVar2;
  
  pcVar1 = &DAT_ffff0000907d0a5b;
  if (param_1 != 0) {
    pcVar1 = (char *)0x0;
  }
  pcVar2 = s_recovery_ffff0000907e5a79;
  if (param_1 != 1) {
    pcVar2 = pcVar1;
  }
  return pcVar2;
}


