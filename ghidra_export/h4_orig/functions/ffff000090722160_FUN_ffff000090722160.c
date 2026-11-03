// ffff000090722160  size=232  FUN_ffff000090722160

void FUN_ffff000090722160(undefined8 param_1)

{
  undefined *puVar1;
  int iVar2;
  int iVar3;
  undefined8 uVar4;
  
  iVar2 = FUN_ffff00009076da24(param_1,s__chosen_ffff0000907ea70b);
  puVar1 = PTR_DAT_ffff0000908184b8;
  if (iVar2 < 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_dconfig_ffff0000907fed9c,0x14e,
               s_offset_>__0_ffff0000907f4116);
  }
  uVar4 = FUN_ffff00009077f250(PTR_DAT_ffff0000908184b8);
  iVar3 = FUN_ffff00009076e6d4(param_1,iVar2,s_atag_pl_sec_ffff0000907d5d16,puVar1,uVar4);
  puVar1 = PTR_DAT_ffff0000908184b0;
  if (iVar3 == 0) {
    uVar4 = FUN_ffff00009077f250(PTR_DAT_ffff0000908184b0);
    iVar2 = FUN_ffff00009076e6d4(param_1,iVar2,s_atag_lk_sec_ffff0000907fcf8c,puVar1,uVar4);
    if (iVar2 == 0) {
      return;
    }
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_common_dconfig_ffff0000907fed9c,0x154,&DAT_ffff0000907d2506);
  }
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_common_dconfig_ffff0000907fed9c,0x151,&DAT_ffff0000907d2506);
}


