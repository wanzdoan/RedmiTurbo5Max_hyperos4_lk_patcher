// ffff00009070bcb0  size=80  FUN_ffff00009070bcb0

void FUN_ffff00009070bcb0(void)

{
  byte bVar1;
  char *pcVar2;
  
  bVar1 = FUN_ffff000090703c64();
  if (bVar1 < 3) {
    pcVar2 = &DAT_ffff000090803de8 + *(int *)(&DAT_ffff000090803de8 + (long)(char)bVar1 * 4);
  }
  else {
    pcVar2 = s_Reason__unknown_ffff0000907d24ff;
  }
  FUN_ffff000090709c60(pcVar2);
  FUN_ffff000090709d50(&DAT_ffff0000907df295);
  return;
}


