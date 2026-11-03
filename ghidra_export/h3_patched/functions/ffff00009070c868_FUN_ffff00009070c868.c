// ffff00009070c868  size=140  FUN_ffff00009070c868

void FUN_ffff00009070c868(void)

{
  char *pcVar1;
  int iVar2;
  undefined8 local_40;
  undefined8 local_38;
  undefined8 uStack_30;
  undefined8 local_28;
  ulong uStack_20;
  int local_14;
  
  local_14 = 0;
  local_28 = 0;
  uStack_20 = 0;
  local_38 = 0;
  uStack_30 = 0;
  local_40 = 0;
  iVar2 = FUN_ffff000090788750(&local_14);
  if (iVar2 == 0) {
    pcVar1 = s_lks___0_ffff0000907d2551;
    if (local_14 != 3) {
      pcVar1 = s_lks___1_ffff0000907e8bad;
    }
    FUN_ffff00009077f434(&local_40,0x28,pcVar1);
    uStack_20 = uStack_20 & 0xffffffffffffff;
    FUN_ffff000090709c60(&local_40);
    FUN_ffff000090709d50(&DAT_ffff0000907df27e);
  }
  else {
    FUN_ffff000090709d10(s_cannot_get_lks_ffff0000907d8bfc);
  }
  return;
}


