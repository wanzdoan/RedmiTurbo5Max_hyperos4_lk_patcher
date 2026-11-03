
void FUN_ffff0000be087a04(uint *param_1)

{
  short sVar1;
  int iVar2;
  uint uVar3;
  
  FUN_ffff0000be05fd98(s___s_RomInfo_hook_ffff0000be0c38b3,s_SEC_ROMINFO_ffff0000be0ac101);
  if (*param_1 < 0x78) {
    FUN_ffff0000be05fd98
              (s___s__Rom_Info_Size_Wrong__Expect_ffff0000be0b28df,s_SEC_ROMINFO_ffff0000be0ac101,
               0x78);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_sboot_s_ffff0000be0a3f1f,0x3b,&DAT_ffff0000be0a553d);
  }
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b630c,0,700);
  FUN_ffff0000be060ae0(&DAT_ffff0000be1b65c8,0,0x78);
  uVar3 = *param_1;
  if (699 < uVar3) {
    uVar3 = 700;
  }
  FUN_ffff0000be0607b0(&DAT_ffff0000be1b630c,param_1 + 2,uVar3);
  uVar3 = *param_1;
  if (0x77 < uVar3) {
    uVar3 = 0x78;
  }
  FUN_ffff0000be0607b0(&DAT_ffff0000be1b65c8,param_1 + 2,uVar3);
  sVar1 = DAT_ffff0000be1b65da;
  iVar2 = FUN_ffff0000be060778(&DAT_ffff0000be1b65c8,s_AND_ROMINFO_v_ffff0000be0b5bc7,0xd);
  if (iVar2 == 0) {
    if (sVar1 == 2) {
      return;
    }
    FUN_ffff0000be05fd98
              (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000be0c153b,s_SEC_ROMINFO_ffff0000be0ac101,
               sVar1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_sboot_s_ffff0000be0a3f1f,0x4b,&DAT_ffff0000be0a553d);
  }
  FUN_ffff0000be05fd98(s___s__Rom_Info_Invalid__ffff0000be0ad108,s_SEC_ROMINFO_ffff0000be0ac101);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_sboot_s_ffff0000be0a3f1f,0x48,&DAT_ffff0000be0a553d);
}


