
void FUN_ffff00009070bd24(void)

{
  byte bVar1;
  char *pcVar2;
  
  bVar1 = FUN_ffff000090703bc0();
  if (bVar1 < 3) {
    pcVar2 = &DAT_ffff000090804010 + *(int *)(&DAT_ffff000090804010 + (long)(char)bVar1 * 4);
  }
  else {
    pcVar2 = s_Reason__unknown_ffff0000907d253c;
  }
  FUN_ffff000090709bec(pcVar2);
  FUN_ffff000090709cdc(&DAT_ffff0000907df338);
  return;
}


