// ffff000090767c2c  size=4  FUN_ffff000090767c2c

void FUN_ffff000090767c2c(void)

{
  uint uVar1;
  char *pcVar2;
  
  uVar1 = FUN_ffff000090783624();
  if (uVar1 < 0x14) {
    pcVar2 = &DAT_ffff00009080e27c + *(int *)(&DAT_ffff00009080e27c + (long)(int)uVar1 * 4);
  }
  else {
    pcVar2 = s_unknown_error_ffff0000907e82a9;
  }
  FUN_ffff000090709c60(pcVar2);
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


