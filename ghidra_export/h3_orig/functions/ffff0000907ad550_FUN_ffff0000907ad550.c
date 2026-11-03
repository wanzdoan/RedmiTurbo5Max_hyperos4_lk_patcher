// ffff0000907ad550  size=368  FUN_ffff0000907ad550

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907ad550(uint *param_1)

{
  short sVar1;
  int iVar2;
  uint uVar3;
  
  FUN_ffff00009077de54(s___s_RomInfo_hook_ffff0000907fe44e,s_SEC_ROMINFO_ffff0000907dbba4);
  if (*param_1 < 0x78) {
    FUN_ffff00009077de54
              (s___s__Rom_Info_Size_Wrong__Expect_ffff0000907e572b,s_SEC_ROMINFO_ffff0000907dbba4,
               0x78);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_s_ffff0000907d065b,0x3b,&DAT_ffff0000907d24c9);
  }
  FUN_ffff00009077eb88(0xffff00009090bd54,0,700);
  FUN_ffff00009077eb88(0xffff00009090c010,0,0x78);
  uVar3 = *param_1;
  if (699 < uVar3) {
    uVar3 = 700;
  }
  FUN_ffff00009077e858(0xffff00009090bd54,param_1 + 2,uVar3);
  uVar3 = *param_1;
  if (0x77 < uVar3) {
    uVar3 = 0x78;
  }
  FUN_ffff00009077e858(0xffff00009090c010,param_1 + 2,uVar3);
  sVar1 = _DAT_ffff00009090c022;
  iVar2 = FUN_ffff00009077e820(0xffff00009090c010,s_AND_ROMINFO_v_ffff0000907e9fc3,0xd);
  if (iVar2 == 0) {
    if (sVar1 == 2) {
      return;
    }
    FUN_ffff00009077de54
              (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000907fab4a,s_SEC_ROMINFO_ffff0000907dbba4,
               sVar1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_mt6991_sboot_s_ffff0000907d065b,0x4b,&DAT_ffff0000907d24c9);
  }
  FUN_ffff00009077de54(s___s__Rom_Info_Invalid__ffff0000907dd5d8,s_SEC_ROMINFO_ffff0000907dbba4);
                    /* WARNING: Subroutine does not return */
  FUN_ffff00009077cd04
            (s_platform_mediatek_mt6991_sboot_s_ffff0000907d065b,0x48,&DAT_ffff0000907d24c9);
}


