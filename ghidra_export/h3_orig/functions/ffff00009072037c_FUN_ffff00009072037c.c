// ffff00009072037c  size=200  FUN_ffff00009072037c

void FUN_ffff00009072037c(long param_1)

{
  int iVar1;
  int iVar2;
  char *pcVar3;
  undefined1 uVar4;
  undefined1 local_24 [4];
  
  if (param_1 == 0) {
    pcVar3 = s__s__fdt_is_null__ffff0000907fcd97;
  }
  else {
    iVar1 = FUN_ffff00009076e064(param_1,0xffffffff,s_mediatek_charger_ffff0000907e275c);
    if (-1 < iVar1) {
      iVar2 = FUN_ffff000090784760();
      local_24[0] = iVar2 != 0;
      if (iVar2 == 0) {
        uVar4 = 0;
      }
      else {
        FUN_ffff00009076e40c(param_1,iVar1,s_atm_is_enabled_ffff0000907d766d,local_24,1);
        uVar4 = local_24[0];
      }
      FUN_ffff00009077de54
                (s__s__atm_is_enabled__d_to_dtsi__ffff0000907e758b,
                 s_set_charger_atm_ffff0000907e0df3,uVar4);
      return;
    }
    pcVar3 = s__s__charger__no_device_tree__ffff0000907ef47b;
  }
  FUN_ffff00009077de54(pcVar3,s_set_charger_atm_ffff0000907e0df3);
  return;
}


