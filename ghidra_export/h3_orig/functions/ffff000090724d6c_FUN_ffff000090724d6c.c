// ffff000090724d6c  size=216  FUN_ffff000090724d6c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090724d6c(long param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  int local_24;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_emi_emi_ffff0000907dc481,0x92,&DAT_ffff0000907d24c9);
  }
  iVar2 = FUN_ffff00009076d75c(param_1,s__soc_emicen_ffff0000907fce50);
  if (iVar2 < 0) {
    iVar2 = FUN_ffff00009076d75c(param_1,s__emicen_ffff0000907fec69);
    if (iVar2 < 0) {
      iVar2 = FUN_ffff00009076d75c(param_1,0);
      if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cd04
                  (s_platform_mediatek_common_emi_emi_ffff0000907dc481,0x9b,&DAT_ffff0000907d24c9);
      }
    }
  }
  local_24 = 0;
  _DAT_ffff00009086cfd0 = 0;
  puVar3 = (uint *)FUN_ffff00009076dcd4(param_1,iVar2,s_met_on_ffff0000907da812,&local_24);
  if ((puVar3 != (uint *)0x0) && (local_24 != 0)) {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    _DAT_ffff00009086cfd0 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  return;
}


