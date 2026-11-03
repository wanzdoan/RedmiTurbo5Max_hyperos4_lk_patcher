
void FUN_ffff0000b806a008(uint *param_1)

{
  short sVar1;
  int iVar2;
  uint uVar3;
  
  FUN_ffff0000b804c33c(s___s_RomInfo_hook_ffff0000b809f033,s_SEC_ROMINFO_ffff0000b808c0d3);
  if (*param_1 < 0x78) {
    FUN_ffff0000b804c33c
              (s___s__Rom_Info_Size_Wrong__Expect_ffff0000b8091367,s_SEC_ROMINFO_ffff0000b808c0d3,
               0x78);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_sboot_s_ffff0000b8085d26,0x3b,&DAT_ffff0000b8086efb);
  }
  FUN_ffff0000b804d070(&DAT_ffff0000b81b804c,0,700);
  FUN_ffff0000b804d070(&DAT_ffff0000b81b8308,0,0x78);
  uVar3 = *param_1;
  if (699 < uVar3) {
    uVar3 = 700;
  }
  FUN_ffff0000b804cd40(&DAT_ffff0000b81b804c,param_1 + 2,uVar3);
  uVar3 = *param_1;
  if (0x77 < uVar3) {
    uVar3 = 0x78;
  }
  FUN_ffff0000b804cd40(&DAT_ffff0000b81b8308,param_1 + 2,uVar3);
  sVar1 = DAT_ffff0000b81b831a;
  iVar2 = FUN_ffff0000b804cd08(&DAT_ffff0000b81b8308,s_AND_ROMINFO_v_ffff0000b8093f04,0xd);
  if (iVar2 == 0) {
    if (sVar1 == 2) {
      return;
    }
    FUN_ffff0000b804c33c
              (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000b809d16b,s_SEC_ROMINFO_ffff0000b808c0d3,
               sVar1);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_sboot_s_ffff0000b8085d26,0x4b,&DAT_ffff0000b8086efb);
  }
  FUN_ffff0000b804c33c(s___s__Rom_Info_Invalid__ffff0000b808cc74,s_SEC_ROMINFO_ffff0000b808c0d3);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_sboot_s_ffff0000b8085d26,0x48,&DAT_ffff0000b8086efb);
}


