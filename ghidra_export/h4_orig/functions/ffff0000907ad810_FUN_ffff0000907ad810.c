// ffff0000907ad810  size=368  FUN_ffff0000907ad810

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907ad810(uint *param_1)

{
  short sVar1;
  int iVar2;
  uint uVar3;
  
  FUN_ffff00009077e11c(s___s_RomInfo_hook_ffff0000907fe5f6,s_SEC_ROMINFO_ffff0000907dbcaf);
  if (*param_1 < 0x78) {
    FUN_ffff00009077e11c
              (s___s__Rom_Info_Size_Wrong__Expect_ffff0000907e582f,s_SEC_ROMINFO_ffff0000907dbcaf,
               0x78);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_s_ffff0000907d067a,0x3b,&DAT_ffff0000907d2506);
  }
  FUN_ffff00009077ee50(0xffff00009090be14,0,700);
  FUN_ffff00009077ee50(0xffff00009090c0d0,0,0x78);
  uVar3 = *param_1;
  if (699 < uVar3) {
    uVar3 = 700;
  }
  FUN_ffff00009077eb20(0xffff00009090be14,param_1 + 2,uVar3);
  uVar3 = *param_1;
  if (0x77 < uVar3) {
    uVar3 = 0x78;
  }
  FUN_ffff00009077eb20(0xffff00009090c0d0,param_1 + 2,uVar3);
  sVar1 = _DAT_ffff00009090c0e2;
  iVar2 = FUN_ffff00009077eae8(0xffff00009090c0d0,s_AND_ROMINFO_v_ffff0000907ea107,0xd);
  if (iVar2 == 0) {
    if (sVar1 == 2) {
      return;
    }
    FUN_ffff00009077e11c
              (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000907face6,s_SEC_ROMINFO_ffff0000907dbcaf,
               sVar1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cfcc
              (s_platform_mediatek_mt6991_sboot_s_ffff0000907d067a,0x4b,&DAT_ffff0000907d2506);
  }
  FUN_ffff00009077e11c(s___s__Rom_Info_Invalid__ffff0000907dd67b,s_SEC_ROMINFO_ffff0000907dbcaf);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cfcc
            (s_platform_mediatek_mt6991_sboot_s_ffff0000907d067a,0x48,&DAT_ffff0000907d2506);
}


