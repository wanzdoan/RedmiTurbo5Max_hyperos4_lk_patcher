
void FUN_ffff000090724fb0(long param_1)

{
  uint uVar1;
  int iVar2;
  uint *puVar3;
  int local_24;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x92,&DAT_ffff0000907d2506);
  }
  iVar2 = FUN_ffff00009076da24(param_1,s__soc_emicen_ffff0000907fcff8);
  if (iVar2 < 0) {
    iVar2 = FUN_ffff00009076da24(param_1,s__emicen_ffff0000907fee11);
    if (iVar2 < 0) {
      iVar2 = FUN_ffff00009076da24(param_1,0);
      if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cfcc
                  (s_platform_mediatek_common_emi_emi_ffff0000907dc557,0x9b,&DAT_ffff0000907d2506);
      }
    }
  }
  local_24 = 0;
  DAT_ffff00009086d068 = 0;
  puVar3 = (uint *)FUN_ffff00009076df9c(param_1,iVar2,s_met_on_ffff0000907da91d,&local_24);
  if ((puVar3 != (uint *)0x0) && (local_24 != 0)) {
    uVar1 = (*puVar3 & 0xff00ff00) >> 8 | (*puVar3 & 0xff00ff) << 8;
    DAT_ffff00009086d068 = uVar1 >> 0x10 | uVar1 << 0x10;
  }
  return;
}


