// ffff000090721f20  size=232  FUN_ffff000090721f20

void FUN_ffff000090721f20(undefined8 param_1)

{
  undefined *puVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  
  iVar2 = FUN_ffff00009076d75c(param_1,s__chosen_ffff0000907ea59a);
  puVar1 = PTR_DAT_ffff000090818498;
  if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dconfig_ffff0000907febf4,0x14e,
               s_offset_>__0_ffff0000907f3f73);
  }
  uVar4 = FUN_ffff00009077ef88(PTR_DAT_ffff000090818498);
  iVar3 = FUN_ffff00009076e40c(param_1,iVar2,s_atag_pl_sec_ffff0000907d5c80,puVar1,uVar4);
  puVar1 = PTR_DAT_ffff000090818490;
  if (iVar3 == 0) {
    uVar4 = FUN_ffff00009077ef88(PTR_DAT_ffff000090818490);
    iVar2 = FUN_ffff00009076e40c(param_1,iVar2,s_atag_lk_sec_ffff0000907fcde4,puVar1,uVar4);
    if (iVar2 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_dconfig_ffff0000907febf4,0x154,&DAT_ffff0000907d24c9);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_common_dconfig_ffff0000907febf4,0x151,&DAT_ffff0000907d24c9);
}


