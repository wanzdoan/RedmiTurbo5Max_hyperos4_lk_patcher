
undefined8 FUN_ffff0000be087b74(void)

{
  short sVar1;
  int iVar2;
  
  sVar1 = DAT_ffff0000be1b65da;
  iVar2 = FUN_ffff0000be060778(&DAT_ffff0000be1b65c8,s_AND_ROMINFO_v_ffff0000be0b5bc7,0xd);
  if (iVar2 != 0) {
    FUN_ffff0000be05fd98
              (s___s__Rom_Info_Invalid_or_Not_Ini_ffff0000be0bdcae,s_SEC_ROMINFO_ffff0000be0ac101);
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000be05ee98
              (s_platform_mediatek_mt6991_sboot_s_ffff0000be0a3f1f,0x57,&DAT_ffff0000be0a553d);
  }
  if (sVar1 == 2) {
    FUN_ffff0000be05fd98(s___s__Rom_Info_Ver____d_ffff0000be0b5e65,s_SEC_ROMINFO_ffff0000be0ac101,2)
    ;
    DAT_ffff0000be0f408c = DAT_ffff0000be1b6614;
    DAT_ffff0000be0f4090 = DAT_ffff0000be1b6610;
    return 0;
  }
  FUN_ffff0000be05fd98
            (s___s__Rom_Info_Ver_Check_Fail__Ex_ffff0000be0c153b,s_SEC_ROMINFO_ffff0000be0ac101,
             sVar1);
                    /* WARNING: Subroutine does not return */
  FUN_ffff0000be05ee98
            (s_platform_mediatek_mt6991_sboot_s_ffff0000be0a3f1f,0x5b,&DAT_ffff0000be0a553d);
}


