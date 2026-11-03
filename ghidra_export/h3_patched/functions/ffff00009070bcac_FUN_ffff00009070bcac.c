// ffff00009070bcac  size=4  FUN_ffff00009070bcac

void FUN_ffff00009070bcac(void)

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
  FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  return;
}


