
undefined8 FUN_ffff0000b806a178(void)

{
  short sVar1;
  int iVar2;
  
  sVar1 = DAT_ffff0000b81b831a;
  iVar2 = FUN_ffff0000b804cd08(&DAT_ffff0000b81b8308,s_AND_ROMINFO_v_ffff0000b8093f04,0xd);
  if (iVar2 != 0) {
    FUN_ffff0000b804c33c
              (s___s__Rom_Info_Invalid_or_Not_Ini_ffff0000b809a425,s_SEC_ROMINFO_ffff0000b808c0d3);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_mt6991_sboot_s_ffff0000b8085d26,0x57,&DAT_ffff0000b8086efb);
  }
  if (sVar1 == 2) {
    FUN_ffff0000b804c33c(s___s__Rom_Info_Ver____d_ffff0000b8093f12,s_SEC_ROMINFO_ffff0000b808c0d3,2)
    ;
    DAT_ffff0000b81076dc = DAT_ffff0000b81b8354;
    DAT_ffff0000b81076e0 = DAT_ffff0000b81b8350;
    return 0;
  }
  FUN_ffff0000b804c33c
            (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000b809d16b,s_SEC_ROMINFO_ffff0000b808c0d3,
             sVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000b804b830
            (s_platform_mediatek_mt6991_sboot_s_ffff0000b8085d26,0x5b,&DAT_ffff0000b8086efb);
}


