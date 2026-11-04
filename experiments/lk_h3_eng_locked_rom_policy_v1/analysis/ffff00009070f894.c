
char * FUN_ffff00009070f894(int param_1)

{
  char *pcVar1;
  char *pcVar2;

  pcVar1 = &DAT_ffff0000907c8ac4;
  if (param_1 != 0) {
    pcVar1 = (char *)0x0;
  }
  pcVar2 = s_recovery_ffff0000907de253;
  if (param_1 != 1) {
    pcVar2 = pcVar1;
  }
  return pcVar2;
}
