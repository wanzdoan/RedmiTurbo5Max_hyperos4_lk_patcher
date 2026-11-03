// ffff000090767ec4  size=4  FUN_ffff000090767ec4

void FUN_ffff000090767ec4(void)

{
  uint uVar1;
  char *pcVar2;
  
  uVar1 = FUN_ffff0000907838ec();
  if (uVar1 < 0x14) {
    pcVar2 = &DAT_ffff00009080e4a4 + *(int *)(&DAT_ffff00009080e4a4 + (long)(int)uVar1 * 4);
  }
  else {
    pcVar2 = s_unknown_error_ffff0000907e8404;
  }
  FUN_ffff000090709bec(pcVar2);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


